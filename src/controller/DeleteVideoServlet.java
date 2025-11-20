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

@WebServlet("/delete-video")
public class DeleteVideoServlet extends HttpServlet {

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
		if (idS != null) {
			int id = Integer.parseInt(idS);
			videoBO bo = new videoBO();
			bo.softDeleteVideo(id, user.getId());
		}

		response.sendRedirect(request.getContextPath() + "/manage-video");
	}
}
