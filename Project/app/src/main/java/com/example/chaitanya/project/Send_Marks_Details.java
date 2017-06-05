package com.example.chaitanya.project;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Send_Marks_Details extends AsyncTask<String, String, String> {
    String roll_no,course,periodical,marks;
    public Send_Marks_Details(String roll_name,String course,String periodical,String marks){
        this.roll_no = roll_name;
        this.course = course;
        this.periodical = periodical;
        this.marks = marks;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://<<SYSTEM-IP>>:<<PORT-NO>>/update_marks");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setConnectTimeout(10000);
            String send_string = URLEncoder.encode("roll_name","UTF-8")+"="+URLEncoder.encode(this.roll_no,"UTF-8");
            send_string += "&" + URLEncoder.encode("course","UTF-8")+"="+URLEncoder.encode(this.course,"UTF-8");
            send_string += "&" + URLEncoder.encode("marks","UTF-8")+"="+URLEncoder.encode(this.marks,"UTF-8");
            send_string += "&" + URLEncoder.encode("periodicals","UTF-8")+"="+URLEncoder.encode(this.periodical,"UTF-8");
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
