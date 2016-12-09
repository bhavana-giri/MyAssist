package com.example.manjunath_k_badiger.dellsa;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manjunath_k_badiger.dellsa.MainActivity;
import com.example.manjunath_k_badiger.dellsa.R;

public class LoginActivity extends Activity {
    Button b;
    EditText et,pass;
    TextView tv;
    HttpPost httppost;
    public static final String MyPREFERENCES = "MyPrefs" ;
    HttpResponse response;
    List<NameValuePair> nameValuePairs;
    SharedPreferences sharedpreferences;
    ProgressDialog dialog = null;
    static String email;
    static JSONObject jobj=null;
    String result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b = (Button)findViewById(R.id.btn_login);
        et = (EditText)findViewById(R.id.input_email);
        pass= (EditText)findViewById(R.id.input_password);
        tv = (TextView)findViewById(R.id.mTextFeild);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(LoginActivity.this, "",
                        "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        login();
                    }
                }).start();
            }
        });
    }

    void login(){
        int flag;
        try{

            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
             email = et.getText().toString();
            Log.i("webservice", "before namevalurepairs");
            nameValuePairs.add(new BasicNameValuePair("username", email.trim()));// $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("password", pass.getText().toString().trim()));
           ClientServerInterface clientServerInterface=new ClientServerInterface();
            jobj=clientServerInterface.makeHttpRequest(config.AUTHENTICATION_URL, (ArrayList<NameValuePair>) nameValuePairs);

            result=jobj.getString("status");

            if(result.compareTo("1")==0) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }
                });

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }else{
                showAlert();
                dialog.dismiss();
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    public void showAlert(){
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    public String convertResponsetoString(InputStream InputStream)

    {

        String mResult="";
        StringBuilder mStringBuilder;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(InputStream,"UTF-8"),8);
            mStringBuilder =new StringBuilder();
            mStringBuilder.append(reader.readLine() +"\n");
            String line="0";
            while((line = reader.readLine()) !=null)
            {
                mStringBuilder.append(line +"\n");
            }
            InputStream.close();
            mResult=mStringBuilder.toString();
            return mResult;

        }catch(Exception e){
            return mResult;
        }

    }


}