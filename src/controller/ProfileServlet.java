package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import helpers.PasswordHelper;
import model.Bean.User;
import model.DAO.userDAO;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private userDAO userDao;
    
    @Override
    public void init() {
        userDao = new userDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Hiển thị trang profile
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");
        
        System.out.println("========================================");
        System.out.println("⭐ ProfileServlet: Updating profile for user " + user.getId());
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        
        // Validate
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên không được để trống!");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email không được để trống!");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra email đã tồn tại chưa (nếu thay đổi email)
        if (!email.equals(user.getEmail())) {
            User existingUser = userDao.getUserByEmail(email);
            if (existingUser != null) {
                request.setAttribute("error", "Email này đã được sử dụng!");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }
        }
        
        // Cập nhật thông tin cơ bản
        user.setName(name);
        user.setEmail(email);
        
        // Nếu muốn đổi mật khẩu
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            System.out.println("🔒 User wants to change password");
            
            // Kiểm tra mật khẩu hiện tại
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập mật khẩu hiện tại!");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }
            
            if (!PasswordHelper.checkPassword(currentPassword, user.getPasswordHash())) {
                request.setAttribute("error", "Mật khẩu hiện tại không đúng!");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }
            
            // Kiểm tra mật khẩu mới và xác nhận
            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "Mật khẩu mới và xác nhận không khớp!");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }
            
            if (newPassword.length() < 6) {
                request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự!");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }
            
            // Hash mật khẩu mới
            String hashedPassword = PasswordHelper.hashPassword(newPassword);
            user.setPasswordHash(hashedPassword);
            
            System.out.println("✅ Password will be updated");
        }
        
        // Cập nhật vào database
        boolean success = userDao.updateUser(user);
        
        if (success) {
            System.out.println("✅ Profile updated successfully!");
            
            // Cập nhật session
            session.setAttribute("user", user);
            
            request.setAttribute("success", "Cập nhật thông tin thành công!");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } else {
            System.out.println("❌ Failed to update profile!");
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại!");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }
        
        System.out.println("========================================");
    }
}