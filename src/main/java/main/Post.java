package main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by alexander on 10.05.15.
 */
public class Post {
    public static JSONObject create(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        statement.execute(QueryGenerator.getQuery("post/create", params), Statement.RETURN_GENERATED_KEYS);
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        keys.next();
        JSONObject body = Basic.by_id(statement, "post", keys.getLong(1));
        body.put("parent", body.has("m_path") ? ((String)body.remove("m_path")).replaceFirst(".*\\.", "") : null);
        body.remove("user_id");
        body.put("user", params.get("user"));
        body.put("thread", body.remove("thread_id"));
        return body;
    }

    public static Object list(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONArray body = Queryer.queryJSONArray(statement, QueryGenerator.getQuery("post/list", params));
        return body;
    }

    public static JSONObject remove(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        params.put("isDeleted", true);
        statement.execute(QueryGenerator.getQuery("post/isDeleted", params));
        body.put("post", params.get("post"));
        return body;
    }

    public static JSONObject restore(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        params.put("isDeleted", false);
        statement.execute(QueryGenerator.getQuery("post/isDeleted", params));
        body.put("post", params.get("post"));
        return body;
    }

    public static JSONObject update(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        statement.execute(QueryGenerator.getQuery("post/update", params));
        return details(statement, params);
    }

    public static JSONObject vote(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        statement.execute(QueryGenerator.getQuery("post/vote", params));
        return details(statement, params);
    }

    public static JSONObject details(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = Basic.by_id(statement, "post", params.get("post"));
        if (params.get("related") != null) {
            List<Object> related = Arrays.asList((Object[]) params.get("related"));
            if (related.contains("user")) {
                body.put("user", User.details(statement, ((Number)body.remove("user_id")).longValue()));
            } else {
                body.put("user", Basic.by_id(statement, "user", body.remove("user_id")).get("email"));
            }
            if (related.contains("forum")) {
                params.put("forum", body.get("forum"));
                body.put("forum", Forum.details(statement, params));
            } else {
                body.put("forum", body.remove("forum_id"));
            }
            if (related.contains("thread")) {
                params.put("thread", body.remove("thread_id"));
                body.put("thread", Thread.details(statement, params));
            } else {
                body.put("thread", body.remove("thread_id"));
            }
        }
        else {
            body.put("user", Basic.by_id(statement, "user", body.remove("user_id")).get("email"));
            body.put("thread", body.remove("thread_id"));
        }
        return body;
    }

}
