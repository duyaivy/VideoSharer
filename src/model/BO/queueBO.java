package model.BO;


import model.Bean.VideoQueue;
import model.DAO.VideoQueueDAO;


public class queueBO {
	VideoQueueDAO dao;
	static queueBO bo = null;

	public queueBO() {
		dao = new VideoQueueDAO();
	}

	public static queueBO getInstance() {
		if (bo == null)
			bo = new queueBO();
		return bo;
	}
	public VideoQueue getNextPendingTask() {
		return dao.getNextPendingTask();
	}

	public boolean updateStatus(int queueId, String status) {
		return dao.updateStatus(queueId, status);
	}
	
	    
	  
	    public boolean incrementCount(int queueId) {
	       return dao.incrementCount(queueId);
	    }
	    
	  
	   
	    
	    
	    public int countPendingTasks() {
	        return dao.countPendingTasks();
	    }
}