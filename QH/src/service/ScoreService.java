package service;

import java.sql.SQLException;

import dao.ScoreDao;

/**
 * 根据传入分数判断是否上榜
 * 
 * @author Sun
 * @date Sep 21, 2018
 * @version V1.0
 */
public class ScoreService {

	/**
	 * 传入分数至dao层进行判断
	 * 
	 * @param score 最终得分
	 * @return 是否上榜信息
	 * @throws SQLException
	 */
	public boolean getScore(int score) throws SQLException {
		
		ScoreDao scoreDao = new ScoreDao();
		
		return scoreDao.getScore(score);
	}

}
