package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;


public class OracleBlockTest6   {
@Test
    public void test_0() throws Exception {
        String sql = "declare" + //
                     "     l_line varchar2(32767);" + //
                     "     l_done number;" + //
                     "     l_buffer varchar2(32767) := '';" + //
                     "    l_lengthbuffer number := 0;" + //
                     "    l_lengthline number := 0;  " + //
                     "begin" + //
                     "   loop" + //
                     "     dbms_output.get_line( l_line, l_done );" + //
                     "     if (l_buffer is null) then" + //
                     "       l_lengthbuffer := 0;" + //
                     "     else" + //
                     "       l_lengthbuffer := length(l_buffer);" + //
                     "     end if;" + //
                     "     if (l_line is null) then" + //
                     "       l_lengthline := 0;" + //
                     "     else" + //
                     "       l_lengthline := length(l_line);" + //
                     "     end if;" + //
                     "     exit when l_lengthbuffer + l_lengthline > :maxbytes " + //
                     "          OR l_lengthbuffer + l_lengthline > 32767 OR l_done = 1;" + //
                     "     l_buffer := l_buffer || l_line || chr(10);" + //
                     "   end loop;" + //
                     "  :done := l_done;" + //
                     "  :buffer := l_buffer;" + //
                     "  :line := l_line; " + //
                     "end;";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
       

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        

        Assert.assertEquals(0, visitor.getTables().size());

        //        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("escrow_trade")));

        Assert.assertEquals(0, visitor.getColumns().size());
        Assert.assertEquals(0, visitor.getConditions().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("departments", "department_id")));
    }
}
