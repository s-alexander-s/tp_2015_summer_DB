package main;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class QueryGenerator {
    private static final Logger logger = LogManager.getLogger();
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
        String query;
        if (logger.getLevel().equals(Level.OFF)) {
            query = stream.toString().replaceAll("\n(\n)*", " ");
        } else {
            query = stream.toString().replaceAll("\n(\n)*", "\n");
        }
        logger.info(filename);
        logger.info("\n" + query);
        return query;
    }
}
