package com.lys.sys.cache;

import java.io.Serializable;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.lys.sys.log.Log;
import com.lys.sys.utils.StringUtils;

/**
 * 缓存方法拦截器核心代码
 * @author shuang
 */
public class EhCacheMethodInterceptor implements MethodInterceptor, InitializingBean {
	@Resource(name="defaultCache")
	private Cache cache;
	public EhCacheMethodInterceptor() {
		super();
	}
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(cache, "Need a cache. Please use setCache(Cache) create it.");  
	}
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String cacheKey = getCacheKey(invocation);//获取缓存key值
		if(StringUtils.hasText(cacheKey)){
			Element element = cache.get(cacheKey);
			// 缓存节点不存在的情况
			if (null == element) {
				synchronized (this) {
					// 这里判断是为了降低强制同步的负面影响,只需第一个操作该添加过程,后来者则跳过
					if (null == cache.get(cacheKey))
						element = putValueToCache(invocation, element, cacheKey);
				}
			}
			// 返回缓存值
			return element.getValue();
		}else{
			return invocation.proceed();
		}
		
	}
	// 新增节点放到缓存区
	private Element putValueToCache(MethodInvocation invocation,Element element, String cacheKey) throws Throwable {
		Object result = invocation.proceed();
		element = new Element(cacheKey, (Serializable) result);
		cache.put(element);
		Log.in.info(new StringBuffer("===添加==缓存key：").append(cacheKey).toString());
		return element;
	}
	/**
	 * 获取缓存key值
	 * @param invocation 执行方法的对象
	 * @return String 缓存key--定义为方法名，那么所有的缓存方法名不能相同，切记
	 */
	private String getCacheKey(MethodInvocation invocation) {
		return invocation.getMethod().getName();
	}

}