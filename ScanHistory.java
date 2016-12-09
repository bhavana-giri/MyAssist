package com.example.manjunath_k_badiger.dellsa;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kaushik_K_R on 4/18/2016.
 */
public class ScanHistory extends AppCompatActivity {


    ProgressBar pbbar1;
    ListView lstpro1;

    dboperations dbo=new dboperations();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanhistory);


        pbbar1 = (ProgressBar) findViewById(R.id.hpbbar);
        lstpro1 = (ListView) findViewById(R.id.hlstproducts);
        FillList hfillList = new FillList();
        hfillList.execute("");
    }


    // -------> Filling the listview elements with the scan history info <------- //
    public class FillList extends AsyncTask<String, String, String> {
        String z = "";
        ResultSet rs;

        List<Map<String, String>> hprolist  = new ArrayList<Map<String, String>>();


        @Override
        protected void onPostExecute(String r) {

            pbbar1.setVisibility(View.GONE);


            String[] from = { "A", "B" };
            int[] views = { R.id.hscancontent,R.id.hscantdisplay};
            final SimpleAdapter ADA = new SimpleAdapter(ScanHistory.this,hprolist, R.layout.hlsstemplate, from,views);
            lstpro1.setAdapter(ADA);

        }
        @Override
        //fetch error code and service tag of the product
        protected String doInBackground(String... params) {

            try {
                Log.i("Calling History", toString());
                rs = dbo.historydisplay();
                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("A", rs.getString("scanhistory"));
                    datanum.put("B", rs.getString("scantime"));
                    hprolist.add(datanum);
                    z = "Success";  //Display on Successful retrieve
                }
            } catch (Exception ex) {
                Log.e("error",""+ex);
                z = "Error retrieving data from table"; //Show this if data not retrieved properly
            }
            return z;
        }
    }




}
