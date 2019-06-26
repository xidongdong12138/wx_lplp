package org.lpjava.commons.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.lpjava.commons.domain.ResponseError;
import org.lpjava.commons.domain.ResponseMessage;
import org.lpjava.commons.domain.ResponseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TokenManagerImpl implements TokenManager {
	private static final Logger LOG = LoggerFactory.getLogger(TokenManagerImpl.class);

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RedisTemplate<String, ResponseToken> tokenRedisTemplate;

	@Override
	public String getToken(String account) {
		// TODO Auto-generated method stub
		BoundValueOperations<String, ResponseToken> ops = tokenRedisTemplate.boundValueOps("weixin_access_token");
		ResponseToken token = ops.get();
		LOG.trace("获取令牌，结果： {}", token);
		
		if(token == null) {
			//增加事务锁
			for (int i=0; i<10; i++) {
			Boolean locked = tokenRedisTemplate.opsForValue().setIfAbsent("weixin_access_token", new ResponseToken());
			LOG.trace("没有令牌，增加事务锁，结果：{}", locked);

			if(locked == true) {
				//增加成功
			try {//再次检查token是否在数据库里面
				token = ops.get();
				if(token == null) {
					LOG.trace("再次检查令牌，还是没有，调用远程接口");
					token = getRemoteToken(account);
				
				// 把token存储到Redis里面
				ops.set(token);
				// 设置令牌的过期时间，减60表示提前一分钟去更新令牌
				ops.expire(token.getExpiresIn() - 60, TimeUnit.SECONDS);
				}else{
				LOG.trace("再次检查令牌，已经有令牌在Redis里面，直接使用");
				}
				synchronized (this) {
					this.notifyAll();
				}
				
				break;
			} finally{
				LOG.trace("删除令牌事务锁");
				//删除事务锁
				tokenRedisTemplate.delete("weixin_access_token");
			}
				
				
			}else{
				//增加不成功，等一分钟
				synchronized (this) {
					try {
						LOG.trace("其他线程锁定了令牌，无法获得锁，等待...");
						this.wait(1000 * 60);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					LOG.error("等待获取分布式的事务锁出现问题：" + e.getLocalizedMessage(),e);
					break;
					}
				}
			
			  }
			}
			if(token !=null) {
				return token.getAccessToken();
			}
		}
		return null;
	}
	
	public ResponseToken getRemoteToken(String account) {
		// TODO Auto-generated method stub
		String appid = "wx5125a0edde2426b6";
		String appsecret = "d5688a147b1a5ed5d30b290c47868d5c";
		
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
				+ "&appid=" + appid
				+ "&secret=" + appsecret;
		HttpClient hc = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.build();
		HttpRequest request = HttpRequest.newBuilder(URI.create(url))
				.GET()
				.build();
		
		ResponseMessage rm;
		try {
			HttpResponse<String> response =  hc.send(request, BodyHandlers.ofString(Charset.forName("UTF-8")));
			String body = response.body();
			LOG.trace("调用远程接口返回的内柔: \n{}", body);
			
			
			if(body.contains("errcode")) {
				rm = objectMapper.readValue(body, ResponseError.class);
				rm.setStatus(2);
			}else{
				rm = objectMapper.readValue(body, ResponseToken.class);
				rm.setStatus(1);
			}
			//return rm;
			if (rm.getStatus() == 1) {
//				return ((ResponseToken) rm).getAccessToken();
				return ((ResponseToken) rm);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("无法获取令牌，因为：" + e.getLocalizedMessage());
		}

		throw new RuntimeException("无法获取令牌，因为：错误代码=" //
				+ ((ResponseError) rm).getErrorCode() //
				+ ",错误描述=" + ((ResponseError) rm).getErrorMessage());
	}
}
