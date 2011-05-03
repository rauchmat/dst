package dst2.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class SchedulerBaseMDB implements MessageListener,
		MessageDrivenBean {

	private static final long serialVersionUID = 1L;

	protected MessageDrivenContext mdc;

	@PersistenceContext
	protected EntityManager em;

	@Resource(mappedName = "dst.Factory")
	private ConnectionFactory connectionFactory;

	private Session session;

	@Resource(mappedName = "topic.dst.SchedulerTopic")
	private Topic topic;

	@PostConstruct
	public void init() throws JMSException {
		Connection connection = connectionFactory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	@Override
	public void ejbRemove() throws EJBException {

	}

	@Override
	public void setMessageDrivenContext(MessageDrivenContext ctx)
			throws EJBException {
		this.mdc = ctx;

	}

	public void sendMessageToScheduler(String message) throws JMSException {
		MessageProducer producer = session.createProducer(topic);
		TextMessage response = session.createTextMessage();
		response.setText(message);
		producer.send(response);
		producer.close();
	}

}
