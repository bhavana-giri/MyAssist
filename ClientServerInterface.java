package com.example.manjunath_k_badiger.dellsa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* this class helps to get data from server*/
public class ClientServerInterface {
    //input stream deals with bytes


    static JSONArray jobj = null;
    static String json = "";
    static JSONObject object = null;
    static InputStream is = null;


    HttpClient httpClient = new DefaultHttpClient();
    HttpPost httppost;
    HttpResponse response;
    String result;

    //constructor
    public ClientServerInterface() {
        Log.i("tag", "object created");

    }

    //this method returns json object.
    public int putdata(String URL, ArrayList<NameValuePair> params) {
        HttpClient httpClient = new DefaultHttpClient();
        Log.i("tag", "after httpClient");
        httppost = new HttpPost(URL); // make sure the url is correct.
        //add your data
        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //Execute HTTP Post Request
        try {
            response = httpClient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream inputStream = null;
            try {
                inputStream = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = convertResponsetoString(inputStream);

        }
        int x=result.compareTo("1");
        if (result.compareTo("1") == 0) {

            return 1;//success
        } else {
            return 0;
        }
    }


    public String convertResponsetoString(InputStream InputStream)

    {

        String mResult = "";
        StringBuilder mStringBuilder;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(InputStream, "UTF-8"), 8);
            mStringBuilder = new StringBuilder();
            mStringBuilder.append(reader.readLine());
            String line = "0";
            while ((line = reader.readLine()) != null) {
                mStringBuilder.append(line);
            }
            InputStream.close();
            mResult = mStringBuilder.toString();
            return mResult;

        } catch (Exception e) {
            return mResult;
        }

    }

    public JSONObject makeHttpRequest(String url,ArrayList<NameValuePair> parameters){
//http client helps to send and receive data

//our request method is post

        try {
//get the response
            httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(parameters));
            response = httpClient.execute(httppost);
            HttpEntity httpentity = response.getEntity();
// get the content and store it into inputstream object.
            is = httpentity.getContent();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
//convert byte-stream to character-stream.
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while((line = reader.readLine())!=null){
                    sb.append(line+"\n");

                }
//close the input stream
                is.close();
                json = sb.toString();
                try {
                    jobj = new JSONArray(json);
                    for(int n = 0; n < jobj.length(); n++)
                    {
                        object = jobj.getJSONObject(n);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }


}