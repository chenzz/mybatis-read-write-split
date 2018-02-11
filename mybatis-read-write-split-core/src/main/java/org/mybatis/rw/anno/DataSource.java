package org.mybatis.rw.anno;

import org.mybatis.rw.constant.DataSourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: chenzz
 * @Date: 2018-01-10
 * @Description:
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    DataSourceType value();
}
