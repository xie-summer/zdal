/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.zdal.parser.druid.bvt.sql.mysql;

import java.util.List;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: LOAD_XML_Syntax_Test.java, v 0.1 2012-5-17 ÉÏÎç10:05:29 xiaoqing.zhouxq Exp $
 */
public class LOAD_XML_Syntax_Test extends TestCase {

    public void test_0() throws Exception {
        //        String sql = "LOAD XML LOCAL INFILE 'person.xml' INTO TABLE person ROWS IDENTIFIED BY '<person>';";
        String sql = "select "
                     + "t1.owner_card_no,opposite_card_no,opposite_name,opposite_nick_name,consume_title,"
                     + "consume_fee,consume_type,t1.biz_type,biz_sub_type,biz_orig,t1.biz_in_no,biz_out_no,"
                     + "biz_extra_no,biz_state,consume_status,consume_refund_status,in_out,consume_site,"
                     + "trade_from,flag_locked,flag_refund,gmt_biz_create,delete_type,delete_time,"
                     + "delete_over_time,gmt_modify from "
                     + "(select owner_card_no,biz_in_no,biz_type from ps_consume_delete15 where owner_card_no = ?  and delete_type IN ( ?,?) ORDER BY gmt_biz_create DESC LIMIT ?, ?) "
                     + " as t1,ps_consume_delete15 t2    "
                     + " where t1.owner_card_no =t2.owner_card_no  and t1.biz_in_no=t2.biz_in_no and  t1.biz_type=t2.biz_type order by biz_type";
        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);
        System.out.println(text);
        //        Assert.assertEquals(
        //            "LOAD XML LOCAL INFILE 'person.xml' INTO TABLE person ROWS IDENTIFIED BY '<person>';",
        //            text);
    }

    private String output(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();

        for (SQLStatement stmt : stmtList) {
            stmt.accept(new MySqlOutputVisitor(out));
            out.append(";");
        }

        return out.toString();
    }

}
