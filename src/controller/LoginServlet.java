package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import helpers.PasswordHelper;
import model.BO.userBO;
import model.Bean.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Nếu đã login rồi → redirect về home
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
      
        String redirectUrl = request.getParameter("redirect");
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            HttpSession newSession = request.getSession();
            newSession.setAttribute("redirect_after_login", redirectUrl);
            System.out.println("⭐ Saved redirect URL: " + redirectUrl);
        }
        
       
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        User user = userBO.getInstance().getUserByEmail(email);
        
        if (user == null) {
            request.setAttribute("error", "Email không tồn tại!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        if (!PasswordHelper.checkPassword(password, user.getPasswordHash())) {
            request.setAttribute("error", "Mật khẩu không đúng!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());
        
        if ("on".equals(remember)) {
            session.setMaxInactiveInterval(7 * 24 * 60 * 60); 
        } else {
            session.setMaxInactiveInterval(30 * 60); 
        }
        
        // ⭐ KIỂM TRA CÓ REDIRECT URL KHÔNG (THÊM ĐOẠN NÀY)
        String redirectUrl = (String) session.getAttribute("redirect_after_login");
        
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
        
            session.removeAttribute("redirect_after_login"); 
            response.sendRedirect(redirectUrl);
        } else {
          
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}