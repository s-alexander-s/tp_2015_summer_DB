package main;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class QueryGenerator {
    private static final Configuration CFG = new Configuration(Configuration.VERSION_2_3_20);
    private static final String DIR = "SQL/QUERY";
    private static final String EXT = ".sql";

    public static String getQuery(String filename, Object data) {
        Writer stream = new StringWriter();
        try {
            Template template = CFG.getTemplate(DIR + File.separator + filename + EXT);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }
}
