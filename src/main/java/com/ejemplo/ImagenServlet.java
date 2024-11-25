package com.ejemplo;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//@WebServlet("/imagen")
public class ImagenServlet extends HttpServlet {
    private static final String REMOTE_FILE_PATH = SftpConfig.REMOTE_DIR+"WhatsApp Image 2024-10-25 at 12.53.31 PM.jpeg";
    String arr[]={
        SftpConfig.REMOTE_DIR+"Presentacion_ServidorArchivos_30_10_2024.jpeg",
        SftpConfig.REMOTE_DIR+"WhatsApp Image 2024-10-25 at 12.53.31 PM.jpeg"
    };
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Descargando...");
        try {
            byte[] imageData = downloadFile(REMOTE_FILE_PATH);
            if (imageData != null) {
                response.setContentType("image/jpeg");
                response.setContentLength(imageData.length);
                try (OutputStream out = response.getOutputStream()) {
                    out.write(imageData);
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] downloadFile(String remoteFilePath) throws JSchException, SftpException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(SftpConfig.SFTP_USER, SftpConfig.SFTP_HOST, SftpConfig.SFTP_PORT);
        session.setPassword(SftpConfig.SFTP_PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        
        

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = channelSftp.get(remoteFilePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        channelSftp.disconnect();
        session.disconnect();

        return outputStream.toByteArray();
    }
}
