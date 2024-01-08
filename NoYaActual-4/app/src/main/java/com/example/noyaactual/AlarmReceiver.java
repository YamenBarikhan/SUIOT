package com.example.noyaactual;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.Arrays;

public class AlarmReceiver extends BroadcastReceiver {
    String activateAndRunCommand = "";
    SchedueleSSH ssh = new SchedueleSSH();

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("MyReceiver", "Received intent");
        String state = intent.getStringExtra("state");
        //Log.d("State", state.toString());
        int temperature = intent.getIntExtra("temperature", -1);
        //Log.d("temp", String.valueOf(temperature));
        int brightness = intent.getIntExtra("brightness", -2);
        //Log.d("brightness", String.valueOf(brightness));
        int colorR = intent.getIntExtra("colorR", -3);
        //Log.d("colorR", String.valueOf(colorR));
        int colorG = intent.getIntExtra("colorG", -4);
        //Log.d("colorG", String.valueOf(colorG));
        int colorB = intent.getIntExtra("colorB", -5);
        //Log.d("colorB", String.valueOf(colorB));

        activateAndRunCommand = buildSSHCommand(state, temperature, brightness, colorR, colorG, colorB);
        ssh.runAsync(activateAndRunCommand);
    }

    private String buildSSHCommand(String state, int temperature, int brightness, int colorR, int colorG, int colorB) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("source IoT/venv/bin/activate && export GOOGLE_APPLICATION_CREDENTIALS=/home/noaalk03/IoT/service-account.json && ");

        if (state.equals("On")) {
            if (temperature != 7777) {
                commandBuilder.append("python IoT/tempApi.py ").append(temperature).append(" && ");
            }

            if (brightness != 0) {
                commandBuilder.append("python IoT/brightnessApi.py ").append(brightness).append(" && ");
            }

            if (colorR != 300 && colorG != 300 && colorB != 300) {
                commandBuilder.append("python IoT/colorApi.py ").append(colorR).append(" ").append(colorG).append(" ").append(colorB).append(" && ");
            }
        } else if (state.equals("off")) {
            commandBuilder.append("python IoT/lightSwitchApi.py off && ");
        }
        //Log.d("commandBuilder", commandBuilder.toString().replaceAll(" && $", " 2>&1"));
        return commandBuilder.toString().replaceAll(" && $", " 2>&1");
    }
}