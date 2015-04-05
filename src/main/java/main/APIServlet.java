package main;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by alexander on 05.04.15.
 */

@WebServlet(urlPatterns = {"/db/api/*"})
public class APIServlet extends HttpServlet {
    private static final String apiUrl = "/db/api/";
    private static final String apiRegex = apiUrl + ".*/.*/";
    public APIServlet() {

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response(req, resp);
    }

    private void response(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURI();
        if (url.matches(apiRegex)) {
            String method = url.replace(apiUrl, "").replaceAll("/$", "");
            StringBuffer jb = new StringBuffer();
            String line;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);
            } catch (Exception e) { /*report an error*/ }

            try {
                System.out.println(jb.toString().trim());
                System.out.println((int)jb.toString().trim().charAt(0));
                JSONObject jsonObject = new JSONObject(jb.toString().trim());
                System.out.println(method + "\n" + jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
