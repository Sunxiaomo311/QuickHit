package dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import domain.Player;
import utils.DataSourceUtils;

/**
 * 更新排名信息
 * 
 * @author Sun
 * @date Sep 21, 2018
 * @version V1.0
 */
public class ChangeScoreDao {
	
	static QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
	
	/**
	 * 	将传入信息更新到数据库
	 * 
	 * @param findOrder 需要更新的列表
	 * @throws SQLException
	 */
	public void setMessage(List<Player> findOrder) throws SQLException {

		String sql = "UPDATE player SET name = ?,score = ?,message = ? WHERE ranking = ?";
		
		for (Player player : findOrder) {	// 依次更新
			qr.update(sql, player.getName(),player.getScore(),player.getMessage(),player.getRanking());
		}
		
	}

	/**
	 * 根据传入分数查询应更新的排名信息集合
	 * 
	 * @param score	得分
	 * @return	应更新的player信息
	 * @throws SQLException
	 */
	public List<Player> findOrder(int score) throws SQLException {
		
		String sql = "select * from player where score > ?";
		
		List<Player> query = qr.query(sql, new BeanListHandler<Player>(Player.class), score);	// 分数高于传入分数的player信息集合
		
		int num = query.size();	// 集合的个数（+1即为应更新的排名）
		
		String sql2 = "select * from player where ranking >= ?";
		
		List<Player> query2 = qr.query(sql2, new BeanListHandler<Player>(Player.class), num+1);
		
		return query2;
	}

}
