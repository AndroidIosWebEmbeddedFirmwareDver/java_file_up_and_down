package com.lys.power.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lys.power.listener.init.InitPowerData;
import com.lys.sys.log.Log;

public class ListenerPower  implements ServletContextListener{
	/**
	 * 系统停止时执行该方法
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		Log.in.info("权限系统监听结束成功!");
	}
	/**
	 * 系统启动时执行该方法
	 */
	public void contextInitialized(ServletContextEvent event) {
		Log.in.info("权限系统监听开始了!");
		Log.in.info("====权限系统数据初始化开始了!=====");
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		InitPowerData initData=context.getBean("InitPowerData",InitPowerData.class);
		initData.init(event);
		Log.in.info("====权限系统数据初始化结束了!=====");
	}

}
