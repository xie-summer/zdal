package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleBlockTest13   {
@Test
    public void test_0() throws Exception {
        String sql = "DECLARE" + //
                     "  emp_id        employees.employee_id%TYPE;" + //
                     "  emp_lastname  employees.last_name%TYPE;" + //
                     "  emp_salary    employees.salary%TYPE;" + //
                     "" + //
                     "BEGIN" + //
                     "  SELECT employee_id, last_name, salary" + //
                     "  INTO emp_id, emp_lastname, emp_salary " + //
                     "  FROM employees" + //
                     "  WHERE employee_id = 120;" + //
                     " " + //
                     "  UPDATE emp_name" + //
                     "  SET salary = salary * 1.1" + //
                     "  WHERE employee_id = emp_id;" + //
                     "" + //
                     "  DELETE FROM emp_name" + //
                     "  WHERE employee_id = 130;" + //
                     "" + //
                     "  SAVEPOINT do_insert;" + //
                     " " + //
                     "  INSERT INTO emp_name (employee_id, last_name, salary)" + //
                     "  VALUES (emp_id, emp_lastname, emp_salary);" + //
                     " " + //
                     "EXCEPTION" + //
                     "  WHEN DUP_VAL_ON_INDEX THEN" + //
                     "    ROLLBACK TO do_insert;" + //
                     "  DBMS_OUTPUT.PUT_LINE('Insert was rolled back');" + //
                     "END;"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
      

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        for (SQLStatement statement : statementList) {
            statement.accept(visitor);
        }



        Assert.assertEquals(2, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("emp_name")));

        Assert.assertEquals(7, visitor.getColumns().size());
        Assert.assertEquals(3, visitor.getConditions().size());
        Assert.assertEquals(1, visitor.getRelationships().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
    }
}
