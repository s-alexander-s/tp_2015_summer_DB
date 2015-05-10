package main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexander on 05.04.15.
 */
public class User {
    public static JSONObject by_email(Statement statement, Object email) throws SQLException, JSONException {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        return Queryer.queryJSONObject(statement, QueryGenerator.getQuery("user/by_email", params));
    }

    public static JSONObject create(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        statement.execute(QueryGenerator.getQuery("user/create", params));
        return by_email(statement, params.get("email"));
    }

    public static JSONObject follow(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject follower = by_email(statement, params.get("follower"));
        JSONObject followee = by_email(statement, params.get("followee"));
        params.put("follower_id", follower.getInt("id"));
        params.put("followee_id", followee.getInt("id"));
        statement.execute(QueryGenerator.getQuery("user/follow", params));
        return details(statement, params.get("follower"));
    }

    public static JSONObject details(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = by_email(statement, params.get("user"));
        body.put("followers", followers(statement, body.getLong("id")));
        body.put("following", following(statement, body.getLong("id")));
        body.put("subsriptions", subscriptions(statement, body.getLong("id")));
        return body;
    }

    public static JSONObject details(Statement statement, long id) throws SQLException, JSONException {
        JSONObject body = Basic.by_id(statement, "user", id);
        body.put("followers", followers(statement, id));
        body.put("following", following(statement, id));
        body.put("subsriptions", subscriptions(statement, id));
        return body;
    }

    public static JSONObject details(Statement statement, Object email) throws SQLException, JSONException {
        Map<String, Object> params = new HashMap<>();
        params.put("user", email);
        return details(statement, params);
    }

    public static JSONObject unfollow(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        statement.execute(QueryGenerator.getQuery("user/unfollow", params));
        return details(statement, params.get("follower"));
    }

    public static JSONObject updateprofile(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        statement.execute(QueryGenerator.getQuery("user/updateprofile", params));
        return details(statement, params.get("user"));
    }

    public static JSONObject listfollowers(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = by_email(statement, params.get("user"));
        params.put("id", body.getLong("id"));
        body.put("followers", Queryer.queryArray(statement, QueryGenerator.getQuery("user/followers", params)));
        body.put("following", following(statement, body.getLong("id")));
        return body;
    }

    public static JSONObject listfollowing(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = by_email(statement, params.get("user"));
        params.put("id", body.getLong("id"));
        body.put("followers", followers(statement, body.getLong("id")));
        body.put("following", Queryer.queryArray(statement, QueryGenerator.getQuery("user/following", params)));
        return body;
    }

    public static Object listposts(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        params.put("id", by_email(statement, params.get("user")).getLong("id"));
        JSONArray body = Queryer.queryJSONArray(statement, QueryGenerator.getQuery("user/posts", params));
        return body;
    }

    public static List<Object> followers(Statement statement, long user_id) throws SQLException, JSONException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", user_id);
        return Queryer.queryArray(statement, QueryGenerator.getQuery("user/followers", params));
    }

    public static List<Object> following(Statement statement, long user_id) throws SQLException, JSONException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", user_id);
        return Queryer.queryArray(statement, QueryGenerator.getQuery("user/following", params));
    }

    public static List<Object> subscriptions(Statement statement, long user_id) throws SQLException, JSONException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", user_id);
        return Queryer.queryArray(statement, QueryGenerator.getQuery("user/subscriptions", params));
    }
}
