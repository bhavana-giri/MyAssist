package com.example.manjunath_k_badiger.dellsa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * This class will retrieve the Master Table values and display it in table.
 */
public class MoreDetails extends ViewProduct {

    TextView A, B, C, D, E, F, G;
    String case_id;


    static int count;
    int status,flag;
    private String a, b, c, d, e, f, g;
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss.SSS");
    static String creationdate,currentdate1;
    Button addbutton;
    TextView caseinfo;
    dboperations dbop = new dboperations();
    static String[] details=new String[4];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moredetails);
        format1.setTimeZone(TimeZone.getTimeZone("GMT"));

        currentdate1 = format1.format(cal.getTime());
        Log.i("creationdate",currentdate1);
        addbutton = (Button) findViewById(R.id.addButton);
        addbutton.setVisibility(View.INVISIBLE);
        caseinfo= (TextView) findViewById(R.id.caseinfo);
        addbutton.setTag(1);//set flag to 1 for addbutton
        flag=1;
        //webservice
        HttpClient httpClient =new DefaultHttpClient();
        httppost= new HttpPost(config.CASE_CREATED_URL); // make sure the url is correct.

        //add your data
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("service_tag",service_tag.trim()));// $Edittext_value = $_POST['Edittext_value'];
        nameValuePairs.add(new BasicNameValuePair("error_code", error_code.toString().trim()));
        ClientServerInterface clientServerInterface=new ClientServerInterface();
        count=clientServerInterface.putdata(config.CASE_CREATED_URL, (ArrayList<NameValuePair>) nameValuePairs);
        details = dbop.caseidretrieve(details);
        FillList detailList = new FillList(); // Start filling the tables with values
        detailList.execute("");



        //Onclick function in accordance with Warranty type
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int stat = (Integer) v.getTag();
                if (stat == 1) {
                    if(flag==1) {
                        creationdate = format1.format(cal.getTime());
                        Log.i("creationdate",creationdate);
                        //add your data
                        nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("stag",service_tag.trim()));// $Edittext_value = $_POST['Edittext_value'];
                        nameValuePairs.add(new BasicNameValuePair("error", error_code.toString().trim()));
                        nameValuePairs.add(new BasicNameValuePair("userid",uid.trim()));
                        nameValuePairs.add(new BasicNameValuePair("currentDate",creationdate.trim()));
                        ClientServerInterface clientServerInterface=new ClientServerInterface();
                        status=clientServerInterface.putdata(config.CASE_INSERTION_URL, (ArrayList<NameValuePair>) nameValuePairs);

                        //this code is to create case that does not exist existed
                        addbutton.setVisibility(View.INVISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Case creation successful!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                        Intent success = new Intent(MoreDetails.this, Confirmation.class);
                        success.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(success);
                    }
                    else{
                        Intent casecreate = new Intent(MoreDetails.this, Case_creation.class);
                        casecreate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        addbutton.setVisibility(View.INVISIBLE);
                        startActivity(casecreate);
                    }
                } else {
                        //This code is to display case id of the case already created
                    showSimplePopUp(details);
                    addbutton.setVisibility(View.VISIBLE);

                }

            }
        });
    }
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
    //pops up a dialog box if the user has a case already created,displaying the case id
    private void showSimplePopUp(String[] details) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Case Information");
        helpBuilder.setMessage("Case Number: CR"+details[0]+"\n"+"Case Status: "+details[3]+"\n"+"Created Date:"+details[1]);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }
    /*this class fetches more details and displays the case creation buttons depending on the users membership level */
    public class FillList extends AsyncTask<String, String, String> {
        String z = "",proplus="2",pro="1",basic="0";
        ResultSet rs=null,_resultset=null;
        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        //displays moredetails table contents and apporopriate buttons for case creation
        protected void onPostExecute(String r) {

            A = (TextView) findViewById(R.id.s_tag);
            B = (TextView) findViewById(R.id.device_id);
            C = (TextView) findViewById(R.id.device_name);
            D = (TextView) findViewById(R.id.error_code);
            E = (TextView) findViewById(R.id.error_desc);
            F = (TextView) findViewById(R.id.warranty);
            G = (TextView)findViewById(R.id.warrantytype);
            A.setText(a); // Assign the retrieved values from the DB
            B.setText(b);
            C.setText(c);
            D.setText(d);
            E.setText(e);
            F.setText(f);
            G.setText(g);

            String warrantytype = dbo.warranty_type();

            if ((currentdate1.compareTo(f) <= 0) && (count <= 0) && (warrantytype.compareTo(proplus) == 0)) {
                caseinfo.setText("");
                addbutton.setVisibility(View.VISIBLE);

            }  else if ((currentdate1.compareTo(f) <= 0) && (count <= 0) && (warrantytype.compareTo(pro) == 0))
            {
                addbutton.setVisibility(View.VISIBLE);
                caseinfo.setText("");
                addbutton.setText("MANUAL");
                flag=0;
            }
            else if ((currentdate1.compareTo(f) <= 0) && (count <= 0) && (warrantytype.compareTo(basic) == 0))
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Upgrade to have more facilities", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();
            }
            else {
                addbutton.setVisibility(View.INVISIBLE);

                if(currentdate1.compareTo(f)> 0)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Warranty Expired", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                    Log.i("info","warranty expired dialog");
                }
                else {
                    addbutton.setVisibility(View.VISIBLE);
                    caseinfo.setText("Case has already been created");
                    addbutton.setText("Check CASE Information");
                    addbutton.setTag(0);                //set tag to 0 to indicate case already created
                }
            }
        }


        @Override
        protected String doInBackground(String... params) {

            try {

                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("s_tag",service_tag.trim()));// $Edittext_value = $_POST['Edittext_value'];
                nameValuePairs.add(new BasicNameValuePair("e_code",error_code.toString().trim()));
                jobj = clientServerInterface.makeHttpRequest(config.MOREDETAILS_URL, (ArrayList<NameValuePair>) nameValuePairs);
                // Retrieving the column values
                    a=jobj.getString("servicetag");
                    b=jobj.getString("deviceid");
                    c=jobj.getString("devicename");
                    d=jobj.getString("errorcode");
                    e=jobj.getString("errordescription");
                    f=jobj.getString("warranty");
                jobj = clientServerInterface.makeHttpRequest(config.WARRANTYTYPE_URL, (ArrayList<NameValuePair>) nameValuePairs);
                String war=jobj.getString("warrantytype");
                if(count>0) {
                    if (currentdate1.compareTo(details[2]) > 0 && details[3].compareTo("Active") == 0) {
                        case_id=details[0];

                        //add your data
                        nameValuePairs = new ArrayList<NameValuePair>(1);
                        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,

                        nameValuePairs.add(new BasicNameValuePair("case_id", case_id.toString().trim()));// $Edittext_value = $_POST['Edittext_value'];
                        clientServerInterface.putdata(config.UPDATE_CASE_STATUS_URL, (ArrayList<NameValuePair>) nameValuePairs);


                    }
                }
                if(war.compareTo("2")==0)
                {
                    g="Pro Plus";
                }
                else if(war.compareTo("1")==0){
                    g="Pro";
                }
                else{
                    g="Basic";
                }
            } catch (Exception ex) {    // Display the message when error occurs while retrieving
                z = "Error retrieving data from table";
                Log.e("error",""+ex);

            }

            return z;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
