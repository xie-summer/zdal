package com.alipay.zdal.springboot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={DataSourceTest.class})
public class ZdsTxTest {

	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@Test
	public void testInsert() {
		
		int savedRow=transactionTemplate.execute(new TransactionCallback<Integer>() {

			@Override
			public Integer doInTransaction(TransactionStatus status) {
				
				try {
					KeyHolder keyHolder = new GeneratedKeyHolder();
					jdbcTemplate.update(new PreparedStatementCreator() {
						
						@Override
						public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
							
							PreparedStatement ps=con.prepareStatement("insert into t_shop(name) values(?)",Statement.RETURN_GENERATED_KEYS);
							ps.setString(1, "test123456");
							return ps;
						}
					},keyHolder);
					
					if(keyHolder.getKey().intValue()>0)
					  throw new Exception("事务回滚实测");
					jdbcTemplate.update("insert into t_user(username,password,shopId) values(?,?,?)", new Object[]{"test44","test55",keyHolder.getKey().intValue()});
					return 1;
				} catch (Exception e) {
					e.printStackTrace();
					status.setRollbackOnly();
					return 0;
				} 
			}
		});
		
		Assert.assertTrue(savedRow>0);
	}

}
