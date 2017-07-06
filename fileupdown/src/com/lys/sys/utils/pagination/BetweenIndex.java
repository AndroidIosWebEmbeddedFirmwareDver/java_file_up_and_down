package com.lys.sys.utils.pagination;

/**
 * 分页表--3
 * @author shuang
 * 类说明：封装分页的开始索引、结束索引
 */
public final class BetweenIndex {
	private Integer startIndex;
	private Integer endIndex;
	/**
	 * 通过开始索引和结束索引构造分页索引起始对象
	 * @param startIndex 开始索引
	 * @param endIndex 结束索引
	 */
	public BetweenIndex(Integer startIndex, Integer endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	/**
	 * 获得开始索引
	 * @return 开始索引
	 */
	public Integer getStartIndex() {
		return startIndex;
	}
	/**
	 * 设置开始索引
	 * @param startIndex 开始索引
	 */
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	/**
	 * 获得结束索引
	 * @return 结束索引
	 */
	public Integer getEndIndex() {
		return endIndex;
	}
	/**
	 * 设置结束索引
	 * @param endIndex 结束索引
	 */
	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}
}
