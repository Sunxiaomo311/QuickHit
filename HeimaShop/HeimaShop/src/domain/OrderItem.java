package domain;

/**
 * 订单项类
 * 
 * @author Sun
 * @date Oct 10, 2018
 * @version V1.0
 */
public class OrderItem {

	private String itemid; // 订单项编号
	private int count; // 订单项的购买数量
	private double subtotal; // 订单项总计
	private Product product; // 订单项中的商品
	private Order order; // 订单项所属的订单

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
