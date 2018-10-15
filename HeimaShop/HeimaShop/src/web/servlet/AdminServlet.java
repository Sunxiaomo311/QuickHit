package web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import domain.Category;
import domain.Order;
import domain.Product;
import service.AdminService;
import utils.CommonUtils;

/**
 * 后台管理相关功能WEB层
 * 
 * @author Sun
 * @date Oct 12, 2018
 * @version V1.0
 */
public class AdminServlet extends BaseServlet {

	// 根据订单编号获得订单详情
	public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			// 前台图片加载效果
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String oid = request.getParameter("oid");
		
		AdminService service = new AdminService();
		List<Map<String,Object>> orderInfo = service.findOrderInfoByOid(oid);
		
		Gson gson = new Gson();
		String json = gson.toJson(orderInfo);
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().write(json);
		
	}
	
	// 获得订单清单
	public void getOrderList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		AdminService service = new AdminService();
		List<Order> orderList = service.getOrderList();
		
		request.setAttribute("orderList", orderList);
		
		request.getRequestDispatcher("admin/order/list.jsp").forward(request, response);
		
	}
	
	
	// 根据pid查询对应商品cid
	public String getCidByPid(String pid) {
		
		String cid = "";
		
		cid = new AdminService().getgetCidByPid(pid);		
		return cid;
	}
	
	// 获得商品信息，用于在修改页面显示
	public void getProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");

		String pid = request.getParameter("pid");
		
		AdminService service = new AdminService();
		Product product = service.getProuct(pid);
		
		// 封装cid
		String cid = getCidByPid(pid);
		Category category = new Category();
		category.setCid(cid);
		product.setCategory(category);
		
		Gson gson = new Gson();
		String json = gson.toJson(product);
		
		response.getWriter().write(json);
		
	}
	
	// 获得商品分类信息，用于在修改商品页面显示
	public void showCategoryList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");

		AdminService service = new AdminService();
		List<Category> categoryList = service.getCategoryList();
		
		Gson gson = new Gson();
		String json = gson.toJson(categoryList);
		
		response.getWriter().write(json);
		
	}

	// 删除商品
	public void delProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pid = request.getParameter("pid");

		AdminService service = new AdminService();
		service.delProduct(pid);

		// 重定向
		response.sendRedirect(request.getContextPath() + "/admin?method=getProductList");

	}

	// 获得商品列表 用于商品管理页面显示
	public void getProductList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AdminService service = new AdminService();
		List<Product> productList = service.getProductList();

		// 结果存入域中，转发给分类页面
		request.setAttribute("productList", productList);
		request.getRequestDispatcher("admin/product/list.jsp").forward(request, response);

	}

	// 添加商品分类
	public void addCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String cname = request.getParameter("cname");

		// 封装分类数据
		Category category = new Category();
		category.setCid(CommonUtils.getUUID());
		category.setCname(cname);

		AdminService service = new AdminService();
		service.addCategory(category);

		// 重定向
		response.sendRedirect(request.getContextPath() + "/admin?method=getCategoryList");

	}

	// 根据传入cid删除分类
	public void delCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String cid = request.getParameter("cid");

		AdminService service = new AdminService();
		service.delCategory(cid);

		// 重定向
		response.sendRedirect(request.getContextPath() + "/admin?method=getCategoryList");

	}

	// 根据传入cid修改分类名称
	public void editCategoryName(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String cid = request.getParameter("cid");
		String newName = request.getParameter("newName");

		AdminService service = new AdminService();
		service.editCategoryName(cid, newName);

		// 重定向
		response.sendRedirect(request.getContextPath() + "/admin?method=getCategoryList");

	}

	// 获得商品分类清单 用于在分类管理页面显示
	public void getCategoryList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AdminService service = new AdminService();
		List<Category> categoryList = service.getCategoryList();

		// 结果存入域中，转发给分类页面
		request.setAttribute("categoryList", categoryList);
		request.getRequestDispatcher("admin/category/list.jsp").forward(request, response);

	}

}
