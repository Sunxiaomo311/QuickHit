package web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.ScoreService;

/**
 * 根据传入分数判断是否上榜
 * 
 * @author Sun
 * @date Sep 21, 2018
 * @version V1.0
 */
@WebServlet("/score")
public class ScoreServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int score = Integer.parseInt(request.getParameter("score")); // 上传的分数

		ScoreService scoreService = new ScoreService();
		boolean exist = true;
		try {
			exist = scoreService.getScore(score);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (exist) {
			response.getWriter().write("in"); // 上榜返回"in"
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}