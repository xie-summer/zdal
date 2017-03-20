package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleBlockTest11   {
@Test
    public void test_0() throws Exception {
        String sql = "DROP TABLE emp;" + "CREATE TABLE emp AS SELECT * FROM employees;" + " "
                     + "DECLARE" + "  CURSOR c1 IS" + "    SELECT * FROM emp"
                     + "    FOR UPDATE OF salary" + "    ORDER BY employee_id;"
                     + "   emp_rec  emp%ROWTYPE;" + "BEGIN" + "  OPEN c1;" + "  LOOP"
                     + "    FETCH c1 INTO emp_rec;  -- fails on second iteration\n"
                     + "    EXIT WHEN c1%NOTFOUND;" + "    DBMS_OUTPUT.PUT_LINE ("
                     + "      'emp_rec.employee_id = ' ||" + "      TO_CHAR(emp_rec.employee_id)"
                     + "    );" + "    " + "    UPDATE emp" + "    SET salary = salary * 1.05"
                     + "    WHERE employee_id = 105;" + " " + "    COMMIT;  -- releases locks\n"
                     + "  END LOOP;" + "END;"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        

        Assert.assertEquals(3, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        for (SQLStatement statement : statementList) {
            statement.accept(visitor);
        }


        Assert.assertEquals(2, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("emp")));

        Assert.assertEquals(4, visitor.getColumns().size());
        Assert.assertEquals(1, visitor.getConditions().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
    }
}
