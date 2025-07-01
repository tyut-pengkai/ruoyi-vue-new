# 多阶段构建Dockerfile
FROM maven:3.8.4-openjdk-8 AS builder

# 设置工作目录
WORKDIR /app

# 复制pom.xml文件
COPY pom.xml .

# 复制源代码
COPY . .

# 构建应用
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 安装必要的工具
RUN apk add --no-cache curl

# 从构建阶段复制jar文件
COPY --from=builder /app/ruoyi-admin/target/ruoyi-admin.jar app.jar

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"] 