package com.ruoyi.framework.config.datapermission;

import com.ruoyi.common.core.domain.model.DataPermissionModel;
import com.ruoyi.common.enums.CommonEnum;
import com.ruoyi.common.enums.DataPermissionType;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**  mybatis 拦截器
 * @author poplar-hub
 * @version 1.0
 * @date 2023/8/30
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class DataPermissionInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();
        final Executor executor = (Executor) target;
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        BoundSql boundSql = (BoundSql) args[5];

        //判断线程上下文中是否有数据权限模型
        DataPermissionModel dataPermissionModel = DataPermissionContextHolder.getContext();
        if(dataPermissionModel == null){
            //如果没有数据权限模型，则不做数据权限处理
            return invocation.proceed();
        }

        //判断XxxMapper类名是否匹配
        List<String> mapperNames = dataPermissionModel.getMappers();
        if(CollectionUtils.isEmpty(mapperNames)){
            return invocation.proceed();
        }
        String namespace = ms.getId();
        String className = namespace.substring(0,namespace.lastIndexOf("."));
        String mapperClassName = className.substring(className.lastIndexOf(".") + 1);
        if(!mapperNames.contains(mapperClassName)){
            return invocation.proceed();
        }

        // 判断当前用户是否是管理员
        if (CommonEnum.YES.getCode().equals(dataPermissionModel.getIsAdmin())) {
            return invocation.proceed();
        }

        //获取当前登录用户所具有数据权限范围
        List<Long> deptIdList = new ArrayList<>();
        String type = dataPermissionModel.getType();
        if(DataPermissionType.ALL.getCode().equals(type)){
            //如果是全部数据权限，则直接返回
            return invocation.proceed();
        }else if(DataPermissionType.DEPT.getCode().equals(type)){
            //如果是部门
            deptIdList = dataPermissionModel.getDeptIds();
        }else if(DataPermissionType.SELF.getCode().equals(type)){
            //如果是仅本人
            deptIdList = Collections.singletonList(-1L);
        }

        String sql = boundSql.getSql();
        log.info("原始的sql:{}",sql);

        //拼接SQL
        String newSql = this.addDeptIdCondition(sql,deptIdList);
        log.info("执行的sql:{}",newSql);

        //更新 BoundSql
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql, boundSql.getParameterMappings(), parameter);
        // 设置 BoundSql 的参数
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        args[5] = newBoundSql;

        // 更新 MappedStatement
        // 这里需要注意，如果是查询，需要修改 MappedStatement 的 resultMaps
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(),
                new StaticSqlSource(ms.getConfiguration(), newBoundSql.getSql()), ms.getSqlCommandType());
        statementBuilder.resultMaps(ms.getResultMaps());
        MappedStatement newMs = statementBuilder.build();
        // 更新 args 中的 MappedStatement
        args[0] = newMs;

        // 继续执行拦截器链
        return invocation.proceed();
    }

    private String addDeptIdCondition(String sql, List<Long> deptIdList) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            PlainSelect select = (PlainSelect) ((Select) statement).getSelectBody();
            FromItem fromItem = select.getFromItem();
            Table table = null;
            if (fromItem instanceof Table) {
                table = (Table) fromItem;
            } else if (fromItem instanceof SubSelect) {
                SubSelect subSelect = (SubSelect) fromItem;
                table = (Table) ((PlainSelect) subSelect.getSelectBody()).getFromItem();
            }

            // 使用JsqlParser修改SQL
            if (CollectionUtils.isNotEmpty(deptIdList)) {
                //拼接SQL： dept_id in (?,?,?)
                InExpression inExpression = new InExpression();
                inExpression.setLeftExpression(new Column("dept_id").withTable(table));
                ExpressionList expressionList = new ExpressionList();
                for (Long deptId : deptIdList) {
                    expressionList.addExpressions(new LongValue(deptId));
                }
                inExpression.setRightItemsList(expressionList);
                Expression whereExpression = select.getWhere();
                if (whereExpression == null){
                    select.setWhere(inExpression);
                }else{
                    AndExpression andExpression = new AndExpression(select.getWhere(),inExpression);
                    select.setWhere(andExpression);
                }
            }else {
                //拼接SQL： dept_id = -1
                EqualsTo equalsTo =new EqualsTo();
                equalsTo.setLeftExpression(new Column("dept_id").withTable(table));
                equalsTo.setRightExpression(new LongValue(-1));
                Expression whereExpression = select.getWhere();
                if (whereExpression == null){
                    select.setWhere(equalsTo);
                }else{
                    AndExpression andExpression = new AndExpression(select.getWhere(),equalsTo);
                    select.setWhere(andExpression);
                }
            }
            return select.toString();
        } catch (JSQLParserException e) {
            log.error("occur JSQLParserException ",e);
            throw new RuntimeException(e);
        }
    }

}
