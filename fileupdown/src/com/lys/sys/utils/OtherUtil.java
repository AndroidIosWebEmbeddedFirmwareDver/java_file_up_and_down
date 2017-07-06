package com.lys.sys.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.lys.sys.mvc.system.dictionary.SysStatic;

/***
 * 系统的公共工具类
 * @author lys
 *
 */
public class OtherUtil {

	/**
	 * 执行数据操作方式
	 */
	public static String INSERT="INSERT";//添加
	public static String DELETE="DELETE";//删除
	public static String UPDATE="UPDATE";//更新
	/**
	 * 执行数据后的返回值类型
	 */
	public static String RETURN_1="RETURN_1";//影响行数
	public static String RETURN_2="RETURN_2";//主键ID

	/**
	 * 获取客户IP地址
	 * @param request
	 * @return String
	 */
	public static String overshot(HttpServletRequest request) {
		String ipAddress = null;
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0|| "unknown".equalsIgnoreCase(ipAddress)) { 
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				InetAddress inet = null;// 根据网卡取本机配置的IP
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
	/**
	 * 获取国际化语言值
	 * @param accept_language 获取语言类型 request.getHeader("accept-language");
	 * @param msgname 要取得的properties 的 key 值
	 * @return 要取得的properties 的 value 值
	 */
	public static String getI18nLanguage(String accept_language,String msgname){
		if(StringUtils.hasText(accept_language)){
			String[] i18nTypes=accept_language.split(",")[0].split("-");
			return SysStatic.i18nmessage.getMessage(msgname,null,"Default",new Locale(i18nTypes[0],i18nTypes[1])); 
		}else{
			return SysStatic.i18nmessage.getMessage(msgname,null,"Default",Locale.CHINA );
		}
	}
}
