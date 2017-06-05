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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class marks_entry extends AppCompatActivity {
    public static String[] a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_entry);
        final String profname = getIntent().getStringExtra("profname");
        ((TextView) findViewById(R.id.name)).setText(profname);
        Spinner spi = (Spinner) findViewById(R.id.courses);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(marks_entry.this, Login_Page.class);
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
            Toast.makeText(marks_entry.this, "No Courses", Toast.LENGTH_SHORT).show();
        }
        else {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(marks_entry.this, android.R.layout.simple_spinner_dropdown_item, a);
            spi.setAdapter(adapter);
            spi.setVisibility(View.VISIBLE);
            final int[] check = {0};
            final ListView listView = (ListView) findViewById(R.id.all_students);
            final Spinner periodicals = (Spinner) findViewById(R.id.peiodical_no);
            periodicals.setVisibility(View.INVISIBLE);
            final String[] periodical = {"Periodicals 1","Periodicals 2"};
            final MyListAdapter1[] cust_adapter = {null};
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
                        Toast.makeText(marks_entry.this, "No Students!!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String[] list1 = list.split(";");
                        periodicals.setVisibility(View.VISIBLE);
                        cust_adapter[0] = new MyListAdapter1(marks_entry.this,list1);
                        listView.setAdapter(cust_adapter[0]);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(marks_entry.this, "Clicked Nothing!!!", Toast.LENGTH_SHORT).show();
                }
            });
            final int[] position2 = new int[1];
            Spinner perio = (Spinner) findViewById(R.id.peiodical_no);
            perio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    position2[0] = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(marks_entry.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
                }
            });
            Button done = (Button) findViewById(R.id.upload_marks);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String result= null;
                    String name_rollno = null;
                    int flag = 0;
                    for (int i=0;i<listView.getCount();i++){
                        String marks = ((EditText)listView.getChildAt(i).findViewById(R.id.marks)).getText().toString();
                        name_rollno = ((TextView) listView.getChildAt(i).findViewById(R.id.roll_name)).getText().toString();
                        if (Integer.parseInt(marks) > 50){
                            Toast.makeText(marks_entry.this, "Invalid Entry for:" + name_rollno, Toast.LENGTH_SHORT).show();
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        for (int i = 0; i < listView.getCount(); i++) {
                            name_rollno = ((TextView) listView.getChildAt(i).findViewById(R.id.roll_name)).getText().toString();
                            String marks = ((EditText) listView.getChildAt(i).findViewById(R.id.marks)).getText().toString();
                            if (marks.equals(""))
                                marks = "0";
                            try {
                                result = (new Send_Marks_Details(name_rollno, a[position1[0]], periodical[position2[0]], marks).execute().get());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                        if(result.equals("true")){
                            Toast.makeText(marks_entry.this, "Marks Added", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(marks_entry.this,marks_entry.class);
                            i.putExtra("profname",profname);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            });
        }
    }
    private class MyListAdapter1 extends ArrayAdapter<String> {
        String[] a = {};
        Context context;
        public MyListAdapter1(Context context, String[] list) {
            super(context, R.layout.listview, list);
            this.context = context;
            this.a = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.marks_entry,null,true);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.roll_name);

            txtTitle.setText(a[position]);
            return convertView;
        }
    }
}
