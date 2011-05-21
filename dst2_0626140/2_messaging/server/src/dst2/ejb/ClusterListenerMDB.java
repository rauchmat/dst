package dst2.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;

import dst2.model.Task;
import dst2.model.TaskComplexity;
import dst2.model.TaskStatus;

@MessageDriven(mappedName = "queue.dst.ClusterQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class ClusterListenerMDB extends BaseMDB {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(AssignListenerMDB.class
			.getName());
	
	@Resource(mappedName = "topic.dst.SchedulerTopic")
	private Topic schedulerTopic;

	@Override
	public void onMessage(Message message) {
		try {
			Long taskId = message.getLongProperty("taskId");
			Task task = em.find(Task.class, taskId);
			task.setRatedBy(message.getStringProperty("ratedBy"));
			
			String complexityString = message.getStringProperty("complexity");
			if (complexityString != null) {
				TaskComplexity complexity = Enum.valueOf(TaskComplexity.class,
						complexityString);
				task.setComplexity(complexity);
				// TODO send to computers
			} else {
				task.setStatus(TaskStatus.PROCESSING_NOT_POSSIBLE);
				sendTextMessage(schedulerTopic, "Task denied: " + task);
			}

		} catch (JMSException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			mdc.setRollbackOnly();
		}
	}

}
