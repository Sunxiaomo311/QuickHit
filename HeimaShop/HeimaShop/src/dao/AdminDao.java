package dao;

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
import domain.Product;
import utils.DataSourceUtils;

/**
 * 后台管理相关功能数据交互层
 * 
 * @author Sun
 * @date Oct 12, 2018
 * @version V1.0
 */
public class AdminDao {

	private static QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());

	// 添加商品
	public void addProduct(Product product) throws SQLException {

		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";

		qr.update(sql, product.getPid(), product.getPname(), product.getMarket_price(), product.getShop_price(),
				product.getPimage(), product.getPdate(), product.getIs_hot(), product.getPdesc(), product.getPflag(),
				product.getCategory().getCid());

	}

	// 查询商品分类列表集合
	public List<Category> getCategoryList() throws SQLException {

		String sql = "select * from category";

		return qr.query(sql, new BeanListHandler<Category>(Category.class));
	}

	// 根据cid修改对应的分类名称
	public void editCategoryName(String cid, String newName) throws SQLException {

		String sql = "update category set cname = ? where cid = ?";

		qr.update(sql, newName, cid);

	}

	// 根据传入cid删除分类
	public void delCategory(String cid) throws SQLException {

		String sql = "DELETE FROM category WHERE cid = ?";

		qr.update(sql, cid);

	}

	// 添加商品分类
	public void addCategory(Category category) throws SQLException {

		String sql = "insert into category values(?,?)";

		qr.update(sql, category.getCid(), category.getCname());

	}

	// 查询商品集合
	public List<Product> getProductList() throws SQLException {

		// 按照时间顺序排行列表
		String sql = "select * from product order by pdate desc";

		return qr.query(sql, new BeanListHandler<Product>(Product.class));
	}

	// 删除商品
	public void delProduct(String pid) throws SQLException {

		String sql = "DELETE FROM product WHERE pid = ?";
		qr.update(sql, pid);

	}

	// 修改商品
	public void editProduct(Product product) throws SQLException {

		if("".equals(product.getPimage()) || product.getPimage() == null) {
			// 未修改商品图片
			String sql = "update product set pname=?,market_price=?,shop_price=?,is_hot=?,pdesc=?,cid=? where pid=?";
			qr.update(sql, product.getPname(), product.getMarket_price(), product.getShop_price(), product.getIs_hot(),
					product.getPdesc(), product.getCategory().getCid(), product.getPid());
		}else {
			// 修改了图片
			String sql = "update product set pname=?,market_price=?,shop_price=?,pimage=?,is_hot=?,pdesc=?,cid=? where pid=?";
			qr.update(sql, product.getPname(), product.getMarket_price(), product.getShop_price(), product.getPimage(),
					product.getIs_hot(), product.getPdesc(), product.getCategory().getCid(), product.getPid());
		}
		
	}

	// 查询商品信息
	public Product getProduct(String pid) throws SQLException {

		String sql = "select * from product where pid=?";

		return qr.query(sql, new BeanHandler<Product>(Product.class), pid);
	}

	public String getgetCidByPid(String pid) throws SQLException {

		String sql = "select cid from product where pid=?";

		return qr.query(sql, new ScalarHandler(), pid).toString();
	}

	// 查询所有订单信息
	public List<Order> getOrderList() throws SQLException {
		
		String sql = "select * from orders";
		
		return qr.query(sql, new BeanListHandler<Order>(Order.class));
	}

	// 根据订单编号查询订单详情
	public List<Map<String, Object>> findOrderInfoByOid(String oid) throws SQLException {
		
		String sql = "select p.pimage,p.pname,p.shop_price,o.count,o.subtotal from orderitem o,product p where o.oid = ? and o.pid = p.pid";
		
		return qr.query(sql, new MapListHandler(), oid);
	}

}
