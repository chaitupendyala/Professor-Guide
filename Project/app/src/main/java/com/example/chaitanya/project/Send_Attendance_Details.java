package com.example.chaitanya.project;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class Send_Attendance_Details extends AsyncTask<String, String, String> {
    String roll_name,course,date,reason,status;
    public Send_Attendance_Details(String roll_name,String reason,String status, String course,String date){
        this.roll_name = roll_name;
        this.course = course;
        this.date = date;
        this.reason = reason;
        this.status = status;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://<<SYSTEM-IP>>:<<PORT-NO>>/update_attendance");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setConnectTimeout(10000);
            String send_string = URLEncoder.encode("roll_name","UTF-8")+"="+URLEncoder.encode(this.roll_name,"UTF-8");
            send_string += "&" + URLEncoder.encode("course","UTF-8")+"="+URLEncoder.encode(this.course,"UTF-8");
            send_string += "&" + URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(this.date,"UTF-8");
            send_string += "&" + URLEncoder.encode("reason","UTF-8")+"="+URLEncoder.encode(this.reason,"UTF-8");
            send_string += "&" + URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(this.status,"UTF-8");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(send_string);
            wr.flush();
            wr.close();
            conn.connect();

            BufferedReader fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return fromServer.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
