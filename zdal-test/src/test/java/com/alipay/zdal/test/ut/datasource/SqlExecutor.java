/**
 * 
 */
package com.alipay.zdal.test.ut.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author <a href="mailto:xiang.yangx@alipay.com">Yang Xiang</a>
 *
 */
public abstract class SqlExecutor extends Thread {

	protected String sql;
	
	protected Object[] parametersList;
	
	protected int intervalMillSec;
	
	protected JdbcTemplate jdbcTemplate;
	
	private boolean running = true;
	
	protected ConcurrentHashMap<Long, List<Exception>> exceptionCollection;
	
	public SqlExecutor(JdbcTemplate jdbcTemplate, String sql, Object[] parametersList, int intervalMillSec){
		this.sql = sql;
		this.jdbcTemplate = jdbcTemplate;
		this.parametersList = parametersList;
		this.intervalMillSec = intervalMillSec;
	}
	
	public SqlExecutor(JdbcTemplate jdbcTemplate, String sql, Object[] parametersList, 
			int intervalMillSec, ConcurrentHashMap<Long, List<Exception>> exceptionCollection){
		this.sql = sql;
		this.jdbcTemplate = jdbcTemplate;
		this.parametersList = parametersList;
		this.intervalMillSec = intervalMillSec;
		this.exceptionCollection = exceptionCollection;
	}
	
	public void stopRunning(){
		running = false;
	}
	
	public void run(){
		while( running ){
			try {
				execute();
				sleep(intervalMillSec);
			}catch(org.springframework.jdbc.CannotGetJdbcConnectionException ooce){
				if( null != exceptionCollection ){
					List<Exception> exceptions = exceptionCollection.get(Thread.currentThread().getId());
					if( null == exceptions ){
						exceptions = new ArrayList<Exception>();
						exceptionCollection.put(Thread.currentThread().getId(), exceptions);
					}
					exceptions.add(ooce);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	abstract void execute(); 
}
