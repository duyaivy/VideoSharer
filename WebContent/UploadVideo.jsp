<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page import="model.Bean.User" %> <% User user =
(User) session.getAttribute("user"); String err = (String)
request.getAttribute("error"); String savedTitle = (String)
request.getAttribute("title"); String savedDescription = (String)
request.getAttribute("description"); 
if (savedTitle == null) savedTitle = ""; if (savedDescription == null)
savedDescription = ""; %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Upload video - VideoSharer</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/index.css"
    />
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
          class="nav-item"
        >
          <span>📹</span> <span>Video của tôi</span>
        </a>
        <a href="${pageContext.request.contextPath}/profile" class="nav-item">
          <span>🙍‍♂️ </span> <span>Thông tin cá nhân</span>
        </a>
      </nav>
    </aside>

    <!-- MAIN CONTENT -->
    <main class="main-content" id="mainContent">
      <div class="upload-ctn">
        <h1>📹 Upload Video</h1>
        <p class="subtitle">Chia sẻ video của bạn với cộng đồng</p>

        <% if(err != null && !err.isEmpty()){ %>
        <div class="error-message show">
          <strong>⚠️ Lỗi:</strong> <%= err %>
        </div>
        <% } %>

        <form
          action="${pageContext.request.contextPath}/upload-video"
          method="post"
          enctype="multipart/form-data"
          id="uploadForm"
        >
          <div class="form-group">
            <label for="title">Tiêu đề video *</label>
            <input
              type="text"
              id="title"
              name="title"
              placeholder="Nhập tiêu đề video..."
              required
              maxlength="200"
              value="<%= savedTitle %>"
            />
          </div>

          <div class="form-group">
            <label for="description">Mô tả</label>
            <textarea
              id="description"
              name="description"
              placeholder="Mô tả về video của bạn..."
              maxlength="1000"
            >
<%= savedDescription %></textarea
            >
          </div>

          <div class="form-group">
            <label>Video file *</label>
            <label for="videoFile" class="file-upload" id="fileUploadLabel">
              <div class="file-upload-icon">📁</div>
              <div class="file-upload-text">
                <strong>Click để chọn file</strong>
              </div>
              <div class="file-upload-hint">
                Hỗ trợ: MP4, AVI, MOV, MKV (Tối đa 100MB)
              </div>
            </label>
            <input
              type="file"
              id="videoFile"
              name="videoFile"
              accept="video/*"
              required
              onchange="displayFileInfo()"
            />
            <div class="file-info" id="fileInfo">
              <div class="file-name" id="fileName"></div>
              <div class="file-size" id="fileSize"></div>
            </div>
          </div>

          <!-- Buttons -->
          <div class="btn-group">
            <button
              type="button"
              class="btn-cancel"
              onclick="window.location.href='${pageContext.request.contextPath}/manage-video'"
            >
              Hủy
            </button>
            <button type="submit" class="btn-submit" id="submitBtn">
              Upload Video
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

      function displayFileInfo() {
        const fileInput = document.getElementById("videoFile");
        const fileInfo = document.getElementById("fileInfo");
        const fileName = document.getElementById("fileName");
        const fileSize = document.getElementById("fileSize");
        const fileUploadLabel = document.getElementById("fileUploadLabel");

        if (fileInput.files.length > 0) {
          const file = fileInput.files[0];
          const sizeMB = (file.size / (1024 * 1024)).toFixed(2);

          fileName.textContent = "📄 " + file.name;
          fileSize.textContent = "📦 Kích thước: " + sizeMB + " MB";

          // Kiểm tra kích thước file
          if (file.size > 100 * 1024 * 1024) {
            fileInfo.style.backgroundColor = "#ffebee";
            fileName.style.color = "#c62828";
            fileSize.style.color = "#c62828";
            fileSize.textContent += " ⚠️ Vượt quá giới hạn 100MB!";
          } else {
            fileInfo.style.backgroundColor = "#e8f5e9";
            fileName.style.color = "#2e7d32";
            fileSize.style.color = "#66bb6a";
          }

          fileInfo.classList.add("show");
          fileUploadLabel.style.borderColor = "#4caf50";
        } else {
          fileInfo.classList.remove("show");
          fileUploadLabel.style.borderColor = "#667eea";
        }
      }
    </script>
  </body>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      background: #f5f5f5;
    }

    .upload-container {
      background: white;
      border-radius: 20px;
      box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
      max-width: 600px;
      width: 100%;
      padding: 40px;
    }

    h1 {
      color: #333;
      margin-bottom: 10px;
      font-size: 28px;
    }

    .subtitle {
      color: #666;
      margin-bottom: 30px;
      font-size: 14px;
    }

    .form-group {
      margin-bottom: 25px;
    }

    label {
      display: block;
      margin-bottom: 8px;
      color: #333;
      font-weight: 600;
      font-size: 14px;
    }

    input[type="text"],
    textarea {
      width: 100%;
      padding: 12px 15px;
      border: 2px solid #e0e0e0;
      border-radius: 8px;
      font-size: 14px;
      transition: border-color 0.3s;
    }

    input[type="text"]:focus,
    textarea:focus {
      outline: none;
      border-color: #667eea;
    }

    textarea {
      resize: vertical;
      min-height: 100px;
    }

    .file-upload {
      border: 2px dashed #667eea;
      border-radius: 12px;
      padding: 30px;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s;
      background: #f8f9ff;
    }

    .file-upload:hover {
      background: #eef1ff;
      border-color: #5568d3;
    }

    .file-upload.dragover {
      background: #e3e7ff;
      border-color: #667eea;
    }

    .file-upload-icon {
      font-size: 48px;
      color: #667eea;
      margin-bottom: 15px;
    }

    .file-upload-text {
      color: #666;
      margin-bottom: 5px;
    }

    .file-upload-hint {
      color: #999;
      font-size: 12px;
    }

    .upload-ctn {
      max-width: 900px;
      margin: 0 auto;
      padding: 40px;
      background-color: #fff;
      border-radius: 15px;
    }
    .file-info {
      margin-top: 15px;
      padding: 15px;
      background: #e8f5e9;
      border-radius: 8px;
      display: none;
    }

    .file-info.show {
      display: block;
    }

    .file-name {
      color: #2e7d32;
      font-weight: 600;
      margin-bottom: 5px;
    }

    .file-size {
      color: #66bb6a;
      font-size: 12px;
    }

    .btn-group {
      display: flex;
      gap: 15px;
      margin-top: 30px;
    }

    button {
      flex: 1;
      padding: 14px 20px;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s;
    }

    .btn-submit {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
    }

    .btn-submit:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
    }

    .btn-submit:disabled {
      background: #ccc;
      cursor: not-allowed;
      transform: none;
    }

    .btn-cancel {
      background: #f5f5f5;
      color: #666;
    }

    .btn-cancel:hover {
      background: #e0e0e0;
    }

    .error-message {
      background: #ffebee;
      color: #c62828;
      padding: 15px 20px;
      border-radius: 8px;
      margin-bottom: 20px;
      display: none;
      border-left: 4px solid #c62828;
      animation: slideDown 0.3s ease-out;
    }

    .error-message.show {
      display: block;
    }

    @keyframes slideDown {
      from {
        opacity: 0;
        transform: translateY(-10px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    input[type="file"] {
      display: none;
    }
  </style>
</html>
