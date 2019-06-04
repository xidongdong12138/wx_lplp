package org.lpjava.commons.domain.event;

import javax.xml.bind.annotation.XmlElement;

import org.lpjava.commons.domain.InMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventInMessage extends InMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="Event")
	@JsonProperty("Event")
	private String event;
	
	@XmlElement(name="EventKey")
	@JsonProperty("EventKey")
	private String eventKey;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
