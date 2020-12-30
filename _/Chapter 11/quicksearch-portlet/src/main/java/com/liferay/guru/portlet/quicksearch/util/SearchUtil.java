package com.liferay.guru.portlet.quicksearch.util;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.PortalUtil;

public class SearchUtil {

	public static String getKeywords(PortletRequest actionRequest) {
		HttpServletRequest request = PortalUtil
				.getOriginalServletRequest(PortalUtil
						.getHttpServletRequest(actionRequest));
		return ParamUtil.get(request, SearchConstants.KEYWORDS,
				StringPool.BLANK);
	}
	
	public static SearchContext createAutocompleteSearchContext(ResourceRequest request) {
		SearchContext searchContext = SearchContextFactory.getInstance(PortalUtil.getHttpServletRequest(request));
        searchContext.setKeywords( SearchUtil.getKeywords( request ) );
        return searchContext;
	}
}
