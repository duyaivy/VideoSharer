package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String UPLOADS_DIR = "D:\\ECLIPSE2020\\VideoSharer\\WebContent\\uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        // VD: /original/video_40/thumb.jpg

        System.out.println("⭐ ImageServlet requested: " + pathInfo);

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(404);
            return;
        }

        String relativePath = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;

        Path filePath = Paths.get(UPLOADS_DIR, relativePath);
        File requestedFile = filePath.toFile();

        System.out.println("📁 Full path: " + requestedFile.getAbsolutePath());
        System.out.println("📁 File exists: " + requestedFile.exists());

        if (!requestedFile.exists() || requestedFile.isDirectory()) {
            System.out.println("file not found!");
            response.sendError(404);
            return;
        }

        String fileName = requestedFile.getName().toLowerCase();
        String contentType;

        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.endsWith(".gif")) {
            contentType = "image/gif";
        } else if (fileName.endsWith(".webp")) {
            contentType = "image/webp";
        } else if (fileName.endsWith(".mp4")) {
            contentType = "video/mp4";
        } else if (fileName.endsWith(".m3u8")) {
            contentType = "application/vnd.apple.mpegurl";
        } else if (fileName.endsWith(".ts")) {
            contentType = "video/MP2T";
        } else {
            contentType = "application/octet-stream";
        }

        response.setContentType(contentType);
        response.setContentLengthLong(requestedFile.length());
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Cache-Control", "public, max-age=31536000");

        try (FileInputStream in = new FileInputStream(requestedFile);
                OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

    }
}