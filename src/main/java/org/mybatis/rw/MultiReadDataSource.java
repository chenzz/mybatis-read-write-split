package org.mybatis.rw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MultiReadDataSource extends AbstractRoutingDataSource
{

    private Logger LOGGER = LoggerFactory.getLogger(MultiReadDataSource.class);
    private static final String MASTER = "master";

    private Object masterDataSource;
    private List<Object> slaveDataSourceList;

    private Map<Object, Object> allDataSources;
    private List<Object> allDataSourceKeyList;

    public void setMasterDataSource(Object masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public void setSlaveDataSourceList(List<Object> slaveDataSourceList) {
        this.slaveDataSourceList = slaveDataSourceList;
    }


    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        //参数检查
        if (null == masterDataSource) {
            throw new IllegalArgumentException("Property 'masterDataSource' is required");
        }
        if (null == slaveDataSourceList) {
            throw new IllegalArgumentException("Property 'slaveDataSourceList' is required");
        }

        //把masterDataSource和slaveDataSourceList丢进targetDataSources中
        allDataSources = new HashMap<>();
        allDataSources.put(MASTER, masterDataSource);
        for (int i = 0; i < slaveDataSourceList.size(); i++) {
            allDataSources.put("slave" + i, slaveDataSourceList.get(i));
        }

        //设置父类的targetDataSource属性
        super.setDefaultTargetDataSource(masterDataSource);
        super.setTargetDataSources(allDataSources);


        //获取所有的dataSource的key
        this.allDataSourceKeyList = new ArrayList<>(this.allDataSources.size());
        for (Map.Entry entry : this.allDataSources.entrySet()) {
            Object lookupKey = resolveSpecifiedLookupKey(entry.getKey());
            this.allDataSourceKeyList.add(lookupKey);
        }
    }

    //获取数据源对应的key
    @Override
    protected Object determineCurrentLookupKey()
    {
        DbOperationType dbOperationType = DbOperationTypeHolder.getDataSourceType();

        if (DbOperationType.READ == dbOperationType) {

            //如果读操作，从所有数据源中随机获取一个数据源
            int randomNum = ThreadLocalRandom.current().nextInt(0, allDataSourceKeyList.size());
            String dataSourceKey = (String) allDataSourceKeyList.get(randomNum);
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