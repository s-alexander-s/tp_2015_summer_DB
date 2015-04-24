package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
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
            Map<String, Object> params = new HashMap<>();
            for (String k : req.getParameterMap().keySet()){
                String[] e = req.getParameterMap().get(k);
                if (e.length == 1) {
                    params.put(k, e[0]);
                } else {
                    params.put(k, e);
                }
            }
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
            Map<String, Object> params = new Gson().fromJson(jb.toString().trim(), new TypeToken<HashMap<String, Object>>() {

            }.getType());
            response(operation, params, resp);
        }
    }

    private void response(String operation, Object params, HttpServletResponse resp) throws IOException {
        logger.info("REQUEST: " + operation + " " + params);
        String[] operands = operation.split("/");
        String essence = operands[0];
        String method = (operands.length > 1) ? operands[1] : "";
        if (method.isEmpty()) {
            method = essence;
            essence = "Basic";
        }
        JSONObject full = new JSONObject();
        try {
            Object body = Class.forName("main." + WordUtils.capitalizeFully(essence)).
                    getMethod(
                            method.toLowerCase(),
                            Statement.class,
                            Map.class
                    ).invoke(
                    null,
                    new Object[]{statement, params}
            );
            full.put("code", 0);
            full.put("response", body);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof SQLException) {
                try {
                    full.put("code", 5);
                    full.put("response", "Error!");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        logger.info("RESPONSE:\n" + full);
        resp.getWriter().print(full);
    }
}
