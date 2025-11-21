package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.BO.videoBO;
import model.Bean.User;
import model.Bean.Video;

@WebServlet("/manage-video")
public class ManageVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int PAGE_SIZE = 5; 

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		HttpSession session = request.getSession(false);
		User user = (session != null) ? (User) session.getAttribute("user") : null;
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
	
		int authorId = user.getId();

		
		int page = 1;
		String pageParam = request.getParameter("page");
		if (pageParam != null) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				page = 1;
			}
		}

		
		int totalVideos = videoBO.getInstance().countVideosByAuthor(authorId);

		int totalPages = (int) Math.ceil(totalVideos * 1.0 / PAGE_SIZE);
		if (totalPages == 0)
			totalPages = 1;
		if (page > totalPages)
			page = totalPages;

		List<Video> videos = videoBO.getInstance().getVideosByAuthorWithPaging(authorId, page, PAGE_SIZE);

		
		request.setAttribute("videos", videos);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("totalVideos", totalVideos);

		RequestDispatcher rd = request.getRequestDispatcher("/ManageVideo.jsp");
		rd.forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
