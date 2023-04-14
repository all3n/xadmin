#!/usr/bin/env bash
set -x
for SQL in `ls /docker-entrypoint-initdb/*.sql|sort`;do
  mysql -uroot -pxadmin xadmin < $SQL
done
