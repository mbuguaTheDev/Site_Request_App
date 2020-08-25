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

    //live
    String ip = "fsn201.truehost.cloud:1434";
    String db = "site";
    String user = "admin";
    String password = "waramba@#12maxwell";

//    //local
//    String ip = "192.168.0.13:49170";
//    String db = "site";
//    String user = "admin";
//    String password = "admin";



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
