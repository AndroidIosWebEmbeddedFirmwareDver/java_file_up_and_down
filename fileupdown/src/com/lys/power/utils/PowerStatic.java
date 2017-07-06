package com.lys.power.utils;

import com.lys.power.biz.PowerCacheBiz;

/***
 * 权限系统的公共类
 * @author lys
 *
 */
public class PowerStatic {
	public static String fileUpUrlSet;//文件上传后的根目录的设置路径 ， 在InitPowerData中配置上传地址
	public static String fileUpUrlDefault;//文件上传后的根目录的默认路径 ，默认值=tomcat下的项目目录， 在InitPowerData中配置上传地址
	public static String USERNAME="developadmin";//开发者帐号
	//权限缓存对象-权限系统初始化赋值
	public static PowerCacheBiz powerCacheBiz;
	
}
