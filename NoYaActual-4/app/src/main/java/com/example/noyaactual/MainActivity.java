package com.example.noyaactual;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.jcraft.jsch.JSch;

import com.jcraft.jsch.Channel;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.*;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ImageButton microphone = null;
    private TextView lightTxt = null;
    private TextView countApi = null;
    private int count = 0;
    private ImageButton lightBtn = null;
    private ImageButton colorBtn = null;
    private ImageButton calendarBtn = null;
    private SeekBar brightnessBtn = null;
    private SeekBar tempBtn = null;
    private Recording recorder;
    private boolean lightStatus = true;
    private boolean onStart = true;
    int brightProgress = 0;
    int useThisBright = 0;
    int tempProgress = 0;
    int useThisTemp = 0;
    private String[] newUI = null;
    private ScheduleManager sR = new ScheduleManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Scheduele.txt");
        if (file.exists()) {
            //Log.d("F1","File Exists");
        } else {
            //Log.d("F2","File Does Not Exist, Making new one");
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        sR.readSchedueled();

        colorBtn = (ImageButton) findViewById(R.id.colorPick);
        microphone = (ImageButton) findViewById(R.id.microphone);
        lightBtn = (ImageButton) findViewById(R.id.lightButton);
        lightBtn.setImageResource(R.drawable.light_off);
        lightTxt = (TextView) findViewById(R.id.lightTxt);
        countApi = (TextView) findViewById(R.id.countApi);
        lightTxt.setText("LIGHT OFF");
        colorBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        brightnessBtn = (SeekBar) findViewById(R.id.brightBar);
        tempBtn = (SeekBar) findViewById(R.id.tempBar);
        calendarBtn = (ImageButton) findViewById(R.id.calendarButton);
        tempBtn.setMin(2700);
        tempBtn.setMax(6500);

        setOnStart(true);
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                newUI = voiceCommandToPi("python IoT/get_initial_state.py");
                setOnStart(false);
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                if(newUI.length > 3) {
                    updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), Integer.parseInt(newUI[3].trim()), Integer.parseInt(newUI[4].trim()));
                }
                else{
                    updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), 0,0);

                }
                    //.d("Boolean", String.valueOf(onStart));
            }
        }.execute(1);

        increaseCount();
        Context context = this;


        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendarMainActivity();
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
                useThisTemp = tempProgress;
                new AsyncTask<Integer, Void, Void>() {
                    @Override
                    protected Void doInBackground(Integer... params) {
                        newUI = voiceCommandToPi("python IoT/tempApi.py " + useThisTemp);
                        //Log.d("Temp", Arrays.toString(newUI));
                        //Log.d("TempVal", String.valueOf(useThisTemp));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void v) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if(newUI.length > 3) {
                            updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), Integer.parseInt(newUI[3].trim()), Integer.parseInt(newUI[4].trim()));
                        }
                        else{
                            updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), 0,0);

                        }
                    }
                }.execute(1);
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

                                //System.out.println(finalRed + " " + finalGreen + " "+ finalBlue);

                                new AsyncTask<Integer, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Integer... params) {
                                        if (finalRed + finalGreen + finalBlue >= 720) {
                                            newUI = voiceCommandToPi("python IoT/colorApi.py " + 255 + " " + 255 + " " + 255);
                                            //Log.d("light on", finalRed + " " + finalGreen + " " + finalBlue + " " + newUI);
                                        } else if (finalRed + finalGreen + finalBlue < 720) {
                                            //Log.d("light on", finalRed + " " + finalGreen + " " + finalBlue + " " + newUI);
                                            newUI = voiceCommandToPi("python IoT/colorApi.py " + finalRed + " " + finalGreen + " " + finalBlue);
                                            return null;
                                        }

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void v) {
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                        if(newUI.length > 3) {
                                            updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), Integer.parseInt(newUI[3].trim()), Integer.parseInt(newUI[4].trim()));
                                        }
                                        else{
                                            updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), 0,0);

                                        }
                                    }
                                }.execute(1);

                            }
                        })
                        .show();
            }
        });


        brightnessBtn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                useThisBright = brightProgress;
                new AsyncTask<Integer, Void, Void>() {
                    @Override
                    protected Void doInBackground(Integer... params) {
                        newUI = voiceCommandToPi("python IoT/brightnessApi.py " + useThisBright);
                        //Log.d("light on", String.valueOf(useThisBright));;
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void v) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if(newUI.length > 3) {
                            updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), Integer.parseInt(newUI[3].trim()), Integer.parseInt(newUI[4].trim()));
                        }
                        else{
                            updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), 0,0);

                        }
                    }
                }.execute(1);
            }
        });

        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightStatus) {
                    lightBtn.setImageResource(R.drawable.light_off);
                    lightBtn.setBackground(null);
                    lightTxt.setText("LIGHT OFF");
                    lightStatus = false;
                    new AsyncTask<Integer, Void, Void>() {
                        @Override
                        protected Void doInBackground(Integer... params) {
                            newUI = voiceCommandToPi("python IoT/lightSwitchApi.py off");
                            //Log.d("light off", Arrays.toString(newUI));
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void v) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            if(newUI.length > 3) {
                                updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), Integer.parseInt(newUI[3].trim()), Integer.parseInt(newUI[4].trim()));
                            }
                            else{
                                updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), 0,0);

                            }
                            colorBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                        }
                    }.execute(1);

                } else if (!lightStatus) {
                    lightBtn.setImageResource(R.drawable.light_on);
                    lightBtn.setBackground(null);
                    lightTxt.setText("LIGHT ON");
                    lightStatus = true;
                    new AsyncTask<Integer, Void, Void>() {
                        @Override
                        protected Void doInBackground(Integer... params) {
                            newUI = voiceCommandToPi("python IoT/lightSwitchApi.py on");
                            //Log.d("light on", Arrays.toString(newUI));
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void v) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            if(newUI.length > 3) {
                                updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), Integer.parseInt(newUI[3].trim()), Integer.parseInt(newUI[4].trim()));
                            }
                            else{
                                updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), 0,0);

                            }
                        }
                    }.execute(1);
                }
            }
        });



        recorder = new Recording();
        microphone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    recorder.startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    recorder.stopRecording();
                    new AsyncTask<Integer, Void, Void>() {
                        @Override
                        protected Void doInBackground(Integer... params) {
                            voiceToPi();
                            newUI = voiceCommandToPi("python IoT/runAll.py");
                            //Log.d("mic", Arrays.toString(newUI));
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void v) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            if(newUI.length > 3) {
                                updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), Integer.parseInt(newUI[3].trim()), Integer.parseInt(newUI[4].trim()));
                            }
                            else{
                                updateUi(newUI[0], Integer.parseInt(newUI[1].trim()), Integer.parseInt(newUI[2].trim()), 0,0);

                            }
                        }
                    }.execute(1);


                }
                return true;
            }
        });

    }

    public void voiceToPi() {

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("noaalk03", "192.168.0.82", 22);
            session.setPassword("noaalk03");
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.put(recorder.getFilePath(), "/home/noaalk03/IoT/");

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }

    }

    public String[] voiceCommandToPi(String command) {
        if(getCount() == 9){
            countApi.setTextColor(Color.RED);
            increaseCount();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            resetCount();
            countApi.setTextColor(Color.BLUE);
        }

        countApi.setText(String.valueOf(getCount()));
        increaseCount();

        String[] updated = null;
        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession("noaalk03", "192.168.0.82", 22);
            session.setPassword("noaalk03");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            String activateAndRunCommand = "source IoT/venv/bin/activate && export GOOGLE_APPLICATION_CREDENTIALS=/home/noaalk03/IoT/service-account.json && " + command + " 2>&1";

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(activateAndRunCommand);

            channel.connect();
            InputStreamReader inputStreamReader = new InputStreamReader(channel.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Thread.sleep(500);
            String line = bufferedReader.readLine();

                if (!onStart) {
                    //values
                    //Log.d("1", line);
                    line = bufferedReader.readLine();
                    //Log.d("Command 1", line);
                    updated = line.split(",");
                    //Log.d("updated cmd 1", Arrays.toString(updated));
                } else if (onStart) {
                /*1 or 2
                if (line.equals("1")) {
                    setGlobalControlUi(1);
                } else if (line.equals("2")) {
                    setGlobalControlUi(2);
                }
                 */

                    //values
                    line = bufferedReader.readLine();
                    //Log.d("Command 2", line);
                    updated = line.split(",");
                }

                channel.disconnect();
                session.disconnect();
                inputStreamReader.reset();
                inputStreamReader.close();
                bufferedReader.reset();
                bufferedReader.close();


            }
        catch(JSchException | IOException | InterruptedException e){
            }
        return updated;
    }

    public void updateUi(String onOff, int brightness, int valueOrRed, int g, int b) {
        String green = "00";
        String blue = "00";
        String red = "00";

        if (onOff.equals("off")) {
            lightBtn.setImageResource(R.drawable.light_off);
            lightBtn.setBackground(null);
            lightTxt.setText("LIGHT OFF");
            lightStatus = false;
            colorBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            brightnessBtn.setProgress(0, true);
            tempBtn.setProgress(0, true);

        } else {
            lightBtn.setImageResource(R.drawable.light_on);
            lightBtn.setBackground(null);
            lightTxt.setText("LIGHT ON");
            lightStatus = true;
            if (valueOrRed < 256 ) {
                if (valueOrRed < 15) {
                    valueOrRed = 0;
                }
                if (g < 15) {
                    g = 0;
                }
                if (b < 15) {
                    b = 0;
                }

                if (valueOrRed == 0) {
                    red = "00";
                } else {
                    red = Integer.toHexString(valueOrRed);
                }
                if (g == 0) {
                    green = "00";
                } else {
                    green = Integer.toHexString(g);
                }
                if (b == 0) {
                    blue = "00";
                } else {
                    blue = Integer.toHexString(b);
                }

                colorBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#" + red + green + blue)));
                tempBtn.setProgress(0, true);
                brightnessBtn.setProgress(brightness, true);

            } else {
                tempBtn.setProgress(valueOrRed, true);
                colorBtn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                brightnessBtn.setProgress(brightness, true);


            }
        }
    }

    public void openCalendarMainActivity() {
        Intent intent = new Intent(this, CalendarMainActivity.class);
        startActivity(intent);
    }

    public void increaseCount(){
        this.count += 1;
    }
    public void resetCount(){
        this.count = 0;
    }
    public int getCount(){
        return this.count;
    }

    public void setOnStart(boolean value){
        this.onStart = value;
    }


}









