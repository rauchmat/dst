package dst2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Cluster {

	private ConnectionFactory connectionFactory;
	private Session session;
	private Queue clusterQueue;
	private Topic clusterTopic;

	private Long currentTask;
	private String name;
	private Connection connection;
	private Thread receiverThread;

	public static void main(String[] args) throws IOException, JMSException,
			NamingException {
		new Cluster(args[0]).run();
	}

	public Cluster(String name) throws JMSException, NamingException {
		connectionFactory = (ConnectionFactory) InitialContext
				.doLookup("dst.Factory");
		clusterQueue = (Queue) InitialContext
				.doLookup("queue.dst.ClusterQueue");
		clusterTopic = (Topic) InitialContext
				.doLookup("topic.dst.ClusterTopic");

		connection = connectionFactory.createConnection();
		this.name = name;
	}

	public void run() throws IOException, JMSException {
		connection.start();
		startReceiverThread();

		System.out.println("Listening to user input...");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while ((line = in.readLine()) != null) {
			handleCommand(line);
		}
	}

	private void startReceiverThread() throws JMSException {
		currentTask = null;
		session = connection.createSession(true, Session.SESSION_TRANSACTED);
		final MessageConsumer consumer = session.createConsumer(clusterTopic);
		receiverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("Waiting for task to rate...");
					Message message = consumer.receive();
					if (receiverThread.isInterrupted())
						return;
					currentTask = message.getLongProperty("taskId");
					System.out.println("Task to rate: "
							+ message.getStringProperty("task"));
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		receiverThread.start();
	}

	private void handleCommand(String line) {
		String[] token = line.split(" ");
		if (token.length == 0) {
			System.out.println("Enter command.");
			return;
		}

		if (token[0].equals("stop")) {
			stop();
		} else if (token[0].equals("accept")) {
			perform(token[1]);
		} else if (token[0].equals("deny")) {
			perform(null);
		} else {
			System.out.println("Invalid command.");
			return;
		}

		System.out.println("Command executed.");
	}

	private void stop() {
		try {
			receiverThread.interrupt();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private void perform(String complexity) {
		if (currentTask == null) {
			System.out.println("No task to rate.");
			return;
		}

		try {
			MessageProducer messageProducer = session
					.createProducer(clusterQueue);
			ObjectMessage message = session.createObjectMessage();
			message.setLongProperty("taskId", currentTask);
			message.setStringProperty("ratedBy", name);
			if (complexity != null)
				message.setStringProperty("complexity", complexity);
			message.setStringProperty("command", "accept");
			messageProducer.send(message);
			messageProducer.close();
			session.commit();
			startReceiverThread();
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
