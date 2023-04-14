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
	# dump the mysql database to a txt file
	#mysqldump \
	#  --skip-create-options \
	#  --compatible=ansi \
	#  --skip-extended-insert \
	#  --compact \
	#  --single-transaction \
	#  -h$mysql_host \
	#  -u$mysql_user \
	#  -P$mysql_port \
	#  -p$mysql_pass \
    #   $mysql_dbname \
	#  > $APP_HOME/tmp/localdb.txt
	
	## remove lines mentioning "PRIMARY KEY" or "KEY"
	#cat $APP_HOME/tmp/localdb.txt \
	#  | grep -v "PRIMARY KEY" \
	#  | grep -v KEY \
	#  > $APP_HOME/tmp/localdb.txt.1
	#
	## mysqldump leaves trailing commas before closing parentheses  
	#perl -0pe 's/,\n\)/\)/g' $APP_HOME/tmp/localdb.txt.1 > $APP_HOME/tmp/localdb.txt.2
	#
	## change all \' to ''
	#sed -e 's/\\'\''/'\'''\''/g' $APP_HOME/tmp/localdb.txt.2 > $APP_HOME/tmp/localdb.txt.3


    $APP_HOME/bin/utils/mysql2sqlite $APP_HOME/docker/mysql/sql/000-xadmin.sql|sed -e "s/_binary ''/1/g" | sed -e "s/_binary '\\\0'/0/g" > $APP_HOME/tmp/sqlite.sql
}


