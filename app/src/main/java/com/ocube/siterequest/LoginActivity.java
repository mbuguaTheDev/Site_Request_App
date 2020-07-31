package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;


public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private SqlConnection sqlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sqlConnection = new SqlConnection();

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });

        checkConn();

    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void checkConn() {
        try {
            Connection conn = sqlConnection.Connect();
            if (conn != null){
                Toast.makeText(this, "Connected Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e)
        {
            e.printStackTrace();

        }

    }

}
