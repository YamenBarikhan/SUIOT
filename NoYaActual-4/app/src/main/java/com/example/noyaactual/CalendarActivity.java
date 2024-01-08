package com.example.noyaactual;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {

    private SeekBar tempBtn = null;
    private TimePicker timePicker = null;
    private SeekBar brightBtn = null;
    private ImageButton colorBtn = null;
    private ImageButton lightBtn = null;
    private TextView lightTxt = null;

    private TextView calendarTxt = null;
    private Button selectedDayButton = null;
    private Button bookBtn = null;
    private Button[] dayButtons = new Button[7];
    File schedueleFile = new File(Environment.getExternalStorageDirectory() + "/Scheduele.txt");
    private HashMap<CharSequence, Boolean> isClickedMap = new HashMap<CharSequence, Boolean>();
    private String chosenDay = "";
    private int chosenTemp = 0;
    private int chosenBright = 0;
    private String chosenColor = "";
    private String chosenState = "";
    private String finalString = "";
    private int tempProgress = 0;
    private int brightProgress = 0;
    private boolean lightStatus = true;
    private CalendarMainActivity numberTemp = new CalendarMainActivity();
    private int numberForUsage = 0;
    private ScheduleManager sR = new ScheduleManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);
        Context context = this;
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        calendarTxt = (TextView) findViewById(R.id.calendarTxt);
        lightTxt = (TextView) findViewById(R.id.lightTxt);
        brightBtn = (SeekBar) findViewById(R.id.brightBar);
        tempBtn = (SeekBar) findViewById(R.id.tempBar);
        colorBtn = (ImageButton) findViewById(R.id.colorPick);
        lightBtn = (ImageButton) findViewById(R.id.lightButtonOn);



        timePicker.setIs24HourView(true);

        Button monBtn = findViewById(R.id.mondayButton);
        Button TueBtn = findViewById(R.id.tuesdayButton);
        Button WedBtn = findViewById(R.id.wednesdayButton);
        Button ThuBtn = findViewById(R.id.thursdayButton);
        Button FriBtn = findViewById(R.id.fridayButton);
        Button SatButn = findViewById(R.id.saturdayButton);
        Button SunBtn = findViewById(R.id.sundayButton);

        dayButtons[0] = monBtn;
        dayButtons[1] = TueBtn;
        dayButtons[2] = WedBtn;
        dayButtons[3] = ThuBtn;
        dayButtons[4] = FriBtn;
        dayButtons[5] = SatButn;
        dayButtons[6] = SunBtn;

        isClickedMap.put("Mon", false);
        isClickedMap.put("Tue", false);
        isClickedMap.put("Wed", false);
        isClickedMap.put("Thu", false);
        isClickedMap.put("Fri", false);
        isClickedMap.put("Sat", false);
        isClickedMap.put("Sun", false);

        chosenBright = 50;
        brightBtn.setProgress(50, true);
        chosenTemp = 2700;
        chosenColor = "300.300.300";

        for (Button dayButton : dayButtons) {
            dayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isClickedMap.get(dayButton.getText())) {
                        dayButton.setBackgroundColor(Color.GREEN);
                        isClickedMap.put(dayButton.getText(), true);
                        chosenDay += dayButton.getText() + ".";
                        //Log.d("DayAdd", chosenDay);
                    } else {
                        dayButton.setBackgroundColor(Color.RED);
                        isClickedMap.put(dayButton.getText(), false);
                        chosenDay = chosenDay.replace(dayButton.getText() + ".", "");
                        //Log.d("DayRemove", chosenDay);
                    }
                }
            });
        }

        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightStatus) {
                    lightBtn.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    lightTxt.setText("Turn Off");
                    lightStatus = false;

                } else if (!lightStatus) {
                    lightBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    lightTxt.setText("Turn On");
                    lightStatus = true;
                }
            }
        });

        tempBtn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempProgress = progress;
                tempBtn.setMin(2700);
                tempBtn.setMax(6500);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                chosenTemp = tempProgress;

            }
        });

        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new ColorPickerDialog
                        .Builder(context)
                        .setTitle("               Pick Desired Color")
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(R.color.white)
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NotNull String colorHex) {


                                colorBtn.setBackgroundTintList(ColorStateList.valueOf(color));

                                String red = colorHex.substring(1, 3);
                                String green = colorHex.substring(3, 5);
                                String blue = colorHex.substring(5, 7);

                                int finalRed = Integer.parseInt(red, 16);
                                int finalGreen = Integer.parseInt(green, 16);
                                int finalBlue = Integer.parseInt(blue, 16);

                                tempBtn.setProgress(0, true);
                                chosenTemp = 7777;

                                chosenColor = finalRed + "." + finalGreen + "." + finalBlue;


                            }
                        })
                        .show();
            }
        });


        brightBtn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                chosenBright = brightProgress;
            }
        });


        bookBtn = findViewById(R.id.bookBtn);

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chosenDay.isEmpty()) {
                    if (chosenTemp > 2699 && chosenTemp < 6800) {
                        chosenColor = "300.300.300";
                    } else {
                        chosenTemp = 7777;
                    }
                    if (lightStatus) {
                        chosenState = "On";
                    } else {
                        chosenState = "Off";
                    }

                    //Log.d("NumUsage", String.valueOf(numberForUsage));

                    finalString += 1 + "#" + chosenDay + "#" + timePicker.getHour() + "." + timePicker.getMinute() + "#" + chosenBright + "." + "#" + chosenTemp + "." + "#" + chosenColor + "#" + chosenState;
                    //Log.d("Final", finalString);

                    writeToFile(finalString);

                    Intent intent = new Intent(CalendarActivity.this, CalendarMainActivity.class);

                    startActivity(intent);

                    sR.readSchedueled();


                } else {
                    //Log.d("Empty Warning", "No Chosen Day!");
                }
            }

        });
    }




    private void setSelectedDay(Button dayButton) {
        selectedDayButton = dayButton;
    }

    private void writeToFile(String data) {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Scheduele.txt");
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(data);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}