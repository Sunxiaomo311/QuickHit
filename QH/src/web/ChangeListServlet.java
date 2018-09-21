package web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.ChangeScoreService;

/**
 * 榜单信息更改
 * 
 * @author Sun
 * @date Sep 21, 2018
 * @version V1.0
 */
@WebServlet("/changeList")
public class ChangeListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8"); // 编码问题（不完善）

		String score = request.getParameter("score");
		String name = request.getParameter("name");
		String desc = request.getParameter("desc");

		System.out.println(name);
		System.out.println(desc); // 后台日志可查看玩家输入信息

		Map<String, String> map = new HashMap<String, String>(); // 玩家数据封装

		map.put("score", score);
		map.put("name", name);
		map.put("desc", desc);

		ChangeScoreService changeScoreService = new ChangeScoreService();
		try {
			changeScoreService.setMessage(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
