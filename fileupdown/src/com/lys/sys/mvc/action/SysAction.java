package com.lys.sys.mvc.action;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.lys.sys.mvc.biz.BaseBiz;
import com.lys.sys.utils.MessageSourceHelper;

/***
 * 公用action方法
 * @author lys
 *
 */
public class SysAction {
	@Resource(name="BaseBizImpl")
	protected BaseBiz baseBiz;//业务执行对象
	@Resource(name="MessageSourceHelper")
	protected MessageSourceHelper message;//国际化语音
	/**
	 * request、response、session对象 action必须是非单例
	 */
	protected HttpServletRequest request; //action必须是非单例
    protected HttpServletResponse response; //action必须是非单例 
    protected HttpSession session;  //action必须是非单例
    protected Model model;  //action必须是非单例 
      
    @ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response,Model model){  
        this.request = request;  
        this.response = response;  
        this.session = request.getSession(); 
        this.model=model;
    } 
    /***
     * 获取request中的参数
     * @param key 值
     * @return
     */
    public String paramsMap(String key){
    	return paramsMap(key, ",");
    }
    /***
     * 获取request中的参数
     * @param key 值
     * @param splt 分隔值
     * @return
     */
    public String paramsMap(String key,String splt){
    	return org.apache.commons.lang.StringUtils.join(this.request.getParameterMap().get(key), splt);
    }
    /**
	 * 获取当前时间
	 * @return
	 */
	public Date getcuttDate(){
		return new Date();
	}
	/**
	 * 把数据print 到前台
	 * @param response 
	 * @param ajax
	 */
	public void createAjax(HttpServletResponse response,Object ajax){
		createAjax(response, ajax, null);
	}
	/**
	 * 把数据print 到前台
	 * @param response
	 * @param ajax
	 * @param contentType 上下文类型
	 */
	public void createAjax(HttpServletResponse response,Object ajax,String contentType){
		if(response!=null){
			if (null == contentType || "".equals(contentType))
				contentType = "text/plain";
			response.setContentType(contentType);
			try {
				response.getWriter().print(ajax);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
