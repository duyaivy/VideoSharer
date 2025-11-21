package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Bean.Video;
import model.DAO.videoDAO;

@WebServlet("/trending")
public class TrendingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private videoDAO videoDao;
    
    @Override
    public void init() {
        videoDao = new videoDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
      
        // Lấy top 20 video có view cao nhất
        List<Video> videos = videoDao.getTrendingVideos(20);
        
    
        request.setAttribute("videos", videos);
        request.setAttribute("isTrendingMode", true); // Đánh dấu đang ở chế độ trending
        
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}