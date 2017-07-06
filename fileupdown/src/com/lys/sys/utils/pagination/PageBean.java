package com.lys.sys.utils.pagination;


import java.util.List;
import java.util.Map;

/**
 * 分页类 --1   分页通用类(模式：每次只取当前页的数据)
 * @author shuang
 * 分页实体未被约束的类型
 */
public class PageBean {
	private Integer pageSize = 3;			//页大小
	private Integer totalElements;			//总行数
	private Integer totalPages;				//总页数
	private Integer currPageNumber = 1;		//当前页页码
	private List<Map<String, Object>> data;	//当前页的数据  
	private Integer showPageNum = 5;		//每页显示的连接数量
	private BetweenIndex betweenIndex;		//每页显示的连接起始索引
	
	/**
	 * 计算当前页的起始连接值
	 */
	private void calcBetweenIndex() {
		//理想情况的起始连接值计算办法
		Integer startIndex = currPageNumber - (showPageNum % 2 == 0 ? (showPageNum / 2 - 1) : showPageNum / 2);
		Integer endIndex = currPageNumber + showPageNum / 2;
		//特殊情况下，计算出的开始连接值可能小于1
		if (startIndex < 1) {
			startIndex = 1;//修正开始连接值为1
			//同时不得不考虑结束连接值的情况：
			//1.当要显示的连接项数目大于等于总页数时，结束连接值就是总页数
			if (showPageNum >= totalPages) {
				endIndex = totalPages;
			//2.当要显示的连接项数目小于总页数时，结束连接值就是连接项数目
			} else {
				endIndex = showPageNum;
			}
		}
		//特殊情况下，计算出的结束连接值可能大于总页数
		if (endIndex > totalPages) {
			endIndex = totalPages;//修正结束连接值为总页数
			//同时不得不考虑开始连接值的情况：
			//1.当要显示的连接项数目大于等于总页数时，开始连接值就是1
			if (showPageNum > totalPages) {
				startIndex = 1;
			//2.当要显示的连接项数目小于总页数时，开始连接值等于总页数 - 连接项数目 + 1
			} else {
				startIndex = endIndex - showPageNum + 1;
			}
		}
		this.betweenIndex = new BetweenIndex(startIndex, endIndex);
	}
	
	/**
	 * 计算总页数
	 */
	private void calcTotalPages() {
		//加一算法
		//totalPages = totalElements % pageSize == 0 ? totalElements / pageSize : totalElements / pageSize + 1;
		//减一算法
		totalPages = (totalElements + pageSize - 1) / pageSize;
		calcBetweenIndex();
	}
	/**
	 * 通过页数据、总行数、当前页页码、页大小构造分页对象
	 * @param data
	 * @param totalElements
	 * @param currPageNumber
	 * @param pageSize
	 */
	public PageBean(QueryResult queryResult,Integer currPageNumber,Integer pageSize) {
		this.data = queryResult.getResult();
		this.totalElements = queryResult.getTotal();
		this.setPageSize(pageSize);
		this.setCurrPageNumber(currPageNumber);
	}	
	/**
	 * 是否是第一页
	 * @return true 是第一页，false 不是第一页
	 */
	public boolean isFirst() {
		return currPageNumber == 1;
	}
	/**
	 * 是否是最后一页
	 * @return true 是最后一页，false 不是最后一页
	 */
	public boolean isLast() {
		return currPageNumber.longValue() >= totalPages.longValue();
	}
	/**
	 * 是否有下一页
	 * @return true 有下一页，false 没有下一页
	 */
	public boolean hasNext() {
		return currPageNumber < totalPages;
	}
	/**
	 * 是否有上一页
	 * @return true 有上一页，false 没有上一页
	 */
	public boolean hasPrevious() {
		return currPageNumber > 1;
	}
	/**
	 * 获取下一页页码
	 * @return 下一页页码
	 */
	public Integer getNext() {
		if (hasNext()) {
			return currPageNumber + 1;
		} else {
			return totalPages;
		}
	}
	/**
	 * 获取上一页页码
	 * @return 上一页页码
	 */
	public Integer getPrevious() {
		if (hasPrevious()) {
			return currPageNumber - 1;
		} else {
			return 1;
		}
	}
	/**
	 * 设置当前页页码
	 * @param currPageNumber 当前页页码
	 */
	public void setCurrPageNumber(Integer currPageNumber) {
		if (currPageNumber > totalPages) {
			this.currPageNumber = totalPages;
		} else if (currPageNumber < 1) {
			this.currPageNumber = 1;
		} else {
			this.currPageNumber = currPageNumber;
		}
		calcBetweenIndex();
	}
	/**
	 * 获取当前页页码
	 * @return 当前页页码
	 */
	public Integer getCurrPageNumber() {
		return currPageNumber;
	}
	/**
	 * 设置页大小
	 * @param pageSize 页大小
	 */
	public void setPageSize(Integer pageSize) {
		if (pageSize > totalElements && totalElements > 0) {
			//this.pageSize = totalElements;
			this.pageSize = pageSize;
		} else if (pageSize < 1) {
			this.pageSize = 1;
		} else {
			this.pageSize = pageSize;
		}
		calcTotalPages();//重新计算总页数
		setCurrPageNumber(currPageNumber);//重新设置当前页页码
	}
	/**
	 * 获取页大小
	 * @return 页大小
	 */
	public Integer getPageSize() {
		return pageSize;
	}
	/**
	 * 获取总行数
	 * @return 总行数
	 */
	public Integer getTotalElements() {
		return totalElements;
	}
	/**
	 * 获取总页数
	 * @return 总页数
	 */
	public Integer getTotalPages() {
		return totalPages;
	}
	/**
	 * 获取当前页的数据
	 * @return 当前页的数据
	 */
	public List<Map<String, Object>> getList() {
		return data;
	}
	/**
	 * 获取每页显示的连接数量
	 * @return 每页显示的连接数量
	 */
	public Integer getShowPageNum() {
		return showPageNum;
	}
	/**
	 * 设置每页显示的连接数量
	 * @param pageNum 每页显示的连接数量
	 */
	public void setShowPageNum(Integer showPageNum) {		
		if (showPageNum < 0) {
			this.showPageNum = 1;
		} else if (showPageNum > this.totalPages) {
			this.showPageNum = this.totalPages;
		} else {
			this.showPageNum = showPageNum;
		}
		calcBetweenIndex();
	}
	/**
	 * 获取每页显示的连接起始索引
	 * @return 每页显示的连接起始索引
	 */
	public BetweenIndex getBetweenIndex() {
		return betweenIndex;
	}
}
