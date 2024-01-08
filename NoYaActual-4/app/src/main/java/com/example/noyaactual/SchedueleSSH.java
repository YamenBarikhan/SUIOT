package com.example.noyaactual;
import android.os.AsyncTask;
import android.util.Log;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SchedueleSSH {
    public void emptyCommandToPi(String command) throws JSchException {
        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession("noaalk03", "192.168.0.82", 22);
            session.setPassword("noaalk03");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            String runCommand = (command);

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(runCommand);
            //Log.d("CommandSSH1", runCommand);

            channel.connect();

            InputStreamReader inputStreamReader = new InputStreamReader(channel.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "hi";
            while (bufferedReader.readLine() != null) {
                line = bufferedReader.readLine();
                //Log.d("Line", line);
            }


            channel.disconnect();
            session.disconnect();

        } catch (JSchException | IOException e) {
        }
    }




    public void runAsync(String command){
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    emptyCommandToPi(command);
                } catch (JSchException e) {
                    throw new RuntimeException(e);
                }
                Log.d("AsyncSSH1", command);
                return null;
            }
        }.execute(1);
    }
}
