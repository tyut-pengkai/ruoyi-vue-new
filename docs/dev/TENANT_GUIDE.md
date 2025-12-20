# 若依多租户架构实现指南

## 文档概述

本文档详细介绍了若依框架的多租户SaaS架构实现方案，包括设计原理、核心组件、集成步骤和最佳实践。适合希望在项目中引入多租户功能的开发者学习和参考。

---

## 第一部分：多租户架构概述

### 1.1 什么是多租户SaaS

**多租户（Multi-Tenancy）** 是一种软件架构模式，单一应用实例可以同时为多个租户（客户组织）提供服务，每个租户的数据相互隔离，互不可见。

**典型应用场景**：
- SaaS产品（如理发店管理系统，每个理发店是一个租户）
- 企业内部多分公司系统
- 连锁门店管理平台
- 培训机构多校区管理

**核心优势**：
- **成本低**：共享服务器资源，降低运维成本
- **易维护**：统一升级，无需为每个客户单独部署
- **易扩展**：新增租户只需添加数据记录，无需改代码

### 1.2 三种多租户隔离模式对比

| 隔离模式 | 实现方式 | 数据隔离性 | 成本 | 适用场景 |
|---------|---------|-----------|-----|---------|
| **独立数据库** | 每个租户独立的数据库实例 | ⭐⭐⭐⭐⭐ 最强 | 💰💰💰 最高 | 金融、医疗等高安全场景 |
| **共享数据库+独立Schema** | 同一数据库，不同Schema（MySQL为database） | ⭐⭐⭐⭐ 强 | 💰💰 中等 | 中大型企业客户 |
| **共享数据库+共享Schema** | 通过tenant_id字段区分 | ⭐⭐⭐ 较强 | 💰 最低 | 中小型租户，数量较多 |

**若依采用第三种模式**：共享数据库+共享Schema+tenant_id隔离

**选型理由**：
- 成本低廉，适合中小型SaaS产品
- 实现简单，维护方便
- 通过拦截器实现自动过滤，业务代码无感知
- 性能优秀，单库可支持数千租户

---

## 第二部分：核心组件实现原理

### 2.1 租户上下文管理（TenantContext）

#### 核心设计

**TenantContext** 是租户ID的存储容器，基于 `TransmittableThreadLocal` 实现。

**为什么不用普通ThreadLocal？**

| ThreadLocal | TransmittableThreadLocal |
|------------|--------------------------|
| 只能在当前线程访问 | 支持父子线程传递 |
| 线程池场景下会丢失 | 线程池场景下自动传递 |
| 异步任务无法获取 | 异步任务自动继承 |

若依框架大量使用了 `@Async` 异步任务和线程池，普通ThreadLocal会导致租户上下文丢失，引发数据泄露风险。

#### 关键方法

```java
public class TenantContext {
    // 存储租户ID
    private static final TransmittableThreadLocal<Long> TENANT_ID_HOLDER = new TransmittableThreadLocal<>();

    // 存储是否忽略租户过滤（超级管理员专用）
    private static final TransmittableThreadLocal<Boolean> IGNORE_TENANT = new TransmittableThreadLocal<>();

    // 设置当前租户ID
    public static void setTenantId(Long tenantId) {
        TENANT_ID_HOLDER.set(tenantId);
    }

    // 获取当前租户ID
    public static Long getTenantId() {
        return TENANT_ID_HOLDER.get();
    }

    // 清除租户上下文（请求结束时必须调用）
    public static void clear() {
        TENANT_ID_HOLDER.remove();
        IGNORE_TENANT.remove();
    }

    // 设置忽略租户过滤（超级管理员全局模式）
    public static void setIgnore(boolean ignore) {
        IGNORE_TENANT.set(ignore);
    }

    // 是否处于全局模式
    public static boolean isIgnore() {
        Boolean ignore = IGNORE_TENANT.get();
        return ignore != null && ignore;
    }
}
```

**文件位置**：`ruoyi-common/src/main/java/com/ruoyi/common/core/context/TenantContext.java`

#### 生命周期管理

```
HTTP请求进入
  → TenantInterceptor.preHandle() 设置租户ID
  → Controller/Service处理业务（从TenantContext获取租户ID）
  → MyBatis拦截器自动添加tenant_id过滤
  → 请求完成
  → TenantInterceptor.afterCompletion() 清除上下文
```

**为什么必须清除？**
Tomcat使用线程池处理请求，如果不清除，线程复用时会串用上一个请求的租户ID，导致数据泄露！

---

### 2.2 Web拦截器（TenantInterceptor）

#### 工作流程

```
HTTP请求
  ↓
判断是否是忽略URL（登录、验证码等）
  ↓ 是 → 跳过租户拦截
  ↓ 否
从SecurityContext获取LoginUser
  ↓
判断用户类型：
  ├─ 超级管理员 → TenantContext.setIgnore(true) [全局模式]
  ├─ 普通用户有tenant_id → TenantContext.setTenantId(tenantId)
  └─ 普通用户无tenant_id → 抛出异常拒绝访问
  ↓
业务处理...
  ↓
请求完成 → afterCompletion() → TenantContext.clear()
```

#### 核心逻辑代码

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String requestUri = request.getRequestURI();

    // 忽略URL判断
    if (TenantConfig.isIgnoreUrl(requestUri)) {
        return true;
    }

    // 获取登录用户
    LoginUser loginUser = SecurityUtils.getLoginUser();

    if (loginUser != null) {
        Long userId = loginUser.getUserId();

        // 超级管理员 → 全局模式
        if (TenantConfig.isSuperAdmin(userId)) {
            TenantContext.setIgnore(true);
            log.warn("【超级管理员】用户 {} 进入全局模式", loginUser.getUsername());
        }
        // 普通用户 → 设置租户ID
        else if (loginUser.getTenantId() != null) {
            TenantContext.setTenantId(loginUser.getTenantId());
        }
        // 未分配租户 → 拒绝访问
        else {
            throw new ServiceException("用户未分配租户，请联系管理员");
        }
    }

    return true;
}

@Override
public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                            Object handler, Exception ex) {
    TenantContext.clear();  // 必须清除！
}
```

**文件位置**：`ruoyi-framework/src/main/java/com/ruoyi/framework/interceptor/TenantInterceptor.java`

#### 超级管理员机制

**超级管理员特权**：
- 登录后自动进入全局模式（`TenantContext.setIgnore(true)`）
- 可以查看和操作所有租户的数据
- 用于系统管理、数据迁移、问题排查

**安全建议**：
- 超级管理员账号必须严格控制
- 所有全局模式操作应记录审计日志
- 生产环境建议禁用超级管理员的数据修改权限

---

### 2.3 SQL拦截器（TenantSqlInterceptor）

#### 工作原理

**TenantSqlInterceptor** 是MyBatis插件，拦截所有SQL执行，自动在WHERE条件中添加 `tenant_id = ?` 过滤。

**技术实现**：
1. 拦截MyBatis的 `StatementHandler.prepare()` 方法
2. 使用 **JSQLParser** 解析SQL语句
3. 判断主表是否需要租户隔离
4. 在WHERE子句中添加租户ID条件
5. 将改写后的SQL设置回BoundSql

#### SQL改写示例

**原始SQL**：
```sql
SELECT * FROM sys_user WHERE user_name = 'admin'
```

**改写后（租户ID=1000）**：
```sql
SELECT * FROM sys_user WHERE tenant_id = 1000 AND user_name = 'admin'
```

**UPDATE语句**：
```sql
-- 原始
UPDATE sys_user SET nick_name = '张三' WHERE user_id = 2

-- 改写
UPDATE sys_user SET nick_name = '张三' WHERE tenant_id = 1000 AND user_id = 2
```

**JOIN查询（自动识别表别名）**：
```sql
-- 原始
SELECT u.*, d.dept_name
FROM sys_user u
LEFT JOIN sys_dept d ON u.dept_id = d.dept_id
WHERE u.user_name = 'admin'

-- 改写
SELECT u.*, d.dept_name
FROM sys_user u
LEFT JOIN sys_dept d ON u.dept_id = d.dept_id
WHERE u.tenant_id = 1000 AND d.tenant_id = 1000 AND u.user_name = 'admin'
```

#### 智能判断机制

**只拦截以下情况**：
1. SQL类型：`SELECT`、`UPDATE`、`DELETE`（INSERT不拦截，由业务代码设置）
2. 主表是租户表（配置在 `TenantConfig.TENANT_TABLES` 中）
3. 非全局模式（超级管理员自动跳过）

**白名单机制**：
- 全局共享表不拦截（`sys_menu`、`sys_dict`、`sys_config`等）
- 日志表不拦截（`sys_logininfor`、`sys_oper_log`）
- 关联表不拦截（`sys_user_role`、`sys_role_menu`）

#### 核心拦截逻辑

```java
@Override
public Object intercept(Invocation invocation) throws Throwable {
    // 获取原始SQL
    BoundSql boundSql = statementHandler.getBoundSql();
    String originalSql = boundSql.getSql();

    // 判断是否需要拦截
    if (!shouldIntercept(sqlCommandType, originalSql)) {
        return invocation.proceed();
    }

    // 超级管理员全局模式 → 跳过拦截
    if (TenantContext.isIgnore()) {
        return invocation.proceed();
    }

    // 获取租户ID
    Long tenantId = TenantContext.getTenantId();
    if (tenantId == null) {
        throw new ServiceException("租户上下文丢失，请重新登录");
    }

    // 使用JSQLParser解析并改写SQL
    String modifiedSql = processSql(originalSql, tenantId, sqlCommandType);

    // 设置改写后的SQL
    metaObject.setValue("delegate.boundSql.sql", modifiedSql);

    return invocation.proceed();
}
```

**文件位置**：`ruoyi-framework/src/main/java/com/ruoyi/framework/interceptor/TenantSqlInterceptor.java`

---

### 2.4 登录流程集成

#### 登录流程图

```
用户登录
  → 临时开启全局模式（TenantContext.setIgnore(true)）
  → Spring Security验证用户名密码
  → 查询sys_user表获取用户信息
  → 提取user.tenantId
  → 检查：非超级管理员必须有tenantId，否则拒绝登录
  → 创建LoginUser对象，设置tenantId
  → 生成JWT Token（包含tenantId）
  → 清除全局模式（TenantContext.clear()）
  → 返回Token给前端
```

#### 为什么登录时要开启全局模式？

这是一个 **"先有鸡还是先有蛋"** 的问题：
- 用户登录时，尚未设置租户上下文
- 但查询 `sys_user` 表需要知道租户ID
- 如果不开启全局模式，SQL拦截器会报错"租户上下文丢失"

**解决方案**：登录阶段临时开启全局模式，验证通过后立即关闭。

#### 关键代码片段

```java
public String login(String username, String password, String code, String uuid) {
    // 临时开启全局模式
    TenantContext.setIgnore(true);

    try {
        // 验证码校验
        validateCaptcha(username, code, uuid);

        // Spring Security认证
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // 获取用户信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysUser user = loginUser.getUser();

        // 提取租户ID
        Long tenantId = user.getTenantId();

        // 非超级管理员必须有租户ID
        if (!TenantConfig.isSuperAdmin(user.getUserId()) && tenantId == null) {
            throw new ServiceException("用户未分配租户，请联系管理员");
        }

        // 设置到LoginUser
        loginUser.setTenantId(tenantId);

        // 生成JWT Token
        return tokenService.createToken(loginUser);

    } finally {
        // 清除全局模式
        TenantContext.clear();
    }
}
```

**文件位置**：`ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/SysLoginService.java`

#### 后续请求的租户识别

```
用户携带Token发起请求
  → Spring Security从Token解析出LoginUser
  → TenantInterceptor从LoginUser提取tenantId
  → 设置到TenantContext
  → 后续所有SQL自动添加tenant_id过滤
```

---

### 2.5 数据库设计

#### 租户主表（sys_tenant）

```sql
CREATE TABLE `sys_tenant` (
  `tenant_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '租户ID',
  `tenant_name` varchar(50) NOT NULL COMMENT '租户名称',
  `tenant_code` varchar(50) NOT NULL COMMENT '租户编码（唯一）',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间（NULL表示永久）',
  `package_id` bigint(20) DEFAULT NULL COMMENT '套餐ID（预留）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`tenant_id`),
  UNIQUE KEY `uk_tenant_code` (`tenant_code`)
) COMMENT='租户信息表';
```

**SQL脚本位置**：`sql/ry_20250522_with_tenant.sql`

#### 业务表改造规范

**所有需要隔离的表添加tenant_id字段**：

```sql
ALTER TABLE sys_user ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE sys_dept ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE sys_role ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT '租户ID';

-- 创建索引（必须！）
CREATE INDEX idx_tenant_id ON sys_user(tenant_id);
CREATE INDEX idx_tenant_id ON sys_dept(tenant_id);
CREATE INDEX idx_tenant_id ON sys_role(tenant_id);

-- 用户表特殊处理：复合索引（提升登录查询性能）
CREATE INDEX idx_tenant_user ON sys_user(tenant_id, user_name);
```

**为什么是 `DEFAULT NULL` 而不是 `NOT NULL`？**
- 超级管理员的tenant_id为NULL（表示全局用户）
- 初始化数据可能没有租户ID
- 便于数据迁移和测试

#### 表分类清单

**需要隔离的表（TENANT_TABLES）**：
```
若依框架表：
- sys_user          用户表
- sys_dept          部门表
- sys_role          角色表
- sys_post          岗位表
- sys_notice        通知公告表

业务表（示例）：
- hl_customer       客户表
- hl_member_card    会员卡表
- hl_service        服务表
- hl_staff          员工表
- hl_order          订单表
```

**全局共享表（IGNORE_TABLES）**：
```
- sys_menu          菜单表（所有租户共享菜单）
- sys_dict_type     字典类型表
- sys_dict_data     字典数据表
- sys_config        系统配置表
- sys_tenant        租户表本身
- sys_user_role     用户角色关联表
- sys_role_menu     角色菜单关联表
- sys_role_dept     角色部门关联表
- sys_logininfor    登录日志表
- sys_oper_log      操作日志表
- sys_job           定时任务表
- sys_job_log       定时任务日志表
```

**配置位置**：`ruoyi-common/src/main/java/com/ruoyi/common/config/TenantConfig.java`

---

## 第三部分：快速集成指南

### 步骤1：Maven依赖配置

在根pom.xml中添加依赖版本：

```xml
<properties>
    <!-- 多租户相关依赖版本 -->
    <transmittable-thread-local.version>2.14.3</transmittable-thread-local.version>
    <jsqlparser.version>4.6</jsqlparser.version>
</properties>

<dependencyManagement>
    <dependencies>
        <!-- TransmittableThreadLocal：支持线程池场景的上下文传递 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>transmittable-thread-local</artifactId>
            <version>${transmittable-thread-local.version}</version>
        </dependency>

        <!-- JSQLParser：SQL解析器，用于SQL改写 -->
        <dependency>
            <groupId>com.github.jsqlparser</groupId>
            <artifactId>jsqlparser</artifactId>
            <version>${jsqlparser.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

在 `ruoyi-common/pom.xml` 中引入：

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>transmittable-thread-local</artifactId>
</dependency>
```

在 `ruoyi-framework/pom.xml` 中引入：

```xml
<dependency>
    <groupId>com.github.jsqlparser</groupId>
    <artifactId>jsqlparser</artifactId>
</dependency>
```

---

### 步骤2：复制核心类文件

#### 必须复制的新增文件

| 文件 | 路径 | 说明 |
|------|------|------|
| TenantContext.java | ruoyi-common/.../context/ | 租户上下文 |
| TenantConfig.java | ruoyi-common/.../config/ | 租户配置 |
| TenantInterceptor.java | ruoyi-framework/.../interceptor/ | Web拦截器 |
| TenantSqlInterceptor.java | ruoyi-framework/.../interceptor/ | SQL拦截器 |
| SysTenant.java | ruoyi-system/.../domain/ | 租户实体 |
| SysTenantMapper.java | ruoyi-system/.../mapper/ | Mapper接口 |
| SysTenantMapper.xml | ruoyi-system/.../mapper/system/ | Mapper XML |
| ISysTenantService.java | ruoyi-system/.../service/ | Service接口 |
| SysTenantServiceImpl.java | ruoyi-system/.../service/impl/ | Service实现 |
| SysTenantController.java | ruoyi-admin/.../controller/system/ | Controller |

#### 需要修改的现有文件

**1. BaseEntity.java** - 添加tenantId字段

```java
public class BaseEntity implements Serializable {
    // ... 现有字段 ...

    /** 租户ID */
    @JsonIgnore
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
```

**2. SysUser.java** - 添加tenantId字段

```java
public class SysUser extends BaseEntity {
    // ... 现有字段 ...

    /** 租户ID */
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
```

**3. LoginUser.java** - 添加tenantId字段

```java
public class LoginUser implements UserDetails {
    // ... 现有字段 ...

    /** 租户ID */
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
```

**4. SysLoginService.java** - 集成租户逻辑

在 `login()` 方法中添加租户处理逻辑（参考2.4节）。

**5. TokenService.java** - Token中包含租户ID

在 `createToken()` 和 `getLoginUser()` 方法中添加租户ID的存取逻辑。

---

### 步骤3：注册拦截器

#### 注册Web拦截器

在 `ResourcesConfig.java` 中注册：

```java
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    @Autowired
    private TenantInterceptor tenantInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 租户拦截器必须在第一位！
        registry.addInterceptor(tenantInterceptor).addPathPatterns("/**");

        // 其他拦截器...
    }
}
```

**文件位置**：`ruoyi-framework/src/main/java/com/ruoyi/framework/config/ResourcesConfig.java`

#### 注册MyBatis拦截器

在 `MyBatisConfig.java` 中注册：

```java
@Configuration
public class MyBatisConfig {

    @Autowired
    private TenantSqlInterceptor tenantSqlInterceptor;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // ... 其他配置 ...

        // 注册租户SQL拦截器
        sessionFactory.setPlugins(tenantSqlInterceptor);

        return sessionFactory.getObject();
    }
}
```

**文件位置**：`ruoyi-framework/src/main/java/com/ruoyi/framework/config/MyBatisConfig.java`

---

### 步骤4：数据库改造

#### 执行SQL脚本

```sql
-- 1. 创建租户表
CREATE TABLE `sys_tenant` ( ... );

-- 2. 为现有表添加tenant_id字段
ALTER TABLE sys_user ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE sys_dept ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE sys_role ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE sys_post ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE sys_notice ADD COLUMN tenant_id bigint(20) DEFAULT NULL COMMENT '租户ID';

-- 3. 创建索引
CREATE INDEX idx_tenant_id ON sys_user(tenant_id);
CREATE INDEX idx_tenant_id ON sys_dept(tenant_id);
CREATE INDEX idx_tenant_id ON sys_role(tenant_id);
CREATE INDEX idx_tenant_id ON sys_post(tenant_id);
CREATE INDEX idx_tenant_id ON sys_notice(tenant_id);

-- 4. 用户表特殊优化
CREATE INDEX idx_tenant_user ON sys_user(tenant_id, user_name);

-- 5. 初始化默认租户
INSERT INTO sys_tenant (tenant_id, tenant_name, tenant_code, status, create_time)
VALUES (1, '默认租户', 'DEFAULT', '0', NOW());

-- 6. 更新现有数据关联到默认租户
UPDATE sys_user SET tenant_id = 1 WHERE user_id > 1;  -- 保留admin为NULL（超级管理员）
UPDATE sys_dept SET tenant_id = 1;
UPDATE sys_role SET tenant_id = 1 WHERE role_id > 1;
```

**完整SQL脚本**：`sql/ry_20250522_with_tenant.sql`

---

### 步骤5：配置租户规则

在 `TenantConfig.java` 中配置：

#### 配置超级管理员

```java
public static final List<Long> SUPER_ADMIN_USER_IDS = Arrays.asList(
    1L  // admin用户ID，根据实际情况调整
);
```

#### 配置忽略URL

```java
public static final List<String> IGNORE_URLS = Arrays.asList(
    "/login",
    "/captchaImage",
    "/logout",
    "/register"
    // 根据项目添加其他公共接口
);
```

#### 配置租户表

```java
public static final List<String> TENANT_TABLES = Arrays.asList(
    // 若依框架表
    "sys_user",
    "sys_dept",
    "sys_role",
    "sys_post",
    "sys_notice",

    // 你的业务表（根据实际情况添加）
    "your_business_table1",
    "your_business_table2"
);
```

#### 配置全局表

```java
public static final List<String> IGNORE_TABLES = Arrays.asList(
    "sys_menu",
    "sys_dict_type",
    "sys_dict_data",
    "sys_config",
    "sys_user_role",
    "sys_role_menu",
    "sys_role_dept",
    "sys_tenant",
    "sys_logininfor",
    "sys_oper_log",
    "sys_job",
    "sys_job_log"
    // 根据项目添加其他全局表
);
```

---

## 第四部分：使用指南

### 4.1 租户管理API

#### 创建租户

**接口**：`POST /link/tenant`

**请求体**：
```json
{
  "tenantName": "理发店A",
  "tenantCode": "SHOP_A",
  "contactName": "张三",
  "contactPhone": "13800138000",
  "expireTime": "2026-12-31 23:59:59",
  "status": "0"
}
```

**响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "tenantId": 1001
  }
}
```

#### 用户分配租户

编辑用户时，设置 `tenantId` 字段：

**接口**：`PUT /system/user`

**请求体**：
```json
{
  "userId": 100,
  "userName": "shop_admin",
  "nickName": "店长",
  "tenantId": 1001,  // 分配到租户1001
  ...
}
```

#### 租户状态管理

**启用租户**：
```json
PUT /link/tenant
{
  "tenantId": 1001,
  "status": "0"
}
```

**停用租户**（用户将无法登录）：
```json
PUT /link/tenant
{
  "tenantId": 1001,
  "status": "1"
}
```

---

### 4.2 开发注意事项

#### 哪些表需要添加tenant_id？

**判断标准**：
- 数据是否属于某个租户（客户组织）？
- 不同租户之间是否需要隔离？

**需要添加**：
- 用户相关表：`sys_user`、`sys_dept`、`sys_role`、`sys_post`
- 业务核心表：订单、客户、产品、服务、员工等
- 业务关联表：订单明细、客户标签等

**不需要添加**：
- 系统配置表：菜单、字典、系统参数
- 日志表：登录日志、操作日志
- 关联表：`sys_user_role`（通过user_id间接关联租户）
- 枚举表：性别、状态等固定选项

#### 新增业务表规范

```sql
CREATE TABLE your_business_table (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  tenant_id bigint(20) DEFAULT NULL COMMENT '租户ID',  -- 必须添加
  your_field1 varchar(50),
  ...
  PRIMARY KEY (id),
  INDEX idx_tenant_id (tenant_id)  -- 必须添加索引
) COMMENT='业务表';
```

然后在 `TenantConfig.TENANT_TABLES` 中注册表名。

#### 特殊场景处理

**场景1：定时任务**

定时任务无HTTP请求，无法自动设置租户上下文，需要手动设置：

```java
@Scheduled(cron = "0 0 2 * * ?")
public void dailyTask() {
    // 遍历所有租户
    List<SysTenant> tenants = tenantService.selectActiveTenants();

    for (SysTenant tenant : tenants) {
        try {
            // 设置租户上下文
            TenantContext.setTenantId(tenant.getTenantId());

            // 执行租户相关任务
            doBusinessLogic();

        } finally {
            // 必须清除
            TenantContext.clear();
        }
    }
}
```

**场景2：异步任务**

使用 `@Async` 注解的异步任务，`TransmittableThreadLocal` 会自动传递租户上下文，无需手动处理：

```java
@Async
public void asyncTask() {
    // TenantContext.getTenantId() 自动获取到主线程的租户ID
    Long tenantId = TenantContext.getTenantId();

    // 执行异步任务...
}
```

**场景3：跨租户查询（谨慎使用）**

某些场景需要查询其他租户数据（如数据统计、迁移工具）：

```java
public void crossTenantQuery() {
    try {
        // 临时开启全局模式
        TenantContext.setIgnore(true);

        // 查询所有租户数据
        List<SysUser> allUsers = userMapper.selectUserList(new SysUser());

        // 处理数据...

    } finally {
        // 必须恢复租户过滤
        TenantContext.setIgnore(false);
    }
}
```

**安全提醒**：跨租户操作必须严格控制权限，并记录审计日志！

---

### 4.3 测试验证

#### 验证租户隔离

**步骤1**：创建2个测试租户

```sql
INSERT INTO sys_tenant (tenant_name, tenant_code, status, create_time)
VALUES
  ('租户A', 'TENANT_A', '0', NOW()),
  ('租户B', 'TENANT_B', '0', NOW());
```

**步骤2**：创建属于不同租户的用户

```sql
-- 租户A的用户
INSERT INTO sys_user (user_name, nick_name, tenant_id, password, create_time)
VALUES ('user_a', '用户A', 1001, '$2a$...', NOW());

-- 租户B的用户
INSERT INTO sys_user (user_name, nick_name, tenant_id, password, create_time)
VALUES ('user_b', '用户B', 1002, '$2a$...', NOW());
```

**步骤3**：登录验证

- 使用 `user_a` 登录，查询用户列表 → 只能看到租户A的用户
- 使用 `user_b` 登录，查询用户列表 → 只能看到租户B的用户
- 使用 `admin` 登录（超级管理员） → 可以看到所有用户

#### 开启SQL日志

在 `application.yml` 中配置：

```yaml
logging:
  level:
    com.ruoyi.system.mapper: DEBUG  # 开启MyBatis日志
```

重启后，控制台会输出执行的SQL：

```sql
==> Preparing: SELECT * FROM sys_user WHERE tenant_id = ? AND user_name = ?
==> Parameters: 1001(Long), user_a(String)
<== Total: 1
```

验证SQL是否包含 `tenant_id` 过滤条件。

#### 常见问题排查

| 问题现象 | 可能原因 | 解决方法 |
|---------|---------|---------|
| 租户ID为空 | LoginUser未设置tenantId | 检查登录流程，确保提取并设置租户ID |
| SQL未添加租户过滤 | 表未在TENANT_TABLES中 | 在TenantConfig中注册表名 |
| 看到其他租户数据 | 拦截器未生效 | 检查拦截器注册顺序 |
| 异步任务丢失租户ID | 使用了普通ThreadLocal | 确认使用TransmittableThreadLocal |
| 线程池场景丢失上下文 | 未清除TenantContext | 检查afterCompletion是否执行 |

---

## 第五部分：最佳实践与常见问题

### 5.1 安全建议

#### 防止数据泄露

- **强制规范**：所有业务表必须添加 `tenant_id` 字段和索引
- **代码审查**：禁止使用原生JDBC或直接拼接SQL（绕过拦截器）
- **测试覆盖**：编写单元测试验证租户隔离
- **审计日志**：记录超级管理员的全局模式操作

```java
// 错误示例：绕过拦截器
jdbcTemplate.query("SELECT * FROM sys_user", ...);  // ❌

// 正确示例：使用MyBatis
userMapper.selectUserList(query);  // ✅
```

#### 异常处理

```java
// 租户ID缺失时拒绝执行
public static Long getRequiredTenantId() {
    Long tenantId = getTenantId();
    if (tenantId == null && !isIgnore()) {
        log.error("【安全警告】租户上下文丢失");
        throw new ServiceException("租户上下文丢失，请重新登录");
    }
    return tenantId;
}
```

#### 数据完整性检查

定期执行SQL检查是否有遗漏的数据：

```sql
-- 检查是否有用户未分配租户（排除超级管理员）
SELECT * FROM sys_user WHERE tenant_id IS NULL AND user_id > 1;

-- 检查是否有租户ID不存在的数据
SELECT * FROM sys_user u
LEFT JOIN sys_tenant t ON u.tenant_id = t.tenant_id
WHERE u.tenant_id IS NOT NULL AND t.tenant_id IS NULL;
```

---

### 5.2 性能优化

#### 索引设计

**单列索引**：
```sql
CREATE INDEX idx_tenant_id ON your_table(tenant_id);
```

**复合索引**（推荐）：
```sql
-- 将tenant_id放在第一位
CREATE INDEX idx_tenant_xxx ON your_table(tenant_id, frequently_queried_field);

-- 示例：用户表登录查询
CREATE INDEX idx_tenant_user ON sys_user(tenant_id, user_name);

-- 示例：订单表按时间查询
CREATE INDEX idx_tenant_time ON hl_order(tenant_id, create_time);
```

**为什么要复合索引？**
- SQL查询：`WHERE tenant_id = 1001 AND user_name = 'admin'`
- 单列索引：先用tenant_id索引，再回表过滤user_name
- 复合索引：直接定位到结果，无需回表

#### 大数据量场景

**场景**：单个租户数据量超过500万

**优化方案**：
1. **分表策略**：按租户ID分表（`hl_order_1001`、`hl_order_1002`）
2. **冷热分离**：历史数据归档到历史表
3. **读写分离**：租户表使用独立的从库

```java
// 动态表名示例
String tableName = "hl_order_" + tenantId;
orderMapper.selectFromTable(tableName, query);
```

#### 缓存策略

**Redis缓存Key设计**：

```java
// 错误：未包含租户ID，不同租户会冲突
String cacheKey = "user:" + userId;  // ❌

// 正确：包含租户ID
String cacheKey = "tenant:" + tenantId + ":user:" + userId;  // ✅

// 或使用前缀
String cacheKey = "T" + tenantId + ":user:" + userId;  // ✅
```

**缓存清除**：

```java
@CacheEvict(value = "user", key = "'T' + #user.tenantId + ':' + #user.userId")
public void updateUser(SysUser user) {
    userMapper.updateUser(user);
}
```

---

### 5.3 常见问题FAQ

#### Q1: TransmittableThreadLocal vs ThreadLocal？

| 特性 | ThreadLocal | TransmittableThreadLocal |
|-----|------------|--------------------------|
| 线程内访问 | ✅ | ✅ |
| 父子线程传递 | ❌ | ✅ |
| 线程池场景 | ❌ 会丢失 | ✅ 自动传递 |
| 性能开销 | 低 | 稍高（可接受） |

**结论**：若依框架使用了大量异步任务和线程池，必须使用TransmittableThreadLocal。

#### Q2: 为什么登录时要临时开启全局模式？

**问题**：用户登录时尚未设置租户上下文，但查询 `sys_user` 需要租户ID。

**解决方案**：登录阶段临时开启全局模式，验证通过后立即关闭。

```java
TenantContext.setIgnore(true);  // 开启全局模式
try {
    // 查询用户、验证密码...
} finally {
    TenantContext.clear();  // 清除全局模式
}
```

#### Q3: JOIN查询如何处理？

**答**：`TenantSqlInterceptor` 使用JSQLParser解析SQL，自动识别JOIN中的每个表，并为租户表添加过滤条件。

```sql
-- 原始SQL
SELECT u.*, d.dept_name FROM sys_user u LEFT JOIN sys_dept d ON u.dept_id = d.dept_id

-- 自动改写
SELECT u.*, d.dept_name
FROM sys_user u LEFT JOIN sys_dept d ON u.dept_id = d.dept_id
WHERE u.tenant_id = 1001 AND d.tenant_id = 1001
```

**注意**：确保JOIN的两个表都有 `tenant_id` 字段且都在 `TENANT_TABLES` 中。

#### Q4: 如何支持租户级别的配置？

**方案1**：租户配置表

```sql
CREATE TABLE sys_tenant_config (
  id bigint(20) PRIMARY KEY,
  tenant_id bigint(20) NOT NULL,
  config_key varchar(50),
  config_value varchar(500),
  INDEX idx_tenant_key (tenant_id, config_key)
);
```

**方案2**：扩展租户表字段

```sql
ALTER TABLE sys_tenant ADD COLUMN config_json TEXT COMMENT '租户配置JSON';
```

#### Q5: 租户数据如何迁移和备份？

**导出单个租户数据**：

```bash
mysqldump -u root -p your_database \
  --where="tenant_id=1001" \
  sys_user sys_dept sys_role hl_order > tenant_1001_backup.sql
```

**导入到新租户**：

```sql
-- 1. 创建新租户
INSERT INTO sys_tenant (...) VALUES (...);  -- 获得新租户ID 1002

-- 2. 导入数据并替换租户ID
-- 使用脚本批量替换 tenant_id=1001 为 tenant_id=1002
```

---

## 总结

若依多租户架构采用 **共享数据库+共享Schema+tenant_id隔离** 模式，通过 **双层拦截机制**（Web拦截器+SQL拦截器）实现自动数据隔离，业务代码无需感知租户逻辑。

**核心优势**：
- 成本低，维护简单
- 透明化隔离，业务代码无侵入
- 性能优秀，单库可支持数千租户
- 扩展性强，支持租户级配置和定制

**关键设计**：
- `TenantContext`：基于TransmittableThreadLocal的租户上下文
- `TenantInterceptor`：Web层识别租户并设置上下文
- `TenantSqlInterceptor`：MyBatis层自动改写SQL添加tenant_id过滤
- 超级管理员机制：支持全局模式管理所有租户数据

**安全提醒**：
- 所有业务表必须添加 `tenant_id` 字段和索引
- 避免使用原生SQL绕过拦截器
- 超级管理员操作必须记录审计日志
- 定期检查数据完整性

**参考资料**：
- 源码位置：`ruoyi-common`、`ruoyi-framework`、`ruoyi-system` 模块
- SQL脚本：`sql/ry_20250522_with_tenant.sql`
- 提交记录：`cc618482 feat(tenant): 实现多租户数据隔离功能`

---

**文档版本**：v1.0
**最后更新**：2025-12-19
**维护者**：HairLink开发团队
