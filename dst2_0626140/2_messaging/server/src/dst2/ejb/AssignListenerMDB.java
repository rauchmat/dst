package dst2.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;

import dst2.model.Task;
import dst2.model.TaskComplexity;
import dst2.model.TaskStatus;

@MessageDriven(mappedName = "queue.dst.AssignQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class AssignListenerMDB extends SchedulerBaseMDB {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(AssignListenerMDB.class
			.getName());

	@Override
	public void onMessage(Message message) {
		try {
			Long jobId = message.getLongProperty("jobId");
			Task task = new Task();
			task.setJobId(jobId);
			task.setStatus(TaskStatus.ASSIGNED);
			task.setComplexity(TaskComplexity.UNRATED);
			em.persist(task);
			sendMessageToScheduler("Created task with id " + task.getId());
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			mdc.setRollbackOnly();
		}
	}

}
