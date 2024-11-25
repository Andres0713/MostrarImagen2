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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//@WebServlet("/pdf")
public class PdfServlet extends HttpServlet {

    public static final String REMOTE_FILE_PATH = "CalendarioEneroJunio2024.pdf";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String fileName = request.getParameter(REMOTE_FILE_PATH);
//        if (fileName == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro 'fileName'");
//            return;
//        }
        String remoteFilePath = REMOTE_FILE_PATH;
        response.setContentType("application/pdf"); // Tipo de contenido para PDF
        try (InputStream inputStream = downloadFile(remoteFilePath); OutputStream outputStream = response.getOutputStream()) {

//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }

            //
            //CODIGO JSP 
            
            int tamanoInput = inputStream.available();
            byte[] datosPDF = new byte[tamanoInput];
            inputStream.read(datosPDF, 0, tamanoInput);
            response.setHeader("Content-disposition","inline; filename=instalacion_tomcat.pdf" );
response.setContentType("application/pdf");
response.setContentLength(tamanoInput);
            response.getOutputStream().write(datosPDF);
            
            inputStream.close();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al descargar el archivo: " + e.getMessage());
        }
    }

    private InputStream downloadFile(String fileName) throws JSchException, SftpException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(SftpConfig.SFTP_USER, SftpConfig.SFTP_HOST, SftpConfig.SFTP_PORT);
        session.setPassword(SftpConfig.SFTP_PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();

        channelSftp.cd(SftpConfig.REMOTE_DIR);
        InputStream inputStream = channelSftp.get(fileName);

        channelSftp.disconnect();
        session.disconnect();

        return inputStream;
    }
}
