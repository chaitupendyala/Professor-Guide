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

/**
 * Created by chaitanya on 19/4/17.
 */

public class Get_Complete_Stud_List extends AsyncTask<String,String,String> {
    public static String a = null;
    public static String selected = null;
    public Get_Complete_Stud_List(String selected){
        this.selected = selected;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://<<SYSTEM-IP>>:<<PORT-NO>>/get_complete_student_list");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setConnectTimeout(10000);
            String send_string = URLEncoder.encode("complete_string","UTF-8")+"="+URLEncoder.encode(this.selected,"UTF-8");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(send_string);
            wr.flush();
            wr.close();
            conn.connect();

            BufferedReader fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            this.a = fromServer.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.a;
    }
}
