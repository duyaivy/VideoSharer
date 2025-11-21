<%@ page contentType="text/html;charset=UTF-8" %>

<h2>Edit Video</h2>

<form method="post" action="edit-video">
    <input type="hidden" name="id" value="${video.videoId}">

    <label>Title:</label>
    <input type="text" name="title" value="${video.title}" required /><br><br>

    <label>Description:</label><br>
    <textarea name="description" rows="5" cols="40">${video.description}</textarea><br><br>

    <button type="submit">Save</button>
</form>

<br>
<a href="manage-video">Back</a>
