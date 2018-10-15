package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import domain.Category;
import domain.Order;
import domain.OrderItem;
import domain.Product;
import utils.DataSourceUtils;

/**
 * 商品数据库相关
 * 
 * @author Sun
 * @date Oct 5, 2018
 * @version V1.0
 */
public class ProductDao {

	private static QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());

	// 查询返回热门商品信息
	public List<Product> getIsHotProduct() throws SQLException {

		String sql = "select * from product where is_hot = ? limit 0,9"; // 查询数量：9

		return qr.query(sql, new BeanListHandler<Product>(Product.class), 1);
	}

	// 查询返回最新商品信息
	public List<Product> getNewProduct() throws SQLException {

		String sql = "select * from product order by pdate desc limit 0,9"; // 查询数量：9

		return qr.query(sql, new BeanListHandler<Product>(Product.class));
	}

	// 查询商品分类信息
	public List<Category> getCategoryList() throws SQLException {

		String sql = "select * from category";

		return qr.query(sql, new BeanListHandler<Category>(Category.class));
	}

	// 根据cid查询商品总数
	public int getProductTotalCountByCid(String cid) throws SQLException {

		String sql = "select count(*) from product where cid = ?";

		Long query = (Long) qr.query(sql, new ScalarHandler(), cid);

		return query.intValue();
	}

	// 根据cid查询商品信息
	public List<Product> getProductListByCid(String cid, int currentPage, int currentCount) throws SQLException {

		String sql = "select * from product where cid = ? limit ?,?";

		return qr.query(sql, new BeanListHandler<Product>(Product.class), cid, currentPage, currentCount);
	}

	// 根据pid查询商品详细信息
	public Product getProductByPid(String pid) throws SQLException {

		String sql = "select * from product where pid = ?";

		Product query = qr.query(sql, new BeanHandler<Product>(Product.class), pid);

		return query;
	}

	// 为数据库添加订单数据
	public void addOrders(Order order) throws SQLException {

		QueryRunner qr = new QueryRunner();

		String sql = "insert into orders values(?,?,?,?,?,?,?,?)";

		Connection conn = DataSourceUtils.getConnection();

		qr.update(conn, sql, order.getOid(),order.getOrdertime(), order.getTotal(), order.getState(), order.getAddress(),
				order.getName(), order.getTelephone(), order.getUser().getUid());

	}

	// 为数据库添加订单的每一个订单项数据
	public void addOrderItem(Order order) throws SQLException {

		QueryRunner qr = new QueryRunner();

		String sql = "insert into orderItem values(?,?,?,?,?)";

		Connection conn = DataSourceUtils.getConnection();

		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem item : orderItems) {
			qr.update(conn, sql, item.getItemid(), item.getCount(), item.getSubtotal(), item.getProduct().getPid(),
					item.getOrder().getOid());
		}

	}

	// 更新收货人信息
	public void updateOrderAdrr(Order order) throws SQLException {
		
		String sql = "update orders set address=?,name=?,telephone=? where oid=?";
		qr.update(sql, order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
		
	}

	// 支付成功后修改订单状态
	public void updateOrderState(String r6_Order) throws SQLException {
		
		String sql = "update orders set state = ? where oid = ?";
		
		qr.update(sql, 1,r6_Order);
	}

	// 根据用户编号查询所有订单信息
	public List<Order> getAllroders(String uid) throws SQLException {
		
		String sql = "select * from orders where uid = ?";
		
		return qr.query(sql, new BeanListHandler<Order>(Order.class), uid);
	}

	// 根据订单编号查询所有订单项和商品信息
	public List<Map<String, Object>> getOrderInfoByOid(String oid) throws SQLException {
		
		String sql = "select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from orderitem i,"
				+ "product p where i.pid=p.pid and i.oid=?";
		
		List<Map<String, Object>> query = qr.query(sql, new MapListHandler(), oid);
		
		return query;
	}

	

}
