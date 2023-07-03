#! /bin/sh
start_gen_rsa(){
    java -cp $APP_HOME/xadmin-framework/target/xadmin-framework-1.0.0-SNAPSHOT.jar com.devhc.xadmin.utils.RsaUtils
    echo "update public key in :xadmin-ui/src/utils/rsaEncrypt.js"
    echo "update private key in:xadmin-framework/src/main/resources/config/application.yml"
    echo "update init-sql user password:docker/mysql/sql/000-xadmin.sql "
}

