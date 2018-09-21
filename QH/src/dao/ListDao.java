package dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import domain.Player;
import utils.DataSourceUtils;

/**
 * 榜单数据更新
 * 
 * @author Sun
 * @date Sep 21, 2018
 * @version V1.0
 */
public class ListDao {

	/**
	 * 获取所有榜单信息并封装为list传输给service层
	 * 
	 * @return player集合list
	 * @throws SQLException
	 */
	public List<Player> getList() throws SQLException {

		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());

		String sql = "select * from player";

		return qr.query(sql, new BeanListHandler<Player>(Player.class));
	}

}
