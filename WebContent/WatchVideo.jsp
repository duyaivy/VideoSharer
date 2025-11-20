<%@page import="model.Bean.Comment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="helpers.ViewPath"%>
<%@page import="model.Bean.Video"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%
Video video = (Video) request.getAttribute("video");
if (video == null) {
	response.sendRedirect(request.getContextPath() + ViewPath.resolve("Login"));
	return;
}
//if (!video.getStatus().equals("done")) {
//	request.setAttribute("err", "Video bạn truy cập đang trong quá trình xử lý hoạc bị lỗi!");
//	request.getRequestDispatcher(ViewPath.resolve("Error")).forward(request, response);
//	return;
//}
String videoUrl = request.getContextPath() + "/" + video.getPath();
int user_id = 1;
Integer likeCount = (Integer) request.getAttribute("like_count");
Integer dislikeCount = (Integer) request.getAttribute("dislike_count");
String userLikeStatus = (String) request.getAttribute("user_like_status");

ArrayList<Video> vdList = (ArrayList<Video>) request.getAttribute("video_list");

ArrayList<Comment> cmtList = (ArrayList<Comment>) request.getAttribute("comment_list");

if (likeCount == null)
	likeCount = 0;
if (dislikeCount == null)
	dislikeCount = 0;
%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><%=video.getTitle()%>| Watch video</title>

<link href="https://vjs.zencdn.net/7.20.3/video-js.css" rel="stylesheet" />

<style>
body {
	margin: 0;
	padding: 20px;
	font-family: Roboto, Arial, sans-serif;
	color: #000;
}

.action-button {
	background-color: #f2f2f2;
	color: #0f0f0f;
	padding: 10px 16px;
	border-radius: 18px;
	font-size: 14px;
	font-weight: 500;
	cursor: pointer;
	display: flex;
	align-items: center;
	gap: 8px;
	transition: all 0.2s ease;
	border: none;
	user-select: none;
}

.cmt-time {
	display: none;
}

.comment-list {
	margin: 20px auto;
	padding: 24px;
	background-color: #fff;
	border-radius: 12px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.comment-list h2 {
	font-size: 20px;
	font-weight: 600;
	color: #0f0f0f;
	margin-bottom: 24px;
	padding-bottom: 0;
	border-bottom: none;
}

.comment-form {
	margin-bottom: 32px;
	display: flex;
	gap: 12px;
	align-items: flex-start;
}

.comment-avatar {
	width: 40px;
	height: 40px;
	border-radius: 50%;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	display: flex;
	align-items: center;
	justify-content: center;
	color: white;
	font-weight: 600;
	font-size: 16px;
	flex-shrink: 0;
}

.comment-input-wrapper {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 12px;
}

.comment-input {
	width: 100%;
	padding: 12px 16px;
	border: 1px solid #e0e0e0;
	border-radius: 8px;
	font-size: 14px;
	font-family: Roboto, Arial, sans-serif;
	transition: all 0.3s ease;
	outline: none;
	box-sizing: border-box;
}

.comment-input:focus {
	border-color: #065fd4;
	box-shadow: 0 0 0 3px rgba(6, 95, 212, 0.1);
}

.comment-input::placeholder {
	color: #aaa;
}

.comment-submit-btn {
	align-self: flex-end;
	background-color: #065fd4;
	color: white;
	border: none;
	padding: 10px 24px;
	border-radius: 18px;
	font-size: 14px;
	font-weight: 500;
	cursor: pointer;
	transition: all 0.2s ease;
}

.comment-submit-btn:hover {
	background-color: #0456be;
}

.comment-submit-btn:active {
	transform: scale(0.95);
}

.comments-container {
	display: flex;
	flex-direction: column;
	gap: 20px;
}

.comment-item {
	display: flex;
	gap: 16px;
	padding: 16px 0;
	border-bottom: 1px solid #f0f0f0;
	transition: background-color 0.2s ease;
}

.comment-item:last-child {
	border-bottom: none;
}

.comment-item:hover {
	background-color: #fafafa;
	border-radius: 8px;
	padding: 16px 12px;
}

.comment-avatar-small {
	width: 40px;
	height: 40px;
	border-radius: 50%;
	background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
	display: flex;
	align-items: center;
	justify-content: center;
	color: white;
	font-weight: 600;
	font-size: 14px;
	flex-shrink: 0;
}

.comment-content {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 8px;
}

.comment-header {
	display: flex;
	align-items: center;
	gap: 8px;
	flex-wrap: wrap;
}

.comment-user {
	font-weight: 600;
	color: #0f0f0f;
	margin: 0;
	font-size: 13px;
}

.comment-email {
	font-size: 12px;
	color: #606060;
	margin: 0;
}

.comment-time {
	font-size: 12px;
	color: #606060;
	margin: 0;
}

.comment-message {
	font-size: 14px;
	line-height: 1.6;
	color: #0f0f0f;
	margin: 0;
	word-wrap: break-word;
}

.action-button:hover {
	background-color: #e5e5e5;
}

.action-button .icon {
	font-size: 18px;
}

#likeCheckbox:checked+.like-btn {
	background-color: #065fd4;
	color: #fff;
}

#likeCheckbox:checked+.like-btn:hover {
	background-color: #0456be;
}

#dislikeCheckbox:checked+.dislike-btn {
	background-color: #f44336;
	color: #fff;
}

#dislikeCheckbox:checked+.dislike-btn:hover {
	background-color: #d32f2f;
}

.action-button:active {
	transform: scale(0.95);
}

.action-button.processing {
	opacity: 0.6;
	pointer-events: none;
}

.container {
	max-width: 1700px;
	margin: 0 auto;
	display: flex;
	gap: 24px;
}

.main-content {
	flex-grow: 1;
	min-width: 0;
}

.video-wrapper {
	height: 66.67vh;
	min-height: 400px;
	background-color: #000;
	border-radius: 12px;
	overflow: hidden;
	margin-bottom: 20px;
}

.video-player {
	width: 100%;
	height: 100%;
}

.related-sidebar {
	width: 420px;
	flex-shrink: 0;
}

.video-info {
	background: #eee;
	padding: 20px;
	border-radius: 12px;
}

.video-title {
	font-size: 24px;
	font-weight: 700;
	margin-bottom: 8px;
}

.stats-and-actions {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 16px;
}

.video-stats {
	color: #aaa;
	font-size: 14px;
}

.video-actions {
	display: flex;
	gap: 10px;
}

.related-video-item {
	display: flex;
	margin-bottom: 10px;
	cursor: pointer;
	list-style-type: none;
	color: #000;
	text-decoration: none;
	transition: transform 0.3s ease;
	padding: 5px;
}
.related-video-item:hover {
	transform: translateY(-5px);
	background-color: rgba(0,0,0,0.2)
}
.thumbnail {
	width: 168px;
	height: 94px;
	border-radius: 8px;
	overflow: hidden;
	margin-right: 8px;
	flex-shrink: 0;
}

.thumbnail img {
	width: 100%;
	height: 100%;
	object-fit: cover;
}

.video-details {
	display: flex;
	flex-direction: column;
}

.related-title {
	font-size: 14px;
	font-weight: 500;
	line-height: 1.3;
	max-height: 2.6em;
	overflow: hidden;
	list-style: none;
}

.related-channel, .related-stats {
	font-size: 13px;
	color: #000;
	margin-top: 4px;
	list-style: none;
}

.vjs-quality-selector {
	position: relative;
}

.vjs-quality-selector button {
	cursor: pointer;
}

.vjs-quality-selector .vjs-menu {
	left: auto !important;
	right: 0 !important;
}

.vjs-menu .vjs-menu-content {
	background-color: rgba(43, 51, 63, 0.9);
	border-radius: 5px;
	max-height: 15em;
	overflow-y: auto;
}

.vjs-menu li {
	padding: 8px 12px;
	color: #fff;
	font-size: 13px;
	cursor: pointer;
	border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.vjs-menu li:last-child {
	border-bottom: none;
}

.vjs-menu li:hover {
	background-color: rgba(255, 255, 255, 0.1);
}

.vjs-menu li.vjs-selected {
	background-color: rgba(33, 150, 243, 0.3);
	color: #fff;
	font-weight: 600;
}

.vjs-menu li.vjs-selected::before {
	content: '✓ ';
	color: #2196F3;
	margin-right: 5px;
}
</style>
</head>
<body>
	<div class="container">

		<div class="main-content">
			<div class="video-wrapper">
			    <% if (!video.getStatus().equals("done")) { %>
			        <div id="processing-msg" class="video-processing-msg">
 			           Video đang được xử lý, vui lòng đợi...
    			    </div>
   				     <video id="videoPlayer"
  		             class="video-js vjs-big-play-centered video-player"
  		             controls preload="auto"
 		              style="display:none;">
 			       </video>
			    <% } else { %>    
 			       <video id="videoPlayer"
 			           class="video-js vjs-big-play-centered video-player" controls
       				     preload="auto"
       				     poster="<%=video.getImg() != null ? request.getContextPath() + "/" + video.getImg() : ""%>">
      			      	<source src="<%=videoUrl%>" type="application/x-mpegURL">
        			</video>
    			<% } %>
			</div>


			<div class="video-info">
				<h1 class="video-title"><%=video.getTitle()%></h1>

				<div class="stats-and-actions">
					<div class="video-stats">
						<span style="margin-right: 20px;"><%=video.getView()%> lượt
							xem</span> <span>Đăng ngày: 19/11/2025</span>
					</div>

					<div class="video-actions">
						<input type="checkbox" id="likeCheckbox"
							<%="like".equals(userLikeStatus) ? "checked" : ""%>
							style="display: none;"> <label for="likeCheckbox"
							class="action-button like-btn" onclick="handleClickLike(event)">
							<span class="icon">👍</span> <span id="likeCount"><%=likeCount%></span>
							<span>Thích</span>
						</label> <input type="checkbox" id="dislikeCheckbox"
							<%="dislike".equals(userLikeStatus) ? "checked" : ""%>
							style="display: none;"> <label for="dislikeCheckbox"
							class="action-button dislike-btn"
							onclick="handleClickDislike(event)"> <span class="icon">👎</span>
							<span id="dislikeCount"><%=dislikeCount%></span> <span>Không
								thích</span>
						</label>

						<div class="action-button share-btn">
							<span class="icon">🔗</span> <span>Chia sẻ</span>
						</div>
					</div>
				</div>

				<%
				if (video.getDescription() != null && !video.getDescription().trim().isEmpty()) {
				%>
				<div class="video-description">
					<%=video.getDescription()%>
				</div>
				<%
				}
				%>
			</div>
			<div class="comment-list">
				<h2>💬 Bình Luận (<%=cmtList.size()%>)</h2>
				
				<form action="comment" method="post" class="comment-form">
					<div class="comment-avatar">U</div>
					<div class="comment-input-wrapper">
						<input name="user_id" type="hidden" value="<%= user_id %>">
						<input name="video_id" type="hidden" value="<%= video.getVideoId() %>">
						<input name="message" type="text" class="comment-input" 
							placeholder="Viết bình luận công khai..." required>
						<button type="submit" class="comment-submit-btn">Bình luận</button>
					</div>
				</form>
				
				<div class="comments-container">
				<%
				for (Comment c : cmtList) {

					// Tính thời gian "x giờ trước / x phút trước"
					long currentMillis = System.currentTimeMillis();
					long commentMillis = c.getCreateAt().getTime();
					long diff = currentMillis - commentMillis;

					long diffMinutes = diff / (60 * 1000);
					long diffHours = diff / (60 * 60 * 1000);
					long diffDays = diff / (24 * 60 * 60 * 1000);

					String timeAgo;
					if (diffMinutes < 1) {
						timeAgo = "Vừa xong";
					} else if (diffMinutes < 60) {
						timeAgo = diffMinutes + " phút trước";
					} else if (diffHours < 24) {
						timeAgo = diffHours + " giờ trước";
					} else {
						timeAgo = diffDays + " ngày trước";
					}
					
					// Lấy chữ cái đầu tiên của tên người dùng
					String firstLetter = c.getUserName() != null && !c.getUserName().isEmpty() 
						? c.getUserName().substring(0, 1).toUpperCase() 
						: "?";
				%>
				<div class="comment-item">
					<div class="comment-avatar-small"><%=firstLetter%></div>
					<div class="comment-content">
						<div class="comment-header">
							<p class="comment-user"><%=c.getUserName()%></p>
							<p class="comment-email"><%=c.getUserEmail()%></p>
							<p class="comment-time">• <%=timeAgo%></p>
						</div>
						<p class="comment-message"><%=c.getMessage()%></p>
					</div>
				</div>
				<%
				}
				%>
				</div>
			</div>

		</div>

		<div class="related-sidebar">
			<h3>Video đề xuất</h3>
			<div class="related-video-list">
				<%
				for (Video vd : vdList) {
				%>
				<a href="<%=ViewPath.getWatchLink(vd.getVideoId())%>"
					class="related-video-item">
					<div class="thumbnail">
						<img
							src="<%=vd.getImg() != null ? request.getContextPath() + "/" + vd.getImg() : ""%>"
							alt="Thumbnail">
					</div>
					<div class="video-details">
						<div class="related-title"><%=vd.getTitle()%></div>
						<div class="related-channel"><%=vd.getStatus().toUpperCase()%></div>
						<div class="related-stats"><%=vd.getView()%>
							lượt xem •
							<%=vd.getCreateAt()%></div>
					</div>
				</a>
				<%
				}
				%>


			</div>
		</div>
	</div>

	<script src="https://vjs.zencdn.net/7.20.3/video.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/videojs-contrib-quality-levels@2.2.0/dist/videojs-contrib-quality-levels.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/videojs-hls-quality-selector@1.1.4/dist/videojs-hls-quality-selector.min.js"></script>
	<script>
var videoId = <%=video.getVideoId()%>;
var userId = 1;
function handleClickLike(e) {
    
   
    const likeCheckbox = document.getElementById('likeCheckbox');
    const dislikeCheckbox = document.getElementById('dislikeCheckbox');
    const likeBtn = document.querySelector('.like-btn');
    
    likeBtn.classList.add('processing');
    
    const isCurrentlyLiked = likeCheckbox.checked;
    var type = 'like';
    const params = new URLSearchParams();
    params.append('user_id', userId);
    params.append('video_id', videoId);
    params.append('type', type);

    fetch('<%=request.getContextPath()%>/api/like', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString() 
    })
    .then(response => response.json())
    .then(data => {
        if (data.status) {
            document.getElementById('likeCount').textContent = data.likeCount;
            document.getElementById('dislikeCount').textContent = data.disLikeCount;
            
            if (data.userStatus === 'like') {
                likeCheckbox.checked = true;
                dislikeCheckbox.checked = false;
            } else if (data.userStatus === 'dislike') {
                likeCheckbox.checked = false;
                dislikeCheckbox.checked = true;
            } else {
                likeCheckbox.checked = false;
                dislikeCheckbox.checked = false;
            }
        } else {
            alert(data.message);
            likeCheckbox.checked = isCurrentlyLiked;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra!');
        likeCheckbox.checked = isCurrentlyLiked;
    })
    .finally(() => {
        likeBtn.classList.remove('processing');
    });
    
    return false;
}

function handleClickDislike(e) {
    e.preventDefault();
    
    const likeCheckbox = document.getElementById('likeCheckbox');
    const dislikeCheckbox = document.getElementById('dislikeCheckbox');
    const dislikeBtn = document.querySelector('.dislike-btn');
    
    dislikeBtn.classList.add('processing');
    
    const isCurrentlyDisliked = dislikeCheckbox.checked;
    const type = 'dislike';
    
    const params = new URLSearchParams();
    params.append('user_id', userId);
    params.append('video_id', videoId);
    params.append('type', type);

    fetch('<%=request.getContextPath()%>/api/like', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString() 
    })
    .then(response => response.json())
    .then(data => {
        if (data.status) {
            document.getElementById('likeCount').textContent = data.likeCount;
            document.getElementById('dislikeCount').textContent = data.dislikeCount;
            
            if (data.userStatus === 'like') {
                likeCheckbox.checked = true;
                dislikeCheckbox.checked = false;
            } else if (data.userStatus === 'dislike') {
                likeCheckbox.checked = false;
                dislikeCheckbox.checked = true;
            } else {
                likeCheckbox.checked = false;
                dislikeCheckbox.checked = false;
            }
        } else {
            alert(data.message);
            likeCheckbox.checked = isCurrentlyLiked;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra!');
        likeCheckbox.checked = isCurrentlyLiked;
    })
    .finally(() => {
        likeBtn.classList.remove('processing');
    });
    
    
    return false;
}
const videoId = <%=video.getVideoId()%>;
const ctx = "<%=request.getContextPath()%>";

var player = videojs('videoPlayer', {
    controls: true,
    autoplay: false,
    preload: 'auto',
    fluid: false,
    responsive: true,
    html5: {
        vhs: {
            overrideNative: true,
            enableLowInitialPlaylist: true
        },
        nativeVideoTracks: false,
        nativeAudioTracks: false,
        nativeTextTracks: false
    }
});

player.ready(function() {
    player.hlsQualitySelector({
        displayCurrentQuality: true
    });
 // Chỉ bật realtime nếu video chưa done
    <% if (!video.getStatus().equals("done")) { %>

    const ws = new WebSocket("ws://localhost:8080/VideoSharer/video-status");

    ws.onmessage = function(e){
        const data = JSON.parse(e.data);

        if (data.videoId !== videoId) return;

        if (data.status === "done") {

            const msg = document.getElementById("processing-msg");
            if (msg) msg.style.display = "none";

            const vp = document.getElementById("videoPlayer");
            vp.style.display = "block";

            player.src({
                src: ctx + "/<%=video.getPath()%>",
                type: "application/x-mpegURL"
            });

            player.play();
        }
    };

    <% } %>

});
</script>
<div id="toast" class="toast"></div>

<script>
function showToast(msg){
    const t = document.getElementById("toast");
    t.innerText = msg;
    t.classList.add("show");
    setTimeout(()=>t.classList.remove("show"), 3000);
}
</script>

</body>
</html>
