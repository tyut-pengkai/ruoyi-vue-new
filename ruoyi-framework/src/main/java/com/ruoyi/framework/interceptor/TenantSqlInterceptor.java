package com.ruoyi.framework.interceptor;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import com.ruoyi.common.exception.ServiceException;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.ruoyi.common.config.TenantConfig;
import com.ruoyi.common.core.context.TenantContext;

/**
 * MyBatis租户SQL拦截器 - 自动在SQL中添加 tenant_id 过滤条件
 *
 * Reason: 借鉴若依DataScope模式，通过SQL解析实现租户隔离
 *
 * 工作原理：
 * 1. 拦截MyBatis的StatementHandler.prepare()方法
 * 2. 获取原始SQL和命令类型
 * 3. 判断是否需要拦截（SELECT/UPDATE/DELETE + 租户表）
 * 4. 使用JSQLParser解析SQL
 * 5. 在WHERE子句中添加 tenant_id = ? 条件
 * 6. 将改写后的SQL设置回BoundSql
 */
@Component
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class TenantSqlInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantSqlInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        BoundSql boundSql = statementHandler.getBoundSql();
        String originalSql = boundSql.getSql();

        // 判断是否需要拦截
        if (!shouldIntercept(sqlCommandType, originalSql)) {
            return invocation.proceed();
        }

        // 超级管理员忽略租户过滤
        if (TenantContext.isIgnore()) {
            log.debug("【全局模式】跳过租户过滤: {}", originalSql.substring(0, Math.min(100, originalSql.length())));
            return invocation.proceed();
        }

        // 获取租户ID
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            log.error("【安全警告】租户ID为空且非全局模式，SQL被拦截: {}",
                     originalSql.substring(0, Math.min(100, originalSql.length())));
            throw new ServiceException("租户上下文丢失，请重新登录");
        }

        // 解析并修改SQL
        try {
            String modifiedSql = processSql(originalSql, tenantId, sqlCommandType);
            if (!modifiedSql.equals(originalSql)) {
                metaObject.setValue("delegate.boundSql.sql", modifiedSql);
                log.debug("【租户SQL拦截】租户ID: {}, 原SQL: {}", tenantId,
                         originalSql.substring(0, Math.min(100, originalSql.length())));
            }
        } catch (Exception e) {
            log.error("租户SQL解析失败: {}, 原SQL: {}", e.getMessage(),
                     originalSql.substring(0, Math.min(100, originalSql.length())));
            // SQL解析失败不影响执行，记录日志后继续
        }

        return invocation.proceed();
    }

    /**
     * 判断是否需要拦截
     *
     * 核心逻辑：通过SQL解析获取主表名，只有主表是租户表或混合模式表时才拦截
     * 这样可以避免：
     * 1. 字符串子串匹配导致的误判（如 sys_role_menu 误匹配 sys_role）
     * 2. JOIN表被错误过滤（只对主表添加租户条件）
     *
     * @param sqlCommandType SQL命令类型
     * @param sql 原始SQL
     * @return true-需要拦截，false-不需要
     */
    private boolean shouldIntercept(SqlCommandType sqlCommandType, String sql) {
        // 拦截 SELECT、UPDATE、DELETE、INSERT
        if (sqlCommandType != SqlCommandType.SELECT &&
            sqlCommandType != SqlCommandType.UPDATE &&
            sqlCommandType != SqlCommandType.DELETE &&
            sqlCommandType != SqlCommandType.INSERT) {
            return false;
        }

        try {
            // 解析SQL获取主表名
            Statement statement = CCJSqlParserUtil.parse(sql);
            String mainTableName = getMainTableName(statement);

            // 租户表或混合模式表都需要拦截
            return mainTableName != null && (isTenantTable(mainTableName) || isHybridTable(mainTableName));
        } catch (Exception e) {
            // SQL解析失败，不拦截（避免影响正常业务）
            log.debug("SQL解析失败，跳过租户拦截: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从SQL语句中获取主表名
     *
     * @param statement SQL语句
     * @return 主表名，解析失败返回null
     */
    private String getMainTableName(Statement statement) {
        if (statement instanceof Select) {
            PlainSelect plainSelect = (PlainSelect) ((Select) statement).getSelectBody();
            FromItem fromItem = plainSelect.getFromItem();
            if (fromItem instanceof Table) {
                return ((Table) fromItem).getName();
            }
        } else if (statement instanceof Update) {
            return ((Update) statement).getTable().getName();
        } else if (statement instanceof Delete) {
            return ((Delete) statement).getTable().getName();
        } else if (statement instanceof Insert) {
            return ((Insert) statement).getTable().getName();
        }
        return null;
    }

    /**
     * 判断表名是否是租户表
     *
     * @param tableName 表名
     * @return true-是租户表，false-不是
     */
    private boolean isTenantTable(String tableName) {
        return TenantConfig.needTenantFilter(tableName);
    }

    /**
     * 判断表名是否是混合模式表
     * Reason: 混合模式表同时支持系统级数据(tenant_id=0)和租户级数据
     *
     * @param tableName 表名
     * @return true-是混合模式表，false-不是
     */
    private boolean isHybridTable(String tableName) {
        return TenantConfig.isHybridTable(tableName);
    }

    /**
     * 处理SQL - 添加租户过滤条件
     *
     * @param sql 原始SQL
     * @param tenantId 租户ID
     * @param sqlCommandType SQL类型
     * @return 改写后的SQL
     */
    private String processSql(String sql, Long tenantId, SqlCommandType sqlCommandType) throws Exception {
        Statement statement = CCJSqlParserUtil.parse(sql);
        String tableName = getMainTableName(statement);
        boolean isHybrid = isHybridTable(tableName);

        if (statement instanceof Select) {
            // 处理 SELECT 语句
            Select selectStatement = (Select) statement;
            PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();

            // 获取主表别名，避免多表JOIN时tenant_id歧义
            String tableAlias = getMainTableAlias(plainSelect);

            // 根据表类型构造不同的租户条件
            net.sf.jsqlparser.expression.Expression tenantCondition;
            if (isHybrid) {
                // 混合模式：(tenant_id = ? OR tenant_id = 0)
                tenantCondition = buildHybridTenantCondition(tenantId, tableAlias);
            } else {
                // 普通租户表：tenant_id = ?
                tenantCondition = buildTenantCondition(tenantId, tableAlias);
            }

            // 合并WHERE条件
            if (plainSelect.getWhere() != null) {
                AndExpression andExpression = new AndExpression(tenantCondition, plainSelect.getWhere());
                plainSelect.setWhere(andExpression);
            } else {
                plainSelect.setWhere(tenantCondition);
            }

            return selectStatement.toString();
        }
        else if (statement instanceof Update) {
            // 处理 UPDATE 语句
            Update updateStatement = (Update) statement;

            // 获取表别名
            String tableAlias = getUpdateTableAlias(updateStatement);

            // 根据表类型构造不同的租户条件
            // Reason: 混合模式表的UPDATE只能操作租户自己的数据，不能修改系统级数据(tenant_id=0)
            net.sf.jsqlparser.expression.Expression tenantCondition;
            tenantCondition = buildTenantCondition(tenantId, tableAlias);

            if (updateStatement.getWhere() != null) {
                AndExpression andExpression = new AndExpression(tenantCondition, updateStatement.getWhere());
                updateStatement.setWhere(andExpression);
            } else {
                updateStatement.setWhere(tenantCondition);
            }

            return updateStatement.toString();
        }
        else if (statement instanceof Delete) {
            // 处理 DELETE 语句
            Delete deleteStatement = (Delete) statement;

            // 获取表别名
            String tableAlias = getDeleteTableAlias(deleteStatement);

            // 根据表类型构造不同的租户条件
            // Reason: 混合模式表的DELETE只能操作租户自己的数据，不能删除系统级数据(tenant_id=0)
            net.sf.jsqlparser.expression.Expression tenantCondition;
            tenantCondition = buildTenantCondition(tenantId, tableAlias);

            if (deleteStatement.getWhere() != null) {
                AndExpression andExpression = new AndExpression(tenantCondition, deleteStatement.getWhere());
                deleteStatement.setWhere(andExpression);
            } else {
                deleteStatement.setWhere(tenantCondition);
            }

            return deleteStatement.toString();
        }
        else if (statement instanceof Insert) {
            // 处理 INSERT 语句
            return processInsert((Insert) statement, tenantId);
        }

        return sql;
    }

    /**
     * 获取SELECT语句主表的别名或表名
     *
     * @param plainSelect SELECT语句
     * @return 表别名或表名，用于构造 tenant_id 条件
     */
    private String getMainTableAlias(PlainSelect plainSelect) {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            Table table = (Table) fromItem;
            if (table.getAlias() != null) {
                return table.getAlias().getName();
            }
            return table.getName();
        }
        return null;
    }

    /**
     * 获取UPDATE语句的表别名或表名
     *
     * @param updateStatement UPDATE语句
     * @return 表别名或表名
     */
    private String getUpdateTableAlias(Update updateStatement) {
        Table table = updateStatement.getTable();
        if (table != null) {
            if (table.getAlias() != null) {
                return table.getAlias().getName();
            }
            return table.getName();
        }
        return null;
    }

    /**
     * 获取DELETE语句的表别名或表名
     *
     * @param deleteStatement DELETE语句
     * @return 表别名或表名
     */
    private String getDeleteTableAlias(Delete deleteStatement) {
        Table table = deleteStatement.getTable();
        if (table != null) {
            if (table.getAlias() != null) {
                return table.getAlias().getName();
            }
            return table.getName();
        }
        return null;
    }

    /**
     * 构造租户过滤条件：[表别名.]tenant_id = ?
     *
     * @param tenantId 租户ID
     * @param tableAlias 表别名（可为null）
     * @return EqualsTo 表达式
     */
    private EqualsTo buildTenantCondition(Long tenantId, String tableAlias) {
        EqualsTo equalsTo = new EqualsTo();
        if (tableAlias != null) {
            equalsTo.setLeftExpression(new Column(new Table(tableAlias), "tenant_id"));
        } else {
            equalsTo.setLeftExpression(new Column("tenant_id"));
        }
        equalsTo.setRightExpression(new LongValue(tenantId));
        return equalsTo;
    }

    /**
     * 构造混合模式租户过滤条件：([表别名.]tenant_id = ? OR [表别名.]tenant_id = 0)
     * Reason: 混合模式表同时支持系统级数据(tenant_id=0)和租户级数据
     *
     * @param tenantId 租户ID
     * @param tableAlias 表别名（可为null）
     * @return Parenthesis 表达式（带括号的OR条件）
     */
    private Parenthesis buildHybridTenantCondition(Long tenantId, String tableAlias) {
        // 构造 tenant_id = ?
        EqualsTo tenantCondition = new EqualsTo();
        if (tableAlias != null) {
            tenantCondition.setLeftExpression(new Column(new Table(tableAlias), "tenant_id"));
        } else {
            tenantCondition.setLeftExpression(new Column("tenant_id"));
        }
        tenantCondition.setRightExpression(new LongValue(tenantId));

        // 构造 tenant_id = 0
        EqualsTo systemCondition = new EqualsTo();
        if (tableAlias != null) {
            systemCondition.setLeftExpression(new Column(new Table(tableAlias), "tenant_id"));
        } else {
            systemCondition.setLeftExpression(new Column("tenant_id"));
        }
        systemCondition.setRightExpression(new LongValue(0));

        // 构造 (tenant_id = ? OR tenant_id = 0)
        OrExpression orExpression = new OrExpression(tenantCondition, systemCondition);
        return new Parenthesis(orExpression);
    }

    /**
     * 处理INSERT语句 - 自动填充tenant_id
     *
     * Reason: 防止开发人员忘记手动设置tenant_id导致孤儿数据
     *
     * @param insertStatement INSERT语句
     * @param tenantId 租户ID
     * @return 改写后的SQL
     */
    private String processInsert(Insert insertStatement, Long tenantId) {
        // 1. 获取表名
        String tableName = insertStatement.getTable().getName();

        // 2. 获取列定义
        List<Column> columns = insertStatement.getColumns();
        if (columns == null || columns.isEmpty()) {
            // 无列名INSERT: INSERT INTO table VALUES (...)
            log.warn("【租户INSERT】无列名语句无法自动填充tenant_id: {}", tableName);
            return insertStatement.toString();
        }

        // 3. 检查是否已有tenant_id
        boolean hasTenantId = columns.stream()
            .anyMatch(col -> "tenant_id".equalsIgnoreCase(col.getColumnName()));

        if (hasTenantId) {
            log.debug("【租户INSERT】已包含tenant_id,跳过自动填充: {}", tableName);
            return insertStatement.toString();
        }

        // 4. 自动添加tenant_id列
        columns.add(new Column("tenant_id"));

        // 5. 处理VALUES
        ItemsList itemsList = insertStatement.getItemsList();
        if (itemsList instanceof ExpressionList) {
            // 单行INSERT
            ExpressionList expressionList = (ExpressionList) itemsList;
            expressionList.getExpressions().add(new LongValue(tenantId));
        }
        else if (itemsList instanceof MultiExpressionList) {
            // 批量INSERT
            MultiExpressionList multiList = (MultiExpressionList) itemsList;
            for (ExpressionList expList : multiList.getExpressionLists()) {
                expList.getExpressions().add(new LongValue(tenantId));
            }
        }
        else if (itemsList instanceof SubSelect) {
            // INSERT SELECT（暂不支持）
            log.warn("【租户INSERT】INSERT SELECT语句暂不支持自动填充: {}", tableName);
            return insertStatement.toString();
        }

        log.info("【租户INSERT拦截】自动填充tenant_id={}: {}", tenantId, tableName);
        return insertStatement.toString();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以从配置文件读取租户表配置
    }
}
