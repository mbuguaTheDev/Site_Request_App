package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Result;

public class SalaryInputActivity extends AppCompatActivity {

    private SqlConnection sqlConnection;
    Button employeeFetch, submit;
    TextView idNo, empName, jbGroup, sName, sId, empPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_input);
        sqlConnection = new SqlConnection(); //instantiate connection

        submit = findViewById(R.id.salarySubmit);
        submit.setVisibility(View.GONE);
    }

    public  void employeeFetch(View view){

        //get date
        Date cDate = new Date();
        final String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);


        SqlConnection sqlConnection = new SqlConnection();
        final String employeeId, employeeName, jobGroup, siteName, siteId, employeePay;
        EditText empId = findViewById(R.id.empId);
        employeeId = empId.getText().toString();

        idNo = findViewById(R.id.employeeId);
        empName = findViewById(R.id.employeeName);
        jbGroup = findViewById(R.id.jobGroup);
        sName = findViewById(R.id.siteName);
        sId = findViewById(R.id.siteId);
        empPay = findViewById(R.id.empPay);
        employeeFetch = findViewById(R.id.employeeFetch);




        try {
            final Statement[] statement = new Statement[1];
            final Connection conn = sqlConnection.Connect(); //Connection Object
            statement[0] = conn.createStatement();
            String empIdQuery ="SELECT idno, name, job, site, sid, pay FROM worker WHERE idno = '"+ employeeId +"'";
            ResultSet rs = statement[0].executeQuery(empIdQuery);

            if (rs.next()){

                //emmpty the Id field
                empId.setText("");

                //get details
                employeeName = rs.getString("name");
                jobGroup = rs.getString("job");
                siteName = rs.getString("site");
                siteId = rs.getString("sid");
                employeePay = rs.getString("pay");

                //populate on the view
                idNo.setText("Id No: " + employeeId);
                empName.setText("Name: " + employeeName);
                jbGroup.setText("Job Group: " + jobGroup);
                sName.setText("Site: " + siteName);
                sId.setText("Site Id: " + siteId);
                empPay.setText("Pay: " + employeePay);

                //make submit button visible
                submit.setVisibility(View.VISIBLE);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try{
                            statement[0] = conn.createStatement();
                            String slryCheckQuery= "SELECT * FROM pay WHERE empid = '"+ employeeId +"' AND date = '"+fDate+"' ";
                            ResultSet result = statement[0].executeQuery(slryCheckQuery);

                            if (result.next()){
                                Toast.makeText(SalaryInputActivity.this, "Pay Request Already Submitted", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                try{
                                    statement[0] = conn.createStatement();
                                    String salaryQuery = "INSERT INTO pay (empid, empname, emppay, siteid, sitename, jgroup) VALUES ('"+employeeId+"', '"+employeeName+"', '"+employeePay+"', '"+siteId+"', '"+siteName+"', '"+jobGroup+"')";
                                    statement[0].executeQuery(salaryQuery);

                                }catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                //clear texts
                                idNo.setText("");
                                empName.setText("");
                                jbGroup.setText("");
                                sName.setText("");
                                sId.setText("");
                                empPay.setText("");

                                Toast.makeText(SalaryInputActivity.this, "Pay Request Submitted", Toast.LENGTH_SHORT).show();
                                submit.setVisibility(View.GONE);
                            }

                        }catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }else{

                Toast.makeText(this, "Employee Does Not Exist", Toast.LENGTH_SHORT).show();

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
