# 安装并运行

### windows下

依赖：java1.7+ 、tomcat7、mysql

#### 1、git clone 本项目

在项目下载路径运行  
```
git clone http://gitlab.fjutacm.com/win2acm/vvv.git
```

#### 2、用idea打开，配置好 jdk、tomcat、git路径

菜单`file->Project Structure`  
设置选项`Project->Project SDK`  
设置`Project Language Level`为7以上

#### 3、将jar包都包含在项目中

都在`web/WEB-INF/lib`和`include`路径下  
选中jar包，右键`Add as Library`

#### 4、mysql导入数据

在mysql的bin目录运行
```
mysql -uroot -p vj < init.sql
```

#### 5、更改配置

更改`src/Config/GlobalVariables.json`配置中的mysql数据库连接和账号密码

#### 6、运行

### 打包到linux运行

1、linux下安装openjdk1.8、tomcat7、mysql

2、将windows下`out/artifacts/`下对应的目录传送到linux下`tomcat/webapp/`下

3、mysql导入数据

4、启动tomcat

#### 注：评测机是另外一个项目，单独安装