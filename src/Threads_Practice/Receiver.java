package Threads_Practice;

import javax.jms.*;
import javax.naming.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "JCG_QUEUE";
 
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
 
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
 
        Destination destination = session.createQueue(subject);
 
        MessageConsumer consumer = session.createConsumer(destination);
 
        Message message = consumer.receive();
 
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Полученные данные\n" + textMessage.getText());
        }
        connection.close();
    }

}
