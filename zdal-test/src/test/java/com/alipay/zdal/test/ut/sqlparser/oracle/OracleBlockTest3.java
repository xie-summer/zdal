package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;


public class OracleBlockTest3   {
@Test
    public void test_0() throws Exception {
        String sql = "DECLARE " + //
                     "  job BINARY_INTEGER := :job; " + //
                     "  next_date DATE := :mydate;  " + //
                     "  broken BOOLEAN := FALSE; " + //
                     "BEGIN " + //
                     "  get_promo_product_search; " + //
                     "  :mydate := next_date; " + //
                     "  IF broken THEN :b := 1; " + //
                     "  ELSE :b := 0; " + //
                     "  END IF; " + //
                     "END; ";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

   

        Assert.assertEquals(0, visitor.getTables().size());

        //        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));

        Assert.assertEquals(0, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("departments", "department_id")));
    }
}
