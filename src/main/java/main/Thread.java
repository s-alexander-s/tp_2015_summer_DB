package main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
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
        body.remove("points");
        body.remove("likes");
        body.remove("dislikes");
        return body;
    }

    public static Object list(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        if (params.containsKey("user")) {
            params.put("user_id", User.by_email(statement, params.get("user")).getLong("id"));
        } else {
            params.put("forum_id", Forum.by_short_name(statement, params.get("forum")).getLong("id"));
        }
        JSONArray body = Queryer.queryJSONArray(statement, QueryGenerator.getQuery("thread/list", params));
        return body;
    }

    public static JSONObject open(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        params.put("isClosed", false);
        statement.execute(QueryGenerator.getQuery("thread/isClosed", params));
        body.put("thread", params.get("thread"));
        return body;
    }

    public static JSONObject close(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        params.put("isClosed", true);
        statement.execute(QueryGenerator.getQuery("thread/isClosed", params));
        body.put("thread", params.get("thread"));
        return body;
    }

    public static JSONObject remove(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        params.put("isDeleted", true);
        statement.execute(QueryGenerator.getQuery("thread/isDeleted", params));
        body.put("thread", params.get("thread"));
        return body;
    }

    public static JSONObject restore(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        params.put("isDeleted", false);
        statement.execute(QueryGenerator.getQuery("thread/isDeleted", params));
        body.put("thread", params.get("thread"));
        return body;
    }

    public static JSONObject subscribe(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        body.put("thread", params.get("thread"));
        body.put("user", params.get("user"));
        statement.execute(QueryGenerator.getQuery("thread/subscribe", params));
        return body;
    }

    public static JSONObject unsubscribe(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        body.put("thread", params.get("thread"));
        body.put("user", params.get("user"));
        statement.execute(QueryGenerator.getQuery("thread/unsubscribe", params));
        return body;
    }

    public static JSONObject update(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        statement.execute(QueryGenerator.getQuery("thread/update", params));
        return details(statement, params);
    }

    public static JSONObject vote(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        statement.execute(QueryGenerator.getQuery("thread/vote", params));
        return details(statement, params);
    }

    public static JSONObject details(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = Basic.by_id(statement, "thread", params.get("thread"));
        if (params.get("related") != null) {
            List<Object> related = Arrays.asList((Object[]) params.get("related"));
            if (related.contains("user")) {
                body.put("user", User.details(statement, ((Number)body.remove("user_id")).longValue()));
            } else {
                body.put("user", Basic.by_id(statement, "user", ((Number)body.remove("user_id")).longValue()).get("email"));
            }
            if (related.contains("forum")) {
                body.put("forum", Forum.details(statement, ((Number)body.remove("forum_id")).longValue()));
            } else {
                body.put("forum", body.remove("forum_id"));
            }
        }
        else {
            body.put("user", Basic.by_id(statement, "user", ((Number)body.remove("user_id")).longValue()).get("email"));
            body.put("forum", body.remove("forum_id"));
        }
        return body;
    }
}
