<template><div><h1 id="宝塔面板部署文档" tabindex="-1"><a class="header-anchor" href="#宝塔面板部署文档" aria-hidden="true">#</a> 宝塔面板部署文档</h1>
<h2 id="一、准备工作" tabindex="-1"><a class="header-anchor" href="#一、准备工作" aria-hidden="true">#</a> 一、准备工作</h2>
<h3 id="_1、软件下载" tabindex="-1"><a class="header-anchor" href="#_1、软件下载" aria-hidden="true">#</a> 1、软件下载</h3>
<p>请加入红叶验证官方群下载最新版本软件包：<a href="https://jq.qq.com/?_wv=1027&amp;k=I2MM7JLa" target="_blank" rel="noopener noreferrer">947144396<ExternalLinkIcon/></a></p>
<h3 id="_2、安装环境" tabindex="-1"><a class="header-anchor" href="#_2、安装环境" aria-hidden="true">#</a> 2、安装环境</h3>
<p>部署本验证系统需要用户自备服务器，Windows/Linux均可，推荐使用Centos服务器，本教程将以WindowsServer2012R2为例进行演示，服务器需安装以下环境：</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>1. JDK >= 1.8 (推荐1.8版本)
2. Mysql >= 5.7.0 (推荐5.7版本)
3. Redis >= 5.0 (推荐7.0版本)
4. Nginx >= 1.20.0
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><div class="custom-container tip"><p class="custom-container-title">提示</p>
<p>为了简化安装步骤，接下来的步骤将使用宝塔面板安装
宝塔面板下载地址：<a href="https://www.bt.cn/new/download.html" target="_blank" rel="noopener noreferrer">https://www.bt.cn/new/download.html<ExternalLinkIcon/></a></p>
</div>
<h4 id="_1-java环境-jdk" tabindex="-1"><a class="header-anchor" href="#_1-java环境-jdk" aria-hidden="true">#</a> （1）Java环境：JDK</h4>
<p>在群共享下载JDK安装包<code v-pre>jdk-8u301-windows-x64.exe</code>并安装即可<br>
安装完毕后按<code v-pre>win+r</code>打开运行对话框，输入<code v-pre>cmd</code>并回车，在弹出的命令框中输入如下命令</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>java -version
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div></div></div><p>输出类似下方文字代表安装成功</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>C:\Users\Administrator>java -version
java version "1.8.0_301"
Java(TM) SE Runtime Environment (build 1.8.0_301-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.301-b09, mixed mode)

C:\Users\Administrator>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h4 id="_2-mysql-redis-nginx" tabindex="-1"><a class="header-anchor" href="#_2-mysql-redis-nginx" aria-hidden="true">#</a> （2）Mysql/Redis/Nginx</h4>
<p>Mysql、Redis、Nginx这三个软件可以在宝塔面板软件商店里直接搜索安装，安装完毕如图所示
<img src="@source/install/windows/bt/1.jpg" alt="image"></p>
<h4 id="_3-新建数据库" tabindex="-1"><a class="header-anchor" href="#_3-新建数据库" aria-hidden="true">#</a> （3）新建数据库</h4>
<h5 id="_1、在安装好的mysql数据库中新建数据库hywlyz-数据库编码选择utf8mb4-如图所示" tabindex="-1"><a class="header-anchor" href="#_1、在安装好的mysql数据库中新建数据库hywlyz-数据库编码选择utf8mb4-如图所示" aria-hidden="true">#</a> 1、在安装好的Mysql数据库中新建数据库<code v-pre>hywlyz</code>，数据库编码选择<code v-pre>utf8mb4</code>，如图所示：</h5>
<p><img src="@source/install/windows/bt/2.jpg" alt="image"></p>
<h5 id="_2、解压程序压缩包" tabindex="-1"><a class="header-anchor" href="#_2、解压程序压缩包" aria-hidden="true">#</a> 2、解压程序压缩包</h5>
<p>解压下载的红叶验证程序压缩包，如图：
<img src="@source/install/windows/bt/3.jpg" alt="image"><br>
将解压出来的<code v-pre>hywlyz.sql</code>文件导入到刚创建的数据库中</p>
<h2 id="二、部署程序" tabindex="-1"><a class="header-anchor" href="#二、部署程序" aria-hidden="true">#</a> 二、部署程序</h2>
<h3 id="_1、上传程序到服务器" tabindex="-1"><a class="header-anchor" href="#_1、上传程序到服务器" aria-hidden="true">#</a> 1、上传程序到服务器</h3>
<p>将<code v-pre>upload</code>文件夹的内容上传到服务器，此处上传到<code v-pre>C:/hywlyz</code>目录为例，后文中出现的此路径均应修改为您使用的路径（建议您使用此路径，后续文中需修改安装路径的指令可无需修改直接复制粘贴）</p>
<h3 id="_2、修改配置文件" tabindex="-1"><a class="header-anchor" href="#_2、修改配置文件" aria-hidden="true">#</a> 2、修改配置文件</h3>
<h4 id="_1-修改数据库连接" tabindex="-1"><a class="header-anchor" href="#_1-修改数据库连接" aria-hidden="true">#</a> （1）修改数据库连接</h4>
<p>编辑<code v-pre>application-config.yml</code>，将<code v-pre>数据库密码</code>更改为您的数据库的密码，此处请注意<code v-pre>jdbc:mysql://127.0.0.1:3306/hywlyz?useUnicode=</code>中的<code v-pre>hywlyz</code>为数据库名，若与您不一致请注意修改</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code># 数据源配置
  datasource:
    druid:
      # 主库数据源
      master:
        url: jdbc:mysql://127.0.0.1:3306/hywlyz?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull&amp;useSSL=true&amp;serverTimezone=GMT%2B8&amp;allowMultiQueries=true
        username: hywlyz
        password: &lt;数据库密码>
</code></pre><div class="highlight-lines"><br><br><br><br><br><div class="highlight-line">&nbsp;</div><div class="highlight-line">&nbsp;</div><div class="highlight-line">&nbsp;</div></div><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>如果配置了Redis数据库密码，需要将<code v-pre>Redis密码</code>处更改为您的密码（如果您未配置过Redis密码，默认密码为空，此处无需修改）</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code># redis 配置
  redis:
    # 地址
    host: 127.0.0.1
    # 端口，默认为6379
    port: 6379
    # 密码
    password: &lt;Redis密码>
</code></pre><div class="highlight-lines"><br><br><br><br><br><br><br><div class="highlight-line">&nbsp;</div></div><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="_3、启动前端程序" tabindex="-1"><a class="header-anchor" href="#_3、启动前端程序" aria-hidden="true">#</a> 3、启动前端程序</h3>
<p>在宝塔面板依次选择<code v-pre>网站</code>-<code v-pre>PHP项目</code>-<code v-pre>添加站点</code>
<img src="@source/install/windows/bt/5.jpg" alt="image"></p>
<p>域名：填写您自己的域名，此处以<code v-pre>hy.coordsoft.com</code>为例<br>
根目录：选择上传的程序包中的<code v-pre>web</code>目录<br>
PHP版本：选择<code v-pre>纯静态</code>
<img src="@source/install/windows/bt/6.jpg" alt="image"><br>
创建完毕后点击网站的<code v-pre>设置</code>，打开<code v-pre>站点修改</code>页面，选择<code v-pre>伪静态</code>
<img src="@source/install/windows/bt/7.jpg" alt="image"><br>
将以下规则填写到下侧框中</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code># HTTP反向代理相关配置开始 >>>
location / {
    try_files $uri $uri/ /index.html;
    index index.html index.htm;
}

location /prod-api/ {
    proxy_set_header Host $http_host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header REMOTE-HOST $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_pass http://127.0.0.1:8080/;
}

location /profile/ {
    proxy_pass http://127.0.0.1:8080/profile/;
}
# HTTP反向代理相关配置结束 &lt;&lt;&lt;
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="_4、启动后端程序" tabindex="-1"><a class="header-anchor" href="#_4、启动后端程序" aria-hidden="true">#</a> 4、启动后端程序</h3>
<p>双击<code v-pre>hy.bat</code>文件即可启动程序<br>
如果出现以下提示代表启动成功
<img src="@source/install/windows/bt/4.jpg" alt="image"></p>
<div class="custom-container tip"><p class="custom-container-title">提示</p>
<p>请记录此处提示的<code v-pre>机器码</code>，需要凭此码获取授权信息</p>
</div>
<p>此时即可通过您的域名尝试访问网站<code v-pre>http://域名/admin</code>，默认管理员账号密码为：admin/admin123，如果登录时提示未授权说明部署成功</p>
<h2 id="三、购买授权" tabindex="-1"><a class="header-anchor" href="#三、购买授权" aria-hidden="true">#</a> 三、购买授权</h2>
<p>购买请访问商城：<a href="https://shop.coordsoft.com/" target="_blank" rel="noopener noreferrer">https://shop.coordsoft.com/<ExternalLinkIcon/></a>
购买注册码后点击 <a href="https://shop.coordsoft.com/getLicense" target="_blank" rel="noopener noreferrer">激活授权<ExternalLinkIcon/></a> 为您的网站在线授权</p>
<h2 id="四、-登录网站" tabindex="-1"><a class="header-anchor" href="#四、-登录网站" aria-hidden="true">#</a> 四、 登录网站</h2>
<p>至此，整个网站部署完成，如果一切正常，您可通过您的域名访问属于您的红叶网络验证系统<br>
商店地址：http://域名<br>
后台地址：http://域名/admin<br>
默认管理员账号密码：admin/admin123</p>
</div></template>


