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
    public static JSONObject by_slug(Statement statement, String slug) throws SQLException, JSONException {
        JSONObject body = Basic.by_field(statement, "thread", "slug", slug);
        ///body.put()
        return null;
    }

    public static JSONObject create(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        params.put("user_id", User.by_email(statement, params.get("user")).getLong("id"));
        params.put("forum_id", Forum.by_short_name(statement, params.get("forum")).getLong("id"));
        statement.execute(QueryGenerator.getQuery("thread/create", params));
        JSONObject body = Basic.by_field(statement, "thread", "slug", params.get("slug"));
        body.put("user", params.get("user"));
        body.put("forum", params.get("forum"));
        body.remove("user_id");
        body.remove("forum_id");
        return body;
    }

    public static JSONObject details(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = Basic.by_field(statement, "thread", "slug", params.get("slug"));

        body.remove("user_id");
        body.remove("forum_id");
        return null;
    }
}
