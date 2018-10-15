package domain;

import java.util.List;

/**
 * 商品显示列表需要的的封装信息
 * @author Sun
 * @date Oct 5, 2018
 * @version V1.0
 * @param <T>
 */
public class PageBean<T> {

	private int currentPage; // 当前页
	private int currentCount; // 每页显示数量
	private int totalCount; // 商品总数
	private int totalPage; // 总页数
	private List<T> list; // 商品信息

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
