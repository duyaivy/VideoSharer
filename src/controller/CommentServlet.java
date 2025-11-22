package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.BO.commentBO;
import model.Bean.Comment;
import model.Bean.User;
import websocket.WatchVideoSocket;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {
        super();
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        int videoId;
        int page = 0;
        int size = 0;
        
        try {
            videoId = Integer.parseInt(request.getParameter("id"));
            page = Integer.parseInt(request.getParameter("page"));
            size = Integer.parseInt(request.getParameter("size"));
            response.sendRedirect(
                    request.getContextPath() + "/watch?id=" + videoId + "&page=" + page + "&size=" + size);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // ⭐ KIỂM TRA ĐĂNG NHẬP
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        
        String video_id = request.getParameter("video_id");
        String message = request.getParameter("message");
        
        try {
            int videoId = Integer.parseInt(video_id);
            Comment c =  commentBO.getInstance().writeComment(userId, videoId, message);
           
            if(c != null) {
                Gson gson = new Gson();
        	    JsonObject json = new JsonObject();
        	    json.addProperty("type", "comment");
        	    json.addProperty("comment_id", c.getCommentId());
        	    json.addProperty("email", c.getUserEmail());
        	    json.addProperty("name", c.getUserName());
        	    json.addProperty("message", c.getMessage());
        	   
        	   String msg =  gson.toJson(json);
        	    
            	WatchVideoSocket.broadcast(video_id, msg);
            	
            }
            response.sendRedirect(request.getContextPath() + "/watch?id=" + videoId);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
    }
}