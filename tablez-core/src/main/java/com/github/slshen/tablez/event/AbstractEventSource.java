package com.github.slshen.tablez.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.EventBus;

public abstract class AbstractEventSource<T extends AbstractEventSource<T>> {

	@Autowired
	private EventBus eventBus;

	public EventBus getEventBus() {
		return eventBus;
	}

	@SuppressWarnings("unchecked")
	public T withtEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
		return (T) this;
	}

	protected void post(Object event) {
		if (eventBus != null) {
			eventBus.post(event);
		}
	}

}
