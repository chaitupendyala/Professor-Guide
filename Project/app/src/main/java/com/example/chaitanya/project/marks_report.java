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

public class marks_report extends AppCompatActivity {
    public static String[] a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_report);
        final String profname = getIntent().getStringExtra("profname");
        ((TextView) findViewById(R.id.name)).setText(profname);
        Spinner spi = (Spinner) findViewById(R.id.courses);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(marks_report.this, Login_Page.class);
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
            Toast.makeText(marks_report.this, "No Courses", Toast.LENGTH_SHORT).show();
        }
        else {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(marks_report.this, android.R.layout.simple_spinner_dropdown_item, a);
            spi.setAdapter(adapter);
            spi.setVisibility(View.VISIBLE);
            final int[] check = {0};
            final ListView listView = (ListView) findViewById(R.id.all_students);
            final int[] position1 = new int[1];
            final MyListAdapter[] cust_adapter1 = {null};
            spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String list = null;
                    position1[0] = position;
                    try {
                        list = (new Complete_Mark_Report(a[position]).execute().get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (list.equals("")) {
                        Toast.makeText(marks_report.this, "No Students!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        String[] list1 = list.split(";");
                        cust_adapter1[0] = new MyListAdapter(marks_report.this,list1);
                        listView.setAdapter(cust_adapter1[0]);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(marks_report.this, "Clicked Nothing!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    static class MyListAdapter extends ArrayAdapter<String> {
        String[] a = {};
        Context context;
        public MyListAdapter(Context context, String[] list) {
            super(context, R.layout.marks_report, list);
            this.context = context;
            this.a = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.marks_report,null,true);
            String[] z = this.a[position].split("/");
            TextView txtTitle = (TextView) convertView.findViewById(R.id.roll_name);
            txtTitle.setText(z[0]);
            TextView perio1 = (TextView) convertView.findViewById(R.id.perio_1);
            perio1.setText("Periodical 1:"+z[1]);
            TextView perio2 = (TextView) convertView.findViewById(R.id.perio_2);
            perio2.setText("Periodical 2:"+z[2]);
            return convertView;
        }
    }
}
