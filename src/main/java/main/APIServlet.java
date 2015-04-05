package main;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by alexander on 05.04.15.
 */

@WebServlet(urlPatterns = {"/db/api/*"})
public class APIServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    private static final String apiUrl = "/db/api/";
    private static final String apiRegex = apiUrl + ".*/";
    private Statement statement;

    public APIServlet(Statement statement) {
        this.statement = statement;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        if (url.matches(apiRegex)) {
            String method = url.replace(apiUrl, "").replaceAll("/$", "");
            Map<String, String[]> params = req.getParameterMap();
            response(method, params, resp);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        if (url.matches(apiRegex)) {
            String operation = url.replace(apiUrl, "").replaceAll("/$", "");
            StringBuffer jb = new StringBuffer();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
            try {
                JSONObject params = new JSONObject(jb.toString().trim());
                response(operation, params, resp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void response(String operation, Object params, HttpServletResponse resp) throws IOException {
        logger.info(operation + " " + params);
        String[] operands = operation.split("/");
        String essence = operands[0];
        String method = (operands.length > 1) ? operands[1] : "";
        JSONObject full = new JSONObject();
        try {
            Class.forName("main." + WordUtils.capitalizeFully(essence)).getMethod(method.toLowerCase()).invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            statement.execute(QueryGenerator.getQuery(operation, params));
            if (method.equals("create")) {
                statement.execute(QueryGenerator.getQuery(essence + "/by_email", params));
            }
            ResultSet resultSet = statement.getResultSet();
            if (resultSet != null) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                JSONArray body = new JSONArray();
                while (resultSet.next()) {
                    JSONObject row = new JSONObject();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnLabel(i), resultSet.getObject(i));
                    }
                    body.put(row);
                }
                if (body.length() == 1) {
                    full.put("response", body.get(0));
                } else {
                    full.put("response", body);
                }
            } else {
                full.put("response", "OK");
            }
            full.put("code", 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().print(full);
    }
}
