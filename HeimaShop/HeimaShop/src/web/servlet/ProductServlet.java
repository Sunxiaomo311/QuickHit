package web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;

import domain.Cart;
import domain.CartItem;
import domain.Category;
import domain.Order;
import domain.OrderItem;
import domain.PageBean;
import domain.Product;
import domain.User;
import redis.clients.jedis.Jedis;
import service.ProductService;
import utils.CommonUtils;
import utils.JedisPoolUtils;
import utils.PaymentUtil;

/**
 * 商品相关功能
 * 
 * @author Sun
 * @date Oct 6, 2018
 * @version V1.0
 */
public class ProductServlet extends BaseServlet {

	// 查看‘我的订单’
	public void myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 判断是否登陆
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			// 未登录，跳转至登陆界面
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		// 封装订单集合对象
		List<Order> orderList = new ArrayList<Order>();

		// 获得该用户的所有订单
		ProductService service = new ProductService();
		orderList = service.getAllOrders(user.getUid());

		// 根据订单编号依次封装订单项和商品对象，并存入相应订单
		for (Order order : orderList) {
			// 获得订单编号
			String oid = order.getOid();
			List<Map<String, Object>> orderItemList = new ArrayList<Map<String, Object>>();
			orderItemList = service.getOrderInfoByOid(oid);
			// 封装order信息
			for (Map<String, Object> map : orderItemList) {
				try {
					// 从map中取出count subtotal 封装到OrderItem中
					OrderItem item = new OrderItem();
					// item.setCount(Integer.parseInt(map.get("count").toString()));
					BeanUtils.populate(item, map);
					// 从map中取出pimage pname shop_price 封装到Product中
					Product product = new Product();
					BeanUtils.populate(product, map);
					// 将product封装到OrderItem
					item.setProduct(product);
					// 将orderitem封装到order中的orderItemList中
					order.getOrderItems().add(item);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		// 将封装完成的orderList存入域中
		request.setAttribute("orderList", orderList);
		
		// 转发到‘我的订单’页面
		request.getRequestDispatcher("order_list.jsp").forward(request, response);

	}

	// 确认订单 更新收获人信息+在线支付
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1、更新收货人信息
		Map<String, String[]> properties = request.getParameterMap();
		Order order = new Order();
		try {
			BeanUtils.populate(order, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		ProductService service = new ProductService();
		service.updateOrderAdrr(order);

		// 2、在线支付
		// 获得 支付必须基本数据
		String orderid = request.getParameter("oid"); // 订单编号
		// String money = request.getParameter("money");
		String money = "0.01";
		// 银行
		String pd_FrpId = request.getParameter("pd_FrpId");

		// 发给支付公司需要哪些数据
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = orderid;
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

		String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId=" + pd_FrpId + "&p0_Cmd=" + p0_Cmd
				+ "&p1_MerId=" + p1_MerId + "&p2_Order=" + p2_Order + "&p3_Amt=" + p3_Amt + "&p4_Cur=" + p4_Cur
				+ "&p5_Pid=" + p5_Pid + "&p6_Pcat=" + p6_Pcat + "&p7_Pdesc=" + p7_Pdesc + "&p8_Url=" + p8_Url
				+ "&p9_SAF=" + p9_SAF + "&pa_MP=" + pa_MP + "&pr_NeedResponse=" + pr_NeedResponse + "&hmac=" + hmac;

		// 重定向到第三方支付平台
		response.sendRedirect(url);

	}

	// 提交订单
	public void submitOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 判断是否登陆
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			// 未登录，跳转至登陆界面
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		// 封装order对象
		Order order = new Order();

		// private String oid; // 该订单的订单号
		String oid = CommonUtils.getUUID();
		order.setOid(oid);

		// private Date ordertime; // 下单时间
		order.setOrdertime(new Date());

		// private double total; // 该订单的总金额
		Cart cart = (Cart) session.getAttribute("cart");
		double total = cart.getTotal();
		order.setTotal(total);

		// private int state; // 订单支付状态 1表示已付款 0表示未付款
		order.setState(0);

		// private String address; // 收货人地址
		order.setAddress(null);

		// private String name; // 收货人姓名
		order.setName(null);

		// private String telephone; // 收货人电话
		order.setTelephone(null);

		// private User user; // 订单所属用户
		order.setUser(user);

		// private List<OrderItem> orderItems = new ArrayList<OrderItem>(); // 订单中的订单项集合
		Map<String, CartItem> cartItems = cart.getCartItems();
		for (Entry<String, CartItem> entry : cartItems.entrySet()) {
			// 取出每一购物项
			CartItem cartItem = entry.getValue();
			// 创建新的订单项
			OrderItem orderItem = new OrderItem();

			// private String itemid; // 订单项编号
			orderItem.setItemid(CommonUtils.getUUID());
			// private int count; // 订单项的购买数量
			orderItem.setCount(cartItem.getBuyNum());
			// private double subtotal; // 订单项总计
			orderItem.setSubtotal(cartItem.getSubtotal());
			// private Product product; // 订单项中的商品
			orderItem.setProduct(cartItem.getProduct());
			// private Order order; // 订单项所属的订单
			orderItem.setOrder(order);

			// 添加进order
			order.getOrderItems().add(orderItem);
		}

		// 数据封装完成后传递到service层
		ProductService service = new ProductService();
		service.submitOrder(order);

		session.setAttribute("order", order);

		// 清空购物车
		session.removeAttribute("cart");

		// 页面跳转
		response.sendRedirect(request.getContextPath() + "/order_info.jsp");

	}

	// 清空购物车
	public void clearCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("cart");

		// 跳转回cart.jsp
		response.sendRedirect(request.getContextPath() + "/cart.jsp");

	}

	// 删除单一商品
	public void delProFromCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得要删除的item的pid
		String pid = request.getParameter("pid");
		// 删除session中的购物车中的购物项集合中的item
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart != null) {
			Map<String, CartItem> cartItems = cart.getCartItems();
			// 需要修改总价
			cart.setTotal(cart.getTotal() - cartItems.get(pid).getSubtotal());
			// 删除
			cartItems.remove(pid);
		}

		session.setAttribute("cart", cart);

		// 跳转回cart.jsp
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}

	// 将商品加入购物车
	public void addProductToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pid = request.getParameter("pid");
		int buyNum = Integer.valueOf(request.getParameter("buyNum"));

		// 获得session域中的购物车对象
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
		}

		// 判断购物车中是否已经存在该商品，如果有则叠加数量
		// 根据pid获得商品
		ProductService service = new ProductService();
		Product product = null;
		try {
			product = service.getProductByPid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 该商品总计
		double subtotal = product.getShop_price() * buyNum;

		Map<String, CartItem> cartItems = cart.getCartItems();
		if (cartItems.containsKey(pid)) {
			// 商品已经存在
			CartItem oldCartItem = cartItems.get(pid);
			// 更新购买数量
			oldCartItem.setBuyNum(oldCartItem.getBuyNum() + buyNum);
			// 更新总价
			oldCartItem.setSubtotal(oldCartItem.getSubtotal() + subtotal);
		} else {
			// 商品不存在
			// 创建购物车项
			CartItem newCartItem = new CartItem();

			newCartItem.setBuyNum(buyNum);
			newCartItem.setProduct(product);
			newCartItem.setSubtotal(subtotal);
			// 存入信息
			cartItems.put(pid, newCartItem);
		}
		// 更新购物车的价格总计
		cart.setTotal(cart.getTotal() + subtotal);

		// 将车再次访问session
		session.setAttribute("cart", cart);

		// 直接跳转到购物车页面
		response.sendRedirect(request.getContextPath() + "/cart.jsp");

	}

	// 根据cid获得商品列表
	public void productListByCid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得cid
		String cid = request.getParameter("cid");

		String currentPageStr = request.getParameter("currentPage");
		if (currentPageStr == null)
			currentPageStr = "1";
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 12;

		ProductService service = new ProductService();
		PageBean<?> pageBean = service.getProductListByCid(cid, currentPage, currentCount);

		// 集合存浏览历史商品信息
		List<Product> historyList = new ArrayList<Product>();
		// 浏览商品历史
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] split = pids.split("-");
					for (String pid : split) {
						Product productByPid = null;
						try {
							productByPid = service.getProductByPid(pid);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						historyList.add(productByPid);
					}
				}
			}
		}

		// 将历史记录集合放入域中
		request.setAttribute("historyList", historyList);

		request.setAttribute("pageBean", pageBean);
		// 传递给页面cid
		request.setAttribute("cid", cid);

		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}

	// 查看商品详细信息
	public void productInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得当前页
		String currentPage = request.getParameter("currentPage");
		// 获得商品类别
		String cid = request.getParameter("cid");

		// 获得要查询的商品的pid
		String pid = request.getParameter("pid");

		ProductService productService = new ProductService();
		Product product = null;
		try {
			product = productService.getProductByPid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 浏览记录标识符
		String pids = pid;
		// 获得客户端携带cookie---获得名字是pids的cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids = cookie.getValue();
					// 1-3-2 本次访问商品pid是8----->8-1-3-2
					// 1-3-2 本次访问商品pid是3----->3-1-2
					// 1-3-2 本次访问商品pid是2----->2-1-3
					// 将pids拆成一个数组
					String[] split = pids.split("-");
					List<String> asList = Arrays.asList(split);
					LinkedList<String> list = new LinkedList<String>(asList);// [3,1,2]
					// 判断集合中是否存在当前pid
					if (list.contains(pid)) {
						// 包含当前查看商品的pid
						list.remove(pid);
					}
					// 不包含当前查看商品的pid 直接将该pid放到头上
					list.addFirst(pid);
					StringBuffer sb = new StringBuffer();
					// 重写为标识字符串
					for (int i = 0; i < 7 && i < list.size(); i++) {
						sb.append(list.get(i));
						sb.append("-");
					}
					// 去掉3-1-2-后的-
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}

		Cookie pids_cookie = new Cookie("pids", pids);
		response.addCookie(pids_cookie);

		request.setAttribute("product", product);
		request.setAttribute("cid", cid);
		request.setAttribute("currentPage", currentPage);

		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
	}

	// 商品分类列表
	public void categoryList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductService service = new ProductService();

		// 先从缓存中查询categoryList 如果有直接使用 没有在从数据库中查询 存到缓存中
		// 1、获得jedis对象 连接redis数据库
		Jedis jedis = JedisPoolUtils.getJedis();
		String categoryListJson = jedis.get("categoryListJson");

		// 2、判断categoryListJson是否为空
		if (categoryListJson == null) {
			System.out.println("缓存没有数据 查询数据库");
			// 准备分类数据
			List<Category> categoryList = null;
			try {
				categoryList = service.getCategoryList();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			Gson gson = new Gson();
			categoryListJson = gson.toJson(categoryList);

			jedis.set("categoryListJson", categoryListJson);
		}

		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}

	// 初始化页面加载商品列表用于首页显示
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ProductService productService = new ProductService();

		// 获得热门商品列表
		List<Product> isHotProductList = null;
		try {
			isHotProductList = productService.getIsHotProduct();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 获得最新商品列表
		List<Product> newProductList = null;
		try {
			newProductList = productService.getNewProduct();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.setAttribute("isHotProductList", isHotProductList);
		request.setAttribute("newProductList", newProductList);

		// 发送给首页
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

}