package com.lys.sys.mvc.system.listener.init;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;

import org.springframework.stereotype.Component;

import com.lys.sys.log.Log;
import com.lys.sys.mvc.biz.BaseBiz;
import com.lys.sys.mvc.system.dictionary.SysStatic;
import com.lys.sys.utils.MessageSourceHelper;

/**
 * 一个数据初始化类
 * @author lys
 *
 */
@Component("InitSysData")
public class InitSysData {
	@Resource(name="BaseBizImpl")
	public BaseBiz baseBiz;//操作数据的对象biz
	@Resource(name="MessageSourceHelper")
	public MessageSourceHelper message;//读取国际化对象
	/**
	 * 系统初始化运行的方法
	 */
	public void init(ServletContextEvent event){ 
		SysStatic.i18nmessage=message;
		SysStatic.baseBiz=baseBiz;
		Log.in.info("初始化国际化对象"+SysStatic.i18nmessage);
		Log.in.info("初始化数据操作对象"+SysStatic.baseBiz);
	}
}
