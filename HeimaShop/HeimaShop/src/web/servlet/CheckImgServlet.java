package web.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 生成验证图片
 * 
 * @author Sun
 * @date Aug 30, 2018
 * @version V2.0
 */
public class CheckImgServlet extends HttpServlet {

	final String DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 1.高和宽
		int height = 30;
		int width = 60;
		
		Random random = new Random();

		// 2.创建一个图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 3.获得画板
		Graphics g = image.getGraphics();

		// 4.填充一个矩形
		// *设置颜色
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		g.setColor(Color.WHITE);
		g.fillRect(1, 1, width - 2, height - 2);

		// *设置字体
		g.setFont(new Font("宋体", Font.BOLD | Font.ITALIC, 25));

		StringBuffer checkImgStr = new StringBuffer();	// 验证码字符串
		
		// 5.写随机字
		for (int i = 0; i < 4; i++) {
			// 设置颜色--随机数
			g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			// 获得随机字
			int index = random.nextInt(DATA.length());
			String str = DATA.substring(index, index + 1);
			checkImgStr.append(str);

			// 写入
			g.drawString(str, width / 6 * (i + 1), 20);
		}

		// 6.干扰线
		for (int i = 0; i < 3; i++) {
			// 设置颜色--随机数
			g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			// 随机绘制线
			g.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
			// 随机点
			g.drawOval(random.nextInt(width), random.nextInt(height), 2, 2);
		}
		
		// 验证码字符串存入session域
		request.getSession().setAttribute("checkCode", checkImgStr.toString());

		// 7.将图片响应给浏览器
		ImageIO.write(image, "jpg", response.getOutputStream());

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}