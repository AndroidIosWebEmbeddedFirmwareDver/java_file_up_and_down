package com.lys.sys.utils;

import java.util.Locale;
import javax.annotation.Resource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件springmvc.xml中的 messageSource的bean加载的 国际化语言文件.properties
 * @author lys
 *
 */
@Component("MessageSourceHelper")
public class MessageSourceHelper {
	@Resource(name="messageSource")
	private ReloadableResourceBundleMessageSource messageSource;
	public String getMessage(String code, Object[] args, String defaultMessage,Locale locale) {
		String msg = messageSource.getMessage(code, args, defaultMessage,locale);
		return msg != null ? msg.trim() : msg;
	}
}
