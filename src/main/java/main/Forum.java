package main;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexander on 14.04.15.
 */
public class Forum {
    public static JSONObject by_short_name(Statement statement, Object short_name) throws SQLException, JSONException {
        Map<String, Object> params = new HashMap<>();
        params.put("short_name", short_name);
        return Queryer.queryJSONObject(statement, QueryGenerator.getQuery("forum/by_short_name", params));
    }

    public static JSONObject create(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        params.put("user_id", User.by_email(statement, params.get("user")).getLong("id"));
        statement.execute(QueryGenerator.getQuery("forum/create", params));
        JSONObject body = by_short_name(statement, params.get("short_name"));
        body.remove("user_id");
        body.put("user", params.get("user"));
        return body;
    }

    public static JSONObject details(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = by_short_name(statement, params.get("forum"));
        if (params.getOrDefault("related", "").equals("user")) {
            body.put("user", User.details(statement, body.getLong("user_id")));
        } else {
            body.put("user", Basic.by_id(statement, "user", body.get("user_id")).getString("email"));
        }
        return body;
    }
}
