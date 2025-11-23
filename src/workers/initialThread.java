package workers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import helpers.EnvLoader;

@WebListener
public class initialThread implements ServletContextListener {
    private static final int NUM_WORKERS = 3;
    private List<Thread> workerThreads;
    private List<VideoQueueWorker> workers;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
       
        EnvLoader.load();
      
        String webappPath = sce.getServletContext().getRealPath("/");
        System.setProperty("webapp.path", webappPath);
   
        
      
        workerThreads = new ArrayList<>();
        workers = new ArrayList<>();
       
        for (int i = 0; i < NUM_WORKERS; i++) {
            VideoQueueWorker worker = new VideoQueueWorker();
            Thread thread = new Thread(worker, "VideoWorker-" + (i + 1));
            thread.setDaemon(true); 
            thread.start();
            
            workers.add(worker);
            workerThreads.add(thread);
         
        }
 
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
     
        for (int i = 0; i < workers.size(); i++) {
            workers.get(i).stop();
          
        }
        
      
        for (int i = 0; i < workerThreads.size(); i++) {
            try {
                workerThreads.get(i).join(10000); 
               
            } catch (InterruptedException e) {
              
                workerThreads.get(i).interrupt();
            }
        }
        
     
    }
}