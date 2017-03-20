/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.config.beans;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;

import com.alipay.zdal.common.util.TableSuffixTypeEnum;
import com.alipay.zdal.rule.config.beans.TableRule.ParseException;

/**
 * 管理解析下标
 * @author liang.chenl
 *
 */
public class SuffixManager {

    private List<Suffix> listSuffix = new ArrayList<Suffix>();
    private String       tbSuffix;

    private Binding      binding    = new Binding();
    private GroovyShell  shell      = new GroovyShell(binding);

    public SuffixManager() {
        Suffix suf = new Suffix();
        listSuffix.add(suf);
    }

    public List<Suffix> getListSuffix() {
        return listSuffix;
    }

    public void init(String[] dbIndexes) {
        Suffix suf = listSuffix.get(0);
        if (suf.getTbSuffixTo() == -1) {
            //tbSuffixTo默认根据dbIndex的个数计算
            suf.setTbSuffixTo(dbIndexes);
        }
        suf.setTbType(TableSuffixTypeEnum.throughAllDB.getValue());
        //suf.setTbNumForEachDb(tbNumForEachDb);
    }

    /**
     * 解析一个range [_00-_99]
     * @param part
     * @param suf
     * @throws ParseException 
     */
    protected void parseOneRange(String part, Suffix suf, int dbIndexSize) throws ParseException {
        if (!part.startsWith("[") || !part.endsWith("]")) {
            throw new ParseException();
        }
        //去掉[]
        part = part.substring(1, part.length() - 1);

        String[] temp = part.split("-");
        if (temp.length != 2) {
            throw new ParseException();
        }
        temp[0] = temp[0].trim();
        temp[1] = temp[1].trim();
        int firstNumFrom = firstNum(temp[0]);
        int firstNot0From = firstNot0(temp[0], firstNumFrom);
        int firstNumTo = firstNum(temp[1]);
        int firstNot0To = firstNot0(temp[1], firstNumTo);
        if (firstNumFrom == -1 || firstNumTo == -1) {
            throw new ParseException();
        }
        if (firstNumFrom != firstNumTo) {
            throw new ParseException("padding width different");
        }
        if (temp[0].length() != temp[1].length() && !(
        //_0-_16
            (firstNot0From == -1 && firstNumFrom == temp[0].length() - 1 && firstNot0To == firstNumTo)
            //_1-_16
            || (firstNot0From == firstNumFrom && firstNot0To == firstNumTo))) {
            throw new ParseException("tbSuffix width different");
        }
        if (firstNumFrom != 0) {
            String fromPadding = temp[0].substring(0, firstNumFrom);
            String toPadding = temp[1].substring(0, firstNumTo);
            if (!fromPadding.equals(toPadding)) {
                throw new ParseException("padding different");
            }
            suf.setTbSuffixPadding(fromPadding);
        } else {
            suf.setTbSuffixPadding("");
        }
        int tbSuffixFrom = firstNot0From == -1 ? 0 //_0-_16
            : Integer.parseInt(temp[0].substring(firstNot0From));
        suf.setTbSuffixFrom(tbSuffixFrom);
        int tbSuffixTo = Integer.parseInt(temp[1].substring(firstNot0To));
        suf.setTbSuffixTo(tbSuffixTo);
        if (tbSuffixTo <= tbSuffixFrom) {
            throw new ParseException();
        }
        int tbSuffixWidth = temp[0].length() != temp[1].length() ? 0 : temp[0].length()
                                                                       - firstNumFrom;
        suf.setTbSuffixWidth(tbSuffixWidth);
        int tbNumForEachDb = -1;
        if (TableSuffixTypeEnum.resetForEachDB.getValue().equals(suf.getTbType())) {
            tbNumForEachDb = -1;
        } else {
            tbNumForEachDb = (tbSuffixTo - tbSuffixFrom + 1) / dbIndexSize;
        }
        suf.setTbNumForEachDb(tbNumForEachDb);
    }

    /**
     * 解析支持2列的表名 (twoColumnForEachDB: [_00-_99],[_00-_11])
     * @param dbIndexes
     * @throws ParseException 
     */
    protected void parseTwoColumn(String part2, int dbIndexSize) throws ParseException {
        Suffix suf = listSuffix.get(0);
        String[] parts = part2.split(",");
        if (parts.length != 2) {
            throw new ParseException("twoColumnForEachDB must have two range");
        }
        int tbNumForEachDb = -1;
        parseOneRange(parts[0], suf, dbIndexSize);
        suf.setTbNumForEachDb(tbNumForEachDb);
        Suffix suf2 = new Suffix();
        parseOneRange(parts[1], suf2, dbIndexSize);
        suf2.setTbNumForEachDb(tbNumForEachDb);
        listSuffix.add(suf2);
    }

    /**
     * 解析和dbindex一样下标的表名 
     * @param dbIndexes
     * @throws ParseException 
     */
    protected void parseDbIndex(String part2, int dbIndexSize) throws ParseException {
        Suffix suf = listSuffix.get(0);
        parseOneRange(part2, suf, dbIndexSize);
        suf.setTbNumForEachDb(-1);
    }

    protected void parseTbSuffix(String[] dbIndexes) throws ParseException {
        Suffix suf = listSuffix.get(0);
        String type;
        //切分分库模式和具体后缀数值
        String[] temp = tbSuffix.split(":");
        if (TableSuffixTypeEnum.groovyAdjustTableList.getValue().equals(temp[0].trim())) {
            if (temp.length != 3) {
                throw new ParseException();
            }
        } else {
            if (temp.length != 2) {
                throw new ParseException();
            }
        }
        //分表模式
        type = temp[0].trim();
        suf.setTbType(type);
        //分表的具体模式字段
        String part2 = temp[1].trim();
        if (TableSuffixTypeEnum.twoColumnForEachDB.getValue().equals(type)) {
            //两个参数分表
            parseTwoColumn(part2, dbIndexes.length);
        } else if (TableSuffixTypeEnum.dbIndexForEachDB.getValue().equals(type)) {
            //按照dbIndex分表
            parseDbIndex(part2, dbIndexes.length);
        } else if (TableSuffixTypeEnum.groovyTableList.getValue().equals(type)) {
            parseGroovyDbindex(part2);
            //将表的个数设置为 -1，则默认每个库都初始化全部的表
            suf.setTbNumForEachDb(-1);
        } else if (TableSuffixTypeEnum.groovyThroughAllDBTableList.getValue().equals(type)) {
            parseGroovyDbindex(part2);
        } else if (TableSuffixTypeEnum.groovyAdjustTableList.getValue().equals(type)) {
            //最适配的配置方式        
            //解析groovy脚本
            String groovyExpression = temp[2].trim();
            parseGroovyDbindex(groovyExpression);
        } else {
            //走以前的逻辑，每个库内的表都是一致的
            parseOneRange(part2, suf, dbIndexes.length);
        }
    }

    private void parseGroovyDbindex(String part2) throws ParseException {
        try {
            //            listSuffix.get(0).setTbType(TableSuffixTypeEnum.groovyTableList.getValue());
            shell.parse(part2);
        } catch (CompilationFailedException e) {
            throw new ParseException("groovy script with syntax error!");
        }
    }

    public String getExpression() throws ParseException {
        String[] temp = tbSuffix.split(":");
        if (TableSuffixTypeEnum.groovyAdjustTableList.getValue().equals(temp[0].trim())) {
            if (temp.length != 3) {
                throw new ParseException();
            }
            return temp[1].trim() + ":" + temp[2].trim();
        } else {
            if (temp.length != 2) {
                throw new ParseException();
            }
            return temp[1].trim();
        }
    }

    private static int firstNum(String str) {
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                return i;
            }
        }
        return -1;
    }

    private static int firstNot0(String str, int start) {
        char c;
        for (int i = start; i < str.length(); i++) {
            c = str.charAt(i);
            if (c != '0') {
                return i;
            }
        }
        return -1;
    }

    public Suffix getSuffix(int index) {
        return listSuffix.get(index);
    }

    public String getTbSuffix() {
        return tbSuffix;
    }

    public void setTbSuffix(String tbSuffix) {
        this.tbSuffix = tbSuffix;
    }

}
