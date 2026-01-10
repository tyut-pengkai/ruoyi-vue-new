# docker/frontend/Dockerfile

# Stage 1: Build the Vue application
# 选择一个 Node.js 镜像 (版本需与 ruoyi-ui 兼容, 查看 package.json 或咨询项目)
FROM node:16 AS builder

WORKDIR /app

# 切换淘宝镜像源加速 (可选)
RUN npm config set registry https://registry.npmmirror.com

# 复制 UI 源代码
COPY ruoyi-ui ./ruoyi-ui

# 进入 UI 目录执行构建
WORKDIR /app/ruoyi-ui
#安装依赖
RUN yarn install
# 查看 ruoyi-ui/package.json 中的 build 命令, 通常是 build:prod
RUN yarn run build:prod
# 如果使用 npm: RUN npm run build:prod

# Stage 2: Serve the built files using Nginx
FROM nginx:1.23-alpine # 使用轻量级 Nginx 镜像

# 移除 Nginx 默认配置
RUN rm /etc/nginx/conf.d/default.conf

# 复制自定义的 Nginx 配置文件 (下一步创建)
COPY docker/frontend/nginx.conf /etc/nginx/conf.d/ruoyi.conf
# 注意: 这个路径是相对于 Dockerfile 的位置，所以 nginx.conf 要放在 docker/frontend/ 下

# 从构建阶段复制构建好的前端静态文件到 Nginx 的 web 根目录
# 构建产物通常在 ruoyi-ui/dist 目录下
COPY --from=builder /app/ruoyi-ui/dist /usr/share/nginx/html

# 暴露 Nginx 端口
EXPOSE 80

# Nginx 默认会以前台模式运行，不需要显式 CMD 或 ENTRYPOINT