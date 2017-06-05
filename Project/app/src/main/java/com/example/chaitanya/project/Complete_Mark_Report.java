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

public class Complete_Mark_Report extends AsyncTask<String,String,String>{
    String a;
    public Complete_Mark_Report(String a){
        this.a = a;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://<<SYSTEM-IP>>:<<PORT-NO>>/complete_mark_report");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setConnectTimeout(10000);
            String send_string = URLEncoder.encode("course","UTF-8")+"="+URLEncoder.encode(this.a,"UTF-8");
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
