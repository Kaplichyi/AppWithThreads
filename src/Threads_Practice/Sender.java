package Threads_Practice;

import javax.jms.*;
 
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
	static Integer ItemCount = null;
	static class parallelReq extends Thread // Параллельный поток для подсчета количества товаров в БД
	{
		@Override
		public void run()
		{
			DBReader DBReader = new DBReader();
			try {
				ItemCount = DBReader.countItems();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
     
    // default URL: tcp://localhost:61616"
    private static String subject = "JCG_QUEUE"; // Queue Name 
     
    static parallelReq trItemCounter;
    
	public static void main(String[] args) throws JMSException {
		
		trItemCounter = new parallelReq();
		trItemCounter.start();
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(subject); 
         
        MessageProducer producer = session.createProducer(destination);
        
        DBReader DBReader = new DBReader();
        String result = null;
		try {
			result = DBReader.getItems();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(trItemCounter.isAlive()) // Проверка завершил ли работу параллельный поток
		{
			try {
				trItemCounter.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String finalmess = "Всего товаров - " + ItemCount + "\nНайденные товары {\n" + result + "}";
        TextMessage message = session.createTextMessage(finalmess);
         
        producer.send(message);
        
        System.out.println(message.getText());
        connection.close();
	}
}
