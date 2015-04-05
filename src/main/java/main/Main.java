package main;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Main {
    private static final String dbClassName = "com.mysql.jdbc.Driver";
    private static final String CONNECTION = "jdbc:mysql://127.0.0.1/forum_db";

    public static void main(String[] args) throws Exception {
        System.out.println(dbClassName);
        Class.forName(dbClassName);
        Properties p = new Properties();
        p.put("user","sosenko");
        p.put("password","Alkox");
        Connection c = DriverManager.getConnection(CONNECTION,p);
        Statement s = c.createStatement();
        Map<String, Object> vars = new HashMap<>();
        vars.put("email", "Alex2@alex.com");
        vars.put("username", "Alex");
        vars.put("name", "Alex");
        vars.put("about", "It is me");
        vars.put("isAnonymous", null);
        String q = QueryGenerator.getQuery("user/create", vars);
        System.out.println(q);
        //s.executeUpdate(q);
        vars.clear();
        vars.put("table", "user");
        ResultSet resultSet = s.executeQuery(QueryGenerator.getQuery("select_all", vars));
        Map<String, Object> row = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            row.clear();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            System.out.println(new Gson().toJson(row));
        }
        System.out.println("It works !");
        c.close();

        int port = 8080;
        if (args.length == 1) {
            port = Integer.valueOf(args[1]);
        }
        Server server = new Server(port);
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.addServlet(new ServletHolder(new APIServlet()), "/");
        server.setHandler(contextHandler);
        server.start();
        server.join();
    }
}
