package com.example.manjunath_k_badiger.dellsa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * This class will take the credentials from user and enter them into case_created table
 */
public class Case_creation extends MainActivity {
    dboperations dbo = new dboperations();
    int status;
    Button submitbtn;
    public String userid,stag,error,firstname,lastname,streetname,locality,country,pincode;
    public String useri,fname,lname,sname,loc,countr,pin;
    List<NameValuePair> nameValuePairs1;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss.SSS");
    static String creationdate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_creation);
        submitbtn = (Button)findViewById(R.id.submit);
        creationdate = format1.format(cal.getTime());

        final TextView servicetag=(TextView)findViewById(R.id.service_tag);
        final TextView errorcode=(TextView)findViewById(R.id.error_code);
        final TextView _userid = (TextView)findViewById(R.id.user_id);
        final EditText _fname = (EditText)findViewById(R.id.f_name);
        final EditText _lname = (EditText)findViewById(R.id.l_name);
        final EditText _streetname = (EditText)findViewById(R.id.street_name);
        final EditText _locality = (EditText)findViewById(R.id.locality);
        final EditText _country = (EditText)findViewById(R.id.country);
        final EditText _pincode = (EditText)findViewById(R.id.pincode);
//        ResultSet _userinfo = dbo.user_info();
//        try {
//            while (_userinfo.next())
//            {
//                firstname=_userinfo.getString("f_name");
//                lastname=_userinfo.getString("l_name");
//                streetname=_userinfo.getString("street_name");
//                locality=_userinfo.getString("locality");
//                country=_userinfo.getString("country");
//                pincode=_userinfo.getString("pincode");
//
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("e_mail", LoginActivity.email.toString().trim()));//
        jobj = clientServerInterface.makeHttpRequest(config.USERINFO_URL, (ArrayList<NameValuePair>) nameValuePairs);
        try {
            firstname=jobj.getString("firstname");
            lastname=jobj.getString("lastname");
            streetname=jobj.getString("streetname");
            locality=jobj.getString("locality");
            country=jobj.getString("country");
            pincode=jobj.getString("pincode");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        _userid.setText(uid);
        servicetag.setText(service_tag);
        errorcode.setText(error_code);
        _fname.setText(firstname);
        _lname.setText(lastname);
        _streetname.setText(streetname);
        _locality.setText(locality);
        _country.setText(country);
        _pincode.setText(pincode);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //auto fill the service tag,error code and user id fields in the case creation form
                stag=servicetag.getText().toString();
                error=errorcode.getText().toString();
                userid=_userid.getText().toString();
                //validates the form values
                if((error.compareTo(error_code)==0)&&(stag.compareTo(service_tag)==0)&&(userid.compareTo(uid)==0)) {

                    useri=_userid.getText().toString();
                    fname=_fname.getText().toString();
                    lname=_lname.getText().toString();
                    sname=_streetname.getText().toString();
                    loc=_locality.getText().toString();
                    countr=_country.getText().toString();
                    pin=_pincode.getText().toString();
                    //webservice
                    httppost= new HttpPost(config.CASE_INSERTION_URL); // make sure the url is correct.

                    //add your data

                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("useri",useri.trim()));// $Edittext_value = $_POST['Edittext_value'];
                    nameValuePairs.add(new BasicNameValuePair("fname", fname.toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("lname",lname.trim()));
                    nameValuePairs.add(new BasicNameValuePair("sname",sname.trim()));
                    nameValuePairs.add(new BasicNameValuePair("loc",loc.trim()));
                    nameValuePairs.add(new BasicNameValuePair("countr",countr.trim()));
                    nameValuePairs.add(new BasicNameValuePair("pin", pin.trim()));
                    ClientServerInterface clientServerInterface=new ClientServerInterface();
                    status=clientServerInterface.putdata(config.UPDATE_USER_INFO_URL, (ArrayList<NameValuePair>) nameValuePairs);


                    nameValuePairs.add(new BasicNameValuePair("userid",userid.trim()));
                    nameValuePairs.add(new BasicNameValuePair("stag", stag.trim()));
                    nameValuePairs.add(new BasicNameValuePair("error",error.trim()));
                    nameValuePairs.add(new BasicNameValuePair("currentDate",creationdate.trim()));// Insert values into db
                    status = clientServerInterface.putdata(config.MANUAL_INSERTION_URL, (ArrayList<NameValuePair>) nameValuePairs);






                    submitbtn.setVisibility(View.INVISIBLE); // set visibility of button to invisible after submission
                    _userid.setText("");
                    servicetag.setText("");
                    errorcode.setText("");
                    if(status!=0)
                    {
                        Intent success = new Intent(Case_creation.this,Confirmation.class);
                        success.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(success);
                    }
                }
            }
        });
    }
}
