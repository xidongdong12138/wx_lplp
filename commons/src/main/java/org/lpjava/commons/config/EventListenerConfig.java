package org.lpjava.commons.config;

import java.util.ArrayList;
import java.util.List;

import org.lpjava.commons.domain.InMessage;
import org.lpjava.commons.domain.event.EventInMessage;
import org.lpjava.commons.service.JsonRedisSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface EventListenerConfig extends 
//表示命令行执行的程序，要求实现一个run方法，在run方法里面启动一个线程等待停止通知
		CommandLineRunner, //
		// 当mvn spring-boot:stop命令执行以后，会发送一个停止的命令给Spring容器。
		// Spring容器在收到此命令以后，会执行停止，于是在停止之前会调用DisposableBean里面的方法。
		DisposableBean {
	public final Object stopMonitor = new Object();

	@Override
	public default void run(String... args) throws Exception {
		new Thread(() -> {
			synchronized (stopMonitor) {
				try {
					// 等待停止通知
					stopMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public default void destroy() throws Exception {
		// 发送停止通知
		synchronized (stopMonitor) {
			stopMonitor.notify();
		}
	}
	
	@Bean
	public default RedisTemplate<String, InMessage> inMessageTemplate(//
			@Autowired RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, InMessage> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

//		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new JsonRedisSerializer());
//		template.setDefaultSerializer(new JsonRedisSerializer());

		return template;
	}

	@Bean//监听器
	public default MessageListenerAdapter messageListener(@Autowired RedisTemplate<String, InMessage> inMessageTemplate) {
		MessageListenerAdapter adapter = new MessageListenerAdapter();
		adapter.setSerializer(inMessageTemplate.getValueSerializer());

		adapter.setDelegate(this);
		
		adapter.setDefaultListenerMethod("handle");

		return adapter;
	}

	@Bean//监听器容器
	public default RedisMessageListenerContainer messageListenerContainer(//
			@Autowired RedisConnectionFactory redisConnectionFactory, //
			@Autowired MessageListener l) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);


		List<Topic> topics = new ArrayList<>();


		topics.add(new ChannelTopic("lplp_1_event"));
		container.addMessageListener(l, topics);

		return container;
	}

	public void handle(EventInMessage msg);

}
