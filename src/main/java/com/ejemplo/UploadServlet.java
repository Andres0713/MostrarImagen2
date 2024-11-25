package com.ejemplo;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;

//@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    //private static final String REMOTE_DIR = "ERP/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain"); // Cambia a text/plain para una respuesta más simple
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        String message;

        try (InputStream fileContent = filePart.getInputStream()) {
            uploadFile(fileName, fileContent);
            message = "Archivo subido con éxito: " + fileName;
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            message = "Error al subir el archivo: " + e.getMessage();
        }

        response.getWriter().println(message); // Enviar el mensaje de vuelta
    }

    private void uploadFile(String fileName, InputStream fileContent) throws JSchException, SftpException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(SftpConfig.SFTP_USER, SftpConfig.SFTP_HOST, SftpConfig.SFTP_PORT);
        session.setPassword(SftpConfig.SFTP_PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();

        channelSftp.cd(SftpConfig.REMOTE_DIR);
        channelSftp.put(fileContent, fileName);

        channelSftp.disconnect();
        session.disconnect();
    }
}
