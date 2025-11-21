<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Bean.User" %>
<%@ page import="model.Bean.Video" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    List<Video> videos = (List<Video>) request.getAttribute("videos");
    String keyword = (String) request.getAttribute("keyword");
    Boolean isSearchMode = (Boolean) request.getAttribute("isSearchMode");
    Boolean isTrendingMode = (Boolean) request.getAttribute("isTrendingMode");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VideoSharer - Chia sẻ video của bạn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
    <style>
        /* ⭐ CSS CHO TRENDING BADGE */
        .trending-badge {
            position: absolute;
            top: 10px;
            left: 10px;
            background: linear-gradient(135deg, #f59e0b 0%, #ef4444 100%);
            color: white;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
            display: flex;
            align-items: center;
            gap: 4px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.3);
            z-index: 10;
        }
        
        .rank-number {
            font-size: 16px;
            font-weight: 900;
        }
        
        .video-thumbnail {
            position: relative;
        }
    </style>
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
                <input type="text" name="q" 
                       value="<%= keyword != null ? keyword : "" %>" 
                       placeholder="Tìm kiếm video..." 
                       class="search-input">
                <button type="submit" class="search-btn">🔍</button>
            </form>
        </div>
        
        <div class="header-right">
            <% if (user != null) { %>
                <a href="${pageContext.request.contextPath}/upload-video" class="upload-btn">📤 Đăng tải</a>
                <div class="user-info">
                    <span class="user-name">👤 <%= user.getName() %></span>
                    <a href="${pageContext.request.contextPath}/profile" style="color: white; text-decoration: none; margin-right: 15px;">⚙️</a>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </div>
            <% } else { %>
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
            <a href="${pageContext.request.contextPath}/home" 
               class="nav-item <%= (isTrendingMode == null || !isTrendingMode) && (isSearchMode == null || !isSearchMode) ? "active" : "" %>">
                <span>🏠</span> <span>Trang chủ</span>
            </a>
            <a href="${pageContext.request.contextPath}/trending" 
               class="nav-item <%= isTrendingMode != null && isTrendingMode ? "active" : "" %>">
                <span>🔥</span> <span>Xu hướng</span>
            </a>
            <hr>
            <a href="${pageContext.request.contextPath}/my-videos" class="nav-item">
                <span>📹</span> <span>Video của tôi</span>
            </a>
            <a href="${pageContext.request.contextPath}/liked" class="nav-item">
                <span>👍</span> <span>Video đã thích</span>
            </a>
        </nav>
    </aside>
    
    <!-- MAIN CONTENT -->
    <main class="main-content" id="mainContent">
        <div class="container">
            <!-- ⭐ TIÊU ĐỀ ĐỘNG (HOME / TRENDING / SEARCH) -->
            <h2 class="section-title">
                <% 
                if (isSearchMode != null && isSearchMode && keyword != null && !keyword.isEmpty()) { 
                %>
                    🔍 Kết quả tìm kiếm: "<%= keyword %>"
                <% 
                } else if (isTrendingMode != null && isTrendingMode) { 
                %>
                    🔥 Video xu hướng
                <% 
                } else { 
                %>
                    📹 Video mới nhất
                <% 
                } 
                %>
            </h2>
            
            <!-- ⭐ MÔ TẢ PHỤ -->
            <% 
            if (isSearchMode != null && isSearchMode && keyword != null && !keyword.isEmpty()) { 
            %>
                <p style="color: #aaa; margin-bottom: 20px; font-size: 14px;">
                    Tìm thấy <%= videos != null ? videos.size() : 0 %> video
                </p>
            <% 
            } else if (isTrendingMode != null && isTrendingMode) { 
            %>
                <p style="color: #aaa; margin-bottom: 20px; font-size: 14px;">
                    Top <%= videos != null ? videos.size() : 0 %> video được xem nhiều nhất
                </p>
            <% 
            } 
            %>
            
            <!-- ⭐ DANH SÁCH VIDEO -->
            <% if (videos != null && videos.size() > 0) { %>
                <div class="video-grid">
                    <% 
                    int rank = 1;
                    for (Video video : videos) { 
                    %>
                        <div class="video-card" onclick="window.location.href='${pageContext.request.contextPath}/watch?id=<%= video.getVideoId() %>'">
                            <div class="video-thumbnail">
                                <% if (video.getImg() != null && !video.getImg().isEmpty()) { %>
                                    <img src="${pageContext.request.contextPath}/<%= video.getImg() %>" 
                                         alt="<%= video.getTitle() %>"
                                         onerror="this.src='https://via.placeholder.com/320x180/667eea/ffffff?text=VideoSharer'">
                                <% } else { %>
                                    <img src="https://via.placeholder.com/320x180/667eea/ffffff?text=VideoSharer" 
                                         alt="No thumbnail">
                                <% } %>
                                
                                <!-- ⭐ HIỂN THỊ RANKING NẾU LÀ TRENDING -->
                                <% if (isTrendingMode != null && isTrendingMode) { %>
                                    <div class="trending-badge">
                                        <span class="rank-number">#<%= rank %></span>
                                        <span>🔥</span>
                                    </div>
                                <% } %>
                                
                                <span class="video-duration">10:25</span>
                            </div>
                            <div class="video-info">
                                <h3 class="video-title"><%= video.getTitle() %></h3>
                                <p class="video-author">👤 <%= video.getAuthorName() %></p>
                                <div class="video-meta">
                                    <span>👁️ <%= video.getView() %> lượt xem</span>
                                    <span>•</span>
                                    <span>📅 2 ngày trước</span>
                                </div>
                            </div>
                        </div>
                    <% 
                        rank++;
                    } 
                    %>
                </div>
            <% } else { %>
                <!-- ⭐ KHÔNG CÓ VIDEO -->
                <div class="no-videos">
                    <% 
                    if (isSearchMode != null && isSearchMode && keyword != null && !keyword.isEmpty()) { 
                    %>
                        <!-- Đang search nhưng không tìm thấy -->
                        <div class="no-videos-icon">🔍</div>
                        <h3>Không tìm thấy kết quả nào!</h3>
                        <p>Không tìm thấy video nào với từ khóa "<%= keyword %>"</p>
                        <a href="${pageContext.request.contextPath}/home" style="color: #667eea; text-decoration: none; margin-top: 15px; display: inline-block; font-size: 14px;">
                            ← Quay lại trang chủ
                        </a>
                    <% 
                    } else if (isTrendingMode != null && isTrendingMode) { 
                    %>
                        <!-- Trending nhưng không có video -->
                        <div class="no-videos-icon">🔥</div>
                        <h3>Chưa có video xu hướng!</h3>
                        <p>Hãy là người đầu tiên đăng tải video</p>
                    <% 
                    } else { 
                    %>
                        <!-- Không có video nào trong hệ thống -->
                        <div class="no-videos-icon">📹</div>
                        <h3>Chưa có video nào!</h3>
                        <p>Hãy là người đầu tiên đăng tải video</p>
                        <% if (user != null) { %>
                            <a href="${pageContext.request.contextPath}/upload-video" class="btn-upload-now">
                                📤 Đăng tải ngay
                            </a>
                        <% } %>
                    <% 
                    } 
                    %>
                </div>
            <% } %>
        </div>
    </main>
    
    <script>
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar');
            const mainContent = document.getElementById('mainContent');
            
            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded');
        }
    </script>
</body>
</html>