@echo off
rem 停止程序

set application_name=ExampleApplication
for /f "delims=" %%j in ('jps') 		^
do for %%p in (%%j) 					^
do if %%p==%application_name% ( for %%s in (%%j) do 	^
echo %%s|findstr /be "[0-9]*" >nul  	^
&& ( echo match pid:%%s & taskkill /F /PID %%s ) 	^
|| echo %%s was stopped & exit /b )						^
else  echo Looking For %application_name% ...

echo ---Not Found %application_name%!---