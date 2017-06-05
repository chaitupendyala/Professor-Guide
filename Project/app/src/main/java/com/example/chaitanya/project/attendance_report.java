package com.example.chaitanya.project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class attendance_report extends AppCompatActivity {
    public static String[] a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
        final String profname = getIntent().getStringExtra("profname");
        ((TextView) findViewById(R.id.name)).setText(profname);
        Spinner spi = (Spinner) findViewById(R.id.courses);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(attendance_report.this, Login_Page.class);
                startActivity(i);
                finish();
            }
        });
        spi.setVisibility(View.INVISIBLE);
        String s = null;
        try {
            s = (new GetStudentList(profname).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        a = s.split(";");
        if (a.length == 0) {
            Toast.makeText(attendance_report.this, "No Courses", Toast.LENGTH_SHORT).show();
        }
        else {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(attendance_report.this, android.R.layout.simple_spinner_dropdown_item, a);
            spi.setAdapter(adapter);
            spi.setVisibility(View.VISIBLE);
            final int[] check = {0};
            final ListView listView = (ListView) findViewById(R.id.all_students);
            final int[] position1 = new int[1];
            final MyListAdapter[] cust_adapter = {null};
            spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String list = null;
                    position1[0] = position;
                    try {
                        list = (new Get_attendance_report(a[position]).execute().get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (list.equals("")) {
                        Toast.makeText(attendance_report.this, "No Students!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        String[] list1 = list.split(";");
                        cust_adapter[0] = new MyListAdapter(attendance_report.this,list1);
                        listView.setAdapter(cust_adapter[0]);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(attendance_report.this, "Clicked Nothing!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    static class MyListAdapter extends ArrayAdapter<String> {
        String[] a = {};
        Context context;
        public MyListAdapter(Context context, String[] list) {
            super(context, R.layout.listview, list);
            this.context = context;
            this.a = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.attendance_report,null,true);
            String[] z = this.a[position].split("/");
            TextView txtTitle = (TextView) convertView.findViewById(R.id.roll_name);
            txtTitle.setText(z[0]);
            TextView perio1 = (TextView) convertView.findViewById(R.id.att_per);
            perio1.setText(z[1]+"%");
            return convertView;
        }
    }
}
