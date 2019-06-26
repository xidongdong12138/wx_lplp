package org.lpjava.weixin.processors.impl;

import org.lpjava.commons.domain.User;
import org.lpjava.commons.domain.event.EventInMessage;
import org.lpjava.commons.processors.EventMessageProcessors;
import org.lpjava.commons.repository.UserRepository;
import org.lpjava.weixin.service.WeixinProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//信息处理器
@Service("subscribeMessageProcessors")
public class SubscribeEventMessageProcessors implements EventMessageProcessors {


	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WeixinProxy weixinProxy;
	@Override
	public void onMessage(EventInMessage msg) {
		// TODO Auto-generated method stub
		//非关注不处理
		if(msg.getEvent().equals("subscribe")) {
			return;
		}
		String openId = msg.getFromUserName();
		User user = this.userRepository.findByOpenId(openId);
		if (user == null || user.getStatus() != User.Status.IS_SUBSCRIBE) {
			// 3.调用远程接口
			// TODO 根据ToUserName找到对应的微信号
			String account = "";
			User wxUser = weixinProxy.getUser(account, openId);
			if(wxUser == null) {
				return;
			}
			if (wxUser == null) {
				return;
			}
			// 4.存储到数据库
			if (user != null) {
			// 原来关注过
			wxUser.setId(user.getId());
			wxUser.setSubTime(user.getSubTime());
			wxUser.setUnsubTime(null);
				}
			wxUser.setStatus(User.Status.IS_SUBSCRIBE);

			// 如果有id的值，会自动update；没有id的值会insert
			this.userRepository.save(wxUser);

			// 通过客服接口，发生一条信息给用户
			weixinProxy.sendText(account, openId, "欢迎关注我的公众号，回复帮助可获得人工智能菜单");
					}
				}
			}