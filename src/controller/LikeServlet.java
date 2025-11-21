package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.BO.likeBO;

/**
 * Servlet implementation class LikeServlet
 */
@WebServlet("/api/like")
public class LikeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LikeServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    
	    
	    
	    String user_id = request.getParameter("user_id");
	    String video_id = request.getParameter("video_id");
	    String type = request.getParameter("type");
	    int likeCount = 0;
	    int disLikeCount = 0;
	    boolean status = false;
	    String message = "";
	    
	    try {
	        int u_id = Integer.parseInt(user_id);
	        int v_id = Integer.parseInt(video_id);
	       
	        if ("like".equals(type)) {
	            status = likeBO.getInstance().likeVideo(v_id, u_id);
	            likeCount = likeBO.getInstance().getLikeCountByVideoId(v_id);
	            disLikeCount = likeBO.getInstance().getLikeCountByVideoId(v_id);
	            
	        } else if ("dislike".equals(type)) {
	            status = likeBO.getInstance().disLikeVideo(v_id, u_id);
	        }
	        
	        message = status ? "Thành công! " : "Thất bại! Bạn đã thực hiện hành động này rồi";
	        
	    } catch (Exception e) {
	        status = false;
	        message = "Lỗi: " + e.getMessage();
	        e.printStackTrace();
	    }
	    
	    Gson gson = new Gson();
	    JsonObject json = new JsonObject();
	    json.addProperty("status", status);
	    json.addProperty("message", message);
	    json.addProperty("likeCount", likeCount);
	    json.addProperty("disLikeCount", disLikeCount);
	    response.getWriter().write(gson.toJson(json));
	}

}
