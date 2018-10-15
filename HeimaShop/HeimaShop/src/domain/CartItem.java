package domain;

/**
 * 购物车项
 * @author Sun
 * @date Oct 8, 2018
 * @version V1.0
 */
public class CartItem {

	private Product product;	// 商品
	private int buyNum;	// 购买数量
	private double subtotal;	// 总价
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	
	
	
}
