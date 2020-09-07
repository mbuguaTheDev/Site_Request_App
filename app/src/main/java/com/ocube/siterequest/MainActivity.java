package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.ocube.siterequest.LoginActivity.SHARED_PREFS;
import static com.ocube.siterequest.LoginActivity.SITE_NAME;
import static com.ocube.siterequest.LoginActivity.USER_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    String username, welcome;
    TextView welcomeText, currentSite;


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
        Button salaryInput = findViewById(R.id.salaryInput);
        welcomeText  = findViewById(R.id.welcomeText);
        currentSite = findViewById(R.id.currentSite);

        //Main screen buttons Onclick listeners
        siteRequest.setOnClickListener(this);
        dailyReport.setOnClickListener(this);
        salaryInput.setOnClickListener(this);

        //show welcome text with username
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(USER_NAME, "");
        welcome = "Welcome " + username + ". What would you like to do today?";
        welcomeText.setText(welcome);
        getAllSites();

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

            case R.id.salaryInput:
                Intent salaryInputScreen = new Intent(this, SalaryInputActivity.class);
                startActivity(salaryInputScreen);
                break;
        }

    }

    //fetch all sites and add to spinner
    public void getAllSites (){
        Spinner chooseSite;
        SqlConnection sqlConnection = new SqlConnection();
        Connection conn = sqlConnection.Connect();
        Statement statement;
        chooseSite = findViewById(R.id.chooseSite);
        ArrayList<String> allSites = new ArrayList<>();

        try {
            statement = conn.createStatement();
            String allSiteQuery = "SELECT * from agent";
            ResultSet resultSet = statement.executeQuery(allSiteQuery);
            while (resultSet.next()){
                String site = resultSet.getString("site");
                allSites.add(site);
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allSites);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            chooseSite.setAdapter(adapter);
            chooseSite.setOnItemSelectedListener(this);

        }catch(SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String chosenSite = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, "You selected " + chosenSite, Toast.LENGTH_SHORT).show();
        currentSite.setText(chosenSite);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SITE_NAME, chosenSite);
        editor.apply();



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
