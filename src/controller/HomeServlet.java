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

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private videoDAO videoDao;
    
    @Override
    public void init() {
        videoDao = new videoDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // KHÔNG check session - ai cũng xem được
        
        // Lấy danh sách video mới nhất (20 video)
        List<Video> videos = videoDao.getLatestVideos(20);
        
        // Truyền data sang JSP
        request.setAttribute("videos", videos);
        
        // Forward sang home.jsp
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}