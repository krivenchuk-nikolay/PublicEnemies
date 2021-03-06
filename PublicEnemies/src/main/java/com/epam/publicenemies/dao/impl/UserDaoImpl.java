package com.epam.publicenemies.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement; 
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.epam.publicenemies.dao.IUserDao;
import com.epam.publicenemies.domain.User;
import com.epam.publicenemies.web.listeners.OnContextLoaderListener;


/**
 * Manages requests for 'user' table
 * 
 * Updated by Chetyrkin S.V.
 * Updated by I. Kostyrko on 30.04.2012: 
 * 		select queries changed; 
 * 		register method;
 * 
 * TODO: add moneyUpdate method; to declare all queries as final fields etc.
 * */
public class UserDaoImpl implements IUserDao {

	private Logger log = Logger.getLogger(OnContextLoaderListener.class);
	private JdbcTemplate jdbcTemplate;
	/* Query template for creating user */
	private final String INSERT_USER_SQL = "INSERT INTO users (email, password, nickName, userCharacter) VALUES (?,?,?,?)";
	private final String INSERT_CHARACTER_SQL = "INSERT INTO characters (sex) VALUES (?)";
	// ... put list of queries here

	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Registers new user with email, password and nick name and returns id
	 * 
	 * @param user - User object
	 * @return id of inserted user
	 * */
	public int registerUser(User user) {
		return this.registerUser(user.getEmail(), user.getPassword(), user.getNickName()); 
	}

	/**
	 * Register new user with email, password, nickName
	 *  
	 * Updated by I. Kostyrko on 30.04.12: one query instead of two (see previous method)
	 *   
	 * @param email - email (unique)
	 * @param password - password 
	 * @param nickname - nickname (unique)
	 * @return id of inserted entry or -1 if parameters are bad 
	 */
	public int registerUser(final String email, final String password,
			final String nickName) {
		// check parameters
		if (email == null || password == null || nickName == null) {
			return -1;
		}
		// will contain id of inserted entry
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, email);
						ps.setString(2, password);
						ps.setString(3, nickName);
						// create new character entry
						ps.setInt(4, createCharacterEntry()); 
						return ps;
					}
				}, keyHolder);
		log.info("UserDaoImpl.regiserUser: ID is " + keyHolder.getKey().intValue());
		return keyHolder.getKey().intValue();		
	}
	
	/**
	* Check for existing registered email
	* @param email - checked email
	* @return boolean exist or not
	* */
	public boolean checkExistedUserEmail (String email){
		String query = "SELECT userId FROM users WHERE email = ?";
		
		int i = jdbcTemplate.queryForInt(query,
				new Object[] {email}, new RowMapper<Integer>() {
					public Integer mapRow(ResultSet resultSet, int rowNum)
							throws SQLException {
						return new Integer(resultSet.getInt("userId"));
					}
				});
		if (i==0) return false;
		else return true;
	}
	
	/**
	*  
	* Find user by its unique id
	* 
	* Updated on 30.04.12 (K.I.): 
	* 	updated SELECT query 
	*  
	* To fix: return statement (there is the list(!) 
	* 		  but we need only one object always);
	* 		  add other parameters for fetching 							
	* 
	* @param id - id of user you are looking for
	* @return User object
	* */
	public User findUserById(final int userId) {
		String query = "SELECT * FROM users WHERE userId=?";
		List<User> list = jdbcTemplate.query(query, new Object[]{userId}, new RowMapper<User>() {
			public User mapRow(ResultSet resultSet, int rowNum)
					throws SQLException {
				return new User(userId, 
						resultSet.getString("email"), 
						resultSet.getString("password"), 
						resultSet.getString("nickName"),
						resultSet.getInt("money"),
						resultSet.getString("avatar"),
						resultSet.getInt("userCharacter")
						);
			}
		});
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	/**
	* Find user by email (unique)
	* Use checkExistedUserEmail() instead of this
	* 
	* @param email - user email
	* @param password - user password
	* @return User object
	* */
	@Deprecated
	public User findUserByEmailAndPassword(final String email, final String password){
		String query = "SELECT * FROM users WHERE email=? AND password=?";
		List <User> list = jdbcTemplate.query(query, new Object[]{email, password}, new RowMapper<User>() {
			public User mapRow(ResultSet resultSet, int rowNum)
					throws SQLException {
				return new User(resultSet.getInt("userId"), 
						resultSet.getString("email"), 
						resultSet.getString("password"), 
						resultSet.getString("nickName"),
						resultSet.getInt("money"),
						resultSet.getString("avatar"),
						resultSet.getInt("userCharacter"));
			}
		});
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	* Find user by email
	* @param email - email of user that you are looking for
	* @return User object
	* */
	public User findUserByEmail(final String email) {
		String query = "SELECT * FROM users WHERE email=?";
		List <User> list = jdbcTemplate.query(query, new Object[]{email}, new RowMapper<User>() {
			public User mapRow(ResultSet resultSet, int rowNum)
					throws SQLException {
				return new User(resultSet.getInt("userId"), 
						resultSet.getString("email"), 
						resultSet.getString("password"), 
						resultSet.getString("nickName"),
						resultSet.getInt("money"),
						resultSet.getString("avatar"),
						resultSet.getInt("userCharacter"));
			}
		});
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	/**
	* Update user email and password
	* @param user - User object
	* @return true if operation successfully 
	* */
	public boolean updateUserEmailAndPassword(User user) {
		String query = "UPDATE users SET email=?, password=?, WHERE userId = ?";
		int i = jdbcTemplate.update(query,
				new Object[] { user.getEmail(), user.getPassword(),
						user.getUserId() });
		if (i==0) return false;
		else return true;
	}

	/**
	* Update user avatar
	* @param userId - id of user
	* @param avatar - path to avatar image on server
	* @return true if operation successfully  
	* */
	public boolean updateUserAvatar(int userId, String avatar){
		String query = "UPDATE users SET avatar=? WHERE userId = ?";
		int i = jdbcTemplate.update(query,
				new Object[] {avatar, userId});
		if (i==0) return false;
		else return true;
	}
	
	/**
	* Update user nick name
	* @param userId - id of user
	* @param nickName - nick name of user
	* @return true if operation successfully  
	* */
	public boolean updateUserNickName(int userId, String nickName){
		String query = "UPDATE users SET nickName=? WHERE userId = ?";
		int i = jdbcTemplate.update(query,
				new Object[] {nickName, userId});
		if (i==0) return false;
		else return true;
	}
	
	/**
	* Change all user personal information (user are fetched by id)
	* @param user - User object
	* @return true if operation successfully  
	* */
	public boolean updateUserInfo (User user){
		String query = "UPDATE users SET email=?, password=?, nickName=?, avatar=?, WHERE userId = ?";
		int i = jdbcTemplate.update(query,
				new Object[] {user.getEmail(), user.getPassword(), user.getNickName(), user.getAvatar(), user.getUserId()});
		if (i==0) return false;
		else return true;
	}
	
	/**
	* Delete user
	* @param userId - id of user
	* */
	public boolean deleteUser(User user) {
		String query = "DELETE FROM users WHERE id = ?";
		int i = jdbcTemplate.update(query, new Object[] { user.getUserId() });
		if (i==0) return false;
		else return true;
	}
	
	/**
	* Delete user
	* @param userId - id of user
	* */
	public boolean deleteUser(int userId){
		String query = "DELETE FROM users WHERE id = ?";
		int i = jdbcTemplate.update(query, new Object[] { userId });
		if (i==0) return false;
		else return true;
	}

	/**
	* Fetch all users from database
	* @return List<User> - list of all users on database
	* */
	public List<User> findAllUsers() {
		String query = "SELECT * FROM users";
		return jdbcTemplate.query(query, new RowMapper<User>() {
			public User mapRow(ResultSet resultSet, int rowNum)
					throws SQLException {
				return new User(resultSet.getInt("userId"), 
						resultSet.getString("email"), 
						resultSet.getString("password"), 
						resultSet.getString("nickName"),
						resultSet.getInt("money"),
						resultSet.getString("avatar"),
						resultSet.getInt("userCharacter"));
			}
		});
	}
	/**
	 * Created character entry. Uses before user creation
	 * @return identifier
	 */
	private int createCharacterEntry() {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		//log.info("UserDaoImpl.createCharacterEntry: try to create new one");
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(INSERT_CHARACTER_SQL, Statement.RETURN_GENERATED_KEYS);
						ps.setBoolean(1, true);						 
						return ps;
					}
				}, keyHolder);
		log.info("UserDaoImpl.createCharacterEntry: ID is " + keyHolder.getKey().intValue());
		return keyHolder.getKey().intValue();	
	}
}