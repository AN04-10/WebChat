package com.webChat.commons;

import java.lang.annotation.*;

/**
 * 数据范围
 * @author liuhua
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 表别名
     * @return
     */
    public String tableAlias() default "";

    /**
     * 字段名
     * @return
     */
    public String tableField() default "";
}
