package com.example.noyaactual;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CalendarMainActivity extends AppCompatActivity {


    private Button addButton = null;
    private Button deleteButton = null;
    private Button[] buttons = new Button[7];
    private Boolean buttonChosen = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_main);

        addButton = (Button) findViewById(R.id.addButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendarActivity();
            }
        });

        Intent intent = getIntent();

        String selectedDayText = intent.getStringExtra("selectedDayText");
        Button button1 = findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonChosen) {
                    button1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    buttonChosen = true;

                } else if (buttonChosen) {
                    button1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    buttonChosen = false;
                }
            }
        });

        buttons[0] = button1;

        button1.setText("Empty");
        button1.setBackgroundColor(Color.GRAY);

        try (BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "/Scheduele.txt"))) {
            String firstLine;
            String[] tempAll = null;
            List<String> days = new ArrayList<>();
            List<String> hoursMinutes = new ArrayList<>();
            List<String> brightness = new ArrayList<>();
            List<String> temp = new ArrayList<>();
            List<String> color = new ArrayList<>();
            List<String> state = new ArrayList<>();

            while ((firstLine = reader.readLine()) != null) {
                if (firstLine != null) {
                    tempAll = firstLine.split("#");


                    days.add(tempAll[1]);
                    hoursMinutes.add(tempAll[2]);
                    brightness.add(tempAll[3]);
                    temp.add(tempAll[4]);
                    color.add(tempAll[5]);
                    state.add(tempAll[6]);

                    days = listIterator(days);
                    hoursMinutes = listIterator(hoursMinutes);
                    brightness = listIterator(brightness);
                    temp = listIterator(temp);
                    color = listIterator(color);
                    state = listIterator(state);


                    //Log.d("First Line", firstLine);
                    //Log.d("Days", String.valueOf(days));
                    //Log.d("Hours and minutes", String.valueOf(hoursMinutes));
                    //Log.d("Brightness", String.valueOf(brightness));
                    //Log.d("Temperature", String.valueOf(temp));
                    //Log.d("Color", String.valueOf(color));
                    //Log.d("State", String.valueOf(state));

                    String all = tempAll[0].toString() + " " + days.toString() + " " + hoursMinutes.toString() + " " + brightness.toString() + " " + temp.toString() + " " + color.toString() + " " + state.toString();

                    setButton(all);

                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (selectedDayText != null) {
            button1.setText(selectedDayText);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonChosen){
                    buttonChosen = false;
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Scheduele.txt");
                    try {
                        file.createNewFile();
                        FileWriter writer = new FileWriter(file);
                        writer.write("1#Mon.#00.00#0.#7777.#300.300.300#On");
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    button1.setBackgroundColor(Color.GRAY);
                    button1.setText("Empty");
                }
            }
        });

    }



    public void openCalendarActivity() {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);


    }
    public void setButton(String s) {
        buttons[0].setText(s);
    }

    public List<String> listIterator(List<String> e) {
        List<String> listToReturn = new ArrayList<>();
        for(String element : e){
            String[] tempArray = null;
            tempArray = element.split("\\.");
            listToReturn.addAll(Arrays.asList(tempArray));
        }
        return listToReturn;
    }






}
