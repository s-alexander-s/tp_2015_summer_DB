package main;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by alexander on 05.04.15.
 */
public class User {
    public static JSONObject create(Statement statement, Object params) throws SQLException, JSONException {
        statement.execute(QueryGenerator.getQuery("user/create", params));
        return Queryer.queryOneJSON(statement, QueryGenerator.getQuery("user/by_email", params));
    }
}
