package org.lpjava.commons.processors;

import org.lpjava.commons.domain.event.EventInMessage;

public interface EventMessageProcessors {
	public void onMessage(EventInMessage msg);

}
