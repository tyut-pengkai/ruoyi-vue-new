<template><div><h2 id="红叶网络验证与软件管理系统部署文档-【windows篇】" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#红叶网络验证与软件管理系统部署文档-【windows篇】">#</a> 红叶网络验证与软件管理系统部署文档 【Windows篇】</h2>
<p><code v-pre>2022年11月29日修订</code> <code v-pre>软件授权管理计费销售一站式解决方案</code> <code v-pre>官方QQ群：947144396</code></p>
<blockquote>
<p>演示地址：
软件商城：<a href="http://demo.coordsoft.com/" rel="noopener noreferrer" target="_blank">http://demo.coordsoft.com/<ExternalLinkIcon/></a>
后台管理：<a href="http://demo.coordsoft.com/admin" rel="noopener noreferrer" target="_blank">http://demo.coordsoft.com/admin<ExternalLinkIcon/></a>
账号密码：admin/admin123</p>
</blockquote>
<h2 id="一、准备工作" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#一、准备工作">#</a> 一、准备工作</h2>
<h3 id="_1、软件下载" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_1、软件下载">#</a> 1、软件下载</h3>
<p>请加入红叶验证官方群下载最新版本软件包：<a href="https://jq.qq.com/?_wv=1027&amp;k=I2MM7JLa" rel="noopener noreferrer" target="_blank">947144396<ExternalLinkIcon/></a></p>
<h3 id="_2、安装环境" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_2、安装环境">#</a> 2、安装环境</h3>
<p>部署本验证系统需要用户自备服务器，Windows/Linux均可，推荐使用Centos服务器，本教程将Windows Server 2012 R2为例进行演示，服务器需安装以下环境：</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>1. JDK >= 1.8 (推荐1.8版本)
2. Mysql >= 5.6.0 (推荐5.7版本)
3. Redis >= 3.0
4. Nginx >= 1.20.0
</code></pre><div aria-hidden="true" class="line-numbers"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><blockquote>
<p>为了简化安装步骤，接下来的步骤将使用UPUPW面板安装
UPUPW面板下载地址：<a href="https://php.upupw.net/" rel="noopener noreferrer" target="_blank">https://php.upupw.net/<ExternalLinkIcon/></a></p>
</blockquote>
<h4 id="_1-java环境-jdk" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_1-java环境-jdk">#</a> （1）Java环境：JDK</h4>
<p>在群共享下载JDK安装包<code v-pre>jdk-8u301-windows-x64.exe</code>并安装即可</p>
<p>安装完毕后按<code v-pre>win+r</code>打开运行对话框，输入<code v-pre>cmd</code>并回车，在弹出的命令框中输入如下命令</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>java -version
</code></pre><div aria-hidden="true" class="line-numbers"><div class="line-number"></div></div></div><p>输出类似下方文字代表安装成功</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>C:\Users\Administrator>java -version
java version "1.8.0_301"
Java(TM) SE Runtime Environment (build 1.8.0_301-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.301-b09, mixed mode)

C:\Users\Administrator>
</code></pre><div aria-hidden="true" class="line-numbers"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h4 id="_2-mysql-redis-nginx" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_2-mysql-redis-nginx">#</a> （2）Mysql/Redis/Nginx</h4>
<p>Mysql、Redis、Nginx这三个软件已经集成在在UPUPW面板中，我们只需安装UPUPW面板即可
注意选择版本时选择Nginx版本进行安装，如图所示：
<img alt="image" src="@source/install/windows/1.jpg">
<img alt="image" src="@source/install/windows/2.jpg">
安装完毕后点击面板右侧<code v-pre>一键开启</code>启动所有服务
<img alt="image" src="@source/install/windows/3.jpg"></p>
<h4 id="_3-新建数据库" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_3-新建数据库">#</a> （3）新建数据库</h4>
<h5 id="_1、获取数据库密码" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_1、获取数据库密码">#</a> 1、获取数据库密码</h5>
<p>选择<code v-pre>数据库管理</code>-<code v-pre>密码管理</code>-<code v-pre>获取root密码</code>：
<img alt="image" src="@source/install/windows/4.jpg">
<img alt="image" src="@source/install/windows/5.jpg"></p>
<h5 id="_2、登录数据库-在mysql数据库中新建数据库hywlyz-如图所示" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_2、登录数据库-在mysql数据库中新建数据库hywlyz-如图所示">#</a> 2、登录数据库，在Mysql数据库中新建数据库<code v-pre>hywlyz</code>，如图所示：</h5>
<p><img alt="image" src="@source/install/windows/6.jpg"></p>
<h5 id="_2、解压程序压缩包" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_2、解压程序压缩包">#</a> 2、解压程序压缩包</h5>
<p>解压下载的红叶验证程序压缩包，如图：
<img alt="image" src="@source/install/windows/7.jpg">
2、将解压出来的<code v-pre>hywlyz.sql</code>文件导入到刚创建的数据库中</p>
<h2 id="二、部署程序" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#二、部署程序">#</a> 二、部署程序</h2>
<h3 id="_1、上传程序到服务器" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_1、上传程序到服务器">#</a> 1、上传程序到服务器</h3>
<p>将<code v-pre>upload</code>文件夹的内容上传到服务器，此处上传到<code v-pre>C:/hywlyz</code>目录为例，后文中出现的此路径均应修改为您使用的路径</p>
<h3 id="_2、修改配置文件" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_2、修改配置文件">#</a> 2、修改配置文件</h3>
<h4 id="_1-修改数据库连接" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_1-修改数据库连接">#</a> （1）修改数据库连接</h4>
<p>编辑<code v-pre>application-config.yml</code>，将数据库用户名更改为<code v-pre>root</code>，<code v-pre>数据库密码</code>更改为上一步获取的数据库的密码，此处请注意<code v-pre>jdbc:mysql://127.0.0.1:3306/hywlyz?useUnicode=</code>中的<code v-pre>hywlyz</code>为数据库名，若与您不一致请注意修改</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code># 数据源配置
  datasource:
    druid:
      # 主库数据源
      master:
        url: jdbc:mysql://127.0.0.1:3306/hywlyz?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull&amp;useSSL=true&amp;serverTimezone=GMT%2B8&amp;allowMultiQueries=true
        username: root
        password: 数据库密码
</code></pre><div class="highlight-lines"><br><br><br><br><br><div class="highlight-line">&nbsp;</div><div class="highlight-line">&nbsp;</div><div class="highlight-line">&nbsp;</div></div><div aria-hidden="true" class="line-numbers"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>如果配置了Redis数据库密码，需要将<code v-pre>Redis密码</code>处更改为您配置的密码</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code># redis 配置
  redis:
    # 地址
    host: 127.0.0.1
    # 端口，默认为6379
    port: 6379
    # 密码
    password: Redis密码
</code></pre><div class="highlight-lines"><br><br><br><br><br><br><br><div class="highlight-line">&nbsp;</div></div><div aria-hidden="true" class="line-numbers"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h4 id="_2-修改上传文件存储位置" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_2-修改上传文件存储位置">#</a> （2）修改上传文件存储位置</h4>
<p>编辑<code v-pre>application-config.yml</code>，将如下配置的值修改为程序下<code v-pre>uploadPath</code>所在目录（注意使用自己的实际路径）：</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>  # 文件路径 示例（ Windows配置D:/hywlyz/uploadPath，Linux配置 /opt/app/hywlyz/uploadPath）
  profile: /opt/app/hywlyz/uploadPath
</code></pre><div class="highlight-lines"><br><div class="highlight-line">&nbsp;</div></div><div aria-hidden="true" class="line-numbers"><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="_3、启动后端程序" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_3、启动后端程序">#</a> 3、启动后端程序</h3>
<p>双击<code v-pre>hy.bat</code>文件即可</p>
<p>如果出现以下提示代表启动成功
<img alt="image" src="@source/install/windows/8.jpg"></p>
<blockquote>
<p>请记录此处提示的<code v-pre>设备码</code>，需要凭此码获取授权信息</p>
</blockquote>
<h3 id="_4、启动前端程序" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#_4、启动前端程序">#</a> 4、启动前端程序</h3>
<p>在UPUPW面板依次选择<code v-pre>全局首页</code>-<code v-pre>+（添加虚拟主机）</code>
<img alt="image" src="@source/install/windows/9.jpg"></p>
<p>域名：填写您自己的域名，此处以<code v-pre>hy.coordsoft.com</code>为例
网站路径：选择上传的程序包中的<code v-pre>web</code>目录
PHP版本：无需选择</p>
<p><img alt="image" src="@source/install/windows/10.jpg"></p>
<p>创建完毕后点击<code v-pre>编辑虚拟主机</code>，选择<code v-pre>配置文件</code></p>
<p><img alt="image" src="@source/install/windows/11.jpg"></p>
<p>使用以下文字替换原本内容，注意修改文中的<code v-pre>hy.coordsoft.com</code>（共1处）为您的域名，<code v-pre>C:/hywlyz</code>（共3处）为您的程序目录</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>server {
    listen   80;
    server_name  hy.coordsoft.com;
    root   "C:/hywlyz/web";
    
    index  index.html index.htm default.html default.htm;
    autoindex off;
    
    location ~ /\.(htaccess|uini)$ {
        deny  all;
    }
    
    location = /favicon.ico {
        log_not_found off;
        access_log off;
    }
    
    include gzip.conf;
    
    location ~* .*\/(attachment|attachments|uploadfiles)\/.*\.(php|phps|php5|php7|fcgi|jsp|jspx|asp|aspx|asa|asax|ascx|ashx|asmx|axd)$ {
        deny all;
    }
    
    location / {
        root  C:/hywlyz/web;
        try_files $uri $uri/ /index.html;
        index  index.html index.htm;
    }

    location /prod-api/ {
        proxy_pass http://127.0.0.1:8080/;
        proxy_set_header Host $Host:$server_port;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header REMOTE-HOST $remote_addr;
        add_header X-Cache $upstream_cache_status;

        proxy_connect_timeout 30s;
        proxy_read_timeout 86400s;
        proxy_send_timeout 30s;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
    
    location /profile/ {
        alias C:/hywlyz/uploadPath;
    }
}
</code></pre><div class="highlight-lines"><br><br><div class="highlight-line">&nbsp;</div><div class="highlight-line">&nbsp;</div><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><div class="highlight-line">&nbsp;</div><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><div class="highlight-line">&nbsp;</div><br><br></div><div aria-hidden="true" class="line-numbers"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>保存完毕后即可通过您的域名尝试访问网站<code v-pre>http://域名/admin</code>，如果登录时提示未授权说明部署成功</p>
<h2 id="三、添加授权文件" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#三、添加授权文件">#</a> 三、添加授权文件</h2>
<p>购买请访问商城：<a href="https://hy.coordsoft.com/" rel="noopener noreferrer" target="_blank">https://hy.coordsoft.com/<ExternalLinkIcon/></a>
购买注册码后点击 <a href="https://hy.coordsoft.com/getLicense" rel="noopener noreferrer" target="_blank">获取授权<ExternalLinkIcon/></a> 为您的网站在线授权</p>
<h2 id="四、-登录网站" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#四、-登录网站">#</a> 四、 登录网站</h2>
<p>至此，整个网站部署完成，如果一切正常，您可通过您的域名访问属于您的红叶网络验证系统</p>
<p>商店地址：http://域名</p>
<p>后台地址：http://域名/admin</p>
<p>默认管理员账号密码：admin/admin123</p>
<h2 id="五、初始化设置" tabindex="-1"><a aria-hidden="true" class="header-anchor" href="#五、初始化设置">#</a> 五、初始化设置</h2>
<p>请登入网站后台后首先打开<code v-pre>系统配置</code>-<code v-pre>网站配置</code>菜单页，将默认信息修改为您的信息，其中<code v-pre>网站域名</code>必须修改为您的实际访问域名(IP)</p>
</div></template>


