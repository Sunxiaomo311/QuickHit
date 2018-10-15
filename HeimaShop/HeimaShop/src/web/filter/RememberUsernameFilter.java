package web.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 记住用户名filter
 * @author Sun
 * @date Sep 27, 2018
 * @version V1.0
 */
public class RememberUsernameFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		// 获得cookie中用户名
		// 定义cookie_username
		String rememberUsername = null;
		// 获得cookie
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// 获得名字是rememberUsername
				if ("rememberUsername".equals(cookie.getName())) {
					rememberUsername = cookie.getValue();
					// 恢复中文用户名
					rememberUsername = URLDecoder.decode(rememberUsername, "UTF-8");
				}
			}
		}

		// 判断username是否是null
		if (rememberUsername != null) {
			// 登录的代码
			// 将登录的用户的username存到session中
			session.setAttribute("rememberUsername", rememberUsername);
		}

		// 放行
		chain.doFilter(req, resp);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}