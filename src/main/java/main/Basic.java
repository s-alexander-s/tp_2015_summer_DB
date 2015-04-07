package main;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by alexander on 07.04.15.
 */
public class Basic {
    public static String clear(Statement statement, Object params) throws SQLException {
        statement.execute(QueryGenerator.getQuery("basic/clear", params));
        return "OK";
    }

    public static JSONObject selectall(Statement statement, Object params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        body.put("entries", Queryer.queryJSONArray(statement, QueryGenerator.getQuery("basic/select_all", params)));
        return body;
    }
}
