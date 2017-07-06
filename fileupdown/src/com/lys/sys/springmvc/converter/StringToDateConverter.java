package com.lys.sys.springmvc.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.lys.sys.log.Log;
import com.lys.sys.utils.DateConverUtil;

/**
 * 全局转换器
 * @author lys
 *
 */
public class StringToDateConverter implements Converter<String, Date> {

	private String dateFormatPattern;

	public StringToDateConverter(String dateFormatPattern) {
		this.dateFormatPattern = dateFormatPattern;
	}
	
	public Date convert(String source) {
		Log.in.info("进入了全局时间转换器!");
		return DateConverUtil.getDbyST(source,dateFormatPattern);
	}
 
}
