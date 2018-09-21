package web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import domain.Player;
import service.ListService;

/**
 * 动态更新排行榜
 * 
 * @author Sun
 * @date Sep 21, 2018
 * @version V1.0
 */
@WebServlet("/list")
public class ListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8"); // 解决传出乱码问题

		ListService listService = new ListService();
		List<Player> list = null;
		try {
			list = listService.getList();	//获得player榜单信息list
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();	// Gson将list封装为json数据
		String json = gson.toJson(list);

		response.getWriter().write(json);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}