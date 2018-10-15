package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import domain.Category;
import domain.Order;
import domain.Product;
import dao.AdminDao;

/**
 * 后台管理相关功能服务层
 * 
 * @author Sun
 * @date Oct 12, 2018
 * @version V1.0
 */
public class AdminService {

	// 添加商品
	public void addProduct(Product product) {

		AdminDao adminDao = new AdminDao();
		try {
			adminDao.addProduct(product);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 返回商品的分类集合
	public List<Category> getCategoryList() {
		AdminDao adminDao = new AdminDao();
		try {
			return adminDao.getCategoryList();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 根据传入cid修改分类名称
	public void editCategoryName(String cid, String newName) {

		AdminDao adminDao = new AdminDao();
		try {
			adminDao.editCategoryName(cid, newName);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 根据传入cid删除分类
	public void delCategory(String cid) {

		AdminDao adminDao = new AdminDao();
		try {
			adminDao.delCategory(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 添加商品分类
	public void addCategory(Category category) {

		AdminDao adminDao = new AdminDao();
		try {
			adminDao.addCategory(category);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 获得商品集合
	public List<Product> getProductList() {
		AdminDao adminDao = new AdminDao();
		try {
			return adminDao.getProductList();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 删除商品
	public void delProduct(String pid) {

		AdminDao adminDao = new AdminDao();
		try {
			adminDao.delProduct(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 修改商品
	public void editProduct(Product product) {

		AdminDao adminDao = new AdminDao();
		try {
			adminDao.editProduct(product);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 获得商品信息
	public Product getProuct(String pid) {

		AdminDao adminDao = new AdminDao();

		try {
			return adminDao.getProduct(pid);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 根据pid查询对应商品cid
	public String getgetCidByPid(String pid) {

		try {
			return new AdminDao().getgetCidByPid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 获得所有订单信息
	public List<Order> getOrderList() {

		AdminDao adminDao = new AdminDao();

		try {
			return adminDao.getOrderList();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 根据订单编号获得订单详情
	public List<Map<String, Object>> findOrderInfoByOid(String oid) {

		AdminDao adminDao = new AdminDao();

		try {
			return adminDao.findOrderInfoByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
