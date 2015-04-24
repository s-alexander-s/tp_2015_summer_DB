package main;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexander on 07.04.15.
 */
public class Basic {
    public static String clear(Statement statement, Map<String, Object> params) throws SQLException {
        statement.execute(QueryGenerator.getQuery("basic/clear", params));
        return "OK";
    }

    public static JSONObject by_field(Statement statement, String essence, String field, Object value) throws SQLException, JSONException {
        Map<String, Object> params = new HashMap<>();
        params.put("essence", essence);
        params.put("field", field);
        params.put("value", value);
        return Queryer.queryJSONObject(statement, QueryGenerator.getQuery("basic/by_field", params));
    }

    public static JSONObject by_id(Statement statement, String essence, Object id) throws SQLException, JSONException {
        return by_field(statement, essence, "id", id);
    }

    public static JSONObject status(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        Map<String, Object> p = new HashMap<>();
        p.put("table", "user");
        body.put("user", Queryer.queryLong(statement, QueryGenerator.getQuery("basic/count", p)));
        p.clear();
        p.put("table", "forum");
        body.put("forum", Queryer.queryLong(statement, QueryGenerator.getQuery("basic/count", p)));
        p.clear();
        p.put("table", "thread");
        p.put("where_clause", "where isDeleted = false");
        body.put("thread", Queryer.queryLong(statement, QueryGenerator.getQuery("basic/count", p)));
        p.put("table", "post");
        body.put("post", Queryer.queryLong(statement, QueryGenerator.getQuery("basic/count", p)));
        return body;
    }

    public static JSONObject selectall(Statement statement, Map<String, Object> params) throws SQLException, JSONException {
        JSONObject body = new JSONObject();
        body.put("entries", Queryer.queryJSONArray(statement, QueryGenerator.getQuery("basic/select_all", params)));
        return body;
    }


}
