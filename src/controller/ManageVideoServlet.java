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
	private static final int PAGE_SIZE = 5; // số video mỗi trang

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. Lấy user từ session (giả sử bạn lưu là "user")
		HttpSession session = request.getSession(false);
		User user = (session != null) ? (User) session.getAttribute("user") : null;
		int authorId;
//		if (user == null) {
//			// Chưa login -> chuyển về login
//			response.sendRedirect(request.getContextPath() + "/login");
//			return;
//		}
//
//		authorId = user.getId();

		if (user == null) {
			// ===========================
			// DEV ONLY – CHO TASK 2
			// Không đụng Task 1, dùng tạm authorId = 1
			// SAU NÀY NHỚ XÓA ĐOẠN NÀY hoặc trả lại redirect
			// ===========================
			authorId = 1; // lấy video của user có id = 1 trong DB
		} else {
			authorId = user.getId();
		}
		// 2. Lấy param page
		int page = 1;
		String pageParam = request.getParameter("page");
		if (pageParam != null) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				page = 1;
			}
		}

		// 3. Gọi BO lấy dữ liệu
		videoBO bo = new videoBO();
		int totalVideos = bo.countVideosByAuthor(authorId);

		int totalPages = (int) Math.ceil(totalVideos * 1.0 / PAGE_SIZE);
		if (totalPages == 0)
			totalPages = 1;
		if (page > totalPages)
			page = totalPages;

		List<Video> videos = bo.getVideosByAuthorWithPaging(authorId, page, PAGE_SIZE);

		// 4. Gửi sang JSP
		request.setAttribute("videos", videos);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("totalVideos", totalVideos);

		RequestDispatcher rd = request.getRequestDispatcher("/ManageVideo.jsp");
		rd.forward(request, response);
	}
}
