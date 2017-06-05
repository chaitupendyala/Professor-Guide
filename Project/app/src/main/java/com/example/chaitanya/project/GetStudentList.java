package com.example.chaitanya.project;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GetStudentList extends AsyncTask<String,String,String>{
    public static String prof_name = null;
    public static String output = null;
    GetStudentList(String prof){
        this.prof_name = prof;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://<<SYSTEM-IP>>:<<PORT-NO>>/get_course_list");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            String send_string = URLEncoder.encode("prof_name","UTF-8")+"="+URLEncoder.encode(this.prof_name,"UTF-8");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(send_string);
            wr.flush();
            wr.close();
            conn.connect();
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            this.output = fromServer.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.output;
    }
}
