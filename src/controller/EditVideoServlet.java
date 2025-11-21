package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.BO.videoBO;
import model.Bean.User;
import model.Bean.Video;

@WebServlet("/edit-video")
public class EditVideoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		User user = (session != null) ? (User) session.getAttribute("user") : null;
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String idS = request.getParameter("id");
		if (idS == null) {
			response.sendRedirect(request.getContextPath() + "/manage-video");
			return;
		}

		int id = Integer.parseInt(idS);
		Video v = videoBO.getInstance().getVideoById(id);
		if (v == null || v.getAuthorId() != user.getId()) {
			response.sendRedirect(request.getContextPath() + "/manage-video");
			return;
		}

		request.setAttribute("video", v);
		request.getRequestDispatcher("/EditVideo.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		User user = (session != null) ? (User) session.getAttribute("user") : null;
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		int id = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String des = request.getParameter("description");

		Video v = videoBO.getInstance().getVideoById(id);

		if (v == null || v.getAuthorId() != user.getId()) {
			response.sendRedirect(request.getContextPath() + "/manage-video");
			return;
		}

		videoBO.getInstance().updateBasicInfo(id, title, des);
		response.sendRedirect(request.getContextPath() + "/manage-video");
	}
}
