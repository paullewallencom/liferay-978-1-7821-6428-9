package com.liferay.guru.portlet.quicksearch.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusException;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.search.SearchContext;

public class QSMessageBusUtil {

	public static JSONArray sendSynchronousMessage(SearchContext searchContext)
			throws MessageBusException {
		Message message = new Message();
		message.setDestinationName(QSDestinationNames.SEARCH_AUTOCOMPLETE);
		message.setPayload(searchContext);
		message.setResponseDestinationName(DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);

		Object response = MessageBusUtil.sendSynchronousMessage(
				message.getDestinationName(), message,
				SearchConstants.AUTOCOMPLETE_TIMEOUT);

		if (response instanceof JSONArray) {
			return (JSONArray) response;
		} else {
			throw new MessageBusException("Invalid message response");
		}
	}
}
