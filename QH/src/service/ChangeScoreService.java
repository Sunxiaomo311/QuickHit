package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dao.ChangeScoreDao;
import domain.Player;

/**
 * 榜单信息更改
 * 
 * @author Sun
 * @date Sep 21, 2018
 * @version V1.0
 */
public class ChangeScoreService {

	/**
	 * 获得数据库信息，处理后再次传入数据库进行更新
	 * 
	 * @param map	web层传入的封装数据
	 * @throws SQLException
	 */
	public void setMessage(Map<String, String> map) throws SQLException {

		ChangeScoreDao changeScoreDao = new ChangeScoreDao();

		int score = Integer.parseInt(map.get("score")); // 获得玩家分数
		
		String name = map.get("name");
		String desc = map.get("desc"); // 获得其他数据
		
		List<Player> findOrder = changeScoreDao.findOrder(score);	// 依次需要更新的所有信息

		for(int i = findOrder.size()-1,j = 1; i >= j; i--) {
			Player player2 = findOrder.get(i);
			Player player = findOrder.get(i-1);
			player2.setName(player.getName());
			player2.setMessage(player.getMessage());
			player2.setScore(player.getScore());
			player2.setRanking(player.getRanking()+1);
		}
		
		Player player = findOrder.get(0);
		player.setName(name);
		player.setMessage(desc);
		player.setScore(score);
		player.setRanking(findOrder.get(0).getRanking());
		findOrder.set(0, player);	// 替换传入信息
		
		changeScoreDao.setMessage(findOrder);	// 进行更新 

	}

}
