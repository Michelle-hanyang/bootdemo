package com.example.bootdemo.config.dds;

/**
 * Author: YANG
 * Date: 2022/10/9 16:19
 * Describe:
 */

import java.lang.annotation.*;

/**
 * 动态数据源注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    /**
     * 数据源key值
     * @return
     */
    String value();
}
