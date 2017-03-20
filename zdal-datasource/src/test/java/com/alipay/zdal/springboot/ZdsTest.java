package com.alipay.zdal.springboot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={DataSourceTest.class})
public class ZdsTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Test
	public void testInsertByJT() {
		int updatedRows=jdbcTemplate.update("insert into t_user(username,password) values(?,?)", new Object[]{"aa","aaa"});
		Assert.assertTrue(updatedRows>0);
	}
	
	@Test
	public void testInsertByNPJT(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("username", "gg");
		map.put("password", "tt");
		SqlParameterSource parameter = new MapSqlParameterSource(map);
		int savedRows=namedParameterJdbcTemplate.update("insert into t_user(username,password) values(:username,:password)",parameter);
		Assert.assertTrue(savedRows>0);
	}
	
	
	@Test
	public void testUpdateByJT(){
	  int updatedRows=jdbcTemplate.update("update t_user set username=?,password=? where userId=?", new Object[]{"ff","123456",1});
	  Assert.assertTrue(updatedRows>0);
	}
	
	@Test
	public void testUpdateByNPJT(){
		User user=new User();
		user.setUserId(2);
		user.setPassword("hh");
		user.setUsername("oo");
		SqlParameterSource paramSource=new BeanPropertySqlParameterSource(user);
		
		int updatedRows=namedParameterJdbcTemplate.update("update t_user set username=:username,password=:password where userId=:userId", paramSource);
		Assert.assertTrue(updatedRows>0);
	}
	
	
	
	@Test
	public void testSelect(){
	
	  List<User> users=namedParameterJdbcTemplate.query("select * from t_user", new BeanPropertyRowMapper<User>(User.class));
	  Assert.assertTrue(users!=null&&users.size()>0);
	}
	
	@Test
	public void testDelete(){
		//int deleted=jdbcTemplate.update("delete from t_user where userid=?",new Object[]{1});
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", 2);
		int deleted=namedParameterJdbcTemplate.update("delete from t_user where userid=:userId", paramMap);
		 Assert.assertTrue(deleted>0);
	}
	
	

}
