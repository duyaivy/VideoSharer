<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý video - VideoSharer</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="container">
    <div class="main-content">
        <!-- HEADER -->
        <div class="page-header">
            <div>
                <h1 class="page-title">🎛️ Quản lý video của bạn</h1>
                <p class="page-subtitle">
                    Theo dõi trạng thái xử lý, chỉnh sửa và xoá video đã tải lên.
                </p>
            </div>
            <a href="${pageContext.request.contextPath}/upload-video" class="primary-btn">
                + Tải video mới
            </a>
        </div>

        <!-- STATS -->
        <div class="stats-row">
            <div class="stat-card">
                <span class="stat-label">Tổng số video</span>
                <span class="stat-value">${totalVideos}</span>
            </div>
        </div>

        <!-- LIST / EMPTY STATE -->
        <c:choose>
            <c:when test="${empty videos}">
                <div class="empty-state">
                    <div class="empty-icon">📭</div>
                    <h2>Chưa có video nào</h2>
                    <p>Hãy tải lên video đầu tiên của bạn để bắt đầu chia sẻ với mọi người.</p>
                    <a href="${pageContext.request.contextPath}/upload-video" class="primary-btn">
                        Tải video ngay
                    </a>
                </div>
            </c:when>

            <c:otherwise>
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
                        <c:forEach var="v" items="${videos}">
                            <tr>
                                <td class="cell-id">#${v.videoId}</td>

                                <td class="cell-thumb">
                                    <c:if test="${not empty v.img}">
                                        <img src="${pageContext.request.contextPath}/${v.img}"
                                             alt="thumbnail"
                                             class="thumb-img">
                                    </c:if>
                                </td>

                                <td class="cell-title">
                                    <div class="video-title-text">${v.title}</div>
                                </td>

                                <td class="cell-desc">
                                    <div class="video-desc-text">
                                        ${v.description}
                                    </div>
                                </td>

                                <td class="cell-date">
                                    ${v.createAt}
                                </td>

                                <td class="cell-status">
                                    <span id="status-${v.videoId}" class="status-badge">
                                        ${v.status}
                                    </span>
                                </td>

                                <td class="cell-actions">
                                    <a href="${pageContext.request.contextPath}/watch?id=${v.videoId}"
                                       class="action-link">
                                        Xem
                                    </a>
                                    <span class="divider">•</span>
                                    <a href="${pageContext.request.contextPath}/edit-video?id=${v.videoId}"
                                       class="action-link">
                                        Sửa
                                    </a>
                                    <span class="divider">•</span>
                                    <a href="${pageContext.request.contextPath}/delete-video?id=${v.videoId}"
                                       class="action-link action-link-danger"
                                       onclick="return confirm('Xoá video này?')">
                                        Xoá
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- PAGINATION -->
                <div class="pagination">
                    <c:if test="${totalPages > 1}">
                        <div class="pagination-inner">
                            <c:if test="${currentPage > 1}">
                                <a href="${pageContext.request.contextPath}/manage-video?page=${currentPage - 1}"
                                   class="page-btn">
                                    ‹ Trước
                                </a>
                            </c:if>

                            <div class="page-numbers">
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <c:choose>
                                        <c:when test="${i == currentPage}">
                                            <span class="page-number active">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/manage-video?page=${i}"
                                               class="page-number">
                                                ${i}
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>

                            <c:if test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/manage-video?page=${currentPage + 1}"
                                   class="page-btn">
                                    Sau ›
                                </a>
                            </c:if>
                        </div>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    const ws = new WebSocket(
        "ws://" + window.location.host + "${pageContext.request.contextPath}/video-status"
    );

    function applyStatusStyle(el, status) {
        el.classList.remove("status-processing", "status-done", "status-failed", "status-other");

        if (status === "done") {
            el.classList.add("status-done");
        } else if (status === "failed") {
            el.classList.add("status-failed");
        } else {
            // các trạng thái đang xử lý / hàng đợi...
            el.classList.add("status-processing");
        }
    }

    ws.onmessage = function (event) {
        const data = JSON.parse(event.data);
        const el = document.getElementById("status-" + data.videoId);

        if (el) {
            el.innerText = data.status;
            applyStatusStyle(el, data.status);
        }
    };

    ws.onclose = function () {
        setTimeout(function () {
            location.reload();
        }, 2000);
    };

    // Áp style ban đầu cho các status đã render từ server
    window.addEventListener("DOMContentLoaded", function () {
        const allStatus = document.querySelectorAll(".status-badge");
        allStatus.forEach(function (el) {
            applyStatusStyle(el, el.textContent.trim());
        });
    });
</script>

<style>
    body {
        margin: 0;
        padding: 20px;
        font-family: Roboto, Arial, sans-serif;
        color: #0f0f0f;
        background: #f5f5f5;
    }

    .container {
        max-width: 1200px;
        margin: 0 auto;
    }

    .main-content {
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

    .video-table th,
    .video-table td {
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

    @media (max-width: 768px) {
        .page-header {
            flex-direction: column;
            align-items: flex-start;
        }

        .primary-btn {
            width: 100%;
            justify-content: center;
        }

        .main-content {
            padding: 16px;
        }
    }
</style>

</body>
</html>
