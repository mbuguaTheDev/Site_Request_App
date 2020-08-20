package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.ocube.siterequest.LoginActivity.AGENT_ID;
import static com.ocube.siterequest.LoginActivity.SHARED_PREFS;
import static com.ocube.siterequest.LoginActivity.SITE_ID;
import static com.ocube.siterequest.LoginActivity.SITE_NAME;
import static com.ocube.siterequest.LoginActivity.USER_NAME;

public class SiteRequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SqlConnection sqlConnection; //SQL Connection Variable
    private String agentId, siteId, agentName, siteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_request);
        sqlConnection = new SqlConnection(); //instantiate connection

        //get all the agent details
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        agentId = sharedPreferences.getString(AGENT_ID, "agentId");
        siteId = sharedPreferences.getString(SITE_ID, "site");
        agentName = sharedPreferences.getString(USER_NAME, "agent");
        siteName = sharedPreferences.getString(SITE_NAME, "site");

        checkReqStatus();
        getItemsList();

        Button viewRequest = findViewById(R.id.viewRequest);
        viewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRequestView();
            }
        });


    }

    //Add items to request table in db
    public void AddItems(View view){

        EditText itemInput, qtyInput, unitsInput, daysInput;

        itemInput = findViewById(R.id.itemInput);
        qtyInput= findViewById(R.id.qtyInput);
        unitsInput = findViewById(R.id.unitsInput);
        daysInput = findViewById(R.id.daysInput);

        String pname = itemInput.getText().toString();
        String qty=qtyInput.getText().toString();
        String unit=unitsInput.getText().toString();
        String need=daysInput.getText().toString();


        Statement statement;
        try {
            Connection conn = sqlConnection.Connect(); //Connection Object
            statement = conn.createStatement();
            String query1 ="Select * from items where name='"+pname+"' ";
            ResultSet resultSet = statement.executeQuery(query1);
            if (resultSet.next()){

                //clear inputs
                itemInput.setText("");
                qtyInput.setText("");
                unitsInput.setText("");
                daysInput.setText("");


                //Show success
                Toast.makeText(this,"ADDED",Toast.LENGTH_SHORT).show();

                try{
                    statement = conn.createStatement();
                    //String deleteAllQuery = "DELETE FROM request"; //only for cleaning the table for testing
                    String reqIdQuery = "SELECT id FROM requetorder WHERE agentid = '"+agentId+"' AND status ='open' ";
                    ResultSet rs = statement.executeQuery(reqIdQuery);
                    if (rs.next()){
                        String requestId = rs.getString("id");
                        String query2= "INSERT  INTO  request (sid,aid,date,pid,pname,rqty,units,day,requestid) VALUES('"+ siteId + "','" + agentId + "','2020-01-01','1','" + pname + "','" + qty + "','"+unit+"','"+need+"', '"+requestId+"') ";
                        statement.executeQuery(query2);
                    }

                }catch (SQLException e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(this,"Item Does Not Exist",Toast.LENGTH_SHORT).show();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Open Requested Items Activity
    public void openRequestView() {
        Intent intent = new Intent(this, RequestedItemsActivity.class);
        startActivity(intent);
    }

    //get the list of all items from the database and add to spinner
    public void getItemsList () {

        try {
            Statement statement;
            Connection conn = sqlConnection.Connect(); //Connection Object
            statement = conn.createStatement();
            String itemsQuery ="Select * from items";
            ResultSet rs = statement.executeQuery(itemsQuery);
            ArrayList<String> allItems = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("name");
                allItems.add(id);
            }

            Spinner itemsList = findViewById(R.id.itemsSpinner);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allItems);
            itemsList.setAdapter(adapter);
            itemsList.setOnItemSelectedListener(this);

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        //Get the selected item from the list the pass it to the product input
        String product = parent.getItemAtPosition(position).toString();
        EditText itemName = findViewById(R.id.itemInput);
        itemName.setText(product);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //check if there is pending request and hide new request button
    public void checkReqStatus(){
        final Button newReqBtn;
        final View requestScroll;

        newReqBtn = findViewById(R.id.newRequestBtn);
        requestScroll = findViewById(R.id.requestScroll);

        try {
            Statement statement;
            Connection conn = sqlConnection.Connect(); //Connection Object
            statement = conn.createStatement();
            String reqOrderQuery = "Select * from requetorder where agentid = '"+agentId+"' and status = 'open'";
            ResultSet rs = statement.executeQuery(reqOrderQuery);
            if (rs.next()) {
                newReqBtn.setVisibility(View.GONE);
            }else{
                newReqBtn.setVisibility(View.VISIBLE);
                requestScroll.setVisibility(View.GONE);

                newReqBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createNewRequest();
                        newReqBtn.setVisibility(View.GONE);
                        requestScroll.setVisibility(View.VISIBLE);

                    }
                });
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createNewRequest(){
        try {

            Statement statement;
            Connection conn = sqlConnection.Connect(); //Connection Object
            statement = conn.createStatement();
            String newRequestQuery ="INSERT INTO requetorder (siteid, agentid, date, sitename, agentname, status) VALUES ('"+siteId+"', '"+agentId+"', '2020-01-01', '"+siteName+"', '"+agentName+"', 'open' )";
            statement.executeQuery(newRequestQuery);
            //Toast.makeText(this, "New Request Created Successfully", Toast.LENGTH_SHORT).show();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
