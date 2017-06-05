package com.example.chaitanya.project;

import android.content.Intent;
import android.media.Image;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;


public class students extends AppCompatActivity {
    public static String[] a;
    public static String[] students;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        final String profname = getIntent().getStringExtra("professor_name");
        ((TextView) findViewById(R.id.name)).setText(profname);
        Spinner spi = (Spinner) findViewById(R.id.courses);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(students.this,Login_Page.class);
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
        if (a.length == 0)
        {
            Toast.makeText(students.this, "No Courses", Toast.LENGTH_SHORT).show();
        }
        else
        {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(students.this,android.R.layout.simple_spinner_dropdown_item,a);
            spi.setAdapter(adapter);
            spi.setVisibility(View.VISIBLE);
            final int[] check = {0};
            final ListView listView = (ListView) findViewById(R.id.students_listview);
            final ListAdapter[] adapter1 = new ListAdapter[1];
            spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String list = null;
                    try {
                        list = (new Get_Complete_Stud_List(a[position]).execute().get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (list.equals("")) {
                        Toast.makeText(students.this, "No Students!!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String[] list1 = list.split(";");
                        students = list1;
                        adapter1[0] = new ArrayAdapter<String>(students.this, android.R.layout.simple_expandable_list_item_1, list1);
                        listView.setAdapter(adapter1[0]);
                        listView.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(students.this, "Clicked Nothing!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        ImageButton save = (ImageButton) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + profname);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] data1={1,1,0,0};
                //write the bytes in file
                if(file.exists())
                {
                    OutputStream fo = null;
                    try {
                        fo = new FileOutputStream(file);
                        for (String i:students)
                            fo.write(i.getBytes());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(students.this, "Data Written to:" + Environment.getExternalStorageDirectory() + File.separator + profname, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
