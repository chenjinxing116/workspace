#!/bin/sh
pid_filename=local.pid
pid=`cat $pid_filename`
kill $pid

echo 正在停止进程 $pid ......

application_count=`ps -ef | grep -v grep | grep $pid | wc -l`

#循环，为了让脚本一直运行监控
while [ $application_count -ne 0 ]
do
    sleep 3s   # 每次监测时间3秒
	
	application_count=`ps -ef | grep -v grep | grep $pid | wc -l`
done

echo 停止进程 $pid 完成!

exit 0

