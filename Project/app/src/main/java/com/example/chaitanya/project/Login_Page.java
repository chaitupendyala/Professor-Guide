package com.example.chaitanya.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Login_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);
        ImageView bun = (ImageView) findViewById(R.id.imagelogin);
        bun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText) findViewById(R.id.username);
                EditText pass = (EditText) findViewById(R.id.password);
                if (username.getText().toString().equals(""))
                {
                    username.setError("Username cannot be Empty");
                }
                else if (pass.getText().toString().equals(""))
                {
                    pass.setError("Password cannot be Empty");
                }
                else
                {
                    new Validate(username.getText().toString(),pass.getText().toString(),Login_Page.this).execute();
                }
            }
        });
    }
}