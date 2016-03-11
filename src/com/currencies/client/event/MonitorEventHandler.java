package com.currencies.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface MonitorEventHandler extends EventHandler {
	void onMain(MonitorEvent event);
}
