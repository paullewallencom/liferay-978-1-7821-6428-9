package com.liferay.guru.portlet.quicksearch;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.liferay.guru.portlet.quicksearch.util.PreferencesUtil;
import com.liferay.guru.portlet.quicksearch.util.QSMessageBusUtil;
import com.liferay.guru.portlet.quicksearch.util.SearchConstants;
import com.liferay.guru.portlet.quicksearch.util.SearchUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.util.PortalUtil;

@Controller
@RequestMapping("VIEW")
public class QuickSearchPortlet {
	public static final String SEARCH_ACTION_NAME = "search";

	@RenderMapping
	public String render(RenderRequest renderRequest,
			RenderResponse renderResponse, Model model) throws PortalException,
			SystemException {
		PortletURL searchURL = renderResponse.createActionURL();
		searchURL.setParameter(ActionRequest.ACTION_NAME, SEARCH_ACTION_NAME);
		model.addAttribute("searchURL", searchURL);
		model.addAttribute( SearchConstants.KEYWORDS, SearchUtil.getKeywords( renderRequest ) );
		
		if (Validator.isNull(getTargetPlid(renderRequest))) {
			if (hasConfigurationPermission(renderRequest)) {
				model.addAttribute("showInvalidConfigInfo", Boolean.TRUE);
			} else {
				// hide portlet
				renderRequest.setAttribute(
						WebKeys.PORTLET_CONFIGURATOR_VISIBILITY, false);
			}
		}

		return "quicksearch/view";
	}

	@ActionMapping(SEARCH_ACTION_NAME)
	public void search(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortalException,
			SystemException, WindowStateException {
		String keywords = SearchUtil.getKeywords(actionRequest);
		LiferayPortletResponse liferayPortletResponse = (LiferayPortletResponse) actionResponse;
		LiferayPortletURL portletURL = liferayPortletResponse
				.createRenderURL(SearchConstants.SEARCH_RESULTS_PORTLET_ID);

		portletURL.setWindowState(WindowState.MAXIMIZED);
		portletURL.setPlid(getTargetPlid(actionRequest));
		if (Validator.isNotNull(keywords)) {
			portletURL.setParameter(SearchConstants.KEYWORDS, keywords);
		}
		portletURL.setParameter("struts_action", "/search/search");

		actionResponse.sendRedirect(portletURL.toString());
	}

	@ResourceMapping
	public void serveAutocompleteSuggestions(ResourceRequest request,
			ResourceResponse response) throws IOException, PortalException,
			SystemException {

		JSONObject json = JSONFactoryUtil.createJSONObject();

		SearchContext searchContext = SearchUtil
				.createAutocompleteSearchContext(request);

		JSONArray result = QSMessageBusUtil
				.sendSynchronousMessage(searchContext);

		json.put("suggestions", result);
		response.setContentType(ContentTypes.APPLICATION_JSON);
		response.getWriter().write(json.toString());
	}

	private static long getTargetPlid(PortletRequest request)
			throws PortalException, SystemException {
		return PreferencesUtil.getLong(request, SearchConstants.TARGET_PLID);
	}

	private static boolean hasConfigurationPermission(PortletRequest request)
			throws PortalException, SystemException {
		return PortletPermissionUtil.contains(
				PermissionThreadLocal.getPermissionChecker(),
				PortalUtil.getPortletId(request), ActionKeys.CONFIGURATION);
	}

}
