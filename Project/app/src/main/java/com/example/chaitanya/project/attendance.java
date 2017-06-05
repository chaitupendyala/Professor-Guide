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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class attendance extends AppCompatActivity {
    public static String[] a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        final String profname = getIntent().getStringExtra("profname");
        ((TextView) findViewById(R.id.name)).setText(profname);
        Spinner spi = (Spinner) findViewById(R.id.courses);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(attendance.this, Login_Page.class);
                startActivity(i);
                finish();
            }
        });
        final EditText date = (EditText) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.DialogFragment d = new DatePickerFragment();
                d.show(getFragmentManager(),"DatePicker");
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
            Toast.makeText(attendance.this, "No Courses", Toast.LENGTH_SHORT).show();
        }
        else {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(attendance.this, android.R.layout.simple_spinner_dropdown_item, a);
            spi.setAdapter(adapter);
            spi.setVisibility(View.VISIBLE);
            final int[] check = {0};
            final ListView listView = (ListView) findViewById(R.id.all_students);
            final MyListAdapter[] cust_adapter = {null};
            final int[] position1 = new int[1];
            spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String list = null;
                    position1[0] = position;
                    try {
                        list = (new Get_Complete_Stud_List(a[position]).execute().get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (list.equals("")) {
                        Toast.makeText(attendance.this, "No Students!!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String[] list1 = list.split(";");
                        cust_adapter[0] = new MyListAdapter(attendance.this,list1);
                        listView.setAdapter(cust_adapter[0]);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(attendance.this, "Clicked Nothing!!!", Toast.LENGTH_SHORT).show();
                }
            });
            Button done = (Button) findViewById(R.id.give_attendance);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String result= null;
                    String name_rollno = null;
                    for (int i = 0; i < listView.getCount(); i++) {
                        name_rollno = ((TextView) listView.getChildAt(i).findViewById(R.id.roll_name)).getText().toString();
                        int selected_id = ((RadioGroup) listView.getChildAt(i).findViewById(R.id.options)).getCheckedRadioButtonId();
                        if (((EditText) findViewById(R.id.date)).getText().toString().equals(""))
                        {
                            Toast.makeText(attendance.this, "Please enter Date!!!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (selected_id != -1) {
                                RadioButton selected = (RadioButton) findViewById(selected_id);
                                String reason = "null";
                                String date = ((EditText) findViewById(R.id.date)).getText().toString();
                                if (selected.getText().toString().equals("OD")) {
                                    reason = ((EditText) listView.getChildAt(i).findViewById(R.id.reason_od)).getText().toString();
                                }
                                try {
                                    result = (new Send_Attendance_Details(name_rollno, reason, selected.getText().toString(), a[position1[0]], date).execute().get());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if(result.equals("true")){
                        Toast.makeText(attendance.this, "Added attendance", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(attendance.this,attendance.class);
                        i.putExtra("profname",profname);
                        startActivity(i);
                        finish();
                    }
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
            convertView = inflater.inflate(R.layout.listview,null,true);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.roll_name);

            txtTitle.setText(a[position]);
            return convertView;
        }
    }
}