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
        
        System.out.println("========================================");
        System.out.println("⭐ HomeServlet: Loading videos...");
        
        List<Video> videos = videoDao.getLatestVideos(20);
        
        System.out.println("⭐ Total videos: " + (videos != null ? videos.size() : "NULL"));
        
        if (videos != null && videos.size() > 0) {
            for (Video v : videos) {
                System.out.println("---");
                System.out.println("ID: " + v.getVideoId());
                System.out.println("Title: " + v.getTitle());
                System.out.println("Img: " + v.getImg());
                System.out.println("Img is null: " + (v.getImg() == null));
                System.out.println("Img is empty: " + (v.getImg() != null && v.getImg().isEmpty()));
            }
        }
        
        System.out.println("========================================");
        
        request.setAttribute("videos", videos);
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}