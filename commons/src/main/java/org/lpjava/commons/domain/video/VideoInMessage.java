package org.lpjava.commons.domain.video;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.lpjava.commons.domain.InMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class VideoInMessage extends InMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="MediaId")
	@JsonProperty("MediaId")
	private String mediaId;
	
	@XmlElement(name="ThumbMediaId")
	@JsonProperty("ThumbMediaId")
	private String thumbMediaId;
	
	public VideoInMessage() {
	super.setMsgType("video");
  }

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	@Override
	public String toString() {
		return "VideoInMessage [mediaId=" + mediaId + ", thumbMediaId=" + thumbMediaId + ", getToUserName()="
				+ getToUserName() + ", getFromUserName()=" + getFromUserName() + ", getCreateTime()=" + getCreateTime()
				+ ", getMsgType()=" + getMsgType() + ", getMsgId()=" + getMsgId() + "]";
	}
}

