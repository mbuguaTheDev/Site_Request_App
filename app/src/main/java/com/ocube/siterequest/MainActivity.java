package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Main Screen Buttons
        Button siteRequest = findViewById(R.id.siteRequest);
        Button dailyReport = findViewById(R.id.dailyReport);
        //Button salaryInput = findViewById(R.id.salaryInput);


        //Main screen buttons Onclick listeners
        siteRequest.setOnClickListener(this);
        dailyReport.setOnClickListener(this);
        //salaryInput.setOnClickListener(this);

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
