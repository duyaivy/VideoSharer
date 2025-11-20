package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.BO.commentBO;
import model.Bean.Comment;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
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

			response.sendRedirect(request.getContextPath() + "/Home.jsp");
			return;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String video_id = request.getParameter("video_id");

		String user_id = request.getParameter("user_id");

		String message = request.getParameter("message");
		try {
			int videoId = Integer.parseInt(video_id);
			int userId = Integer.parseInt(user_id);
		
			commentBO.getInstance().writeComment(userId, videoId, message);
			response.sendRedirect(
					request.getContextPath() + "/watch?id=" + videoId);
			return;

		} catch (Exception e) {

			e.printStackTrace();

			response.sendRedirect(
					request.getContextPath() + "/Home.jsp");
			return;
		}
	
	}

}
