package com.lys.sys.mvc.system.dictionary;

import com.lys.sys.mvc.biz.BaseBiz;
import com.lys.sys.utils.MessageSourceHelper;

/**
 * 一个存储系统 部分重要属性 值 的类
 * @author lys
 *
 */
public class SysStatic {
	/**
	 * 系统属性
	 */
	public static String dbName="MYSQL";//当前使用的数据库名,默认是MYSQL
	/**
	 * 各种数据库名
	 */
	public static String MYSQL="MYSQL";
	public static String ORACLE="ORACLE";
	/**
	 * 其他
	 */
	public static MessageSourceHelper i18nmessage;//读取国际化语言文件对象，在系统初始化赋值
	public static BaseBiz baseBiz;//读取数据操作对象
}
