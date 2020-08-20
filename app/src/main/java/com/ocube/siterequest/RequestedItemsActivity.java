package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.ocube.siterequest.LoginActivity.AGENT_ID;
import static com.ocube.siterequest.LoginActivity.SHARED_PREFS;
import static com.ocube.siterequest.LoginActivity.SITE_ID;
import static com.ocube.siterequest.LoginActivity.SITE_NAME;
import static com.ocube.siterequest.LoginActivity.USER_NAME;

public class RequestedItemsActivity extends AppCompatActivity {

    private ArrayList<RequestedItemsModel> itemArrayList;  //List items Array
    private MyAppAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false; // boolean
    private SqlConnection sqlConnection; //SQL Connection Variable
    private String agentId, siteId, agentName, siteName;

    Button submitRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_items);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        agentId = sharedPreferences.getString(AGENT_ID, "agentId");
        siteId = sharedPreferences.getString(SITE_ID, "site");
        agentName = sharedPreferences.getString(USER_NAME, "agent");
        siteName = sharedPreferences.getString(SITE_NAME, "site");

        submitRequest = findViewById(R.id.submitRequest);
        submitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRequest();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        sqlConnection = new SqlConnection();
        itemArrayList = new ArrayList<>(); // Arraylist Initialization

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");
    }

    public void submitRequest() {

        try {

            Statement statement;
            Connection conn = sqlConnection.Connect(); //Connection Object
            statement = conn.createStatement();
            String submitRequestQuery = "UPDATE request SET status = 'open' WHERE aid = '" + agentId + "' ";
            statement.executeQuery(submitRequestQuery);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateOrder();

        //go back to the request screen
        finish();



    }

    public void updateOrder() {

        try {

            Statement statement;
            Connection conn = sqlConnection.Connect(); //Connection Object
            statement = conn.createStatement();
            String postOrderQuery = "UPDATE requetorder SET status = 'posted' WHERE agentid = '" + agentId + "' ";
            statement.executeQuery(postOrderQuery);

            //Toast.makeText(this, "New Request Created Successfully", Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Async Task with three override methods,
    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(RequestedItemsActivity.this, "Fetching ",
                    "Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, query and add items to array list
        {
            try
            {
                Connection conn = sqlConnection.Connect(); //Connection Object
                if (conn == null)
                {
                    success = false;
                }
                else {
                    String query = "select pname,rqty,units,day from request where status = 'pending' and aid = '"+agentId+"' ";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new RequestedItemsModel(rs.getString("pname"),rs.getString("rqty"),rs.getString("units"), rs.getString("day")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Items Found";
                        success = true;
                    } else {
                        msg = "No Items found!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg)
        {
            progress.dismiss();
            Toast.makeText(RequestedItemsActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {

            }
            else {
                try
                {
                    myAppAdapter = new MyAppAdapter(itemArrayList , RequestedItemsActivity.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex)
                {

                }

            }
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder> {
        private List<RequestedItemsModel> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView pName;
            public TextView qty;
            public TextView units;
            public TextView needed;

            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                pName = v.findViewById(R.id.item);
                qty = v.findViewById(R.id.qty);
                units = v.findViewById(R.id.units);
                needed = v.findViewById(R.id.needed);

            }
        }

        // Constructor
        public MyAppAdapter(List<RequestedItemsModel> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.requested_items_content, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final RequestedItemsModel requestedItems = values.get(position);
            holder.pName.setText(requestedItems.getItem());
            holder.qty.setText(requestedItems.getQty());
            holder.units.setText(requestedItems.getUnits());
            holder.needed.setText(requestedItems.getNeeded());

        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {

            return values.size();
        }


    }

}