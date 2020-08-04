
@set dist_root=%cd%
@set dist_bin=%dist_root%
@set dist_lib=%dist_root%\lib
@set dist_etc=file:\\\%dist_root%\config\application.properties

@rem 计算本地路径
@rem	-------------------------------------------------------------------------

@rem 运行
@rem	-------------------------------------------------------------------------
@pushd %dist_root%

@echo starting...

@start "%1" cmd /c java  -Dfile.encoding=UTF-8 -cp %dist_lib%\*;%dist_bin%\* com.chen.ExampleApplication  %dist_etc%
@echo %1>local.pid

@echo start server success!
@popd
@rem	-------------------------------------------------------------------------