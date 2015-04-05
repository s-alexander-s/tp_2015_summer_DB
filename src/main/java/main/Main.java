package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;


public class Main {
    private static final Logger logger = LogManager.getLogger();
    private static final String dbClassName = "com.mysql.jdbc.Driver";
    private static final String CONNECTION = "jdbc:mysql://127.0.0.1/forum_db";

    public static void main(String[] args) throws Exception {
        Class.forName(dbClassName);
        Properties p = new Properties();
        p.put("user","sosenko");
        p.put("password","Alkox");
        p.put("allowMultiQueries", "true");
        Connection c = DriverManager.getConnection(CONNECTION, p);
        logger.info("Establish DB connection " + c);
        Statement s = c.createStatement();
        int port = 8080;
        if (args.length == 1) {
            port = Integer.valueOf(args[1]);
        }
        Server server = new Server(port);
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.addServlet(new ServletHolder(new APIServlet(s)), "/");
        server.setHandler(contextHandler);
        server.start();
        logger.info("Start server at port " + port);
        server.join();
        c.close();
    }
}
