/**
 * This class will call scanning and view detail function when user clicks on Scan and View Button in MainActivity.xml.
 * To connect to the DataBase this will call the function doConnect using ConnectionClass Object
 */


package com.example.manjunath_k_badiger.dellsa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button scanhistoryBtn,scanBtn,viewBtn;    // Declare variables
    static String scanContent,service_tag,error_code,uid;
    Calendar cal = Calendar.getInstance();
    HttpPost httppost;
    static JSONObject jobj = null;
    HttpResponse response;
    List<NameValuePair> nameValuePairs;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss.SSS");
    static String currDate,emailid;
    Map<String, String> hashMap = new HashMap<String, String>();
    ClientServerInterface clientServerInterface = new ClientServerInterface();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (Button) findViewById(R.id.scan_button);


        writefile();
        // -------> Start scanning activity if user press scan button <------ //
        scanBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.initiateScan();
            }

        });
    }



    public void writefile()
    {
        try {
            final File path = new File(
                    Environment.getExternalStorageDirectory(), "mPCAssist");
            if (!path.exists()) {
                path.mkdir();
            }
            Runtime.getRuntime().exec(
                    "logcat  -d -f " + path + File.separator
                            + "mPCA_"+System.currentTimeMillis()+"logcat"
                            + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.testmenu, menu);
        menu.getItem(2).setVisible(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.about:
                aboutmenuitem();
                break;
            case R.id.profile:
                profilesetting();
                break;
            case R.id.upgrade:
                upgrademenuitem();
                break;
            case R.id.contact:
                contact();
                break;
            case R.id.scanhistory:
                scan_history();
                break;
            case R.id.logout:
                logout();
        }
        return true;
    }



    public void aboutmenuitem(){
        new AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage("This is an about Alert Dialogue")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    public void profilesetting(){
        Intent infointent = new Intent(this,Infouser.class);
        startActivity(infointent);
    }



    public void exitmenuitem(){
    }
    public void contact(){
        Intent intent = new Intent(this,Contact.class);
        startActivity(intent);

    }
    public void scan_history(){
        Intent scanintent = new Intent(this,ScanHistory.class);
        startActivity(scanintent);

    }
    public void upgrademenuitem(){
        new android.app.AlertDialog.Builder(this)
                .setTitle("Upgrade")
                .setMessage("Upgradation to Pro plus will be provided soon")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    public  void logout(){
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    // -------> Retrieve the scan content <------- //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        System.out.println("hdgf"+scanningResult+scanContent);
        if (scanningResult!=null) {
            // --------> Assigning scanned content to scanContent variable and retrieving all necessary fields from it using pattern matching ------>
            if(scanningResult.getContents() != null) {
                scanContent=scanningResult.getContents().toString();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Scan successful!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                Log.i("Info", "Scanning Successful");
                currDate = format1.format(cal.getTime());
                emailid = LoginActivity.email;
                dboperations db = new dboperations();
                db.historyinsertion(emailid, scanContent);

                Pattern pattern = Pattern.compile("(\\w+)=(\\w+)");
                Matcher matcher = pattern.matcher(scanContent);
                while (matcher.find()) {

                    hashMap.put(matcher.group(1), matcher.group(2));  // Putting values into HashMap
                }
                service_tag = hashMap.get("serviceTag");      // Getting the values
                error_code = hashMap.get("errCode");
                uid = hashMap.get("uID");
                String val_code = hashMap.get("ValCode");

                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
            else{                                                       // If not scanned
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();

                Log.i("Info","No data received");
            }


        }


    }
    @Override
    public void onClick(View v) {

    }
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this,"Press Back again to Exit.",Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
    // ----> Enter this block when user clicks on View Button <----- //
    public class DoLogin extends AsyncTask<String,String,String>
    {
        Boolean isSuccess = false;
        String z = "";
        @Override
        protected void onPostExecute(String r) {
            Toast toast = Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();

            if(isSuccess) {
                Intent i = new Intent(MainActivity.this,ViewProduct.class); // On success start viewproduct activity
                startActivity(i);
            }

        }



        @Override
        protected String doInBackground(String... params) {
            Statement stmt=null;
            ResultSet rs=null;
            ConnectionClass connection1;
            Connection con=null;
            if(scanContent.compareTo("null")==0) // if not scanned display "Please Scan" Toast
                z = "Please Scan";
            else
            {
                try {
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("service_tag",service_tag.trim()));// $Edittext_value = $_POST['Edittext_value'];
                    ClientServerInterface clientServerInterface=new ClientServerInterface();
                    int a=clientServerInterface.putdata(config.VALIDSTAG_URL, (ArrayList<NameValuePair>) nameValuePairs);
                    if (a > 0) // if YES display Scan successful
                    {
                        z = "Valid";
                        isSuccess = true;
                    } else {
                        z = "Not Valid";  // if NO display Not Valid
                        isSuccess = false;
                    }

                }
                catch (Exception ex) // Exceptions
                {
                    isSuccess = false;
                    Log.e(ex.toString(),toString());
                    z = "Exceptions";
                }
                finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException e) { /*Closing Resultset */}
                    }
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException e) { /*Closing Statement */}
                    }
                    if (con != null) {
                        try {
                            con.close();
                        } catch (SQLException e) { /* Closing connection */}
                    }
                }
                System.out.println(isSuccess);
            }
            return z;
        }
    }
}
