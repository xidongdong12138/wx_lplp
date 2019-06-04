package org.lpjava.weixin.processors.impl;

import org.lpjava.commons.domain.event.EventInMessage;
import org.lpjava.commons.processors.EventMessageProcessors;
import org.springframework.stereotype.Service;

//信息处理器
@Service("unsubscribeMessageProcessors")
public class UnsubscribeEventMessageProcessors implements EventMessageProcessors {

	@Override
	public void onMessage(EventInMessage msg) {
		// TODO Auto-generated method stub
		//取消关注
	}

}
