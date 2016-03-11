package com.currencies.client.event;

import com.google.gwt.event.shared.GwtEvent;


		public class MonitorEvent extends GwtEvent<MonitorEventHandler> {
			
		public static Type<MonitorEventHandler> TYPE = new Type<MonitorEventHandler>();

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<MonitorEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(MonitorEventHandler handler) {
		    handler.onMain(this);
			
		}

	

	}


