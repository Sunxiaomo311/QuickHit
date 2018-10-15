package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单类
 * @author Sun
 * @date Oct 10, 2018
 * @version V1.0
 */
public class Order {

	/*`oid` varchar(32) NOT NULL,
	  `ordertime` datetime DEFAULT NULL,
	  `total` double DEFAULT NULL,
	  `state` int(11) DEFAULT NULL,
	  `address` varchar(30) DEFAULT NULL,
	  `name` varchar(20) DEFAULT NULL,
	  `telephone` varchar(20) DEFAULT NULL,
	  `uid` varchar(32) DEFAULT NULL*/

	private String oid; // 该订单的订单号
	private Date ordertime; // 下单时间
	private double total; // 该订单的总金额
	private int state; // 订单支付状态 1表示已付款 0表示未付款
	private String address; // 收货人地址
	private String name; // 收货人姓名
	private String telephone; // 收货人电话
	private User user; // 订单所属用户

	private List<OrderItem> orderItems = new ArrayList<OrderItem>();	// 订单中的订单项集合

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
}
