package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dao.ProductDao;
import domain.Category;
import domain.Order;
import domain.PageBean;
import domain.Product;
import utils.DataSourceUtils;

/**
 * 商品服务相关
 * 
 * @author Sun
 * @date Oct 5, 2018
 * @version V1.0
 */
public class ProductService {

	private static ProductDao productDao = new ProductDao();

	// 获得热门商品集合信息
	public List<Product> getIsHotProduct() throws SQLException {

		List<Product> isHotProductList = productDao.getIsHotProduct();

		return isHotProductList;
	}

	// 获得最新商品集合信息
	public List<Product> getNewProduct() throws SQLException {

		List<Product> newProductList = productDao.getNewProduct();

		return newProductList;
	}

	// 获得商品分类列表
	public List<Category> getCategoryList() throws SQLException {

		return productDao.getCategoryList();
	}

	// 根据cid获得商品清单的pageBean
	public PageBean<Product> getProductListByCid(String cid, int currentPage, int currentCount) {

		PageBean<Product> pageBean = new PageBean<Product>();

		// 封装pageBean信息
		// 当前页
		pageBean.setCurrentPage(currentPage);
		// 每页显示数量
		pageBean.setCurrentCount(currentCount);
		// 商品总数
		int totalCount = 0;
		try {
			totalCount = productDao.getProductTotalCountByCid(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		// 总页数
		int totalPage = (int) Math.ceil(1.0 * totalCount / currentCount);
		pageBean.setTotalPage(totalPage);
		// 商品信息
		List<Product> productList = null;
		try {
			productList = productDao.getProductListByCid(cid, currentPage, currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setList(productList);

		return pageBean;
	}

	// 根据pid获得商品详细信息
	public Product getProductByPid(String pid) throws SQLException {

		return productDao.getProductByPid(pid);
	}

	// 订单数据写入数据库
	public void submitOrder(Order order) {

		try {
			// 1、开启事务
			DataSourceUtils.startTransaction();
			// 2、调用dao存储order表数据的方法
			productDao.addOrders(order);
			// 3、调用dao存储orderitem表数据的方法
			productDao.addOrderItem(order);

		} catch (SQLException e) {
			try {
				// 事务回滚
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				// 释放资源（提交并且 关闭资源及从ThreadLocall中释放）
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 更新收货人信息
	public void updateOrderAdrr(Order order) {
		
		try {
			productDao.updateOrderAdrr(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	// 支付成功后修改订单状态
	public void updateOrderState(String r6_Order) {
		try {
			productDao.updateOrderState(r6_Order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 根据用户编号获取该用户的所有订单
	public List<Order> getAllOrders(String uid) {
		
		List<Order> orderList = null;
		try {
			orderList = productDao.getAllroders(uid);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return orderList;
	}

	// 根据订单编号获得相应订单项和商品信息
	public List<Map<String, Object>> getOrderInfoByOid(String oid) {
		
		List<Map<String, Object>> orderItemList = null;
		try {
			orderItemList = productDao.getOrderInfoByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return orderItemList;
	}


}
