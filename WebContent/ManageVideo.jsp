<%@page import="model.Bean.User"%>
<%@page import="model.Bean.Video"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%
List<Video> videos = (List<Video>) request.getAttribute("videos");
Integer totalVideos = (Integer) request.getAttribute("totalVideos");
Integer currentPage = (Integer) request.getAttribute("currentPage");
Integer totalPages = (Integer) request.getAttribute("totalPages");
totalVideos = 0;
if (currentPage == null)
	currentPage = 1;
if (totalPages == null)
	totalPages = 1;
if (videos == null)
	videos = new java.util.ArrayList<>();
User u = (User) request.getSession().getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Quản lý video - VideoSharer</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/index.css" />
</head>
<body>
	<header class="header">
		<div class="header-left">
			<button class="menu-btn" onclick="toggleSidebar()">☰</button>
			<a href="${pageContext.request.contextPath}/home" class="logo">
				🎬 <span>VideoSharer</span>
			</a>
		</div>

		<div class="header-center">
			<form action="${pageContext.request.contextPath}/search" method="GET"
				class="search-form">
				<input type="text" name="q" value="" placeholder="Tìm kiếm video..."
					class="search-input">
				<button type="submit" class="search-btn">🔍</button>
			</form>
		</div>

		<div class="header-right">
			<%
			if (u != null) {
			%>
			<a href="${pageContext.request.contextPath}/upload-video"
				class="upload-btn">📤 Đăng tải</a>
			<div class="user-info">

				<a href="${pageContext.request.contextPath}/profile"
					style="color: white; text-decoration: none; margin-right: 15px;"><span
					class="user-name">🙍‍♂️ <%=u.getName()%></span></a> <a
					href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng
					xuất</a>
			</div>
			<%
			} else {
			%>
			<div class="auth-links">
				<a href="${pageContext.request.contextPath}/login">Đăng nhập</a> <a
					href="${pageContext.request.contextPath}/signup">Đăng ký</a>
			</div>
			<%
			}
			%>
		</div>
	</header>

	<!-- SIDEBAR -->
	<aside class="sidebar" id="sidebar">
		<nav>
			<a href="${pageContext.request.contextPath}/home" class="nav-item">
				<span>🏠</span> <span>Trang chủ</span>
			</a> <a href="${pageContext.request.contextPath}/trending"
				class="nav-item"> <span>🔥</span> <span>Xu hướng</span>
			</a>
			<hr />
			<a href="${pageContext.request.contextPath}/manage-video"
				class="nav-item"> <span>📹</span> <span>Video của tôi</span>
			</a> <a href="${pageContext.request.contextPath}/profile"
				class="nav-item"> <span>🙍‍♂️ </span> <span>Thông tin cá
					nhân</span>
			</a>
		</nav>
	</aside>

	<!-- MAIN CONTENT -->
	<main class="main-content" id="mainContent">
		<div class="container">
			<div class="page-content">
				<!-- HEADER -->
				<div class="page-header">
					<div>
						<h1 class="page-title">🎛️ Quản lý video của bạn</h1>
						<p class="page-subtitle">Theo dõi trạng thái xử lý, chỉnh sửa
							và xoá video đã tải lên.</p>
					</div>
					<a href="<%=request.getContextPath()%>/upload-video"
						class="primary-btn"> + Tải video mới </a>
				</div>

				<!-- STATS -->
				<div class="stats-row">
					<div class="stat-card">
						<span class="stat-label">Tổng số video</span> <span
							class="stat-value"><%=totalVideos%></span>
					</div>
				</div>

				<!-- LIST / EMPTY STATE -->
				<%
				if (videos.isEmpty()) {
				%>
				<div class="empty-state">
					<div class="empty-icon">📭</div>
					<h2>Chưa có video nào</h2>
					<p>Hãy tải lên video đầu tiên của bạn để bắt đầu chia sẻ với
						mọi người.</p>
					<a href="<%=request.getContextPath()%>/upload-video"
						class="primary-btn"> Tải video ngay </a>
				</div>
				<%
				} else {
				%>
				<div class="table-wrapper">
					<table class="video-table">
						<thead>
							<tr>
								<th class="col-id">ID</th>
								<th class="col-thumb">Thumbnail</th>
								<th class="col-title">Tiêu đề</th>
								<th class="col-desc">Mô tả</th>
								<th class="col-date">Ngày tạo</th>
								<th class="col-status">Trạng thái</th>
								<th class="col-actions">Hành động</th>
							</tr>
						</thead>
						<tbody>
							<%
							for (Video v : videos) {
							%>
							<tr>
								<td class="cell-id">#<%=v.getVideoId()%></td>

								<td class="cell-thumb">
									<%
									if (v.getImg() != null && !v.getImg().isEmpty()) {
									%> <img src="<%=request.getContextPath() + "/" + v.getImg()%>"
									alt="thumbnail" class="thumb-img" /> <%
 }
 %>
								</td>

								<td class="cell-title">
									<div class="video-title-text"><%=v.getTitle()%></div>
								</td>

								<td class="cell-desc">
									<div class="video-desc-text">
										<%=v.getDescription()%>
									</div>
								</td>

								<td class="cell-date"><%=v.getCreateAt()%></td>

								<td class="cell-status"><span
									id="status-<%=v.getVideoId()%>" class="status-badge"> <%=v.getStatus()%>
								</span></td>

								<td class="cell-actions"><a
									href="<%=request.getContextPath()%>/watch?id=<%=v.getVideoId()%>"
									class="action-link"> Xem </a> <span class="divider">•</span> <a
									href="<%=request.getContextPath()%>/edit-video?id=<%=v.getVideoId()%>"
									class="action-link"> Sửa </a> <span class="divider">•</span> <a
									href="<%=request.getContextPath()%>/delete-video?id=<%=v.getVideoId()%>"
									class="action-link action-link-danger"
									onclick="return confirm('Xoá video này?')"> Xoá </a></td>
							</tr>
							<%
							}
							%>
						</tbody>
					</table>
				</div>

				<!-- PAGINATION -->
				<div class="pagination">
					<%
					if (totalPages > 1) {
					%>
					<div class="pagination-inner">
						<%
						if (currentPage > 1) {
						%>
						<a
							href="<%=request.getContextPath()%>/manage-video?page=<%=currentPage - 1%>"
							class="page-btn"> ‹ Trước </a>
						<%
						}
						%>

						<div class="page-numbers">
							<%
							for (int i = 1; i <= totalPages; i++) {
							%>
							<%
							if (i == currentPage) {
							%>
							<span class="page-number active"><%=i%></span>
							<%
							} else {
							%>
							<a href="<%=request.getContextPath()%>/manage-video?page=<%=i%>"
								class="page-number"> <%=i%>
							</a>
							<%
							}
							%>
							<%
							}
							%>
						</div>

						<%
						if (currentPage < totalPages) {
						%>
						<a
							href="<%=request.getContextPath()%>/manage-video?page=<%=currentPage + 1%>"
							class="page-btn"> Sau › </a>
						<%
						}
						%>
					</div>
					<%
					}
					%>
				</div>
				<%
				}
				%>
			</div>
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

	<script>
	const ws = new WebSocket(
		    "ws://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/video-status/<%=u.getId()%>
		");
		function applyStatusStyle(el, status) {
			el.classList.remove("status-processing", "status-done",
					"status-failed", "status-other");

			if (status === "done") {
				el.classList.add("status-done");
			} else if (status === "failed") {
				el.classList.add("status-failed");
			} else {

				el.classList.add("status-processing");
			}
		}

		ws.onmessage = function(event) {
			const data = JSON.parse(event.data);
			const el = document.getElementById("status-" + data.videoId);

			if (el) {
				el.innerText = data.status;
				applyStatusStyle(el, data.status);
			}
		};

		ws.onclose = function() {
			setTimeout(function() {
				location.reload();
			}, 2000);
		};

		window.addEventListener("DOMContentLoaded", function() {
			const allStatus = document.querySelectorAll(".status-badge");
			allStatus.forEach(function(el) {
				applyStatusStyle(el, el.textContent.trim());
			});
		});
	</script>

	<style>
body {
	margin: 0;
	padding: 0;
	font-family: Roboto, Arial, sans-serif;
	color: #0f0f0f;
	background: #f5f5f5;
}

.container {
	max-width: 1200px;
	margin: 0 auto;
}

.page-content {
	background: #ffffff;
	border-radius: 16px;
	padding: 24px 24px 28px;
	box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
}

.page-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	gap: 16px;
	margin-bottom: 24px;
}

.page-title {
	margin: 0 0 4px;
	font-size: 24px;
	font-weight: 700;
}

.page-subtitle {
	margin: 0;
	font-size: 14px;
	color: #606060;
}

.primary-btn {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	padding: 10px 18px;
	border-radius: 999px;
	background: #ff4e45;
	color: #ffffff;
	font-size: 14px;
	font-weight: 600;
	border: none;
	text-decoration: none;
	cursor: pointer;
	box-shadow: 0 4px 12px rgba(255, 78, 69, 0.3);
	white-space: nowrap;
}

.primary-btn:hover {
	opacity: 0.9;
}

.stats-row {
	display: flex;
	gap: 16px;
	margin-bottom: 20px;
	flex-wrap: wrap;
}

.stat-card {
	background: #fafafa;
	border-radius: 12px;
	padding: 16px 18px;
	min-width: 160px;
	box-shadow: 0 0 0 1px #e0e0e0;
}

.stat-label {
	font-size: 12px;
	text-transform: uppercase;
	letter-spacing: 0.08em;
	color: #757575;
	display: block;
	margin-bottom: 4px;
}

.stat-value {
	font-size: 20px;
	font-weight: 700;
}

.table-wrapper {
	overflow-x: auto;
	border-radius: 12px;
	border: 1px solid #e0e0e0;
}

.video-table {
	width: 100%;
	border-collapse: collapse;
	min-width: 800px;
}

.video-table thead {
	background: #fafafa;
}

.video-table th, .video-table td {
	padding: 12px 14px;
	text-align: left;
	font-size: 14px;
}

.video-table th {
	font-weight: 600;
	color: #606060;
	border-bottom: 1px solid #e0e0e0;
	white-space: nowrap;
}

.video-table tbody tr:nth-child(even) {
	background: #fcfcfc;
}

.video-table tbody tr:hover {
	background: #f5f5f5;
}

.col-id {
	width: 80px;
}

.col-thumb {
	width: 140px;
}

.col-status {
	width: 140px;
}

.col-actions {
	width: 170px;
}

.thumb-img {
	width: 110px;
	height: 62px;
	object-fit: cover;
	border-radius: 8px;
	background: #eee;
	display: block;
}

.cell-id {
	font-weight: 600;
	font-size: 13px;
	color: #424242;
}

.cell-title .video-title-text {
	font-weight: 600;
	min-width: 200px;
	font-size: 14px;
	color: #111;
}

.cell-desc .video-desc-text {
	font-size: 13px;
	color: #616161;
	max-height: 40px;
	overflow: hidden;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
}

.cell-date {
	font-size: 13px;
	color: #757575;
}

.cell-status {
	font-size: 13px;
}

.status-badge {
	display: inline-flex;
	align-items: center;
	padding: 4px 10px;
	border-radius: 999px;
	font-size: 12px;
	font-weight: 500;
	border: 1px solid transparent;
	background: #fff3e0;
	color: #ef6c00;
	text-transform: capitalize;
}

.status-processing {
	background: #fff3e0;
	color: #ef6c00;
	border-color: #ffe0b2;
}

.status-done {
	background: #e8f5e9;
	color: #2e7d32;
	border-color: #c8e6c9;
}

.status-failed {
	background: #ffebee;
	color: #c62828;
	border-color: #ffcdd2;
}

.status-other {
	background: #eceff1;
	color: #455a64;
	border-color: #cfd8dc;
}

.cell-actions {
	white-space: nowrap;
	font-size: 13px;
}

.action-link {
	text-decoration: none;
	color: #1e88e5;
}

.action-link:hover {
	text-decoration: underline;
}

.action-link-danger {
	color: #e53935;
}

.divider {
	margin: 0 4px;
	color: #bdbdbd;
}

.pagination {
	margin-top: 20px;
	display: flex;
	justify-content: center;
}

.pagination-inner {
	display: inline-flex;
	align-items: center;
	gap: 8px;
	padding: 6px 10px;
	border-radius: 999px;
	background: #fafafa;
	border: 1px solid #e0e0e0;
}

.page-btn {
	text-decoration: none;
	font-size: 13px;
	color: #1e88e5;
	padding: 4px 8px;
	border-radius: 999px;
}

.page-btn:hover {
	background: #e3f2fd;
}

.page-numbers {
	display: inline-flex;
	gap: 4px;
	align-items: center;
}

.page-number {
	min-width: 28px;
	text-align: center;
	border-radius: 999px;
	font-size: 13px;
	padding: 4px 8px;
	text-decoration: none;
	color: #424242;
}

.page-number.active {
	background: #1e88e5;
	color: #ffffff;
	font-weight: 600;
}

.page-number:not(.active):hover {
	background: #eeeeee;
}

.empty-state {
	text-align: center;
	padding: 60px 20px 40px;
}

.empty-icon {
	font-size: 40px;
	margin-bottom: 12px;
}

.empty-state h2 {
	margin: 0 0 8px;
	font-size: 22px;
}

.empty-state p {
	margin: 0 0 20px;
	color: #616161;
	font-size: 14px;
}

@media ( max-width : 768px) {
	.page-header {
		flex-direction: column;
		align-items: flex-start;
	}
	.primary-btn {
		width: 100%;
		justify-content: center;
	}
	.page-content {
		padding: 16px;
	}
}
</style>
</body>
</html>

