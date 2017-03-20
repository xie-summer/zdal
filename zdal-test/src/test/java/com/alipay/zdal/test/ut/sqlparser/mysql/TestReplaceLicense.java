package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.alipay.zdal.parser.sql.util.IOUtils;

import junit.framework.TestCase;

public class TestReplaceLicense extends TestCase {
	
	private String license;
    private String lineSeparator;

    protected void setUp() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("License.txt");
        Reader reader = new InputStreamReader(is);
        license = IOUtils.read(reader);
        reader.close();
        System.out.println(license);

        lineSeparator = "\n"; // (String) java.security.AccessController.doPrivileged(new
                              // sun.security.action.GetPropertyAction("line.separator"));
    }

    public void test_0() throws Exception {
        File file = new File("/usr/alibaba/workspace/druid");
        listFile(file);

    }

    public void listFile(File file) throws Exception {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                listFile(child);
            }
        } else {
            if (file.getName().endsWith(".java")) {
                listJavaFile(file);
            }
        }
    }

    public void listJavaFile(File file) throws Exception {
        FileInputStream in = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(in, "utf-8");
        String content = IOUtils.read(reader);
        reader.close();

        if (!content.startsWith(license)) {
            String newContent;
            int index = content.indexOf("package ");
            if (index != -1) {
                newContent = license + lineSeparator + content.substring(index);
            } else {
                newContent = license + lineSeparator + content;
            }
            FileOutputStream out = new FileOutputStream(file);
            Writer writer = new OutputStreamWriter(out, "utf-8");
            writer.write(newContent);
            writer.close();
        } else {

        }
    }

}
