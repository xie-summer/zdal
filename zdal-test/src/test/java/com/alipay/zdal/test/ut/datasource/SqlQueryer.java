/**
 * 
 */
package com.alipay.zdal.test.ut.datasource;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:xiang.yangx@alipay.com">Yang Xiang</a>
 *
 */
public class SqlQueryer extends SqlExecutor {

	public SqlQueryer(JdbcTemplate jdbcTemplate, String sql,
			Object[] parametersList, int intervalMillSec) {
		super(jdbcTemplate, sql, parametersList, intervalMillSec);
	}

	public SqlQueryer(JdbcTemplate jdbcTemplate, String sql,
			Object[] parametersList, int intervalMillSec, ConcurrentHashMap<Long, List<Exception>> exceptionCollection) {
		super(jdbcTemplate, sql, parametersList, intervalMillSec, exceptionCollection);
	}
	
	@Override
	void execute() {
		List<?> resultList = jdbcTemplate.queryForList(sql, parametersList);
		Assert.notNull(resultList);
	}

}
