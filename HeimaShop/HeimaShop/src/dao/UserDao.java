package dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import domain.User;
import utils.DataSourceUtils;

/**
 * 用户数据相关
 * 
 * @author Sun
 * @date Sep 24, 2018
 * @version V1.0
 */
public class UserDao {

	private static QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());

	// 用户激活
	public int userRegister(User user) throws SQLException {

		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";

		int update = qr.update(sql, user.getUid(), user.getUsername(), user.getPassword(), user.getName(),
				user.getEmail(), user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(),
				user.getCode());

		return update;
	}

	// 用户注册
	public int userActivation(String activeCode) throws SQLException {

		String sql = "update user set state=1 where code=?";

		int row = qr.update(sql, activeCode);

		return row;
	}

	// 判断用户名是否存在
	public Long checkUsername(String username) throws SQLException {

		String sql = "select count(*) from user where username=?";

		Long query = (Long) qr.query(sql, new ScalarHandler(), username);

		return query;
	}

	// 判断登陆信息是否正确
	public User loginCheck(String username, String password) throws SQLException {

		String sql = "select * from user where username=? and password=?";

		User user = qr.query(sql, new BeanHandler<User>(User.class), username,password);

		return user;
	}

}
