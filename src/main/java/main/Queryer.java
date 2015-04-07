package main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by alexander on 07.04.15.
 */
public class Queryer {
    public static JSONObject queryOneJSON(Statement statement, String query) throws SQLException, JSONException {
        return queryJSONArray(statement, query).getJSONObject(0);
    }

    public static JSONArray queryJSONArray(Statement statement, String query) throws SQLException, JSONException {
        statement.execute(query);
        ResultSet resultSet = statement.getResultSet();
        if (resultSet != null) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            JSONArray jsonArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject row = new JSONObject();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnLabel(i), resultSet.getObject(i));
                }
                jsonArray.put(row);
            }
            return jsonArray;
        } else {
            return null;
        }
    }
}
