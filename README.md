[TOC]

### 1、简介

* 用处

mybatis-read-write-split 用来实现**业务透明**的读写分离mybatis类库。

* 原理

通过设置主库进行写操作，多个备库进行读操作。

* 好处

有效减小了DB的压力，提高了DB的并发能力。

### 2、用法

* pom.xml 添加依赖

```xml
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-read-write-split-core</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

* mybatis配置文件添加interceptor

```xml
   <plugins>
        <plugin interceptor="org.mybatis.rw.ReadWriteDistinguishInterceptor">
        </plugin>
    </plugins>
```

* 配置数据源

```xml
    <!--替换原本的DataSource-->
    <bean id="dataSource" class="org.mybatis.rw.MultiReadDataSource">
        <property name="masterDataSource" ref="masterDataSource"/>
        <property name="slaveDataSourceList">
            <list>
                <ref bean="slaveDataSource1"></ref>
                <ref bean="slaveDataSource2"></ref>
            </list>
        </property>
    </bean>
    
    <bean id="masterDataSource" class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close">
        <!--各数据源配置-->
    </bean>
    
    <bean id="slaveDataSource1" class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close">
        <!--各数据源配置-->
    </bean>
    
    <bean id="slaveDataSource2" class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close">
        <!--各数据源配置-->
    </bean>
```

### 3、内部实现

![](https://ws3.sinaimg.cn/large/006tNc79ly1fnxk33nhwdj315f0uqtbm.jpg)

1. Mapper调用MyBatis进行读写
2. MyBatis分析读写类型，并存入ThreadLocal中
3. 自定义DataSource从ThreadLocal里获取读写类型，根据读写类型通过对应的DataSource
4. 使用选出的DataSource进行读写操作

