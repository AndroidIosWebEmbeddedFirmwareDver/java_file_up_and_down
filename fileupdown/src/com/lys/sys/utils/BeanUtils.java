package com.lys.sys.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.lys.sys.log.Log;
import com.lys.sys.utils.annotation.MyColumn;



/**
 * 对象工具类
 * 自己写的 对象赋值处理的方法，继承 org.springframework.beans.BeanUtils 
 * @author shuang
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {
	
	/**
	 * 结果集(SqlRowSet)转换成 List<Map<String, Object>>
	 * @param rs 传入结果集对象
	 * @return List<Map<String, Object>>类型数据
	 */
	public static List<Map<String, Object>>  processList(SqlRowSet rs){
		List<Map<String, Object>> objs = new LinkedList<Map<String, Object>>();
		if(rs!=null){
			try {
				int columnCount = rs.getMetaData().getColumnCount();
				Map<String, Object> obj=null;
				while (rs.next()) {
					obj =  new LinkedCaseInsensitiveMap<Object>();//不区分大小写
					int i = 1;
					for (int j = 0; j < columnCount; j++) 
						obj.put(rs.getMetaData().getColumnName(j+1), rs.getString(i++));
					objs.add(obj);
				}
			} catch (Exception e) {
				throw new RuntimeException("结果集转换成List<Map<String, Object>>失败!");
			}
		}
		return objs;
	}
	/**
	 * Bean转换成Map集合 
	 * @param bean 传入的实体 （必须有属性的get方法）
	 * @return （key=实体字段，value=实体字段名）的Map
	 */
	public static Map<String, Object>  turnMap(Object bean) {
		return turnMap(bean,false);
	}
	/**
	 * Bean转换成Map集合 
	 * @param bean 传入的实体 （必须有属性的get方法）
	 * @param isturnMyColumn 是否 根据 对象 的MyColumn注解的值  来 做Map的key
	 * @return （key=实体字段上的注解Name，value=实体字段名）的Map
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object>  turnMap(Object bean,Boolean isturnMyColumn) {
		if (bean == null)
			return new HashMap<String, Object>();
		Map<String, Object> targetMap = new LinkedCaseInsensitiveMap<Object>();//不区分大小写
		Class clazz=bean.getClass();
		PropertyDescriptor sourcePds[] = getPropertyDescriptors(clazz);
		int j = sourcePds.length;
		for (int i = 0; i < j; i++) {
			PropertyDescriptor sourcePd = sourcePds[i];
			if (sourcePd != null && sourcePd.getWriteMethod() != null) {
				try {
					Method readMethod = sourcePd.getReadMethod();
					if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()))
						readMethod.setAccessible(true);
					Object value = readMethod.invoke(bean, new Object[0]);// 取得Bean中的get方法返回的值
					if (value == null)
						continue;
					String keyName=sourcePd.getName();
					if(isturnMyColumn ){
						MyColumn column= clazz.getDeclaredField(sourcePd.getName()).getAnnotation(MyColumn.class);//只找本类总的字段
						keyName = (column==null) ? keyName : column.column();
					}
					targetMap.put(keyName, value);
				} catch (Throwable ex) {
					throw new FatalBeanException("对象转换Map集合失败!", ex);
				}
			}
		}
		Log.in.info("对象转换后的Map集合 ："+targetMap);
		return targetMap;
	}
	/**
	 * Map 转换成 bean 
	 * @param map 有数据的map
	 * @param target 一个 bean 对象，对象不能为空
	 * @throws BeansException
	 */
	@SuppressWarnings("rawtypes")
	public static  Object turnBean(Map map, Class  requiredType) {
		return turnBean(map,requiredType,false);
	}
	/**
	 * Map 转换成 bean 
	 * @param map 有数据的map
	 * @param target 一个 bean 对象，对象不能为空
	 * @param isturnMyColumn 是否根据对象 属性 MyColumn注解 的值(key)来 取 Map的 value值
	 * @throws BeansException
	 * @说明：存在转换类型问题，如果map里面存入的类型与bean中set属性参数类型不符合，则抛出异常
	 */
	@SuppressWarnings("rawtypes")
	public static  Object turnBean(Map map, Class  requiredType, Boolean isturnMyColumn){
		return turnBean(map, requiredType, isturnMyColumn,false);
	}
	/**
	 * Map 转换成 bean 
	 * @param map 有数据的map
	 * @param requiredType 一个 bean 对象，对象不能为空
	 * @param isturnMyColumn 是否根据对象 属性 MyColumn注解 的值(key)来 取 Map的 value值
	 * @param isturnMapType 是否把map中的value类型转换成Bean中的set参数对应的类型
	 * @throws BeansException
	 * @说明：存在转换类型问题，如果map里面存入的类型与bean中set属性参数类型不符合，则抛出异常
	 */
	@SuppressWarnings("rawtypes")
	public static  Object turnBean(Map map, Class  requiredType, Boolean isturnMyColumn, Boolean isturnMapType)   {
		Object obj=null;
		if (map != null && map.size() > 0&&requiredType!=null) {
			try {
				obj=requiredType.newInstance();
			} catch (Throwable e) {
				throw new  FatalBeanException("创建"+requiredType+"的新实例失败!",e);
			}
			PropertyDescriptor targetPds[] = getPropertyDescriptors(requiredType);
			PropertyDescriptor apropertydescriptor[];
			int j = (apropertydescriptor = targetPds).length;
			for (int i = 0; i < j; i++) {
				PropertyDescriptor targetPd = apropertydescriptor[i];
				Method writeMethod = targetPd.getWriteMethod();
				Object value = map.get(targetPd.getName());
				if (writeMethod!= null) {
					try {
						if(value==null&&isturnMyColumn){
							Field field=getField(requiredType, targetPd.getName());
							if(field!=null){
								MyColumn column=field.getAnnotation(MyColumn.class);//只找本类总的字段
								value = (column==null) ? value : map.get(column.column());
							}
						}
						if(value==null)
							continue;
						if(isturnMapType){//bean中set属性参数值的第一个参数类型
							value=StringUtils.strToType(value, writeMethod.getParameterTypes()[0].getName());
						}
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers()))
							writeMethod.setAccessible(true);
						writeMethod.invoke(obj, new Object[] { value });
					}catch (Throwable ex) {
						throw new  FatalBeanException("不能把值："+value+"赋给bean的"+targetPd.getName()+"属性",ex);
					}
				}
			}
		}
		return obj;
	}
	/***
	 * 获取对象中的某个字段对象，包括读取对象的所有父类
	 * @param clazz 对象
	 * @param name 字段名
	 * @return Field字段对象
	 */
	@SuppressWarnings("rawtypes")
	public static Field getField(Class clazz,String name){
		Field field=null;
		try {
			field = clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			if(clazz.getSuperclass().getSuperclass()!=null){
				field=getField(clazz.getSuperclass(), name);
			}
		} catch (Exception e) {
			field= null;
		}
		return field;
	}
	/**
	 * 对象的属性赋值 ，判断null是否也赋值上-----来源与spring中的 beanutils 的 copyProperties 方法! 
	 * @param source 来源对象
	 * @param target 目标对象
	 * @param isNullToTarget 是否完全覆盖，包括null字段也覆盖target
	 * @throws BeansException
	 */
	public static void copyProperties(Object source, Object target,Boolean isNullToTarget) throws BeansException {
		if(!isNullToTarget){
			Assert.notNull(source, "Source must not be null");
			Assert.notNull(target, "Target must not be null");
			PropertyDescriptor targetPds[] = getPropertyDescriptors(target.getClass());
			PropertyDescriptor apropertydescriptor[];
			int j = (apropertydescriptor = targetPds).length;
			for (int i = 0; i < j; i++) {
				PropertyDescriptor targetPd = apropertydescriptor[i];
				if (targetPd.getWriteMethod() != null) {
					PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
					if (sourcePd != null && sourcePd.getReadMethod() != null)
						try {
							Method readMethod = sourcePd.getReadMethod();
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()))
								readMethod.setAccessible(true);
							Object value = readMethod.invoke(source, new Object[0]);
							if(value==null)
								continue;
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers()))
								writeMethod.setAccessible(true);
							writeMethod.invoke(target, new Object[] { value });
						} catch (Throwable ex) {
							throw new FatalBeanException("Could not copy properties from source to target",ex);
						}
				}
			}
		}else{
			copyProperties(source,target);
		}
	}
	
}
