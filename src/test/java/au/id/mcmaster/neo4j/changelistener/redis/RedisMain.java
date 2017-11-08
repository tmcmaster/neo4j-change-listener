package au.id.mcmaster.neo4j.changelistener.redis;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

/**
 * Required libraries:
 * 
 * spring-data-redis-2.0.1.RELEASE.jar
 * spring-tx-5.0.1.RELEASE.jar
 * spring-beans-5.0.1.RELEASE.jar
 * commons-pool2-2.4.2.jar
 * spring-core-5.0.1.RELEASE.jar
 * spring-data-commons-2.0.1.RELEASE.jar
 * aopalliance-1.0.jar
 * 
 * @author tim
 *
 */
public class RedisMain {
	public static void main(String[] args) {
		try
		{
			// configure the connection factory
			JedisShardInfo sharedInfo = new JedisShardInfo("localhost",6379);
			//JedisPoolConfig poolconfig = new JedisPoolConfig();;
			RedisConnectionFactory connectionFactory = new JedisConnectionFactory(sharedInfo);			

			// send message
			StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
			System.out.println("Sending message to Redis");
			template.convertAndSend("chat", "Hello from Redis!");
			System.out.println("About to exit!");
			System.exit(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

