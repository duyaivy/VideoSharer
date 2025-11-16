<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Upload video</title>
</head>
<%
String err =(String) request.getAttribute("error");
%>
<body>
	<h1>📹 Upload Video</h1>
	<p class="subtitle">Chia sẻ video của bạn với cộng đồng</p>

	<div class="error-message" id="errorMessage"></div>

	<form action="${pageContext.request.contextPath}/upload-video"
		method="post" enctype="multipart/form-data" id="uploadForm">

		<div class="form-group">
			<label for="title">Tiêu đề video *</label> <input type="text"
				id="title" name="title" placeholder="Nhập tiêu đề video..." required
				maxlength="200" />
		</div>


		<div class="form-group">
			<label for="description">Mô tả</label>
			<textarea id="description" name="description"
				placeholder="Mô tả về video của bạn..." maxlength="1000"></textarea>
		</div>

		<div class="form-group">
			<label>Video file *</label> <label for="videoFile"
				class="file-upload">
				<div class="file-upload-icon">📁</div>
				<div class="file-upload-text">
					<strong>Click để chọn file</strong>
				</div>
				<div class="file-upload-hint">Hỗ trợ: MP4, AVI, MOV, MKV (Tối
					đa 100MB)</div>
			</label> <input type="file" id="videoFile" name="videoFile" accept="video/*"
				required />

		</div>
		
		
		<%
		if(err != null && !err.isEmpty()){
			
		
		%>
		<p style ="color: red">
		<%= err %>
		</p>
		<%} %>
		<!-- Buttons -->
		<div class="btn-group">
			<button type="button" class="btn-cancel"
				onclick="window.location.href='video-list'">Hủy</button>
			<button type="submit" class="btn-submit" id="submitBtn">
				Upload Video</button>
		</div>
	</form>
</body>
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: Arial, sans-serif;
	min-height: 100vh;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	padding: 20px;
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

input[type="text"], textarea {
	width: 100%;
	padding: 12px 15px;
	border: 2px solid #e0e0e0;
	border-radius: 8px;
	font-size: 14px;
	transition: border-color 0.3s;
}

input[type="text"]:focus, textarea:focus {
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

/* input[type="file"] {
      display: none;
    } */
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
	padding: 12px 15px;
	border-radius: 8px;
	margin-bottom: 20px;
	display: none;
}

.error-message.show {
	display: block;
}
</style>
</html>
