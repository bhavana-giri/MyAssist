package com.example.manjunath_k_badiger.dellsa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by Manjunath_K_Badiger on 3/21/2016.
 */
/*displays a confirmation about successful case creation and displays the case id */
public class Confirmation extends MainActivity {
    dboperations dbo=new dboperations();
    ResultSet rs;
    String a;
    String[] details = new String[5];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        TextView confirm= (TextView)findViewById(R.id.confirm);
//        rs=dbo.user_info();
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("e_mail", LoginActivity.email.toString().trim()));//
        jobj = clientServerInterface.makeHttpRequest(config.USERINFO_URL, (ArrayList<NameValuePair>) nameValuePairs);
        try {
            a=jobj.getString("firstname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        details=dbo.caseidretrieve(details);
        System.out.println(details[1]);
        String message = a+", your case has been created successfully on "+details[1]+"(GMT)"+"\n"+"Please note down your case id 'CR"+details[0]+"'";
        confirm.setText(message);
    }
    //invokes a function to display quit dialog
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

}
