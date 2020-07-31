package com.ocube.siterequest;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {

    // sql connection variables
    String ip = "197.248.205.134";
    String db = "site";
    String user = "max";
    String password = "12345";

    @SuppressLint("NewApi")
    public Connection Connect()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";" + "databaseName=" + db + ";user=" + user + ";password=" + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("Connection Error", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("Connection Error", e.getMessage());
        } catch (Exception e) {
            Log.e("Connection Failed", e.getMessage());
        }
        return conn;
    }

}
