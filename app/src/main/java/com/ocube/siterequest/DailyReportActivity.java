package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.ocube.siterequest.LoginActivity.AGENT_ID;
import static com.ocube.siterequest.LoginActivity.SHARED_PREFS;
import static com.ocube.siterequest.LoginActivity.SITE_ID;
import static com.ocube.siterequest.LoginActivity.SITE_NAME;
import static com.ocube.siterequest.LoginActivity.USER_NAME;

public class DailyReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText siteVisitors, inspections, accidents, workProgress, itemsAccomplished, meetings, unresolvedIssues, otherNotes;
    Spinner weather, condition;
    String  agentId, siteId, agentName, siteName;
    Button submitReport;

    private SqlConnection sqlConnection; //SQL Connection Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        weather = findViewById(R.id.weather);
        condition = findViewById(R.id.conditions);

        ArrayAdapter<CharSequence> weatherAdapter = ArrayAdapter.createFromResource(this, R.array.weather, android.R.layout.simple_spinner_item);
        weatherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weather.setAdapter(weatherAdapter);
        weather.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> condAdapter = ArrayAdapter.createFromResource(this, R.array.conditions, android.R.layout.simple_spinner_item);
        condAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        condition.setAdapter(condAdapter);



        submitReport = findViewById(R.id.submitReport);
        submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport();
            }
        });
        
    }


    public void sendReport(){

        siteVisitors = findViewById(R.id.siteVisitors);
        inspections = findViewById(R.id.inspections);
        accidents = findViewById(R.id.accidents);
        workProgress = findViewById(R.id.workProgress);
        itemsAccomplished = findViewById(R.id.itemsAccomplished);
        meetings = findViewById(R.id.meetings);
        unresolvedIssues = findViewById(R.id.unresolvedIssues);
        otherNotes = findViewById(R.id.otherNotes);


        //get all the agent details
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        agentId = sharedPreferences.getString(AGENT_ID, "agentId");
        siteId = sharedPreferences.getString(SITE_ID, "site");
        agentName = sharedPreferences.getString(USER_NAME, "agent");
        siteName = sharedPreferences.getString(SITE_NAME, "site");


        Statement statement;
        try{
            Connection conn = sqlConnection.Connect(); //Connection Object
            statement = conn.createStatement();
            String reportQuery = "INSERT INTO dailyreprt (sid, aid, day, temperature, conditionam, visitors, inspection, unevent, progwork, iacomplished, othernotes, sagent) VALUES ('"+siteId+"', '"+agentId+"', 'today' ,'"+weather+"', '"+condition+"', '"+siteVisitors+"', '"+inspections+"', '"+accidents+"', '"+workProgress+"', '"+itemsAccomplished+"', '"+otherNotes+"', '"+agentName+"')";
            statement.executeQuery(reportQuery);

        }catch (SQLException e) {
            e.printStackTrace();
        }


        //clear inputs
        siteVisitors.setText("");
        inspections.setText("");
        accidents.setText("");
        workProgress.setText("");
        itemsAccomplished.setText("");
        meetings.setText("");
        unresolvedIssues.setText("");
        otherNotes.setText("");

        Toast.makeText(this, "Report Submitted", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> weatherAd, View view, int i, long l) {
        String selectedWeather = weatherAd.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
