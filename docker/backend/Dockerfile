# docker/backend/Dockerfile

# Stage 1: Build the application using Maven
# 选择一个包含 Maven 和 JDK 的基础镜像 (根据 RuoYi 要求选择 JDK 版本, 如 8 或 11)
FROM registry.cn-hangzhou.aliyuncs.com/snow-io/maven:3.8-openjdk-11 AS builder

# 设置工作目录为 /app
WORKDIR /app

# 复制 Maven 包装器和项目定义文件 (利用缓存)
# 注意：这里的 pom.xml 和模块路径是相对于 Dockerfile 的位置调整的
# 我们将在 Docker Compose 中设置 build context 为 RuoYi-Vue 根目录
COPY pom.xml .
COPY ruoyi-common ruoyi-common
COPY ruoyi-framework ruoyi-framework
COPY ruoyi-system ruoyi-system
COPY ruoyi-quartz ruoyi-quartz
COPY ruoyi-generator ruoyi-generator
COPY ruoyi-admin ruoyi-admin
# 如果有其他依赖模块，也需要复制

# 下载依赖 (如果 pom.xml 或模块定义未改变，此层会被缓存)
# 如果只需要构建 ruoyi-admin，可以优化此步骤，但完整构建更简单

# 复制所有源代码 (如果源代码改变，从这里开始的层会重新构建)
# (上面已经复制了，这一步是为了确保所有文件都在)
# 可以省略，因为上面已经分模块复制了

# 执行 Maven 打包命令，跳过测试
# -pl ruoyi-admin 指定只打包 ruoyi-admin 模块
# -am 同时构建依赖的模块
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
# 使用一个精简的 JRE 镜像
FROM registry.cn-hangzhou.aliyuncs.com/snow-io/eclipse-temurin:11-jre

WORKDIR /app

# 从构建阶段复制构建好的 Jar 包
# Jar 包通常在模块的 target 目录下
COPY --from=builder /app/ruoyi-admin/target/ruoyi-admin.jar ./app.jar

# 暴露后端服务端口 (默认 8080)
EXPOSE 8080

# 设置容器启动时执行的命令
# 使用 exec 格式，允许传递信号
# 通过环境变量传递数据库和 Redis 配置 (将在 docker-compose 中设置)
ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", \
            "app.jar", \
            "--spring.datasource.druid.master.url=jdbc:mysql://${RUOYI_MYSQL_HOST:mysql}:${RUOYI_MYSQL_PORT:3306}/${RUOYI_MYSQL_DB:ry-vue}?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai", \
            "--spring.datasource.druid.master.username=${RUOYI_MYSQL_USER:root}", \
            "--spring.datasource.druid.master.password=${RUOYI_MYSQL_PASS:cpc!23#@xyz}", \
            "--spring.redis.host=${RUOYI_REDIS_HOST:redis}", \
            "--spring.redis.port=${RUOYI_REDIS_PORT:6379}", \
            "--spring.redis.password=${RUOYI_REDIS_PASS:123456}" \
           ]
# 注意: 上述配置覆盖了 application.yml 中的值。确保环境变量名与 Docker Compose 中一致。
# useSSL=false 在开发或内部网络通常可以接受，生产环境建议根据情况配置 SSL。