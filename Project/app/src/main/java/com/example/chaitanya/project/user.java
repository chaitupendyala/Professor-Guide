package com.example.chaitanya.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        final String profname = getIntent().getStringExtra("username");
        ((TextView)findViewById(R.id.name)).setText(profname);
        Button b = (Button) findViewById(R.id.logout);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this,Login_Page.class);
                startActivity(i);
                finish();
            }
        });
        Button list = (Button) findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this,students.class);
                i.putExtra("professor_name",profname);
                startActivity(i);
            }
        });
        final Button attendance = (Button) findViewById(R.id.attendance);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this,attendance.class);
                i.putExtra("profname",profname);
                startActivity(i);
            }
        });
        Button marks = (Button) findViewById(R.id.marks);
        marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this,marks_entry.class);
                i.putExtra("profname",profname);
                startActivity(i);
            }
        });
        final Button marks_report = (Button) findViewById(R.id.report);
        marks_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this,marks_report.class);
                i.putExtra("profname",profname);
                startActivity(i);
            }
        });
        Button att_report = (Button) findViewById(R.id.attendance_report);
        att_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this,attendance_report.class);
                i.putExtra("profname",profname);
                startActivity(i);
            }
        });
    }
}
