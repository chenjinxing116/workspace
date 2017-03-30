
rem 计算源码目录
rem	-------------------------------------------------------------------------
set myself=%~dp0
set project_dir=%myself%
rem	-------------------------------------------------------------------------

echo %deploy_root%
IF "%deploy_root%"=="" (
	set deploy_root=%myself%package
) 
set deploy_war=%deploy_root%/gmvcs-doc


rem	-------------------------------------------------------------------------

rem 生成目标目录
rem	-------------------------------------------------------------------------
mkdir "%deploy_war%"

pushd %project_dir%target
	del "target" /S /F /Q
popd

pushd %project_dir%
	cmd.exe /c mvn -f pom.xml package
popd

pushd %project_dir%
	xcopy	"target\*.war" "%deploy_war%" /Y  /I	
popd

rem 正在复制gmdoc文档和sql脚本，请稍等..
pushd %myself%
	cd..
	set "bd=%cd%"
	xcopy "%bd%\docs" "%deploy_root%\docs" /Y  /I	
popd
rem	-------------------------------------------------------------------------
pushd %myself%
	cd..
	set "bd=%cd%"
	xcopy "%bd%\sql" "%deploy_root%\sql" /Y  /I	
popd
