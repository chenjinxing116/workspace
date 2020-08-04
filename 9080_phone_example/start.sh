#!/bin/sh
# 批处理文件接口约定
#	%1	dist_root:			项目总根目录
#	%2	dist_bin:			二进制文件目录
#	%3	dist_lib:			库文件目录
#	%4	dist_config:		配置文件目录
#	-------------------------------------------------------------------------

#	-------------------------------------------------------------------------
myself=`which $0`
myself=`dirname ${myself}`
myself=`cd "$myself"; pwd`

echo $myself

dist_root=$myself
dist_bin=$dist_root
dist_lib=$dist_root/lib
dist_config=$dist_root/config

#从系统变量中获取配置文件路径
#	-------------------------------------------------------------------------
properties=file:///$dist_root/config/application.properties
echo $properties
#	-------------------------------------------------------------------------
echo $dist_root

# start
#	-------------------------------------------------------------------------
cd $dist_root
java -Dfile.encoding=UTF-8 \
$LEVAM_JAVA_MEM \
$LEVAM_JAVA_GC \
-cp $dist_lib/*:$dist_bin/* ExampleApplication $properties
# exec echo $! > $pid_filename
#	-------------------------------------------------------------------------

