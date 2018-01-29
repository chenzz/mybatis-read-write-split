package org.mybatis.rw.datasource;

import org.mybatis.rw.holder.DbOperationType;
import org.mybatis.rw.holder.DbOperationTypeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ReadWriteSplitSource extends AbstractRoutingDataSource
{

    private Logger LOGGER = LoggerFactory.getLogger(ReadWriteSplitSource.class);
    private static final String MASTER = "master";

    private Object masterDataSource;
    private List<Object> slaveDataSourceList;

    private Map<Object, Object> allDataSources;
    private List<Object> readDataSourceKeyList;

    public void setMasterDataSource(Object masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public void setSlaveDataSourceList(List<Object> slaveDataSourceList) {
        this.slaveDataSourceList = slaveDataSourceList;
    }


    public void afterPropertiesSet() {

        //参数检查
        if (null == masterDataSource) {
            throw new IllegalArgumentException("Property 'masterDataSource' is required");
        }
        if (null == slaveDataSourceList) {
            throw new IllegalArgumentException("Property 'slaveDataSourceList' is required");
        }

        //把masterDataSource和slaveDataSourceList丢进targetDataSources中
        allDataSources = new HashMap<>();
        readDataSourceKeyList = new ArrayList<>(slaveDataSourceList.size());


        //添加主库
        allDataSources.put(MASTER, masterDataSource);

        //添加从库
        for (int i = 0; i < slaveDataSourceList.size(); i++) {
            String dataSourceKey = "slave" + i;
            allDataSources.put(dataSourceKey, slaveDataSourceList.get(i));
            readDataSourceKeyList.add(dataSourceKey);
        }

        //设置父类的targetDataSource属性
        super.setDefaultTargetDataSource(masterDataSource);
        super.setTargetDataSources(allDataSources);


        super.afterPropertiesSet();
    }

    //获取数据源对应的key
    @Override
    protected Object determineCurrentLookupKey()
    {
        DbOperationType dbOperationType = DbOperationTypeHolder.getDataSourceType();

        if (DbOperationType.READ == dbOperationType) {

            //如果读操作，读数据源中随机获取一个数据源
            int randomNum = ThreadLocalRandom.current().nextInt(0, readDataSourceKeyList.size());
            String dataSourceKey = (String) readDataSourceKeyList.get(randomNum);
            LOGGER.debug("operateType: read");
            LOGGER.debug("dataSourceKey: " + dataSourceKey);

            return dataSourceKey;
        } else {

            //如果更新操作，返回主库
            LOGGER.debug("operateType: write");
            LOGGER.debug("dataSourceKey: " + MASTER);

            return MASTER;
        }
    }

}