package org.mybatis.rw;

/**
 * @Author: chenzz
 * @Date: 2018-01-10
 * @Description:
 */
public class DbOperationTypeHolder {

    private static ThreadLocal<DbOperationType> dbOperationTypeThreadLocal = new ThreadLocal<>();

    public static void setDataSourceType(DbOperationType dbOperationType) {
        dbOperationTypeThreadLocal.set(dbOperationType);
    }

    public static DbOperationType getDataSourceType() {
        DbOperationType dbOperationType = dbOperationTypeThreadLocal.get();

        if (null == dbOperationType) {
            dbOperationType = DbOperationType.UPDATE;
        }

        return dbOperationType;
    }

    public static void clear() {
        dbOperationTypeThreadLocal.set(null);
    }
}
