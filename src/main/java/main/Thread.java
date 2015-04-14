package main;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by alexander on 14.04.15.
 */
public class Thread {
    public static JSONObject create(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        params.put("user_id", User.by_email(statement, params.get("user")).getLong("id"));
        params.put("forum_id", Forum.by_short_name(statement, params.get("forum")).getLong("id"));
        statement.execute(QueryGenerator.getQuery("thread/create", params));
        return Basic.by_field(statement, "thread", "slug", params.get("slug"));
    }
}
