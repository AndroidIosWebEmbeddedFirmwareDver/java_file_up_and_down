package com.lys.power.listener.init;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.lys.power.biz.PowerCacheBiz;
import com.lys.power.utils.PowerStatic;
import com.lys.sys.log.Log;
import com.lys.sys.mvc.system.dictionary.SysStatic;
import com.lys.sys.utils.StringUtils;

/**
 * 一个数据初始化类
 * @author lys
 *
 */
@Component("InitPowerData")
public class InitPowerData {
	@Resource(name="PowerCacheBizImpl")
	public PowerCacheBiz powerCacheBiz;//操作数据的对象biz
	/**
	 * 系统初始化运行的方法
	 */
	public void init(ServletContextEvent event){ 
		PowerStatic.powerCacheBiz=powerCacheBiz;//缓存对象赋值
		PowerStatic.fileUpUrlDefault= event.getServletContext().getRealPath("/");//文件上传后的根目录的默认路径，默认值=tomcat下的项目目录
		//属性初始化
		ClassPathResource resource=new ClassPathResource("properties/jdbc.properties");
		Properties prop;
		try {
			/**如果没有Tomcat集群的话，将要手动更新在线用户的登录日志记录状态*/
			
			prop = PropertiesLoaderUtils.loadProperties(resource);
			PowerStatic.fileUpUrlSet=prop.getProperty("fileUpUrlSet");//文件上传后的根目录的设置路径 
			//如果没有设置fileUpUrlSet ，则默认为tomcat下的项目目录
			if(StringUtils.hasText(PowerStatic.fileUpUrlSet)){
				PowerStatic.fileUpUrlSet=PowerStatic.fileUpUrlDefault;
			}
			SysStatic.dbName=StringUtils.hasText(prop.getProperty("dbName"))?prop.getProperty("dbName"):"MYSQL";//当前使用的数据库名,默认是MYSQL
		}catch(Exception e){
			e.printStackTrace();
			Log.in.error("读取jdbc.properties文件异常:"+e.getMessage());
		}
		Log.in.info("初始化字典缓存对象PowerStatic.powerCacheBiz="+PowerStatic.powerCacheBiz);
		Log.in.info("初始化PowerStatic.fileUpUrlDefault="+PowerStatic.fileUpUrlDefault);
		Log.in.info("初始化PowerStatic.fileUpUrlSet="+PowerStatic.fileUpUrlSet);
		Log.in.info("初始化SysStatic.dbName="+SysStatic.dbName);
	}
}
