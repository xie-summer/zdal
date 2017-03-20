/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.expr;

import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.util.HexBin;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLHexExpr.java, v 0.1 2012-11-17 ÏÂÎç3:17:15 xiaoqing.zhouxq Exp $
 */
public class SQLHexExpr extends SQLExprImpl implements SQLLiteralExpr {

    private static final long serialVersionUID = 1L;

    private final String      hex;

    public SQLHexExpr(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }

    public void output(StringBuffer buf) {
        buf.append("0x");
        buf.append(this.hex);

        String charset = (String) getAttribute("USING");
        if (charset != null) {
            buf.append(" USING ");
            buf.append(charset);
        }
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hex == null) ? 0 : hex.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SQLHexExpr other = (SQLHexExpr) obj;
        if (hex == null) {
            if (other.hex != null) {
                return false;
            }
        } else if (!hex.equals(other.hex)) {
            return false;
        }
        return true;
    }

    public byte[] toBytes() {
        return HexBin.decode(this.hex);
    }
}
