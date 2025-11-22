package controller;

import model.BO.videoBO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import model.Bean.User;
import helpers.PathHelper;

@WebServlet("/upload-video")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 100, // 100MB
        maxRequestSize = 1024 * 1024 * 100 // 100MB
)
public class UploadVideoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String[] ALLOWED_EXTENSIONS = {
            ".mp4", ".avi", ".mov", ".mkv", ".flv", ".wmv"
    };
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
          
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher(PathHelper.resolve("UploadVideo")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int authorId = user.getId();

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            Part filePart = request.getPart("videoFile");

            String fileName = getFileName(filePart);
            String fileExtension = getFileExtension(fileName);

            if (!isValidExtension(fileExtension)) {
                request.setAttribute("error",
                        "Định dạng file không hợp lệ! Chỉ chấp nhận: MP4, AVI, MOV, MKV, FLV, WMV");
                request.getRequestDispatcher(PathHelper.resolve("UploadVideo")).forward(request, response);
                return;
            }

            long fileSize = filePart.getSize();
            if (fileSize > MAX_FILE_SIZE) {
                request.setAttribute("error", "File quá lớn! Kích thước tối đa: 100MB");
                request.getRequestDispatcher(PathHelper.resolve("UploadVideo")).forward(request, response);
                return;
            }

            boolean isDone = videoBO.getInstance().uploadVideo(authorId, title, description, filePart);

            if (isDone) {
            	
                request.getRequestDispatcher("/manage-video").forward(request, response);
                return;
            }

            request.setAttribute("error", "Có lỗi trong lúc tải file");
            request.getRequestDispatcher(PathHelper.resolve("UploadVideo")).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi upload video: " + e.getMessage());
            request.getRequestDispatcher(PathHelper.resolve("UploadVideo")).forward(request, response);
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "";
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot).toLowerCase();
        }
        return "";
    }

    private boolean isValidExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}