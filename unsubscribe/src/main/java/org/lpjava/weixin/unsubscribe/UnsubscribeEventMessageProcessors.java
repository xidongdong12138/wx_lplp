package org.lpjava.weixin.unsubscribe;

import java.util.Date;

import javax.transaction.Transactional;

import org.lpjava.commons.domain.User;
import org.lpjava.commons.domain.event.EventInMessage;
import org.lpjava.commons.processors.EventMessageProcessors;
import org.lpjava.commons.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//信息处理器
@Service("unsubscribeMessageProcessors")
public class UnsubscribeEventMessageProcessors implements EventMessageProcessors {

	private static final Logger LOG = LoggerFactory.getLogger(UnsubscribeEventMessageProcessors.class);
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void onMessage(EventInMessage msg) {
		// TODO Auto-generated method stub
		//非取消关注不处理
		if(msg.getEvent().equals("unsubscribe")) {
			return;
		}
		LOG.trace("处理取消关注的消息：" + msg);

		// 由于方法上面有@Transactional注解，调用对象的set方法，会自动更新到数据库
		User user = this.userRepository.findByOpenId(msg.getFromUserName());
		if (user != null) {
			user.setStatus(User.Status.IS_UNSUBSCRIBE);
			user.setUnsubTime(new Date());
		}
				}
			}