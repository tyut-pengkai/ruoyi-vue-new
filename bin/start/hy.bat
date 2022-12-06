@echo off
set ver=1.1.0
mode con cols=160
title hywlyz-%ver%
:start
for /f "tokens=2,11 delims= " %%a in ('tasklist /V ^| findstr hywlyz ^| findstr /V findstr ^| findstr /V hywlyz-%ver%') do (
	taskkill /f /PID %%a
	goto start
)
echo %~dp0bin\hy-%ver%
%~dp0bin\hy-%ver% java -Xmx1024m -Xms256m -Dloader.path=%~dp0lib -Dfile.encoding=utf-8 -Dlog.charset=gbk -jar %~dp0hywlyz-%ver%-release.jar
pause