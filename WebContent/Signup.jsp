<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - VideoSharer</title>
    <%-- ⭐ QUAN TRỌNG: Link đến file CSS --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-box">
            <h1>🎬 VideoSharer</h1>
            <h2>Đăng ký tài khoản</h2>
            
            <%-- Hiển thị lỗi --%>
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <form action="${pageContext.request.contextPath}/signup" method="POST">
                <div class="form-group">
                    <label for="name">Tên của bạn</label>
                    <input type="text" id="name" name="name" placeholder="Nguyễn Văn A" required>
                </div>
                
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" placeholder="email@example.com" required>
                </div>
                
                <div class="form-group">
                    <label for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password" placeholder="••••••••" 
                           minlength="6" required>
                    <small>Tối thiểu 6 ký tự</small>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Xác nhận mật khẩu</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" 
                           placeholder="••••••••" required>
                </div>
                
                <button type="submit" class="btn btn-primary">Đăng ký</button>
            </form>
            
            <p class="auth-link">
                Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
            </p>
        </div>
    </div>
</body>
</html>