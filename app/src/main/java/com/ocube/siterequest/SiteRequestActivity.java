package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SiteRequestActivity extends AppCompatActivity {

    private SqlConnection sqlConnection; //SQL Connection Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_request);
        sqlConnection = new SqlConnection(); //instantiate connection

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

        itemInput =(EditText) findViewById(R.id.itemInput);
        qtyInput=(EditText) findViewById(R.id.qtyInput);
        unitsInput =(EditText) findViewById(R.id.unitsInput);
        daysInput=(EditText) findViewById(R.id.daysInput);

        String pname=itemInput.getText().toString();
        String qty=qtyInput.getText().toString();
        String unit=unitsInput.getText().toString();
        String need=daysInput.getText().toString();

        Statement statement = null;
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
                    String query2= "INSERT  INTO  request (sid,aid,date,pid,pname,rqty,units,day) VALUES('1','1','2020-01-01','1','" + pname + "','" + qty + "','"+unit+"','"+need+"') ";
                    ResultSet resultSet1 = statement.executeQuery(query2);

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

}
