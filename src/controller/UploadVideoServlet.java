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

    private static final String[] ALLOWED_EXTENSIONS = { ".mp4", ".avi", ".mov", ".mkv", ".flv", ".wmv" };
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB

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

        String title = "";
        String description = "";

        try {
            title = request.getParameter("title");
            description = request.getParameter("description");

            Part filePart = null;
            try {
                filePart = request.getPart("videoFile");
            } catch (IllegalStateException e) {

                handleError(request, response,
                        "❌ File quá lớn! Kích thước tối đa cho phép là 100MB. Vui lòng chọn file nhỏ hơn.", title,
                        description);
                return;
            } catch (Exception e) {
                // Kiểm tra message có chứa thông tin về size limit
                String errMsg = e.getMessage();
                if (errMsg != null
                        && (errMsg.toLowerCase().contains("exceeds") || errMsg.toLowerCase().contains("maximum")
                                || errMsg.toLowerCase().contains("size") || e.getClass().getName().contains("SizeLimit")
                                || e.getClass().getName().contains("FileSize"))) {
                    handleError(request, response,
                            "❌ File quá lớn! Kích thước tối đa cho phép là 100MB. Vui lòng chọn file nhỏ hơn.", title,
                            description);
                    return;
                }
                throw e;
            }

            if (filePart == null || filePart.getSize() == 0) {
                handleError(request, response, "⚠️ Vui lòng chọn file video để upload!", title, description);
                return;
            }

            String fileName = getFileName(filePart);
            String fileExtension = getFileExtension(fileName);

            if (!isValidExtension(fileExtension)) {
                handleError(request, response,
                        "⚠️ Định dạng file không hợp lệ! Chỉ chấp nhận: MP4, AVI, MOV, MKV, FLV, WMV", title,
                        description);
                return;
            }

            long fileSize = filePart.getSize();
            if (fileSize > MAX_FILE_SIZE) {
                String errorMsg = String.format(
                        "❌ File quá lớn! Kích thước file: %.2f MB, Giới hạn cho phép: 100 MB. Vui lòng chọn file nhỏ hơn.",
                        fileSize / (1024.0 * 1024.0));
                handleError(request, response, errorMsg, title, description);
                return;
            }

            if (title == null || title.trim().isEmpty()) {
                handleError(request, response, "⚠️ Vui lòng nhập tiêu đề video!", title, description);
                return;
            }

            // Upload video
            boolean isDone = videoBO.getInstance().uploadVideo(authorId, title, description, filePart);

            if (isDone) {
                // Upload thành công - chuyển đến trang quản lý video
                response.sendRedirect(request.getContextPath() + "/manage-video?success=true");
                return;
            }

            // Upload thất bại
            handleError(request, response, "❌ Có lỗi xảy ra trong quá trình tải file. Vui lòng thử lại!", title,
                    description);

        } catch (IllegalStateException e) {
            // Catch lỗi liên quan đến size limit
            e.printStackTrace();
            handleError(request, response,
                    "❌ File quá lớn! Kích thước tối đa cho phép là 100MB. Vui lòng chọn file nhỏ hơn.", title,
                    description);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "❌ Lỗi khi upload video: " + e.getMessage();
            handleError(request, response, errorMsg, title, description);
        }
    }

    /**
     * Xử lý lỗi và trả về trang upload với thông báo lỗi
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage,
            String title, String description) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.setAttribute("title", title);
        request.setAttribute("description", description);
        request.getRequestDispatcher(PathHelper.resolve("UploadVideo")).forward(request, response);
    }

    private String getFileName(Part part) {
        if (part == null) {
            return "";
        }
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) {
            return "";
        }
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "";
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot).toLowerCase();
        }
        return "";
    }

    private boolean isValidExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return false;
        }
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}