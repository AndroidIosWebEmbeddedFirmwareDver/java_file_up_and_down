package com.lys.power.intercept;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lys.sys.log.Log;
import com.lys.sys.mvc.biz.BaseBiz;
/***
 * 方法拦截器---权限控制处
 * springmvc.xml注入
 * @author lys
 *
 */
public class MethodsInterceptor extends HandlerInterceptorAdapter {
	@Resource(name="BaseBizImpl")
	public BaseBiz baseBiz;
	/**
	 * 在Controller方法执行前进行拦截
	 */
	  /** 
     * 在业务处理器处理请求之前被调用 
     */  
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handler2=(HandlerMethod) handler;
		if(handler2!=null){
			StringBuffer s=new StringBuffer("本次准备访问方法的绝对路劲：");
			s.append(handler2.getBeanType().getName()+" ");//访问的类路径
			Method method=handler2.getMethod();
			s.append( Modifier.toString(method.getModifiers())+" " + method.getReturnType().getSimpleName() + " " + method.getName() + "(");
			for (Class<?> param : method.getParameterTypes()) {
				s.append(param.getSimpleName()).append(",");
			}
			if(method.getParameterTypes().length>0){
				s.deleteCharAt(s.length() - 1).append(")");
			}else{
				s.append(")");
			}
			Log.in.info(s.toString()); 
			//权限控制
			
			
		}
		return true;
	}
	/**
	 * 在Controller方法执行后进行拦截
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null){
			Log.in.info("跳转页面：WEB-INF/jsp/"+modelAndView.getViewName()+".jsp");
		}
	}
	/**
	 * 在DispatcherServlet完全处理完请求后被调用，可以在该方法中进行一些资源清理的操作???
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		Log.in.info("【执行action方法完毕】");
	}
	
}
