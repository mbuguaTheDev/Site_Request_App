package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import static com.ocube.siterequest.LoginActivity.SHARED_PREFS;
import static com.ocube.siterequest.LoginActivity.USER_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String username, welcome;
    TextView welcomeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show();

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //Main Screen Buttons
        Button siteRequest = findViewById(R.id.siteRequest);
        Button dailyReport = findViewById(R.id.dailyReport);
        //Button salaryInput = findViewById(R.id.salaryInput);
        welcomeText  = findViewById(R.id.welcomeText);


        //Main screen buttons Onclick listeners
        siteRequest.setOnClickListener(this);
        dailyReport.setOnClickListener(this);
        //salaryInput.setOnClickListener(this);

        //show welcome text with username
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(USER_NAME, "");
        welcome = "Welcome " + username + ". What would you like to do today?";

        welcomeText.setText(welcome);

    }



    //respond to the button clicks on the home screen
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.siteRequest:
               Intent siteRequestScreen = new Intent(this, SiteRequestActivity.class);
               startActivity(siteRequestScreen);
               break;

            case R.id.dailyReport:
                Intent dailyRptScreen = new Intent(this, DailyReportActivity.class);
                startActivity(dailyRptScreen);
                break;

//            case R.id.salaryInput:
//                Intent salaryInputScreen = new Intent(this, SalaryInputActivity.class);
//                startActivity(salaryInputScreen);
//                break;
        }

    }
}
