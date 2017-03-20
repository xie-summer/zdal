/**
 * 
 */
package com.alipay.zdal.test.ut.datasource;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:xiang.yangx@alipay.com">Yang Xiang</a>
 * 
 */
public class SqlUpdater extends SqlExecutor {

	private TransactionTemplate transactionTemplate;

	public SqlUpdater(JdbcTemplate jdbcTemplate, String sql,
			Object[] parametersList, int intervalMillSec) {
		super(jdbcTemplate, sql, parametersList, intervalMillSec);
		DataSourceTransactionManager tnManager = new DataSourceTransactionManager(
				jdbcTemplate.getDataSource());
		transactionTemplate = new TransactionTemplate(tnManager);
	}

	public SqlUpdater(JdbcTemplate jdbcTemplate, String sql,
			Object[] parametersList, int intervalMillSec, ConcurrentHashMap<Long, List<Exception>> exceptionCollection) {
		super(jdbcTemplate, sql, parametersList, intervalMillSec, exceptionCollection);
		DataSourceTransactionManager tnManager = new DataSourceTransactionManager(
				jdbcTemplate.getDataSource());
		transactionTemplate = new TransactionTemplate(tnManager);
	}
	
	@Override
	void execute() {
		Object result = null;
		result = transactionTemplate.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				try {
					int insertResult = jdbcTemplate.update(sql, parametersList);
					return insertResult;
				} catch (Exception e) {
					e.printStackTrace();
					status.setRollbackOnly();
				}
				return 0;
			}
		});
		Assert.isTrue( result != null );
		Assert.isTrue( Integer.parseInt(result.toString()) == 1);
	}

}
