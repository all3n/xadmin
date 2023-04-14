# xadmin

## BUILD
1. mvn clean package
1. cd xadmin-ui; npm install; npm run dev

## ADD MODULE
1. gen code
```
作者名称
all3n
 类上面的作者名称
模块名称
xadmin-hello
 模块的名称，请选择项目中已存在的模块
至于包下
com.devhc.xadmin
 项目包的名称，生成的代码放到哪个包里面
接口名称
Hello
 接口的名称，用于控制器与接口文档中
前端路径
xadmin-ui/src/views/hello
 输入views文件夹下的目录，不存在即创建
去表前缀
默认不去除表前缀
 默认不去除表前缀，可自定义
是否覆盖 否
```
2. add menu
```
编辑菜单
菜单类型 目录 [菜单] 按钮 
菜单图标 app 
外链菜单是 否 
菜单缓存是 否
菜单可见是 否 
菜单标题 Hello管理
权限标识
权限标识
路由地址 hello
菜单排序 999
组件名称 Hello
组件路径 hello/index
上级类目
```
3. enable menu in role

## hot dev
1. Settings——Build,Execution,Deployment——Compiler——✔Build project automatically 
2. Settings——Advanced Settings——✔Allow auto-make to start even if developed application is currently running