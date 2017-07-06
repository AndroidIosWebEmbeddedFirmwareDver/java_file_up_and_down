package com.lys.power.biz.bizImpl;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;

import org.springframework.stereotype.Component;

import com.lys.power.biz.PowerCacheBiz;
import com.lys.sys.mvc.dao.BaseDao;

/**
 * 数据缓存类
 * @author Administrator
 *
 */
@Component("PowerCacheBizImpl")
public class PowerCacheBizImpl implements PowerCacheBiz {
	@Resource(name = "BaseDaoImpl")
	public BaseDao baseDao;
	@Resource(name="defaultCache")
	private Cache cache;
//
//	/**
//	 * 删除菜单缓存
//	 * @return
//	 */
//	public boolean del_power_allmenu(){
//		Log.in.info(new StringBuffer("===移除==缓存key：").append("get_power_allmenu"));
//		return cache.remove("get_power_allmenu");
//	}
//	/**
//	 * 取得所有菜单
//	 * @return 菜单的map集合
//	 */
//	public Map<String, Object> get_power_allmenu() {
//		Map<String,Object> powermap=new LinkedCaseInsensitiveMap<Object>();//不区分大小写
//		return powermap;
//	}
//	/**
//	 * 删除字典缓存
//	 * @return
//	 */
//	public boolean del_power_allcode(){
//		Log.in.info(new StringBuffer("===移除==缓存key：").append("get_power_allcode"));
//		return cache.remove("get_power_allcode");
//	}
//	/**
//	 * 取得所有字典
//	 * @return 字典的map集合
//	 */
//	public Map<String,Map<String,Object>> get_power_allcode() {
//		Map<String,Map<String,Object>> powermap=new HashMap<String, Map<String,Object>>();
//		return powermap;
//	}
	
}
