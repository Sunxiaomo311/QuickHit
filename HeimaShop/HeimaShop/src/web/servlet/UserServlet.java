package web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import domain.User;
import service.UserService;
import utils.CommonUtils;
import utils.MailUtils;

/**
 * 用户相关功能模块
 * 
 * @author Sun
 * @date Oct 6, 2018
 * @version V1.0
 */
public class UserServlet extends BaseServlet {

	// 注册用户 接收注册表单提交数据
	public void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 表单提交的信息
		Map<String, String[]> properties = request.getParameterMap();
		User user = new User();
		try {
			// 自定义一个类型转换器(将String转成Date)
			ConvertUtils.register(new Converter() {
				@Override
				public Object convert(Class clazz, Object value) {
					// 将String转成date
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date parse = null;
					try {
						parse = (Date) format.parse(value.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return parse;
				}
			}, Date.class);
			// 映射封装
			BeanUtils.populate(user, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		// 表单未提交的数据
		user.setUid(CommonUtils.getUUID());
		user.setState(0); // 0为未激活 1为激活
		user.setTelephone(null);
		String code = CommonUtils.getUUID();
		user.setCode(code); // 存入激活码

		// 传入service层
		UserService userService = new UserService();
		boolean isRegisterSuccess = false;
		try {
			isRegisterSuccess = userService.userRegister(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (isRegisterSuccess) { // 注册成功
			// 发送激活邮件
			String emailMsg = "恭喜您注册成功，请点击链接进行用户激活：<a href='http://localhost:8080" 
					+ "/HeimaShop/user?method=active&activeCode="
					+ code + "'>" + code + "</a>";
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}

			// 跳转到注册成功页面
			response.sendRedirect(request.getContextPath() + "/registerSuccess.jsp");
		} else { // 注册失败
			// 跳转到注册失败页面
			response.sendRedirect(request.getContextPath() + "/registerFail.jsp");
		}
	}

	// 注销功能
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 移除当前用户对象
		request.getSession().removeAttribute("user");

		// 移除cookie对象
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// 获得名字是cookie_username和cookie_password
				if ("cookie_username".equals(cookie.getName())) {
					// 删除cookie
					cookie.setMaxAge(0);
					cookie.setPath(request.getContextPath());
					// 发送cookie
					response.addCookie(cookie);
				}
				if ("cookie_password".equals(cookie.getName())) {
					cookie.setMaxAge(0);
					cookie.setPath(request.getContextPath());
					response.addCookie(cookie);
				}
			}
		}

		// 回首页
		response.sendRedirect(request.getContextPath());
	}

	// 用户登陆
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		// 获取数据
		String username = request.getParameter("username");// 中文 张三
		String password = request.getParameter("password");

		UserService service = new UserService();
		User user = null;
		try {
			user = service.loginCheck(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 登录成功
		if (user != null) {
			// 判断用户是否勾选自动登录
			String autoLogin = request.getParameter("autoLogin");
			if (autoLogin != null) {
				// 对中文张三进行编码
				String username_code = URLEncoder.encode(username, "UTF-8");// %AE4%kfj

				Cookie cookie_username = new Cookie("cookie_username", username_code);
				Cookie cookie_password = new Cookie("cookie_password", password);
				// 设置cookie的持久化时间
				cookie_username.setMaxAge(60 * 2);
				cookie_password.setMaxAge(60 * 2);
				// 设置cookie的携带路径
				cookie_username.setPath(request.getContextPath());
				cookie_password.setPath(request.getContextPath());
				// 发送cookie
				response.addCookie(cookie_username);
				response.addCookie(cookie_password);
			}

			// 判断是否记住用户名
			String RememberUsername = request.getParameter("RememberUsername");
			if (RememberUsername != null) {
				// 对用户名进行编码
				String rememberUsername = URLEncoder.encode(username, "UTF-8");// %AE4%kfj
				Cookie cookie_username = new Cookie("rememberUsername", rememberUsername);
				// 设置cookie的持久化时间
				cookie_username.setMaxAge(60 * 60);
				// 设置cookie的携带路径
				cookie_username.setPath(request.getContextPath());
				// 发送cookie
				response.addCookie(cookie_username);
			}

			// 将登录的用户的user对象存到session中
			session.setAttribute("user", user);
			// 重定向到首页
			response.sendRedirect(request.getContextPath());

		} else {
			// 失败 转发到登录页面 提出提示信息
			request.setAttribute("loginFail", "用户名或密码错误");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

	// 登录验证码验证
	public void loginCheck(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String code1 = request.getParameter("randomCode");
		String code2 = request.getSession().getAttribute("randomCode").toString();
		request.getSession().removeAttribute("randomCode");
		if (!code1.equals(code2)) {
			// 验证码错误
			request.setAttribute("msg", "验证码错误");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		} else {
			// 验证正确 转发至注册servlet
			request.getRequestDispatcher("/user?method=login").forward(request, response);
		}
	}

	// 用户激活
	public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String activeCode = request.getParameter("activeCode"); // 获取激活码

		UserService userService = new UserService();
		boolean isActive = false;
		try {
			isActive = userService.userActivation(activeCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (isActive) {
			// 激活成功 跳转登陆页面
			response.sendRedirect(request.getContextPath() + "/activeSuccesstoLogin.jsp");
		} else {
			// 激活失败 跳转失败页面
			response.sendRedirect(request.getContextPath() + "/registerFail.jsp");
		}

	}

	// 验证用户名是否存在
	public void checkUsername(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");

		UserService userService = new UserService();
		boolean usernameIsExist = userService.checkUsername(username);

		String IsExistJson = "{\"isExist\":" + usernameIsExist + "}"; // 返回true说明用户名已存在

		response.getWriter().write(IsExistJson);
	}

	// 验证码验证
	public void check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String code1 = request.getParameter("checkCode").toLowerCase();
		String code2 = request.getSession().getAttribute("checkCode").toString().toLowerCase();
		request.getSession().removeAttribute("checkCode");
		if (!code1.equals(code2)) {
			// 验证码错误
			request.setAttribute("msg", "验证码错误");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
		} else {
			// 验证正确 转发至注册servlet
			request.getRequestDispatcher("/user?method=register").forward(request, response);
		}
	}
}