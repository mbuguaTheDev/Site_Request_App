package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private SqlConnection sqlConnection;
    EditText pwdInput, userInput;

    //getting user details
    public static final String SHARED_PREFS = "userdetails";
    public  static final String USER_NAME = "user";
    public  static final String AGENT_ID = "agentId";
    public  static final String SITE_ID = "siteId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        pwdInput = findViewById(R.id.pwdInput);
        userInput = findViewById(R.id.userInput);

    }

    private class DoLoginForUser extends AsyncTask<String, Void, String> {
        String username, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            username = userInput.getText().toString();
            password = pwdInput.getText().toString();
            loginButton.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                sqlConnection = new SqlConnection();
                Connection connect = sqlConnection.Connect();

                String query = "select * from agent where name ='" + username +"' ";
                PreparedStatement ps = connect.prepareStatement(query);

                Log.e("query",query);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String passcode = rs.getString("password");

                    //add agent details to shared prefs
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(USER_NAME,rs.getString("name"));
                    editor.putString(AGENT_ID, rs.getString("id"));
                    editor.putString(SITE_ID, rs.getString("sid"));
                    editor.apply();

                    connect.close();
                    rs.close();
                    ps.close();
                    if (passcode != null && !passcode.trim().equals("") && passcode.equals(password))
                        return "success";
                    else
                        return "Invalid Credentials";
                } else
                    return "User does not exists.";
            } catch (SQLException e) {
                return "Error:" + e.getMessage().toString();
            } catch (Exception e) {
                return "Error:" + e.getMessage().toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            loginButton.setVisibility(View.VISIBLE);
            if (result.equals("success")) {

                //clear inputs
                userInput.setText("");
                pwdInput.setText("");

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(LoginActivity.this, "Login Failed. Please Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void DoLogin(View v) {
        DoLoginForUser login = new DoLoginForUser();
        login.execute("");
    }

}
