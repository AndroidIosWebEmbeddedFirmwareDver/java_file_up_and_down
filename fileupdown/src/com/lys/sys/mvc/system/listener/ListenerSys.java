package com.lys.sys.mvc.system.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lys.sys.log.Log;
import com.lys.sys.mvc.system.listener.init.InitSysData;



/**
 * 系统初始化
 * @author lys
 *
 */
public class ListenerSys implements ServletContextListener{
	/**
	 * 系统停止时执行该方法
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		Log.in.info("系统结束成功!");
	}
	/**
	 * 系统启动时执行该方法
	 */
	public void contextInitialized(ServletContextEvent event) {
		Log.in.info("系统监听开始了!");
		Log.in.info("====系统数据初始化开始了!=====");
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		InitSysData initData=context.getBean("InitSysData",InitSysData.class);
		initData.init(event);
		Log.in.info("====系统数据初始化结束了!=====");
	}

}
