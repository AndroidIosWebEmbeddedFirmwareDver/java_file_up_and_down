package com.lys.sys.bo;

import java.util.HashMap;
import java.util.Map;

import com.lys.sys.utils.JsonUtil;
/***
 * 消息提醒通用类
 * @author lys
 *
 */
public class Msg {
	private int f;// 1 操作成功， 0 操作 失败  2 异常 ，  其它数字自定义，必须填写
	private String m;//提示信息--给用户看，建议填写
	private String e;//存储异常的信息 
	private String b;//标识，返回页面用作业务作用，后期可以做个封装，弄个code码
	private Map<String,Object> d;//用做 向页面 发送参数
	
	/***
	 * @param f设置标识  1 操作成功， 0 操作 失败  2 异常 ，  其它数字自定义，必须填写
	 */
	public Msg(int f){
		this.f = f;
	}
	/***
	 * @return  json 字符串
	 * @说明：对bean中有Date类型的数据可以成功转换成yyyy-MM-dd HH:mm:ss格式的时间类型
	 */
	public String ok() {
		return JsonUtil.getJsonByObj(this);
	}
	/***
	 * @param dateType 在转换字符串 的时候，如果有时间类型数据，时间格式转换后的字符串格式，例如yyyy-MM-dd HH:mm:ss
	 * @return  json 字符串
	 */
	public String ok(String dateType) {
		return JsonUtil.getJsonByObj(this,dateType);
	}
	
	public Msg f(int f) {
		this.f = f;
		return this;
	}
	/***
	 * 设置提示信息，建议填写
	 * @param m
	 * @return
	 */
	public Msg m(String m) {
		this.m = m;
		return this;
	}
	public Msg e(String e) {
		this.e = e;
		return this;
	}
	public Msg b(String b) {
		this.b = b;
		return this;
	}
	/**
	 * 设置其它参数
	 * @param s key值
	 * @param o value值
	 * @return
	 */
	public Msg d(String s,Object o) {
		if(this.d==null){
			this.d=new HashMap<String, Object>();
		} 
		this.d.put(s, o);
		return this;
	}
	public int getF() {
		return f;
	}
	public String getM() {
		return m;
	}
	public Map<String, Object> getD() {
		return d;
	}
	public String getE() {
		return e;
	}
	public String getB() {
		return b;
	}
	
	
	
	

}
