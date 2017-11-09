本项目适用于对SpringBoot框架的初步学习：

###一、项目概况

1、项目介绍：文件共享系统用于更细粒度的文件下载和查看操作管理，包含了文件管理，目录管理，用户管理三大模块。

2、总体架构：后端利用SpringDataJPA用于管理数据层、SpringSecurity用于登录以及浅层的权限管理。同时还配置了EhCache、JavaMail模块来解决缓存和邮件发送需求；项目前端很自然地结合了SpringBoot框架推荐的Thymeleaf模版引擎，包含了Layout、Security标签的使用；项目使用Gradle进行自动化构建。

3、特别说明：由于前端代码大部分不是由本人完成的，可能还存在一些缺陷没有被修正或者测试出来。

###二、项目启动

说明：git导入项目后，可能会出现编译错误（类名不匹配）问题，是由于git对大小写不敏感，后期对（VO）相关类名进行规范修改后未生效，请手动将出错的类名进行修改（后缀VO全大写）。

由于项目使用的SpringBoot框架，在将resources文件下的sql导入到本地以及按照c3p0.sample.properties注释内容修改c3p0配置后。可直接运行Application类启动项目。

###三、要点解析

1、c3p0连接池配置，直接将返回ComboPooledDataSource()类的DataSource的方法注释成bean，SpringBoot会自动搜索读取配置文件。

2、本项目使用到了SpringDataJPA的高级功能，JpaSpecificationExecutor动态查询。具体参见文件管理后台代码。

3、如有任何疑问、意见或者建议欢迎使用Issue或者通过邮箱取得联系，乐意与大家共同探讨、交流与学习。