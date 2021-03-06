package au.id.mcmaster.neo4j.changelistener.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.id.mcmaster.neo4j.changelistener.dto.ChangeEvent;
import au.id.mcmaster.neo4j.changelistener.dto.ChangeEventNodeChanged;
import au.id.mcmaster.neo4j.changelistener.dto.KeyValue;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;

public class RedisService {
	public static final String CHANGE_EVENT_TOPIC = "change-events";
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private RedisConnectionFactory connectionFactory;
	
	public RedisService()
	{
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost",6379);
		this.connectionFactory = new JedisConnectionFactory(config);
	}
	
	public void sendChangeEvent(ChangeEvent changeEvent) {
		try
		{
			String messageString = marshalChangeEvent(changeEvent);
			
			sendMessage(messageString);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Could not send the ChangeEvent", e);
		}
	}
	
	protected ChangeEvent unmarshalChangeEvent(String message) 
			throws JsonParseException, JsonMappingException, IOException
	{
		ChangeEventNodeChanged object = objectMapper.readValue(message.getBytes(), ChangeEventNodeChanged.class);
		System.out.println("ChangeEventNodeChanged: " + object);
		return object;
	}
	protected String marshalChangeEvent(ChangeEvent changeEvent) 
			throws JsonGenerationException, JsonMappingException, IOException {
		boolean canSerialize = objectMapper.canSerialize(changeEvent.getClass());
		System.out.println("Can Serialize: " + canSerialize);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		objectMapper.writeValue(baos, changeEvent);
		return baos.toString();
	}

	private void sendMessage(String messageString) {
		StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
		System.out.println("Sending message to Redis: " + messageString);
		template.convertAndSend(CHANGE_EVENT_TOPIC, messageString);
	}

	public void registerListener(ChangeEventListener listener) {
//		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//		container.setConnectionFactory(connectionFactory);
//		ChangeEventAdapter changeEventAdapter = new ChangeEventAdapter(listener);
//		MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(changeEventAdapter, "receiveMessage");
//		container.addMessageListener(listenerAdapter, new PatternTopic(CHANGE_EVENT_TOPIC));
		
		final ChangeEventAdapter changeEventAdapter = new ChangeEventAdapter(listener);
		final BinaryJedisPubSub binaryJedisPubSub = new BinaryJedisPubSub() {
	    		public void onMessage(byte[] channel, byte[] message) {
	    			System.out.println("Received <" + new String(message) + ">");
	    			changeEventAdapter.receiveMessage(new String(message));
	    		}
	    	};
		new Thread(new Runnable() {
	    		public void run() {
	        		Jedis jedis = new Jedis("localhost");
	        		jedis.subscribe(binaryJedisPubSub,CHANGE_EVENT_TOPIC.getBytes());
	        		jedis.close();
	        	}
	    	}).start();
	}
	
	public static interface ChangeEventListener
	{
		public void process(ChangeEvent changeEvent);
	}
	
	private class ChangeEventAdapter {
	    RedisService.ChangeEventListener changeEventListener;
	    
	    @Autowired
	    public ChangeEventAdapter(RedisService.ChangeEventListener changeEventListener) {
	    		this.changeEventListener = changeEventListener;
	    }

	    @SuppressWarnings("unused")
		public void receiveMessage(String message) {
	        System.out.println("Received <" + message + ">");
			try {
				ChangeEvent changeEvent = RedisService.this.unmarshalChangeEvent(message);
				changeEventListener.process(changeEvent);
			} catch (IOException e) {
				throw new RuntimeException("Could not process message: " + message, e);
			}
	        
	    }
	}
}


