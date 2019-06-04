package org.lpjava.weixin.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lpjava.commons.domain.InMessage;
import org.lpjava.commons.domain.event.EventInMessage;
import org.lpjava.commons.domain.text.TextInMessage;

public class MessageTypeMapper {

	private static Map<String,Class<? extends InMessage>> typeMap = new ConcurrentHashMap<>();
	
    static {
    	typeMap.put("text",TextInMessage.class);
    	typeMap.put("link",TextInMessage.class);
    	typeMap.put("image",TextInMessage.class);
    	typeMap.put("location",TextInMessage.class);
    	typeMap.put("video",TextInMessage.class);
    	typeMap.put("shortvideo",TextInMessage.class);
    	typeMap.put("voice",TextInMessage.class);
    	typeMap.put("event",EventInMessage.class);
    	
    }
    @SuppressWarnings("unchecked")
    public static <T extends InMessage> Class<T> getClass(String type) {
    	return (Class<T>) typeMap.get(type);
    }
    
}
