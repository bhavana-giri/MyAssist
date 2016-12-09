package com.example.manjunath_k_badiger.dellsa;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * This Class will handle all the Database queries used in the activities. We are calling different methods based on our requirement
 */
public class dboperations extends MainActivity {

    public int countcase, count6, flag = 0;
    String caseid = "";
    Calendar calendar1 = Calendar.getInstance();
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MMMM-dd h:mm:ss.SSS");
    String currentDate = formatter1.format(calendar1.getTime());

    // ------> This function will check check whether case is created in db or not <------- //


    // This function will insert case into is_case_created Table

    // This function will retrieve all the data in sys_info table

    // This function will retrieve all the data in master table


    // This method will insert case into is_case_created table when manual button is pressed

    // This function will do the authentication of the user while login

    // This method will retrieve all the user information stored in user_info table


    // This method will retrive the caseid for is_case_created table
    public String[] caseidretrieve(String[] details) {
        ResultSet rs8 = null;
        Connection con8 = null;
        Timestamp _date = null;
        PreparedStatement ps8 = null;
        try {
            ConnectionClass connection8 = new ConnectionClass(); // establishing connection to db
            con8 = connection8.doConnect();
            if (con8 == null) {
                Log.e("Error", "Error in SQL Connection"); // error occured while connecting to db
            } else {
                String query = "select * from is_case_created where user_id='" + uid + "';"; // select all the info regarding the case created by user having particular uid
                ps8 = con8.prepareStatement(query);
                rs8 = ps8.executeQuery(); // execute the query and store info in result set
                while (rs8.next()) {
                    details[0] = rs8.getString("case_id");
                    _date = rs8.getTimestamp("creation_date");
                    details[3] = rs8.getString("Status");

                }
                details[1] = _date.toString();
                calendar1.setTime(_date);
                calendar1.add(Calendar.DAY_OF_WEEK, 7);
                _date.setTime(calendar1.getTime().getTime());
                _date = new Timestamp(calendar1.getTime().getTime());
                details[2] = _date.toString();

            }
        } catch (Exception ex) {
            Log.e("Error",ex.toString()); //Show this if data not retrieved properly
        } finally {
            Log.i("Info", "caseidretrieve connections closing");
            try {
                ps8.close();
            } catch (Exception e) { /* ignored */ }
            try {
                con8.close();
            } catch (Exception e) { /* ignored */ }
            Log.i("Info", "caseidretrieve connections closed");
        }

        return details; // return caseid

    }

    public String warranty_type() {
        String _warrantytype = null;
        ResultSet rs9 = null;
        PreparedStatement ps9 = null;
        Connection con9 = null;
        try {
            Log.i("Info", "Entered user_info method and retrieving the info");
            ConnectionClass connection9 = new ConnectionClass(); // establishing connection to db
            con9 = connection9.doConnect();
            if (con9 == null) {
                Log.e("Error", "Error in SQL Connection"); // error occured while connecting to db
            } else {
                String query = "select warranty_type from device_inventory where device_id in (select device_id from master where s_tag='" + service_tag + "' and error_code='" + error_code+"');"; // select all the information of user having particular user id
                ps9 = con9.prepareStatement(query);
                rs9 = ps9.executeQuery(); // execute the query and store all the info in rs7 resultset
                while (rs9.next()) {
                    _warrantytype = rs9.getString("warranty_type");
                }
            }
        } catch (Exception ex) {
            Log.e("Error", "" + ex); //Show this if data not retrieved properly
        } finally {
            Log.i("Info", "warrantytype connections closing");
            try {
                ps9.close();
            } catch (Exception e) { /* ignored */ }
            try {
                con9.close();
            } catch (Exception e) { /* ignored */ }
            Log.i("Info", "warrantytype connections closed");
        }


        return _warrantytype; // return result set
    }




    public static void historyinsertion(String id, String hitroy) {

        Connection con12 = null;
        PreparedStatement ps12 = null;
        try {
            Log.i("Info", "Entered the Scan History Method");
            ConnectionClass connection12 = new ConnectionClass(); // establishing connection to db
            con12 = connection12.doConnect();
            if (con12 == null) {
                Log.e("Error", "Error in SQL Connection");   // error occured while connecting to db
            } else {
                // Insert values into the table
                String query = "insert into scan_history (emailid,scanhistory,scantime) values ('" + id + "','" + hitroy + "','" + currDate + "');";
                ps12 = con12.prepareStatement(query);
                ps12.executeQuery(); // Execute the insert query
                Log.i("Info", "inserted the case Manually");

            }
        } catch (Exception ex) {
            Log.e("Error", "" + ex); //Show this if data not retrieved properly
        } finally {
            Log.i("Info", "Manual insertion connections closing");
            try {
                ps12.close();
            } catch (Exception e) { /* ignored */ }
            try {
                con12.close();
            } catch (Exception e) { /* ignored */ }
            Log.i("Info", "Manual Insertion connections closed");
        }
    }



    public ResultSet historydisplay() {
        Log.i("Info", "Entered history display method");
        ResultSet rs13 = null;
        PreparedStatement ps13 = null;
        Connection con13 = null;
        try {
            ConnectionClass connection13 = new ConnectionClass();
            con13 = connection13.doConnect();  // establishing connection to db
            if (con13 == null) {
                Log.e("Error", "Error in SQL Connection"); // error occured while connecting to db
            } else {
                String query = "select top 10 scanhistory,scantime from scan_history where emailid='" + LoginActivity.email + "'order by scantime desc;"; // select all the information of product having particular service tag from db
                ps13 = con13.prepareStatement(query);
                rs13 = ps13.executeQuery(); // execute the query and store it in Result set
                Log.i("Info", "Scan display executed");

            }
        } catch (Exception ex) {
            Log.e("Error", "" + ex); //Show this if data not retrieved properly
        }


        return rs13; // return result set
    }



}
