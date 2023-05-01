## 初始化设置
系统搭建完毕后请登入网站后台后首先打开`系统配置`-`网站配置`菜单页，将默认信息修改为您的信息，其中`网站域名`必须修改为您的实际访问域名(或IP)

![image](./1.jpg)

## 配置开机自启
登入宝塔后台，打开`计划任务`页面，任务类型选择`Shell脚本`，任务名称可随意设置，执行周期选择`N分钟`-`1分钟`，脚本内容如下：  
![image](./2.jpg)  
  
:::: code-group
::: code-group-item Linux-CentOs
```shell{6}
# 脚本代码（注意其中的/opt/app/hywlyz使用自己的实际路径）

pid=`ps -ef|grep hywlyz-.*-release.jar|grep -v grep|awk '{print $2}' `
# 如果不存在返回1，存在返回0 
if [ -z "${pid}" ]; then
 cd /opt/app/hywlyz && sh hy.sh start
else
 echo "hywlyz is already running. pid=${pid} ."
fi
```
:::
::: code-group-item Windows
```batch{9}
@REM 脚本代码（注意其中的C:/hywlyz使用自己的实际路径）

@echo off
tasklist /FI "IMAGENAME eq hy.exe" 2>NUL | find /I /N "hy.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo Program is running   
) else (
    echo Program is NOT running
    start /MIN /d"C:/hywlyz" hy.bat
)
pause
```
:::
::::
