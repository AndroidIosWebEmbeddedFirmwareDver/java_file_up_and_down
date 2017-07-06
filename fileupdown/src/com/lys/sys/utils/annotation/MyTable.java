package com.lys.sys.utils.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库对应表----------自定义注解
 * @author shuang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyTable {
	/**
	 * 表名--不能为空
	 * @return
	 */
	public String tableName();
	/**
	 * 表主键名---默认为"" ，一般用于自增主键获取
	 * @return
	 */
	public String pkName() default "";
}