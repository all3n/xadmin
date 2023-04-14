
start_gen_app(){
    local NAME=$1
    local MOD_DIR=$APP_HOME/xadmin-$NAME
    mkdir -p $MOD_DIR
    mkdir -p $MOD_DIR/src/main/sql
    mkdir -p $MOD_DIR/src/main/java/com/devhc/xadmin/domain
    mkdir -p $MOD_DIR/src/main/java/com/devhc/xadmin/repository
    mkdir -p $MOD_DIR/src/main/java/com/devhc/xadmin/rest
    mkdir -p $MOD_DIR/src/main/java/com/devhc/xadmin/service
    mkdir -p $MOD_DIR/src/main/java/com/devhc/xadmin/service/dto
    mkdir -p $MOD_DIR/src/main/java/com/devhc/xadmin/service/impl
    mkdir -p $MOD_DIR/src/main/java/com/devhc/xadmin/service/mapstruct
    cp $APP_HOME/templates/pom.xml $MOD_DIR/
}
