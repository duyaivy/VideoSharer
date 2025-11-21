<%@ page contentType="text/html;charset=UTF-8" %> <%@ page
import="model.Bean.User" %> <% User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Chỉnh sửa video - VideoSharer</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/index.css"
    />
    <style>
      body {
        margin: 0;
        padding: 0;
        background: #f5f5f5;
        font-family: Arial, sans-serif;
      }

      .edit-container {
        max-width: 700px;
        margin: 40px auto;
        padding: 40px;
        background: white;
        border-radius: 16px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      }

      .edit-container h2 {
        margin: 0 0 30px;
        font-size: 28px;
        color: #333;
      }

      .form-group {
        margin-bottom: 24px;
      }

      .form-group label {
        display: block;
        font-weight: 600;
        color: #333;
        margin-bottom: 8px;
        font-size: 14px;
      }

      .form-group input,
      .form-group textarea {
        width: 100%;
        padding: 12px 16px;
        border: 1px solid #e0e0e0;
        border-radius: 8px;
        font-size: 14px;
        font-family: Arial, sans-serif;
        box-sizing: border-box;
      }

      .form-group input:focus,
      .form-group textarea:focus {
        border-color: #667eea;
        outline: none;
      }

      .form-group textarea {
        resize: vertical;
        min-height: 120px;
      }

      .btn-group {
        display: flex;
        gap: 12px;
        margin-top: 30px;
      }

      .btn {
        flex: 1;
        padding: 14px;
        border: none;
        border-radius: 8px;
        font-size: 15px;
        font-weight: 600;
        cursor: pointer;
        text-decoration: none;
        text-align: center;
        display: inline-block;
      }

      .btn-primary {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
      }

      .btn-secondary {
        background: #f0f0f0;
        color: #333;
      }

      .btn-secondary:hover {
        background: #e0e0e0;
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
        <form
          action="${pageContext.request.contextPath}/search"
          method="GET"
          class="search-form"
        >
          <input
            type="text"
            name="q"
            placeholder="Tìm kiếm video..."
            class="search-input"
          />
          <button type="submit" class="search-btn">🔍</button>
        </form>
      </div>

      <div class="header-right">
        <% if (user != null) { %>
        <a
          href="${pageContext.request.contextPath}/upload-video"
          class="upload-btn"
          >📤 Đăng tải</a
        >
        <div class="user-info">
          <a
            href="${pageContext.request.contextPath}/profile"
            style="color: white; text-decoration: none; margin-right: 15px"
            ><span class="user-name">🙍‍♂️ <%= user.getName() %></span></a
          >
          <a href="${pageContext.request.contextPath}/logout" class="logout-btn"
            >Đăng xuất</a
          >
        </div>
        <% } %>
      </div>
    </header>

    <!-- SIDEBAR -->
    <aside class="sidebar" id="sidebar">
      <nav>
        <a href="${pageContext.request.contextPath}/home" class="nav-item">
          <span>🏠</span> <span>Trang chủ</span>
        </a>
        <a href="${pageContext.request.contextPath}/trending" class="nav-item">
          <span>🔥</span> <span>Xu hướng</span>
        </a>
        <hr />
        <a
          href="${pageContext.request.contextPath}/manage-video"
          class="nav-item active"
        >
          <span>📹</span> <span>Video của tôi</span>
        </a>
        <a href="${pageContext.request.contextPath}/liked" class="nav-item">
          <span>👍</span> <span>Video đã thích</span>
        </a>
      </nav>
    </aside>

    <!-- MAIN CONTENT -->
    <main class="main-content" id="mainContent">
      <div class="edit-container">
        <h2>✏️ Chỉnh sửa video</h2>

        <form method="post" action="edit-video">
          <input type="hidden" name="id" value="${video.videoId}" />

          <div class="form-group">
            <label>Tiêu đề:</label>
            <input type="text" name="title" value="${video.title}" required />
          </div>

          <div class="form-group">
            <label>Mô tả:</label>
            <textarea name="description">${video.description}</textarea>
          </div>

          <div class="btn-group">
            <a href="manage-video" class="btn btn-secondary">← Quay lại</a>
            <button type="submit" class="btn btn-primary">
              💾 Lưu thay đổi
            </button>
          </div>
        </form>
      </div>
    </main>

    <script>
      function toggleSidebar() {
        const sidebar = document.getElementById("sidebar");
        const mainContent = document.getElementById("mainContent");
        sidebar.classList.toggle("collapsed");
        mainContent.classList.toggle("expanded");
      }
    </script>
  </body>
</html>
