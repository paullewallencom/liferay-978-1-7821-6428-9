package com.liferay.guru.portlet.quicksearch.util;

import com.liferay.portal.kernel.messaging.DestinationNames;

public interface QSDestinationNames extends DestinationNames {

	public static final String SEARCH_AUTOCOMPLETE = "liferay/search_autocomplete";

	public static final String SEARCH_AUTOCOMPLETE_RESPONSE = "liferay/search_autocomplete/response";
}
