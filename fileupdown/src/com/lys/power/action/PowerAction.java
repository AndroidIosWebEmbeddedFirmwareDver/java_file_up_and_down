package com.lys.power.action;

import com.lys.power.biz.PowerCacheBiz;
import com.lys.power.utils.PowerStatic;
import com.lys.sys.mvc.action.SysAction;

/***
 * 公用action方法
 * @author lys
 *
 */
public class PowerAction extends SysAction{
	//权限缓存对象
	protected PowerCacheBiz powerCacheBiz=PowerStatic.powerCacheBiz;
 
}
