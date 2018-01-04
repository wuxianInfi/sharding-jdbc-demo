# sharding-jdbc-demo概述
 - 集成sharding-jdbc实现分库分表，主从分离等功能
 - mybatis作为ORM, 自定义mybatis-plugin
 - spring boot项目直接启动
# 运行
 - 创建数据库order_db0, order_db0_slave0, order_db1, order_db1_slave0
 - 分别执行sql-scripts/order_ddl.sql
 - run Application.java
 - restapi in OrderController.java