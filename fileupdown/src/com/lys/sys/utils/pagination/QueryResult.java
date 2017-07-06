package com.lys.sys.utils.pagination;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分页类--2：当前页的数据和总行数
 * @author shuang
 * 分页实体不被约束的类型
 */
public final class QueryResult {
	private Integer total=0;
	private List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
	/**
	 * 获得总记录数
	 * @return 总记录数
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * 设置总记录数
	 * @param total 总记录数
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	/**
	 * 获得当前页的数据列表
	 * @return 数据列表
	 */
	public List<Map<String, Object>> getResult() {
		return result;
	}
	/**
	 * 设置当前页的数据列表
	 * @param result 数据列表
	 */
	public void setResult(List<Map<String, Object>> result) {
		this.result = result;
	}
}