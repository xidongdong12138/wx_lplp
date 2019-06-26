package org.lpjava.commons.service;

public interface TokenManager {

	/**
	 * 
	 * 
	 * @param account 传入公众号的微信号，在内部要根据微信号找到appid，根据appid才能获取对应的令牌。
	 * @return
	 */
	public String getToken(String account);
}
