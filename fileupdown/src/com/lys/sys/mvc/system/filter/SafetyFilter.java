package com.lys.sys.mvc.system.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.util.NestedServletException;

import com.lys.sys.bo.Msg;
import com.lys.sys.log.Log;
import com.lys.sys.utils.myexception.MyoneException;
/**
 * 安全过滤器
 * 
 * @author shuang
 */
public class SafetyFilter implements Filter {
	protected String encoding = "UTF-8";
	
	public void doFilter(final ServletRequest request,final ServletResponse response, final FilterChain fc)throws java.io.IOException, javax.servlet.ServletException {
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse responses = (HttpServletResponse)response;
		req.setCharacterEncoding(encoding);
		responses.setCharacterEncoding(encoding);
		responses.setContentType(" text/html;charset=UTF-8"); 
		String ctxPath=req.getRequestURL().toString();//获取请求地址
		String requestResource =(req.getQueryString() != null ? ("?"+req.getQueryString()):"");//参数
		requestResource= URLDecoder.decode(requestResource,"UTF-8");
		Log.in.info("请求地址:" + ctxPath +requestResource);
		try {
			fc.doFilter(req, response);
		}catch (NestedServletException e) {
			if(e.getCause() instanceof MaxUploadSizeExceededException){
				if(response!=null){
					response.setContentType("text/plain");
					try {
						response.getWriter().print(new Msg(2).m("文件上传失败,单文件大小超过"+((MaxUploadSizeExceededException)e.getCause()).getMaxUploadSize()/(1024*1024)+"M!").e(e.getMessage()).ok());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}else if(e.getCause() instanceof MyoneException){//自定义的异常不用记录日志
				response.getWriter().print(new Msg(2).m("系统错误!").e(e.getCause().getMessage()).ok());
			} else {
				response.getWriter().print(new Msg(2).m("系统错误!").e(e.getMessage()).ok());
				logException(e);
			}
		}  catch (final IOException e) {
			response.getWriter().print(new Msg(2).m("系统错误!").e(e.getMessage()).ok());
			logException(e);
		} catch (final ServletException e) {
			response.getWriter().print(new Msg(2).m("系统错误!").e(e.getMessage()).ok());
			logException(e);
		} catch (final RuntimeException e) {
			response.getWriter().print(new Msg(2).m("系统错误!").e(e.getMessage()).ok());
			logException(e);
		} catch (Exception e) {
			response.getWriter().print(new Msg(2).m("系统错误!").e(e.getMessage()).ok());
			logException(e);
		}
		Log.in.info("-------***一个请求结束***-------");
	}
	/**
	 * 销毁
	 */
	public void destroy() {
		encoding = null;
	}
	/**
	 * 初始化
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		String encodingWebxml = filterConfig.getInitParameter("encoding");
		if(encodingWebxml!=null){
			encoding=encodingWebxml;
		}
	}

	private void logException(final Exception e) {
		final Throwable cause = e.getCause();
		if (cause != null && cause != e) {
			Log.in.error("引起异常的原因为：" + cause, cause);
		}
	}
}

