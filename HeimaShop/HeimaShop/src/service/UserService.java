package service;

import java.sql.SQLException;

import dao.UserDao;
import domain.User;

/**
 * 用户功能 服务层
 * @author Sun
 * @date Sep 24, 2018
 * @version V1.0
 */
public class UserService {
	
	private static UserDao userDao = new UserDao();

	// 注册用户
	public boolean userRegister(User user) throws SQLException {
		
		int row = userDao.userRegister(user);
		
		return row>0 ? true:false;	// 有更新行数则表示注册成功
	}

	// 用户激活
	public boolean userActivation(String activeCode) throws SQLException {
		
		int row = userDao.userActivation(activeCode);
		
		return row>0 ? true:false;	// 有更新行数说明注册成功
	}

	// 判断用户名是否存在
	public boolean checkUsername(String username) {
		
		Long isExist = 0L;
		try {
			isExist = userDao.checkUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isExist>0 ? true:false;	// 返回行数大于0则说明存在用户名
	}

	public User loginCheck(String username, String password) throws SQLException {
		
		User user = userDao.loginCheck(username,password);
		
		return user;
	}

}
