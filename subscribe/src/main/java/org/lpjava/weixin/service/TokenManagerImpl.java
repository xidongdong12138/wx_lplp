package org.lpjava.weixin.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;

import org.lpjava.commons.domain.ResponseError;
import org.lpjava.commons.domain.ResponseMessage;
import org.lpjava.commons.domain.ResponseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TokenManagerImpl implements TokenManager {
	private static final Logger LOG = LoggerFactory.getLogger(TokenManagerImpl.class);

	@Autowired
	private ObjectMapper objectMapper;
	@Override
	public String getToken(String account) {
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
				return ((ResponseToken) rm).getAccessToken();
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
