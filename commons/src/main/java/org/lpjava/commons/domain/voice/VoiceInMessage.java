package org.lpjava.commons.domain.voice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.lpjava.commons.domain.InMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class VoiceInMessage extends InMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="Format")
	@JsonProperty("Format")
	private String format;
	
	@XmlElement(name="MediaId")
	@JsonProperty("MediaId")
	private String mediaId;
	
	public VoiceInMessage() {
	super.setMsgType("voice");
  }

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Override
	public String toString() {
		return "VoiceInMessage [format=" + format + ", mediaId=" + mediaId + ", getToUserName()=" + getToUserName()
				+ ", getFromUserName()=" + getFromUserName() + ", getCreateTime()=" + getCreateTime()
				+ ", getMsgType()=" + getMsgType() + ", getMsgId()=" + getMsgId() + "]";
	}
}