package com.rperez.myapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This is the activity that displays all stored records in a listview.
 * Also implemented is an "add record" floating action button which starts
 * the ViewRecordActivity viewer/editor, along with tapping an existing record.
 */
public class ViewRecordsList extends AppCompatActivity {

    private static final String FILENAME = "file.sav";

    private ListView oldPersonsList;
    private ArrayAdapter<Person> adapter;
    private ArrayList<Person> personList;
    private TextView recordsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addRecord = (FloatingActionButton) findViewById(R.id.new_record);
        oldPersonsList = (ListView) findViewById(R.id.record_list);
        recordsCount = (TextView) findViewById(R.id.record_count);
        loadFromFile();

        recordsCount.setText(String.valueOf(personList.size()));

        /**
         * Action when a record in the record list is clicked. The position of the
         * clicked record is passed to  ViewRecordActivity to be viewed with the option
         * to edit
         */
        oldPersonsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(ViewRecordsList.this, ViewRecordActivity.class);
                intent1.putExtra("record_position", position);
                startActivity(intent1);
            }
        });


        /**
         * On clicking the floating action button, Create a new record.
         * Here, -1 is passed as an intent for the record's position
         * to signal that a new record should be instantiated instead of
         * editing existing ones
         */
        addRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent2 = new Intent(ViewRecordsList.this, ViewRecordActivity.class);
                intent2.putExtra("record_position", -1);
                startActivity(intent2);


            }
        });

    }


    @Override
    /**
     * on start, refresh record list
     */
    protected void onStart() {
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<Person>(this,
                R.layout.list_item, personList);
        oldPersonsList.setAdapter(adapter);
    }

    /**
     * on resume, refresh record list
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile();
        recordsCount.setText(String.format(Locale.getDefault(),"%d",personList.size()));
        adapter.notifyDataSetChanged();
    }



    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            //Taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2017-01-24 18:19PM
            Type listType = new TypeToken<ArrayList<Person>>(){}.getType();
            personList = gson.fromJson(in, listType);


        } catch (FileNotFoundException e) {
            personList = new ArrayList<Person>();

        }
    }
}
