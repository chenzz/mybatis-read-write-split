package org.mybatis.rw.holder;

import org.mybatis.rw.constant.DataSourceType;
import org.mybatis.rw.constant.DbOperationType;

/**
 * @Author: chenzz
 * @Date: 2018-01-10
 * @Description:
 */
public class DataSourceInfoHolder {

    private static ThreadLocal<DataSourceType> dataSourceTypeThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<DbOperationType> dbOperationTypeThreadLocal = new ThreadLocal<>();


    public static void setDataSourceType(DataSourceType dataSourceType) {
        dataSourceTypeThreadLocal.set(dataSourceType);
    }

    public static DataSourceType getDataSourceType() {
        return dataSourceTypeThreadLocal.get();
    }

    public static void setDbOperationType(DbOperationType dbOperationType) {
        dbOperationTypeThreadLocal.set(dbOperationType);
    }

    public static DbOperationType getDbOperationType() {
        DbOperationType dbOperationType = dbOperationTypeThreadLocal.get();

        if (null == dbOperationType) {
            dbOperationType = DbOperationType.UPDATE;
        }

        return dbOperationType;
    }

    public static void clearDataSourceType() {
        dataSourceTypeThreadLocal.remove();
    }

}
