package com.example.noyaactual;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleManager {
    private Context context;

    public ScheduleManager(Context context) {
        this.context = context;
    }
    CalendarMainActivity useIterator = new CalendarMainActivity();
    public void createScheduele(int day, int hour, int minute, String state, int temperature, int brightness, int colorR, int colorG, int colorB){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        Calendar calendar = Calendar.getInstance();
        //Sunday is 1, Friday is 6
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        intent.putExtra("state", state);
        intent.putExtra("temperature", temperature);
        intent.putExtra("brightness", brightness);
        intent.putExtra("colorR", colorR);
        intent.putExtra("colorG", colorG);
        intent.putExtra("colorB", colorB);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerTime = calendar.getTimeInMillis();
        if (System.currentTimeMillis() > triggerTime) {
            calendar.add(Calendar.DAY_OF_WEEK, 7);
            triggerTime = calendar.getTimeInMillis();
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }
    public void readSchedueled(){
        try (BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "/Scheduele.txt"))) {
            String dayForUse = "";
            int dayForScheduele = 8;
            int hour = 0;
            int minute = 0;
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

                    days = useIterator.listIterator(days);
                    hoursMinutes = useIterator.listIterator(hoursMinutes);
                    brightness = useIterator.listIterator(brightness);
                    temp = useIterator.listIterator(temp);
                    color = useIterator.listIterator(color);
                    state = useIterator.listIterator(state);

                    dayForUse = days.get(0);
                    if(dayForUse.equals("Mon")){
                        dayForScheduele = 2;
                    }
                    else if (dayForUse.equals("Tue")) {
                        dayForScheduele = 3;
                    }
                    else if (dayForUse.equals("Wed")) {
                        dayForScheduele = 4;
                    }
                    else if (dayForUse.equals("Thu")) {
                        dayForScheduele = 5;
                    }
                    else if (dayForUse.equals("Fri")) {
                        dayForScheduele = 6;
                    }
                    else if (dayForUse.equals("Sat")) {
                        dayForScheduele = 7;
                    }
                    else if (dayForUse.equals("Sun")) {
                        dayForScheduele = 1;
                    }


                    hour = Integer.parseInt(hoursMinutes.get(0));
                    minute = Integer.parseInt(hoursMinutes.get(1));

                    createScheduele(dayForScheduele, hour, minute, state.get(0), Integer.parseInt(temp.get(0)), Integer.parseInt(brightness.get(0)),  Integer.parseInt(color.get(0)), Integer.parseInt(color.get(1)), Integer.parseInt(color.get(2)));
                    //Log.d("state", state.get(0));
                    //Log.d("temp", String.valueOf(Integer.parseInt(temp.get(0))));
                    //Log.d("brightness", String.valueOf(Integer.parseInt(brightness.get(0))));
                    //Log.d("colorR", String.valueOf(Integer.parseInt(color.get(0))));
                    //Log.d("colorG", String.valueOf(Integer.parseInt(color.get(1))));
                    //Log.d("colorB", String.valueOf(Integer.parseInt(color.get(2))));
                    //Log.d("hour", String.valueOf(hour));
                    //Log.d("minute", String.valueOf(minute));

                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
