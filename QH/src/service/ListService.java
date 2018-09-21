package service;

import java.sql.SQLException;
import java.util.List;

import dao.ListDao;
import domain.Player;

/**
 * 榜单更新
 * @author Sun
 * @date Sep 21, 2018
 * @version V1.0
 */
public class ListService {

	/**
	 * 调用dao层获取榜单信息
	 * @return	player的list集合
	 * @throws SQLException
	 */
	public List<Player> getList() throws SQLException {

		ListDao listDao = new ListDao();

		return listDao.getList();
	}

}
