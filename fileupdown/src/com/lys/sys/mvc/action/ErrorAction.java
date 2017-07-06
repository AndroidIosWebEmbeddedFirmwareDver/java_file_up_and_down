package com.lys.sys.mvc.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 异常处理 Action
 * @author shuang
 */
@Scope(value = "prototype")
@Controller("ErrorAction")
@RequestMapping(value = "sys/error")
public class ErrorAction extends SysAction {
	/**
	 * SESSION并发处理
	 * @return
	 */
	@RequestMapping(value="sessionFailure")
	public String sessionFailure(){
		return "sys/error/sessionFailure";	
	}
	/**
	 * 没有访问url的菜单
	 * @return
	 */
	@RequestMapping(value="nonFunction")
	public String nonFunction(){
		return "sys/error/nonFunction";	
	}
}
