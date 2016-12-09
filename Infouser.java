package com.example.manjunath_k_badiger.dellsa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaushik_K_R on 4/18/2016.
 */
public class Infouser extends AppCompatActivity {
//
//    dboperations dbo = new dboperations();
    int status;
    List<NameValuePair> nameValuePairs;
    Button change;
    static JSONObject jobj=null;
    public String userid1,firstname1,lastname1,streetname1,locality1,country1,pincode1;
    public String useri,fname,lname,sname,loc,countr,pin;
    public EditText _userid1,_fname1,_lname1,_streetname1,_locality1,_country1,_pincode1;
    ClientServerInterface clientServerInterface = new ClientServerInterface();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infouser);
        change = (Button)findViewById(R.id.change);
        change.setVisibility(View.INVISIBLE);


        _userid1=(EditText) findViewById(R.id.userid1);
        _fname1 = (EditText)findViewById(R.id.f_name1);
        _lname1 = (EditText)findViewById(R.id.l_name1);
        _streetname1 = (EditText)findViewById(R.id.street_name1);
        _locality1 = (EditText)findViewById(R.id.locality1);
        _country1 = (EditText)findViewById(R.id.country1);
        _pincode1 = (EditText)findViewById(R.id.pincode1);

//        -----> blocks the editing option <-----
        _userid1.setFocusable(false);
        _fname1.setFocusable(false);
        _lname1.setFocusable(false);
        _streetname1.setFocusable(false);
        _locality1.setFocusable(false);
        _country1.setFocusable(false);
        _pincode1.setFocusable(false);



//        ResultSet _userinfo1 = dbo.user_info();
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("e_mail", LoginActivity.email.toString().trim()));//
        jobj = clientServerInterface.makeHttpRequest(config.USERINFO_URL, (ArrayList<NameValuePair>) nameValuePairs);
        try {

                userid1=jobj.getString("userid");
                firstname1=jobj.getString("firstname");
                lastname1=jobj.getString("lastname");
                streetname1=jobj.getString("streetname");
                locality1=jobj.getString("locality");
                country1=jobj.getString("country");
                pincode1=jobj.getString("pincode");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        _userid1.setText(userid1);
        _fname1.setText(firstname1);
        _lname1.setText(lastname1);
        _streetname1.setText(streetname1);
        _locality1.setText(locality1);
        _country1.setText(country1);
        _pincode1.setText(pincode1);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useri=_userid1.getText().toString();
                fname= _fname1.getText().toString();
                lname=_lname1.getText().toString();
                sname=_streetname1.getText().toString();
                loc=_locality1.getText().toString();
                countr=_country1.getText().toString();
                pin=_pincode1.getText().toString();
                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("useri",useri.trim()));// $Edittext_value = $_POST['Edittext_value'];
                nameValuePairs.add(new BasicNameValuePair("fname", fname.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("lname",lname.trim()));
                nameValuePairs.add(new BasicNameValuePair("sname",sname.trim()));
                nameValuePairs.add(new BasicNameValuePair("loc",loc.trim()));
                nameValuePairs.add(new BasicNameValuePair("countr",countr.trim()));
                nameValuePairs.add(new BasicNameValuePair("pin", pin.trim()));
                ClientServerInterface clientServerInterface=new ClientServerInterface();
                clientServerInterface.putdata(config.USERINFO_URL, (ArrayList<NameValuePair>) nameValuePairs);
//                dbo.update_userinfo(useri,fname,lname,sname,loc,countr,pin);
                change.setVisibility(View.INVISIBLE);
                showSimplePopUp("Changes to your profile have been saved");

                _fname1.setFocusable(false);
                _lname1.setFocusable(false);
                _streetname1.setFocusable(false);
                _locality1.setFocusable(false);
                _country1.setFocusable(false);
                _pincode1.setFocusable(false);


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.infomenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.edit:
                edit();
                break;
        }
        return true;
    }

    public void edit(){
        change.setVisibility(View.VISIBLE);

//        -----> Enables editing option <-----
        _fname1.setFocusableInTouchMode(true);
        _lname1.setFocusableInTouchMode(true);
        _streetname1.setFocusableInTouchMode(true);
        _locality1.setFocusableInTouchMode(true);
        _country1.setFocusableInTouchMode(true);
        _pincode1.setFocusableInTouchMode(true);


    }

    private void showSimplePopUp(String content) {

        android.app.AlertDialog.Builder helpBuilder = new android.app.AlertDialog.Builder(this);
        helpBuilder.setTitle("Saved");
        helpBuilder.setMessage(content);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });
        android.app.AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }
}
