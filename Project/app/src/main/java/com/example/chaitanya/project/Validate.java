package com.example.chaitanya.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.List;

public class Validate extends AsyncTask<String, String, String> {
    public static String user_name = null;
    public static String pass = null;
    public static String result = null;
    private Context context;
    public Validate(String username,String password,Context context){
        this.user_name = username;
        this.pass = password;
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        // write show progress Dialog code here
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        if (this.result.equals("true"))
        {
            Intent i = new Intent(context,user.class);
            i.putExtra("username",this.user_name);
            context.startActivity(i);
            ((Activity)context).finish();
        }
        else
        {
            Toast.makeText(context, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected String doInBackground(String... par) {
        try {
            URL url = new URL("http://<<SYSTEM-IP>>:<<PORT-NO>>/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setConnectTimeout(10000);
            String send_string = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(this.user_name,"UTF-8");
            send_string += "&" + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(this.pass,"UTF-8");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(send_string);
            wr.flush();
            wr.close();
            conn.connect();

            BufferedReader fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            this.result = fromServer.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getQuery(List<AbstractMap.SimpleEntry> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (AbstractMap.SimpleEntry pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode((String) pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) pair.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
