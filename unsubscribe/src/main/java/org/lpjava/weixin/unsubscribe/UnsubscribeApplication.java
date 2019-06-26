package org.lpjava.weixin.unsubscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.lpjava.commons.config.EventListenerConfig;
import org.lpjava.commons.domain.InMessage;
import org.lpjava.commons.domain.event.EventInMessage;
import org.lpjava.commons.processors.EventMessageProcessors;
import org.lpjava.commons.service.JsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@ComponentScan("org.lpjava")
@EnableJpaRepositories("org.lpjava")
@EntityScan("org.lpjava")
public class UnsubscribeApplication implements EventListenerConfig,ApplicationContextAware {
//CommandLineRunner在RUN方法里启动一个线程等待程序
	private static final Logger LOG = LoggerFactory.getLogger(UnsubscribeApplication.class);
	
	private ApplicationContext ctx;//spring容器
	@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			// TODO Auto-generated method stub
			ctx = applicationContext;
		}
	
//对应handle方法
	@Override
	public void handle(EventInMessage msg) {
		String id = msg.getEvent().toLowerCase() + "MessageProcessors";
		try {
			EventMessageProcessors mp = (EventMessageProcessors) ctx.getBean(id);
			if (mp !=null) {
				mp.onMessage(msg);
			} else {
				LOG.warn("Bean的ID {} 无法调用对应的消息处理器: {} 对应的Bean不存在", id, id);
			}
		} catch (Exception e) {
			LOG.warn("Bean的ID {} 无法调用对应的消息处理器: {}", id, e.getMessage());
			LOG.trace(e.getMessage(), e);
		}
	}
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(UnsubscribeApplication.class, args);
//		System.out.println("Spring Boot应用启动成功");
//		CountDownLatch countDownLatch = new CountDownLatch(1);
//		countDownLatch.await();
	}

}
