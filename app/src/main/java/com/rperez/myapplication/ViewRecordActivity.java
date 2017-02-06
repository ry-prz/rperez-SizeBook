package com.rperez.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;


public class ViewRecordActivity extends AppCompatActivity {
    private ArrayList<Person> personList;
    private static final String FILENAME = "file.sav";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * Prevent soft keyboard popping up automatically
         * code from:
         * http://stackoverflow.com/questions/9732761/how-to-avoid-automatically-appear-android-keyboard-when-activity-start
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_new_record);
        final int recordPosition = getIntent().getExtras().getInt("record_position");
        Button saveButton = (Button) findViewById(R.id.record_add_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                loadFromFile();

                Person person;
                EditText nameText = (EditText) findViewById(R.id.name);
                EditText neckText = (EditText) findViewById(R.id.neck);
                EditText commentText = (EditText) findViewById(R.id.record_comment);
                EditText inseamText = (EditText) findViewById(R.id.inseam);
                EditText hipText = (EditText) findViewById(R.id.hip);
                EditText waistText = (EditText) findViewById(R.id.waist);
                EditText bustText = (EditText) findViewById(R.id.bust);
                EditText chestText = (EditText) findViewById(R.id.chest);
                EditText dateText = (EditText) findViewById(R.id.date);


                if (!nameText.getText().toString().isEmpty()) {
                    if (recordPosition!= -1)
                        person = personList.get(recordPosition);
                    else
                        person = new Person(nameText.getText().toString());

                    if (!neckText.getText().toString().isEmpty()) {
                        float neck = Float.valueOf(neckText.getText().toString());
                        person.setNeck(neck);
                    }
                    if (!bustText.getText().toString().isEmpty()) {
                        float bust = Float.valueOf(bustText.getText().toString());
                        person.setBust(bust);
                    }

                    if (!chestText.getText().toString().isEmpty()) {
                        float chest = Float.valueOf(chestText.getText().toString());
                        person.setChest(chest);
                    }

                    if (!waistText.getText().toString().isEmpty()) {
                        float waist = Float.valueOf(waistText.getText().toString());
                        person.setWaist(waist);
                    }

                    if (!hipText.getText().toString().isEmpty()) {
                        float hip = Float.valueOf(hipText.getText().toString());
                        person.setHip(hip);
                    }

                    if (!inseamText.getText().toString().isEmpty()) {
                        float inseam = Float.valueOf(inseamText.getText().toString());
                        person.setInseam(inseam);
                    }

                    String date = dateText.getText().toString();
                    person.setDate(date);

                    String comment = commentText.getText().toString();
                    person.setComment(comment);
                    if (recordPosition== -1)
                        personList.add(person);
                    saveInFile();
                    finish();

                } else {
                        Toast.makeText(getApplicationContext(), "Name is required", Toast.LENGTH_SHORT).show();

                }
            }
        });
        Button deleteButton = (Button) findViewById(R.id.record_delete_button);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        loadFromFile();
        int recordPosition = getIntent().getExtras().getInt("record_position");

        if (recordPosition != -1) {
            Person person = personList.get(recordPosition);
            EditText edit = (EditText) findViewById(R.id.name);
            edit.setText(personList.get(recordPosition).getName());

            if (person.getNeck() != 0f) {
                edit = (EditText) findViewById(R.id.neck);
                edit.setText(String.format(Locale.getDefault(),
                        "%.1f", personList.get(recordPosition).getNeck()));
            }

            if (person.getBust() != 0f) {
                edit = (EditText) findViewById(R.id.bust);
                edit.setText(String.format(Locale.getDefault(),
                        "%.1f", person.getBust()));
            }

            if (person.getChest() != 0f) {
                edit = (EditText) findViewById(R.id.chest);
                edit.setText(String.format(Locale.getDefault(),
                        "%.1f", person.getChest()));
            }

            if (person.getWaist() != 0f) {
                edit = (EditText) findViewById(R.id.waist);
                edit.setText(String.format(Locale.getDefault(),
                        "%.1f", person.getWaist()));
            }

            if (person.getHip() != 0f) {
                edit = (EditText) findViewById(R.id.hip);
                edit.setText(String.format(Locale.getDefault(),
                        "%.1f", person.getHip()));
            }

            if (person.getInseam() != 0f) {
                edit = (EditText) findViewById(R.id.inseam);
                edit.setText(String.format(Locale.getDefault(),
                        "%.1f", person.getInseam()));
            }

            if (!person.getComment().isEmpty()) {
                edit = (EditText) findViewById(R.id.record_comment);
                edit.setText(person.getComment());
            }

            if (!person.getDate().isEmpty()) {
                edit = (EditText) findViewById(R.id.date);
                edit.setText(personList.get(recordPosition).getDate());
            }



        }

    }


    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            //Taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2017-01-24 18:19PM
            Type listType = new TypeToken<ArrayList<Person>>() {
            }.getType();
            personList = gson.fromJson(in, listType);


        } catch (FileNotFoundException e) {
            personList = new ArrayList<Person>();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(personList, out);
            out.flush();


            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Handle the Exception properly later
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    //http://stackoverflow.com/questions/11740311/android-confirmation-message-for-delete
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Confirm Deletion")
                .setMessage("Delete this record?")
                //.setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();
                        int s = getIntent().getExtras().getInt("record_position");
                        personList.remove(s);
                        saveInFile();
                        finish();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
