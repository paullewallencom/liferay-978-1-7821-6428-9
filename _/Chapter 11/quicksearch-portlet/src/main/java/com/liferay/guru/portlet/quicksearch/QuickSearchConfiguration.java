package com.liferay.guru.portlet.quicksearch;

import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.liferay.guru.portlet.quicksearch.util.PreferencesUtil;
import com.liferay.guru.portlet.quicksearch.util.SearchConstants;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

public class QuickSearchConfiguration extends DefaultConfigurationAction {
	@Override
	public String render(PortletConfig config, RenderRequest request,
			RenderResponse response) throws Exception {

		List<Pair<Long, String>> publicLayouts = getValidTargetLayouts(request,
				false);
		List<Pair<Long, String>> privateLayouts = getValidTargetLayouts(
				request, true);
		if (publicLayouts.isEmpty() && privateLayouts.isEmpty()) {
			request.setAttribute("showNoValidLayoutsError", Boolean.TRUE);
		}
		request.setAttribute("publicTargetLayouts", publicLayouts);
		request.setAttribute("privateTargetLayouts", privateLayouts);
		request.setAttribute(SearchConstants.TARGET_PLID,
				PreferencesUtil.getLong(request, SearchConstants.TARGET_PLID));

		return super.render(config, request, response);
	}

	private static List<Pair<Long, String>> getValidTargetLayouts(
			RenderRequest request, boolean privateLayout)
			throws SystemException {

		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
				themeDisplay.getScopeGroupId(), privateLayout,
				LayoutConstants.TYPE_PORTLET);

		return FluentIterable.from(layouts).filter(containsTargetPortlet)
				.transform(getLayoutData).toList();
	}

	private static final Predicate<Layout> containsTargetPortlet = new Predicate<Layout>() {

		@Override
		public boolean apply(Layout target) {
			if (!target.isTypePortlet()) {
				return false;
			}
			List<String> portletIds = ((LayoutTypePortlet) target
					.getLayoutType()).getPortletIds();
			return portletIds
					.contains(SearchConstants.SEARCH_RESULTS_PORTLET_ID);

		}
	};

	private static final Function<Layout, Pair<Long, String>> getLayoutData = new Function<Layout, Pair<Long, String>>() {

		@Override
		public Pair<Long, String> apply(Layout target) {
			return Pair.of(target.getPlid(), target.getFriendlyURL());
		}
	};
}
