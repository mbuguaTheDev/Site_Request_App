package com.ocube.siterequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchImageActivity extends AppCompatActivity {

    private ArrayList<ImagesModel> imageArrayList;  //Images Array
    private ImagesAdapter imagesAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false; // boolean
    private SqlConnection sqlConnection; //SQL Connection Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);

        recyclerView = findViewById(R.id.imagesRecycler);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        sqlConnection = new SqlConnection();
        imageArrayList = new ArrayList<>();

        Button searchImageBtn = findViewById(R.id.srchImgBtn);
        searchImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncData orderData = new SyncData();
                orderData.execute("");

            }
        });
    }


    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Error connecting to the server!";
        ProgressDialog progress;

        EditText requestid = findViewById(R.id.requestId);
        String reqId = requestid.getText().toString();


        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(SearchImageActivity.this, "Fetching ",
                    "Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database,
        {
            try
            {
                Connection conn = sqlConnection.Connect(); //Connection Object
                if (conn == null)
                {
                    success = false;
                }
                else {
                    String query = "SELECT image FROM request WHERE requestid = '"+reqId+"'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                imageArrayList.add(new ImagesModel(rs.getString("image")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Images Found";
                        success = true;
                    } else {
                        msg = "Images not found!";
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
            Toast.makeText(SearchImageActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (!success) {


            }
            else {
                requestid.setText("");

                try
                {
                    imagesAdapter = new ImagesAdapter(imageArrayList , SearchImageActivity.this);
                    recyclerView.setAdapter(imagesAdapter);
                } catch (Exception ex)
                {

                }

            }
        }
    }

    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
        private List<ImagesModel> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder
        {

            public ImageView decodedImage;
            public View layout;

            public ViewHolder(View v)
            {
                super(v);
                layout = v;
                decodedImage = v.findViewById(R.id.decodedImage);

            }
        }

        // Constructor
        public ImagesAdapter(List<ImagesModel> myDataset,Context context)
        {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.images_content, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final ImagesModel imagesModel = values.get(position);
            byte[] encodedString = Base64.decode(imagesModel.getImageString(), Base64.DEFAULT);
            Bitmap decoded = BitmapFactory.decodeByteArray(encodedString, 0, encodedString.length);
            holder.decodedImage.setImageBitmap(decoded);


        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }

    }
}
