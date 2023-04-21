#! /bin/sh
#
# 4-dump.sh
# Copyright (C) 2023 wanghuacheng <wanghuacheng@wanghuacheng-PC>
#
# Distributed under terms of the MIT license.
#



start_dump_mysql_to_sqlite(){
	mysql_host=127.0.0.1
	mysql_user=root
	mysql_dbname=xadmin
	mysql_port=23306
    mysql_pass=xadmin123
	mkdir -p $APP_HOME/tmp
    $APP_HOME/bin/utils/mysql2sqlite $APP_HOME/docker/mysql/sql/000-xadmin.sql|sed -e "s/_binary ''/1/g" | sed -e "s/_binary '\\\0'/0/g" > $APP_HOME/tmp/sqlite.sql
}


