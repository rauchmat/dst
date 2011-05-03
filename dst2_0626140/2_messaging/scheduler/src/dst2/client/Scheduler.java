package dst2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Scheduler implements MessageListener {

	private ConnectionFactory connectionFactory;
	private Session session;
	private Queue assignQueue;
	private Queue infoQueue;
	private Topic schedulerTopic;

	private BufferedReader in;
	private Connection connection;

	public static void main(String[] args) throws IOException, JMSException,
			NamingException {
		new Scheduler().run();
	}

	public Scheduler() throws JMSException, NamingException {
		connectionFactory = (ConnectionFactory) InitialContext
				.doLookup("dst.Factory");
		assignQueue = (Queue) InitialContext.doLookup("queue.dst.AssignQueue");
		infoQueue = (Queue) InitialContext.doLookup("queue.dst.InfoQueue");
		schedulerTopic = (Topic) InitialContext
				.doLookup("topic.dst.SchedulerTopic");

		connection = connectionFactory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		in = new BufferedReader(new InputStreamReader(System.in));

		MessageConsumer consumer = session.createConsumer(schedulerTopic);
		consumer.setMessageListener(this);
		connection.start();
	}

	public void run() throws IOException {
		System.out.println("Listening to user input...");

		String line = null;
		while ((line = in.readLine()) != null) {
			handleCommand(line);
		}
	}

	private void handleCommand(String line) {
		String[] token = line.split(" ");
		if (token.length == 0) {
			System.out.println("Enter command.");
			return;
		}

		if (token[0].equals("stop")) {
			stop();
		} else if (token[0].equals("assign")) {
			Long jobId = null;
			try {
				jobId = Long.parseLong(token[1]);
			} catch (NumberFormatException e) {
				System.out.println("jobId must be a number");
			}
			performAssign(jobId);
		} else if (token[0].equals("info")) {
			Long taskId = null;
			try {
				taskId = Long.parseLong(token[1]);
			} catch (NumberFormatException e) {
				System.out.println("jobId must be a number");
			}
			performInfo(taskId);
		}
		else {
			System.out.println("Invalid command.");
			return;
		}
			

		System.out.println("Command executed.");;
	}

	private void stop() {
		try {
			connection.close();
			session.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private void performInfo(Long taskId) {
		try {
			MessageProducer messageProducer = session.createProducer(infoQueue);
			ObjectMessage message = session.createObjectMessage();
			message.setLongProperty("taskId", taskId);
			messageProducer.send(message);
			messageProducer.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void performAssign(Long jobId) {
		try {
			MessageProducer messageProducer = session
					.createProducer(assignQueue);
			ObjectMessage message = session.createObjectMessage();
			message.setLongProperty("jobId", jobId);
			messageProducer.send(message);
			messageProducer.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println("Message received: " + textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
