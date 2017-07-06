package com.lys.sys.utils.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库对应表的对应字段----------自定义注解
 * @author shuang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyColumn {
	/**
	 * 列名---默认为"" 
	 * @return
	 */
	public String column();
}