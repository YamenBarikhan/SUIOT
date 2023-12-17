package com.example.noyaactual;

import android.os.AsyncTask;
import android.os.Environment;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FileSender {
    public void sendFile(String filePath) {

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("noaalk03", "192.168.0.82", 22);
            session.setPassword("noaalk03");
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.put(filePath, "/home/noaalk03/IoT/");
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }
    }

    }

