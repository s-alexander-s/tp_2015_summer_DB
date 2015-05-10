package main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public static Object listposts(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONArray body = Queryer.queryJSONArray(statement, QueryGenerator.getQuery("forum/listPosts", params));
        for (int i = 0; i < body.length(); i++) {
            JSONObject e = body.getJSONObject(i);
            if (params.get("related") != null) {
                List<Object> related = Arrays.asList((Object[]) params.get("related"));
                params.remove("related");
                if (related.contains("user")) {
                    e.put("user", User.details(statement, ((Number)e.remove("user_id")).longValue()));
                } else {
                    e.put("user", Basic.by_id(statement, "user", e.remove("user_id")).get("email"));
                }
                if (related.contains("forum")) {
                    params.put("forum", e.get("forum"));
                    e.put("forum", Forum.details(statement, params));
                }
                if (related.contains("thread")) {
                    params.put("thread", e.get("thread_id"));
                    e.put("thread", Thread.details(statement, params));
                } else {
                    e.put("thread", e.remove("thread_id"));
                }
            }
            else {
                e.put("user", Basic.by_id(statement, "user", e.remove("user_id")).get("email"));
                e.put("thread", e.remove("thread_id"));
            }
        }
        return body;
    }

    public static Object listthreads(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONArray body = Queryer.queryJSONArray(statement, QueryGenerator.getQuery("forum/listThreads", params));
        for (int i = 0; i < body.length(); i++) {
            JSONObject e = body.getJSONObject(i);
            if (params.get("related") != null) {
                List<Object> related = Arrays.asList((Object[]) params.get("related"));
                params.remove("related");
                if (related.contains("user")) {
                    e.put("user", User.details(statement, ((Number)e.remove("user_id")).longValue()));
                } else {
                    e.put("user", Basic.by_id(statement, "user", e.remove("user_id")).get("email"));
                }
                if (related.contains("forum")) {
                    params.put("forum", Basic.by_id(statement, "forum", e.remove("forum_id")).get("short_name"));
                    e.put("forum", Forum.details(statement, params));
                } else {
                    e.put("forum", Basic.by_id(statement, "forum", e.remove("forum_id")).get("short_name"));
                }
            }
            else {
                e.put("user", Basic.by_id(statement, "user", e.remove("user_id")).get("email"));
                e.put("forum", Basic.by_id(statement, "forum", e.remove("forum_id")).get("short_name"));
            }
        }
        return body;
    }

    public static Object listusers(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        List<Object> users = Queryer.queryArray(statement, QueryGenerator.getQuery("forum/listUsers", params));
        JSONArray body = new JSONArray();
        for (Object email : users) {
            body.put(User.details(statement, email));
        }
        return body;
    }

    public static JSONObject details(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = by_short_name(statement, params.get("forum"));
        if (params.get("related") != null) {
            List<Object> related = Arrays.asList((Object[]) params.get("related"));
            if (related.contains("user")) {
                body.put("user", User.details(statement, ((Number)body.remove("user_id")).longValue()));
            } else {
                body.put("user", Basic.by_id(statement, "user", ((Number)body.remove("user_id")).longValue()).get("email"));
            }
        }
        else {
            body.put("user", Basic.by_id(statement, "user", ((Number)body.remove("user_id")).longValue()).get("email"));
        }
        return body;
    }

    public static JSONObject details(Statement statement, long id) throws SQLException, JSONException {
        Map<String, Object> params = new HashMap<>();
        params.put("forum", Basic.by_id(statement, "forum", id).get("short_name"));
        return details(statement, params);
    }
}
