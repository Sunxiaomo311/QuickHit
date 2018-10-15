package web.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 生成验证码图片（纯数字）
 * 
 * @author Sun
 * @date Sep 26, 2018
 * @version V1.0
 */
public class CheckNumServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int width = 84;
		int height = 28;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();

		// 创建一个随机数生成器类
		Random random = new Random();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定
		Font font = new Font("雅黑", Font.ITALIC, 30);
		// 设置字体
		g.setFont(font);
		// 画边框
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		// 随机产生160条干扰线，使图像中的认证码不易被其他程序探测到
		g.setColor(Color.GRAY);
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;
		// 随机产生4位数字的验证码
		for (int i = 0; i < 4; i++) {
			// 得到产生的随机验证码数字
			String strRand = String.valueOf(random.nextInt(10));
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同
			red = random.nextInt(200);
			green = random.nextInt(200);
			blue = random.nextInt(200);

			// 产生随机高度13至height之间
			float imght = 0;
			while (imght <= 12) {
				imght = Float.parseFloat(String.valueOf(random.nextInt(height)));
			}
			// 用随机产生的颜色将验证码绘制到图像中
			g.setColor(new Color(red, green, blue));
			g.drawString(strRand, 13 * i + 6, height);
			// 将产生的四个随机数组合在一起
			randomCode.append(strRand);
		}
		// 将四位数字的验证码保存到Session中
		HttpSession session = request.getSession();
		session.setAttribute("randomCode", randomCode.toString());
		// 禁止图像缓存
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 将图像输出到Servlet输出流中
		ServletOutputStream stream = response.getOutputStream();
		ImageIO.write(image, "jpeg", stream);
		stream.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}