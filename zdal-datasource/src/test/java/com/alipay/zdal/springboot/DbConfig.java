package com.alipay.zdal.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.datasource.util.PoolCondition;

@Configuration
public class DbConfig {

	private final static Logger logger = LoggerFactory.getLogger(DbConfig.class);

	@Bean(name = "dataSource")
	public ZDataSource createZDataSourceBean() throws Exception {
		logger.info("createZDataSourceBean");
		LocalTxDataSourceDO dataSourceDO = new LocalTxDataSourceDO();
		dataSourceDO.setDsName("TestDs");
		dataSourceDO.setUserName("root");
		dataSourceDO.setPassWord("123456");
		dataSourceDO.setDriverClass("com.mysql.jdbc.Driver");
		dataSourceDO.setMinPoolSize(0);
		dataSourceDO.setMaxPoolSize(5);
		dataSourceDO.setExceptionSorterClassName(MySQLExceptionSorter.class.getName());
		dataSourceDO.setPreparedStatementCacheSize(0);
		dataSourceDO
				.setConnectionURL("jdbc:mysql://localhost:3306/diamond?useUnicode=true&amp;characterEncoding=UTF-8");
		
		ZDataSource dataSource=new ZDataSource(dataSourceDO);
		
		PoolCondition poolInfo=dataSource.getPoolCondition();
		
		logger.info("pool:{}",poolInfo);
		return dataSource;
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate createJdbcTemplateBean() throws Exception {
		logger.info("createJdbcTemplateBean");
		return new JdbcTemplate(createZDataSourceBean());
	}

	@Bean(name = "namedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate createNamedParameterJdbcTemplateBean() throws Exception {
		logger.info("createNamedParameterJdbcTemplateBean");
		return new NamedParameterJdbcTemplate(createZDataSourceBean());
	}

	@Bean(name = "transactionTemplate")
	public TransactionTemplate createTransactionTemplateBean() throws Exception {
		return new TransactionTemplate(new DataSourceTransactionManager(createZDataSourceBean()));
	}

}
