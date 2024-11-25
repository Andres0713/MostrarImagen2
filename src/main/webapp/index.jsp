<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Mostrar Imagen</title>
</head>
<body>
    <h1>Imagen desde el servidor!!!</h1>
    <img src="imagen" alt="Imagen no disponible"/>
    <h1>Subir Archivos al Servidor SFTP</h1>
    <form action="upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" required>
        <input type="submit" value="Subir Archivo">
    </form>
    <a href="index.jsp">Volver al Inicio</a>
    <h1>Documento PDF desde el Servidor de Archivos</h1>
    <!-- El iframe muestra el archivo PDF -->
    <iframe src="pdf" width="100%" height="600px"></iframe>
</body>
</html>
