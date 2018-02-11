[TOC]

### 1、简介

前段时间有项目有读写分离的需要，因此完成了该类库`mybatis-read-write-split`来实现读写分离。

* 特点
支持两种模式的主备分离：
1. 业务透明的读写分离。自动解析sql的读写类型并进行路由转发。
2. 基于注解的读写分离。通过注解中的配置来进行读写分离。

以上两种模式可以混合使用：缺省自动解析sql的读写类型，如果注解有指定数据源，则根据注解进行路由。

### 2、用法
* pom.xml 添加依赖

```xml
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-read-write-split-core</artifactId>
            <version>2.0-SNAPSHOT</version>
        </dependency>
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

#### 2.1、业务透明区分读写

mybatis自动分析读or写操作，并进行相应的路由操作

* mybatis配置文件添加interceptor

```xml
   <plugins>
        <plugin interceptor="org.mybatis.rw.interceptor.ReadWriteDistinguishInterceptor">
        </plugin>
    </plugins>
```

#### 2.2、通过注解区分读写

通过方法上的注解显示指定读主库or备库

* 在目标方法上添加 `@DataSource()`

如

```java
    @DataSource(DataSourceType.MASTER)
    public User getUserByIdFromMaster(Integer userId) {
        //some operation
    }
```


### 3、内部实现

#### 2.1、业务透明区分读写
![](https://raw.githubusercontent.com/chenzz/static-resource/master/941DC39B-846A-4F86-8F61-F810F9543AB0.png)

1. Mapper调用MyBatis进行读写
2. MyBatis分析读写类型，并存入ThreadLocal中
3. 自定义DataSource从ThreadLocal里获取读写类型，路由给对应的子DataSource
4. 使用对应的子DataSource进行读写操作

#### 2.2、通过注解实现读写区分

1. Spring的切面读取注解的内容，分析 读/写 操作
2. 把分析结果丢到 ThreadLocal中
3. 自定义DataSource从ThreadLocal里获取DataSource类型，路由给对应的子DataSource
4. 使用对应的子DataSource进行读写操作

