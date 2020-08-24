package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

public class SalaryInputActivity extends AppCompatActivity {

    EditText empId, empName, empTel, empAmount;
    Button salarySubmit;
    private SqlConnection sqlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_input);

        sqlConnection = new SqlConnection();

        empId = findViewById(R.id.empId);
        empName = findViewById(R.id.empName);
        empTel = findViewById(R.id.empTel);
        empAmount = findViewById(R.id.empAmount);
        salarySubmit = findViewById(R.id.salarySubmit);

        salarySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salarySubmit();
            }
        });
    }

    public  void salarySubmit (){

        String idNO = empId.getText().toString();
        String mobileNo = empTel.getText().toString();
        String name = empName.getText().toString();
        String amount = empAmount.getText().toString();

        try {
            Statement statement;
            Connection conn = sqlConnection.Connect(); //Connection Object
            statement = conn.createStatement();
            String empIdQuery ="SELECT * from pay WHERE empid = '"+empId+"'";
            ResultSet rs = statement.executeQuery(empIdQuery);

            if (!rs.next()){
                Toast.makeText(this, "Request Already posted", Toast.LENGTH_SHORT).show();
            }else{
                empId.setText("");
                empTel.setText("");
                empName.setText("");
                empAmount.setText("");

                String salaryReqQuery = "INSERT INTO pay (empid, empname, emptell, emppay, status, paydate) VALUES ('"+idNO+"', '"+name+"', '"+mobileNo+"', '"+amount+"', 'posted', 'today',)";
                statement.executeQuery(salaryReqQuery);

                Toast.makeText(this, "Salary Request Posted", Toast.LENGTH_SHORT).show();
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
