<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage My Videos</title>
</head>
<body>

<h2>Manage My Videos</h2>

<p>Total videos: ${totalVideos}</p>

<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>Thumbnail</th>
        <th>Title</th>
        <th>Description</th>
        <th>Created At</th>
        <th>Action</th>
    </tr>

    <c:forEach var="v" items="${videos}">
        <tr>
            <td>${v.videoId}</td>
            <td>
                <c:if test="${not empty v.img}">
                    <img src="${v.img}" alt="thumbnail" style="width: 100px; height: auto;">
                </c:if>
            </td>
            <td>${v.title}</td>
            <td>${v.description}</td>
            <td>${v.createAt}</td>
            <td>
                <a href="${pageContext.request.contextPath}/watch-video?id=${v.videoId}">View</a>
                |
                <a href="${pageContext.request.contextPath}/edit-video?id=${v.videoId}">Edit</a>
                |
                <a href="${pageContext.request.contextPath}/delete-video?id=${v.videoId}"
                   onclick="return confirm('Delete this video?');">
                    Delete
                </a>
            </td>
        </tr>
    </c:forEach>
</table>

<!-- Phân trang -->
<div style="margin-top: 16px;">
    <c:if test="${totalPages > 1}">
        <!-- Previous -->
        <c:if test="${currentPage > 1}">
            <a href="${pageContext.request.contextPath}/manage-video?page=${currentPage - 1}">Previous</a>
        </c:if>

        <!-- Số trang -->
        <c:forEach var="i" begin="1" end="${totalPages}">
            <c:choose>
                <c:when test="${i == currentPage}">
                    <strong>[${i}]</strong>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/manage-video?page=${i}">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <!-- Next -->
        <c:if test="${currentPage < totalPages}">
            <a href="${pageContext.request.contextPath}/manage-video?page=${currentPage + 1}">Next</a>
        </c:if>
    </c:if>
</div>

</body>
</html>
