package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helpers.PasswordHelper;
import model.Bean.User;
import model.BO.userBO;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/Signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập tên!");
            request.getRequestDispatcher("/Signup.jsp").forward(request, response);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email!");
            request.getRequestDispatcher("/Signup.jsp").forward(request, response);
            return;
        }

        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher("/Signup.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("/Signup.jsp").forward(request, response);
            return;
        }

        User existingUser = userBO.getInstance().getUserByEmail(email);
        if (existingUser != null) {
            request.setAttribute("error", "Email đã được sử dụng!");
            request.getRequestDispatcher("/Signup.jsp").forward(request, response);
            return;
        }

        String hashedPassword = PasswordHelper.hashPassword(password);

        User newUser = new User(name, email, hashedPassword);

        boolean success = userBO.getInstance().createUser(newUser);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("error", "Đăng ký thất bại! Vui lòng thử lại.");
            request.getRequestDispatcher("/Signup.jsp").forward(request, response);
        }
    }
}
