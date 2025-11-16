package workers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class initialThread implements ServletContextListener{
	 private Thread workerThread;
	    private VideoQueueWorker worker;
	    
	    @Override
	    public void contextInitialized(ServletContextEvent sce) {
	    
	        
	
	        String webappPath = sce.getServletContext().getRealPath("/");
	        System.setProperty("webapp.path", webappPath);
	        
	        worker = new VideoQueueWorker();
	        workerThread = new Thread(worker);
	        workerThread.setDaemon(true); // Daemon thread
	        workerThread.start();
	        
	        
	    }
	    
	    @Override
	    public void contextDestroyed(ServletContextEvent sce) {
	        System.out.println("stop");
	        
	        if (worker != null) {
	            worker.stop();
	        }
	        
	        if (workerThread != null) {
	            try {
	                workerThread.join(5000); 
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        
	        System.out.println("stopped success!");
	    }
}
