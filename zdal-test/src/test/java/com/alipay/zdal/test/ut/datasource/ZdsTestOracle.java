package com.alipay.zdal.test.ut.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;


public class ZdsTestOracle {
    protected static DataSource   dataSource = null;
    protected static JdbcTemplate jt         = null;

    @BeforeClass
    public static void setUp() throws Exception {
        LocalTxDataSourceDO dsDo = new LocalTxDataSourceDO();
        dsDo.setDsName("test");
        dsDo.setConnectionURL("jdbc:oracle:thin:@perf6.lab.alipay.net:1521:perfdb6");
        dsDo.setUserName("Acm");
        dsDo.setPassWord("ali88");
        dsDo.setDriverClass("oracle.jdbc.OracleDriver");
        dsDo.setMinPoolSize(0);
        dsDo.setMaxPoolSize(5);
        dsDo
            .setExceptionSorterClassName("com.alipay.zdal.datasource.resource.adapter.jdbc.vendor.OracleExceptionSorter");
        dsDo.setPreparedStatementCacheSize(0);
        dataSource = new ZDataSource(dsDo);
        jt = new JdbcTemplate(dataSource);
    }

    /**
     * select
     */
    @Test
    public void test1() {
        List<Result> results = jt.query("select id,name from testcase", new TestCaseRowmapper());
        for (Result result : results) {
            System.out.println(result.getId());
            System.out.println(result.getName());
        }
        //        System.out.println(jt.queryForList("select * from test09 "));

    }

    /**
     * insert update delete 
     */
    @Test
    public void test2() {
        System.out.println(jt.update("insert into testcase (id,name) values(1,'ddd')")
                           + " affected");
        System.out.println(jt.queryForList("select * from testcase"));
        System.out.println(jt.update("update testcase set name='xxx' where id=1") + " affected");
        System.out.println(jt.queryForList("select * from testcase"));
        System.out.println(jt.update("delete  from  testcase") + " affected");
        System.out.println(jt.queryForInt("select count(*) from testcase"));

    }

    class TestCaseRowmapper implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Result result = new Result();
            result.setId(rs.getInt(1));
            result.setName(rs.getString(2));
            return result;
        }

    }

    class Result {
        private int    id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    //    @Test
    //    public void test2() {
    //    }
}