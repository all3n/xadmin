export PROJECT=xadmin
export PROJECT_DESC=$(cat << EOF
 ${PROJECT}
EOF
)
if [[ "$DEBUG" == "true" ]];then
  set -x
fi

. $APP_HOME/bin/functions.sh

if [[ -d $APP_HOME/bin/action.d ]];then
  for ACTION_SH in $(ls $APP_HOME/bin/action.d/*.sh|sort);do
    . $ACTION_SH
  done
fi

# this option only use dev mode
if [[ -f $APP_HOME/dev.env.sh ]];then
   source $APP_HOME/dev.env.sh
fi

