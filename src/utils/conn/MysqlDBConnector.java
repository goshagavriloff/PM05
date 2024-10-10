package utils.conn;


import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;
import org.json.JSONObject;
import utils.listener.IListener;

import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MysqlDBConnector extends Connector{

    private String _host;
    private String _port;
    private String _db;
    private String _user;
    private String _pwd;

    private String _sql;


    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String url="jdbc:mysql://%s:%s/%s?serverTimezone=UTC".formatted(_host,_port,_db);


                try{
                    Connection conn= DriverManager.getConnection(url,_user,_pwd);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(_sql);
                    List<JSONObject> result=getFormattedResult(rs);

                    _listener.onResult(result);

                    conn.close();

                }
                catch (SQLException e) {
                    _listener.onError(e);
                }

            }
        }).start();

    }

    @Override
    public void setup(HashMap settings) {
        _host= (String) settings.get("host");
        _port=(String) settings.get("port");
        _db=(String) settings.get("db");
        _user=(String) settings.get("user");
        _pwd=(String) settings.get("pwd");

    }

    @Override
    public void set_listener(IListener _listener) {
        this._listener = _listener;
    }

    public void setup(){
        Dotenv dotenv = null;
        dotenv = Dotenv.configure().load();

        _host= dotenv.get("DB_HOST");
        _port=dotenv.get("DB_PORT");
        _db=dotenv.get("DB_NAME");
        _user=dotenv.get("MYSQL_USER");
        _pwd=dotenv.get("MYSQL_PASSWORD");

    }

    public void set_sql(String _sql) {
        this._sql = _sql;
    }

    private List<JSONObject> getFormattedResult(ResultSet rs) {
        List<JSONObject> resList = new ArrayList<JSONObject>();
        try {

            // get column names
            ResultSetMetaData rsMeta = rs.getMetaData();
            int columnCnt = rsMeta.getColumnCount();
            List<String> columnNames = new ArrayList<String>();
            for(int i=1;i<=columnCnt;i++) {
                String columnName=rsMeta.getColumnName(i).toUpperCase();

                if (columnNames.contains(columnName)){
                    columnName=rsMeta.getColumnLabel(i).toUpperCase();
                }

                columnNames.add(columnName);
            }

            while(rs.next()) { // convert each object to an human readable JSON object
                JSONObject obj = new JSONObject();
                for(int i=1;i<=columnCnt;i++) {
                    String key = columnNames.get(i - 1);
                    String value = rs.getString(i);
                    obj.put(key, value);
                }
                resList.add(obj);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resList;
    }

}
