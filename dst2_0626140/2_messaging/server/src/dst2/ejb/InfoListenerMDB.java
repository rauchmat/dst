package dst2.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.Topic;

import dst2.model.Task;

@MessageDriven(mappedName = "queue.dst.InfoQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class InfoListenerMDB  extends BaseMDB {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(InfoListenerMDB.class
			.getName());
	
	@Resource(mappedName = "topic.dst.SchedulerTopic")
	protected Topic schedulerTopic;

	@Override
	public void onMessage(Message message) {
		try {
			Long taskId = message.getLongProperty("taskId");
			logger.info("Loading task: " + taskId);
			Task task = em.find(Task.class, taskId);
			sendTextMessage(schedulerTopic, task.toString());
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			mdc.setRollbackOnly();
		}
	}

}
