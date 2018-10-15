package web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import domain.Category;
import domain.Product;
import service.AdminService;
import utils.CommonUtils;

/**
 * 管理页面添加商品
 * 
 * @author Sun
 * @date Oct 12, 2018
 * @version V1.0
 */
public class AdminAddProductServlet extends HttpServlet {

	@SuppressWarnings("all")
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			// 1、创建磁盘文件项工厂
			// 作用：设置缓存文件的大小 设置临时文件存储的位置
			String path_temp = this.getServletContext().getRealPath("temp");
			// DiskFileItemFactory factory = new DiskFileItemFactory(1024*1024, new
			// File(path_temp));
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 1024);
			factory.setRepository(new File(path_temp));
			// 2、创建文件上传的核心类
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置上传文件的名称的编码
			upload.setHeaderEncoding("UTF-8");
			
			// 封装商品对象
			Product product = new Product();
			// 创建封装信息map，用于接收页面传输字段，再用BeanUtils封装至product
			Map<String,Object> map = new HashMap<String,Object>();

			// ServletFileUpload的API
			boolean multipartContent = upload.isMultipartContent(request);// 判断表单是否是文件上传的表单
			if (multipartContent) {
				// 是文件上传的表单
				// ***解析request获得文件项集合
				List<FileItem> parseRequest = upload.parseRequest(request);
				if (parseRequest != null) {
					for (FileItem item : parseRequest) {
						// 判断是不是一个普通表单项
						boolean formField = item.isFormField();
						if (formField) {
							// username=zhangsan
							String fieldName = item.getFieldName();
							String fieldValue = item.getString("UTF-8");// 对普通表单项的内容进行编码

							map.put(fieldName, fieldValue);
							
							// 当表单是enctype="multipart/form-data"时 request.getParameter相关的方法全部失效
						} else {
							// 文件上传项
							// 文件的名
							String fileName = item.getName();
							// 获得上传文件的内容
							InputStream in = item.getInputStream();
							String path_store = this.getServletContext().getRealPath("upload");
							OutputStream out = new FileOutputStream(path_store + "/" + fileName);
							IOUtils.copy(in, out);
							in.close();
							out.close();

							// 删除临时文件
							item.delete();
							// 放入图片地址
							map.put("pimage", "upload/"+fileName);

						}
					}
					// 封装数据
					BeanUtils.populate(product,map);
					
					// 页面中没有封装的商品字段：
					product.setPid(CommonUtils.getUUID());
					product.setPdate(new Date());
					product.setPflag(0);
					
					Category category = new Category();
					category.setCid(map.get("cid").toString());
					product.setCategory(category);
				}
			}
			
			// 传入service层
			AdminService service = new AdminService();
			service.addProduct(product);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 重定向到商品列表
		response.sendRedirect(request.getContextPath()+"/admin?method=getProductList");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}