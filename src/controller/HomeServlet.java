package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BO.videoBO;
import model.Bean.Video;


@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

       
		String pageS = request.getParameter("page");
		String sizeS = request.getParameter("size");
     
		try {

			int page = 1;
			int size = 20;

			if (pageS != null && !pageS.isEmpty()) {
				try {
					page = Integer.parseInt(pageS);
				} catch (NumberFormatException ignore) {
				}
			}

			if (sizeS != null && !sizeS.isEmpty()) {
				try {
					size = Integer.parseInt(sizeS);
				} catch (NumberFormatException ignore) {
				}
			}

			  
	        ArrayList<Video> videos = videoBO.getInstance().getVideoLastest(page, size);
	        request.setAttribute("videos", videos);
 
	        
	        request.setAttribute("isTrendingMode", false); 
	        request.getRequestDispatcher("/home.jsp").forward(request, response);
	    
			

		} catch (NumberFormatException e) {
			request.setAttribute("message", "Lỗi trong quá trình tải trang");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
    }
}