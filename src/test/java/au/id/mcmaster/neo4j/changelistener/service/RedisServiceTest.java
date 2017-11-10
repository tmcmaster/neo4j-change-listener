package au.id.mcmaster.neo4j.changelistener.service;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import au.id.mcmaster.neo4j.changelistener.dto.ChangeEvent;
import au.id.mcmaster.neo4j.changelistener.dto.ChangeEventNodeChanged;
import au.id.mcmaster.neo4j.changelistener.dto.KeyValue;

public class RedisServiceTest {

	@Test
	public void marshalTest() {
		try
		{
			RedisService redisService = new RedisService();
			ChangeEvent changeEvent = new ChangeEventNodeChanged(10);
			changeEvent.addProperty("a","b");
			changeEvent.addProperty("c","d");
			String message = redisService.marshalChangeEvent(changeEvent);
			System.out.println("Message: " + message);
			ChangeEvent object = redisService.unmarshalChangeEvent(message);
			System.out.println("ChangeEventNodeChanged: " + object);
			Iterator<KeyValue> iterator = object.getProperties();
			System.out.println(String.format("%s(%s)", object.getType().toString(), object.getId()));
			while (iterator.hasNext()) {
				KeyValue keyValue = iterator.next();
				System.out.println(String.format("  - %s(%s)",keyValue.getKey(), keyValue.getValue()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
}
