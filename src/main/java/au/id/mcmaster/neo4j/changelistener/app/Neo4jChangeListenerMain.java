package au.id.mcmaster.neo4j.changelistener.app;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import au.id.mcmaster.neo4j.changelistener.dto.ChangeEvent;
import au.id.mcmaster.neo4j.changelistener.service.RedisService;
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
 * jackson-databind-2.8.7.jar
 * jackson-core-2.8.7.jar
 * jackson-annotations-2.8.0.jar
 * 
 * @author tim
 *
 */
public class Neo4jChangeListenerMain {
    @SuppressWarnings({ "unused", "serial" })
	private static Map<String, Class<? extends Runnable>> RUN_CLASSES = new HashMap<String,Class<? extends Runnable>>() {{
		put("default", ListenForChanges.class);
	}};
    
	public static void main(String[] args) {
		//SimpleCommandLinePropertySource ps = new SimpleCommandLinePropertySource(new String[] {});
		String command = (args.length > 0 ? args[0] : "default");
		try
		{
			
		} catch (Exception e) {
			
		}
	}
}

class ListenForChanges implements Runnable
{
	private final String[] args;
	
	public ListenForChanges(String[] args) {
		this.args = args;
	}
	
	@Override
	public void run() {
		try
		{
			RedisService redisService = new RedisService();
			redisService.registerListener(new ListenToChanges());
			Thread.sleep(600000);
			System.out.println("RedisReceiverMain exiting.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	class ListenToChanges implements RedisService.ChangeEventListener {
		@Override
		public void process(ChangeEvent changeEvent) {
	        System.out.println("Received <" + changeEvent + ">");
		}
	}
}


