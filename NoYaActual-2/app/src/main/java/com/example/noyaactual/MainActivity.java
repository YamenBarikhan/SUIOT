package com.example.noyaactual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jcraft.jsch.JSch;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.*;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.StreamGobbler;


public class MainActivity extends AppCompatActivity {

    private Button microphone = null;
    private ImageView microphone1 = null;
    private Recording recorder;
    public static final int RECORD_AUDIO = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        microphone = (Button) findViewById(R.id.microphone);
        microphone1 = (ImageView) findViewById(R.id.microphone1);


        microphone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                recorder = new Recording();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    recorder.startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    recorder.stopRecording();
                    new AsyncTask<Integer, Void, Void>() {
                        @Override
                        protected Void doInBackground(Integer... params) {
                            voiceToPi();
                            run("python3 /home/noaalk03/IoT/apiTest.py");
                            run("python3 /home/noaalk03/IoT/runAll.py");
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void v) {
                            // Add code to preform actions after doInBackground
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

    public void run (String command) {
        StringBuilder output = new StringBuilder();
        String hostname = "192.168.0.82";
        String username = "noaalk03";
        String password = "noaalk03";

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try
        {
            Connection conn = new Connection(hostname);
            conn.setClient2ServerCiphers(new String[]{"diffie-hellman-group-exchange-sha256"});
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            ch.ethz.ssh2.Session sess = conn.openSession();
            sess.execCommand(command);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
//reads text
            while (true){
                String line = br.readLine(); // read line

                if (line == null)
                    break;
                else{
                    output.append(line);
                }
            }

            /* Show exit status, if available (otherwise "null") */

            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close(); // Close this session
            conn.close();
        }
        catch (IOException e)
        { e.printStackTrace(System.err);
            System.exit(2); }
    }

}
