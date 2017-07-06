package com.lys.sys.utils.db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.FatalBeanException;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.lys.sys.utils.BeanUtils;

/**
 * 对数据库增删该查的一个工具类
 * @author lys
 *
 */
public class DbUtil {
	
	/***
	 * 根据传入的要修改字段 数组  和 数据对象，来构建修改的map对象
	 * @param bean 数据对象
	 * @param ss 要修改的字段
	 * @return
	 */
	public static Map<String,Object> getMapUpdByObj(Object bean,Object... ss){
		String[] sss=Arrays.asList(ss).toArray(new String[0]);
		return getMapUpdByObj(bean, sss);
	} 
	/***
	 * 根据传入的要修改字段 数组  和 数据对象，来构建修改的map对象
	 * @param bean 数据对象
	 * @param ss 要修改的字段
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,Object> getMapUpdByObj(Object bean,String... ss){
		Map<String,Object> map=new LinkedCaseInsensitiveMap<Object>();
		if(ss!=null&&ss.length>0&&bean!=null){
			Map<String,Integer> tempmap=new LinkedCaseInsensitiveMap<Integer>();
			for(String s:ss){
				tempmap.put(s, 1);
			}
			Class clazz=bean.getClass();
			PropertyDescriptor sourcePds[] = BeanUtils.getPropertyDescriptors(clazz);
			int j = sourcePds.length;
			for (int i = 0; i < j; i++) {
				PropertyDescriptor sourcePd = sourcePds[i];
				if (sourcePd != null && sourcePd.getWriteMethod() != null) {
					String keyName=sourcePd.getName();
					if(tempmap.get(keyName)==null){
						continue;
					}
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()))
							readMethod.setAccessible(true);
						Object value = readMethod.invoke(bean, new Object[0]);// 取得Bean中的get方法返回的值
						//判断keyName是需要修改
						if(tempmap.get(keyName)==1){
							map.put(keyName, value);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Map数据构建失败!", ex);
					}
				}
			}
		}
		return map;
	} 
}
