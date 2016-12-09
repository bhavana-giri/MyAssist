package com.example.manjunath_k_badiger.dellsa;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.execchain.ClientExecChain;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class will retrieve the data from Sys-info Table and display it in table
 */
public class ViewProduct extends MainActivity{
    ProgressBar pbbar;
    ListView lstpro;
    private Button viewDetails;
    dboperations dbo=new dboperations();
    List<NameValuePair> nameValuePairs;
    String _warrantytype="",result;
    HttpEntity entity;
    InputStream is;
    static JSONObject jobj = null;
    static String json = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewproduct);
        _warrantytype=dbo.warranty_type();
        viewDetails = (Button)findViewById(R.id.b_detail);
        // -----> When user clicks on View More Details Button it will start Moredetails.class <------ //
        viewDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent detailIntent= new Intent(ViewProduct.this,MoreDetails.class);
                detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(detailIntent);
            }

        });


        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        lstpro = (ListView) findViewById(R.id.lstproducts);
        FillList fillList = new FillList();
        fillList.execute("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.testmenu, menu);
        if(_warrantytype.compareTo("2")==0)
        {
            menu.getItem(2).setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent back_to_main = new Intent(ViewProduct.this,MainActivity.class);
        startActivity(back_to_main);
    }

    // -------> Filling the listview elements with the product info <------- //
    public class FillList extends AsyncTask<String, String, String> {
        String z = "";
        // ResultSet rs;

        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();

        @Override
        //displays the progress bar
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);


            String[] from = { "A", "B" };
            int[] views = { R.id.s_tag,R.id.error_code};
            final SimpleAdapter ADA = new SimpleAdapter(ViewProduct.this,prolist, R.layout.lsttemplate, from,views);
            lstpro.setAdapter(ADA);

        }
        @Override
        //fetch error code and service tag of the product
        protected String doInBackground(String... params) {

            try {
                ClientServerInterface clientServerInterface = new ClientServerInterface();
                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("s_tag",service_tag.trim()));// $Edittext_value = $_POST['Edittext_value'];
                nameValuePairs.add(new BasicNameValuePair("e_code",error_code.toString().trim()));
                jobj = clientServerInterface.makeHttpRequest(config.VIEWPRODUCT_URL, (ArrayList<NameValuePair>) nameValuePairs);
                Log.i("JSON",jobj.getString("servicetag"));
                Map<String, String> datanum = new HashMap<String, String>();
                datanum.put("A", jobj.getString("servicetag"));
                datanum.put("B", jobj.getString("errorcode"));
                prolist.add(datanum);

                z = "Success";
            }catch (Exception e)
            {

            }
            return z;
        }
    }

}



