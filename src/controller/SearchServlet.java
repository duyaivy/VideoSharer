package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Bean.Video;
import model.BO.videoBO;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("q");
        String pageS = request.getParameter("page");
        String sizeS = request.getParameter("size");

        if (keyword == null || keyword.trim().isEmpty()) {
            // Không có keyword → Redirect về home
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        int page = 1;
        int size = 20;

        if (pageS != null && !pageS.isEmpty()) {
            try {
                page = Integer.parseInt(pageS);
            } catch (NumberFormatException ignore) {
            }
        }

        if (sizeS != null && !sizeS.isEmpty()) {
            try {
                size = Integer.parseInt(sizeS);
            } catch (NumberFormatException ignore) {
            }
        }

        // Tìm kiếm video
        List<Video> videos = videoBO.getInstance().searchVideos(keyword.trim(), page, size);

        // Tính totalPages
        int totalVideos = videoBO.getInstance().countSearchVideos(keyword.trim());
        int totalPages = (int) Math.ceil((double) totalVideos / size);

        System.out.println("✅ Found " + (videos != null ? videos.size() : 0) + " videos");
        System.out.println("========================================");

        // ⭐ FORWARD VỀ home.jsp với kết quả search
        request.setAttribute("videos", videos);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("isSearchMode", true); // Đánh dấu đang ở chế độ search

        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}