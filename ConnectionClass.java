package com.example.manjunath_k_badiger.dellsa;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class will connect an android application to the Backend (Microsoft SQL Server DataBase)
 * It will use JTDS library for connecting database
 */

public class ConnectionClass
    {
        // -------> Database and Server Credentials <------- //
                String ip = "192.168.43.152:1433";//apache port no here
                //String ip = "192.168.43.124:1433";//apache port no here
                String db = "sysdb";
                String un = "admin";
                String password = "Dell@56";


        // ------> Connecting to Database <------- //
                @SuppressLint("NewApi")
                public Connection doConnect(){
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Connection conn = null;
                String ConnURL = null;
                try {
                        // ------> Establishing connection to db <------- //
                        Class.forName("net.sourceforge.jtds.jdbc.Driver");
                        ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"+ "databaseName=" + db + ";user=" + un + ";password="+ password + ";"; // Connection URL
                        conn = DriverManager.getConnection(ConnURL); // Connecting to DB
                        Log.i("Info",conn.toString());
                    }
                catch (SQLException se)
                {
                        Log.e("ERRO", se.getMessage());
                }
                catch (ClassNotFoundException e)
                {
                        Log.e("ERRO", e.getMessage());
                }
                catch (Exception e)
                {
                        Log.e("ERRO", e.getMessage());
                }
         return conn; // Returning connection variable
    }
}
