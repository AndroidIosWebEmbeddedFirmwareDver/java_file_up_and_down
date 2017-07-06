package com.lys.sys.utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.lys.sys.mvc.system.dictionary.SysStatic;
/**
 * JSON对象工具类
 * 
 * @author shuang  
 */
public final class JsonUtil {
	private JsonUtil() {}
	/***
	 * 值过滤器
	 */
	private static ValueFilter filter = new ValueFilter() {
		@Override
		public Object process(Object obj, String s, Object v) {
			if (v == null)
				return "";
			return v;
		}
	};
	private static SerializerFeature[]  feature={
		//所有Key转小写
		SerializerFeature.AllKeyToLower,
		//解决FastJson循环引用的问题
		SerializerFeature.DisableCircularReferenceDetect,
		//输出值为null的字段
		SerializerFeature.WriteMapNullValue
		}; 
	private static SerializeConfig mapping = new SerializeConfig();
	static {
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		mapping.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));//数据库的一个时间类型
	}
	/**
	 * 将对象转换成JSON字符串 --固定时间格式"yyyy-MM-dd HH:mm:ss"
	 * @param BO/VO,map,数组,list 对象
	 * @return JSON字符串
	 * @说明：对bean中有Date类型的数据可以成功转换成yyyy-MM-dd HH:mm:ss格式的时间类型,例如："barDate":yyyy-MM-dd HH:mm:ss
	 */
	public static String getJsonByObj(Object bean) {
		return getJsonByObj(bean,mapping);
	}
	/**
	 * 将对象转换成JSON字符串 --特定时间格式--所有Key为小写
	 * @param BO/VO,map,数组,list 对象
	 * @dateType 时间格式转换后的字符串格式，例如yyyy-MM-dd HH:mm:ss
	 * @return JSON字符串
	 * @说明：对bean中有Date类型的数据可以成功转换成yyyy-MM-dd HH:mm:ss格式的时间类型,例如："barDate":yyyy-MM-dd HH:mm:ss
	 */
	public static String getJsonByObj(Object bean,String dateType) {
		SerializeConfig zdymapping=new SerializeConfig();
		zdymapping.put(Date.class, new SimpleDateFormatSerializer(dateType));
		return getJsonByObj(bean,zdymapping);
	}
	/**
	 * 将对象转换成JSON字符串 ---效率高一些--不处理key 也不处理循环引用的问题--也不处理时间格式
	 * @param BO/VO,map,数组,list 对象
	 * @return JSON字符串
	 * @说明：对bean中有Date类型的数据可以成功转换成long格式的时间类型,例如："barDate":1458268099098
	 */
	public static String getJsonDefaultByObj(Object bean) {
		return JSON.toJSONString(bean);
	}
	/**
	 * 将JSON数据转换为ListBean集合
	 * @param <T>
	 * @param data JSON数组数据
	 * @param beanClass 待转换的Bean类型 --LinkedCaseInsensitiveMap
	 * @return 
	 */
	public static <T> List<T> getListBean(String json, Class<T> calzz) {
		return JSON.parseArray(json, calzz);
	}
	/**
	 * 将JSON数据转换为List集合
	 * @param <T>
	 * @param data JSON数组数据
	 * @param beanClass 待转换的Bean类型 --LinkedCaseInsensitiveMap
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getList(String json, Class calzz) {
		return getListBean(json, calzz);
	}
	/**
	 * 将JSON数据转换为 Java Bea n对象 
	 * @param data JSON字符串
	 * @param calzz 待转换的Bean类型--LinkedCaseInsensitiveMap
	 * @return
	 */
	public static <T> T getObjet(String json,Class<T> calzz) {
		return JSON.parseObject(json, calzz) ;
	}
	/***
	 * 通用封装--获取json字符串
	 * @param bean 对象
	 * @param mappingx 时间类型计划等
	 * @return
	 */
	private static String getJsonByObj(Object bean,SerializeConfig mappingx){
		if(SysStatic.ORACLE.equals(SysStatic.dbName)){
			return JSON.toJSONString(bean,mappingx,filter,feature); //所有Key为小写
		}else{
			return JSON.toJSONString(bean,mappingx,filter,feature[1],feature[2]);
		}
	}
}
