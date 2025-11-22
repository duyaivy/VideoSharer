package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import helpers.ViewPath;
import model.BO.commentBO;
import model.BO.likeBO;
import model.BO.videoBO;
import model.Bean.Comment;
import model.Bean.Video;

@WebServlet("/watch")
public class WatchVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WatchVideoServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String params = request.getParameter("id");
		String pageS = request.getParameter("page");
		String sizeS = request.getParameter("size");
		if (params == null || params.isEmpty()) {
			response.sendRedirect(request.getContextPath() + ViewPath.resolve("Login"));
			return;
		}

	

		try {
			int videoId = Integer.parseInt(params);

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

			Video vd = videoBO.getInstance().watchVideo(videoId);
			if (vd == null) {
				request.setAttribute("message", "Video không tồn tại!");
				request.getRequestDispatcher("/Error.jsp").forward(request, response);
				return;
			}

			ArrayList<Video> vdList = videoBO.getInstance().getVideoTrending(page, size);

			ArrayList<Comment> commentList = commentBO.getInstance().getCommentByVideoId(vd.getVideoId(), page, size);
			
			
			int likeCount = likeBO.getInstance().getLikeCountByVideoId(vd.getVideoId());
			request.setAttribute("like_count", likeCount);
			int disLikeCount = likeBO.getInstance().getDisLikeCountByVideoId(vd.getVideoId());
			request.setAttribute("dislike_count", disLikeCount);
			request.setAttribute("video", vd);
			request.setAttribute("video_list", vdList);
			request.setAttribute("comment_list", commentList);
			request.getRequestDispatcher(ViewPath.resolve("WatchVideo")).forward(request, response);

		} catch (NumberFormatException e) {
			request.setAttribute("message", "ID video không hợp lệ!");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
