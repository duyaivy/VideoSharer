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


@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
       
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
     
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    
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
            User existingUser = userBO.getInstance().getUserByEmail(email);
            if (existingUser != null) {
                request.setAttribute("error", "Email này đã được sử dụng!");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }
        }
        
       
        user.setName(name);
        user.setEmail(email);
        
        
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            System.out.println("🔒 User wants to change password");
            
       
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
            
           
            String hashedPassword = PasswordHelper.hashPassword(newPassword);
            user.setPasswordHash(hashedPassword);
            
            System.out.println("✅ Password will be updated");
        }
        
     
        boolean success = userBO.getInstance().updateUser(user);
        
        if (success) {
        
            session.setAttribute("user", user);
            
            request.setAttribute("success", "Cập nhật thông tin thành công!");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } else {
          
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại!");
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }
       
    }
}