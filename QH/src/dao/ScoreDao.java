package dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import utils.DataSourceUtils;

/**
 * 根据传入分数判断是否上榜
 * 
 * @author Sun
 * @date Sep 21, 2018
 * @version V1.0
 */
public class ScoreDao {

	/**
	 * 查询数据库判断传入分数是否大于榜单最低得分
	 * 
	 * @param score	分数
	 * @return	是否上榜信息
	 * @throws SQLException
	 */
	public boolean getScore(int score) throws SQLException {

		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());

		String sql = "SELECT MIN(score) FROM player";

		int theScore = (Integer) qr.query(sql, new ScalarHandler());

		if (theScore >= score) {
			return false; // 无需入榜单
		}

		return true; // 分数高于榜单最低人，可以上榜
	}

}
