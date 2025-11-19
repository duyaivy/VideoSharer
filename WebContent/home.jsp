<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Bean.User" %>
<%@ page import="model.Bean.Video" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    List<Video> videos = (List<Video>) request.getAttribute("videos");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ - VideoSharer</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
</head>
<body>
    <!-- HEADER -->
    <header class="header">
        <div class="header-left">
            <button class="menu-btn" onclick="toggleSidebar()">☰</button>
            <a href="${pageContext.request.contextPath}/home" class="logo">
                🎬 <span>VideoSharer</span>
            </a>
        </div>
        
        <div class="header-center">
            <form action="${pageContext.request.contextPath}/search" method="GET" class="search-form">
                <input type="text" name="q" placeholder="Tìm kiếm video..." class="search-input">
                <button type="submit" class="search-btn">🔍</button>
            </form>
        </div>
        
        <div class="header-right">
            <% if (user != null) { %>
                <!-- User đã đăng nhập -->
                <a href="${pageContext.request.contextPath}/upload-video" class="upload-btn">📤 Đăng tải</a>
                <div class="user-info">
                    <span class="user-name">👤 <%= user.getName() %></span>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </div>
            <% } else { %>
                <!-- User chưa đăng nhập -->
                <div class="auth-links">
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/signup">Đăng ký</a>
                </div>
            <% } %>
        </div>
    </header>
    
    <!-- SIDEBAR -->
    <aside class="sidebar" id="sidebar">
        <nav>
            <a href="${pageContext.request.contextPath}/home" class="nav-item active">
                <span>🏠</span> <span>Trang chủ</span>
            </a>
            <a href="${pageContext.request.contextPath}/trending" class="nav-item">
                <span>🔥</span> <span>Xu hướng</span>
            </a>
            <a href="${pageContext.request.contextPath}/subscriptions" class="nav-item">
                <span>📺</span> <span>Đăng ký kênh</span>
            </a>
            <hr>
            <a href="${pageContext.request.contextPath}/library" class="nav-item">
                <span>📚</span> <span>Thư viện</span>
            </a>
            <a href="${pageContext.request.contextPath}/history" class="nav-item">
                <span>🕐</span> <span>Lịch sử</span>
            </a>
            <a href="${pageContext.request.contextPath}/liked" class="nav-item">
                <span>👍</span> <span>Video đã thích</span>
            </a>
        </nav>
    </aside>
    
    <!-- MAIN CONTENT -->
    <main class="main-content" id="mainContent">
        <div class="container">
            <h2 class="section-title">Video mới nhất</h2>
            
            <div class="video-grid">
                <% if (videos != null && videos.size() > 0) { 
                    for (Video video : videos) { %>
                        <div class="video-card" onclick="window.location.href='${pageContext.request.contextPath}/watch?id=<%= video.getVideoId() %>'">
                            <div class="video-thumbnail">
                                <% if (video.getImg() != null && !video.getImg().isEmpty()) { %>
                                    <img src="${pageContext.request.contextPath}/<%= video.getImg() %>" alt="<%= video.getTitle() %>">
                                <% } else { %>
                                    <img src="https://via.placeholder.com/320x180/667eea/ffffff?text=No+Thumbnail" alt="No thumbnail">
                                <% } %>
                                <span class="video-duration">10:25</span>
                            </div>
                            <div class="video-info">
                                <h3 class="video-title"><%= video.getTitle() %></h3>
                                <p class="video-author"><%= video.getAuthorName() %></p>
                                <div class="video-meta">
                                    <span><%= video.getView() %> lượt xem</span>
                                    <span>•</span>
                                    <span>2 ngày trước</span>
                                </div>
                            </div>
                        </div>
                <% } 
                } else { %>
                    <p class="no-videos">Chưa có video nào! 😢</p>
                <% } %>
            </div>
        </div>
    </main>
    
    <script>
        // Toggle sidebar
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar');
            const mainContent = document.getElementById('mainContent');
            
            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded');
        }
    </script>
</body>
</html>