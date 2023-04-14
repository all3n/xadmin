start_dev(){
  cd $APP_HOME
  JVM_OPTS="-Dname=$PROJECT -Duser.timezone=Asia/Shanghai \
    -Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m  \
    -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails \
    -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"
  LOG_PATH=$APP_HOME/logs
  mkdir -p $LOG_PATH
  nohup java -jar $JVM_OPTS $APP_HOME/xadmin-framework/target/xadmin.jar >$LOG_PATH/xadmin.log 2>&1 &
  echo $! > $APP_HOME/run.pid
}
start_stop(){
  PID_FILE=$APP_HOME/run.pid 
  if [[ -f $PID_FILE ]];then
    PID=$(cat $PID_FILE)
    echo "kill $PID"
    kill $PID
  fi
}
