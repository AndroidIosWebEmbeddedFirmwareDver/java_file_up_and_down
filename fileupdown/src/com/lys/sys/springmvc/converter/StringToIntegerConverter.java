package com.lys.sys.springmvc.converter;

import org.springframework.core.convert.converter.Converter;

import com.lys.sys.log.Log;
import com.lys.sys.utils.StringUtils;

/**
 * 全局转换器-String转int
 * @author lys
 *
 */
public class StringToIntegerConverter implements Converter<String, Integer> {

	public Integer convert(String source) {
		Log.in.info("进入了全局数字转换器!");
		return StringUtils.toIntegerByObject(source);
	}
 
}
