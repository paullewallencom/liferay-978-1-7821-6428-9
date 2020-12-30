package com.packtpub.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class RolesPortlet extends MVCPortlet {

	private Log _log = LogFactoryUtil.getLog(RolesPortlet.class);

	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		String renderPagePath = getInitParameter("view-guest-template");

		if (request.isUserInRole("logged-user")) {
			renderPagePath = getInitParameter("view-user-template");
		}

		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);
		boolean secretSection = true;
		try {
			PermissionChecker permissionChecker = themeDisplay
					.getPermissionChecker();
			String portletId = themeDisplay.getPortletDisplay().getId();

			PortletPermissionUtil.check(permissionChecker, portletId,
					"ADD_SECTION");

		} catch (PrincipalException pe) {
			secretSection = false;
		} catch (SystemException | PortalException e) {
			_log.error(e);
		}

		request.setAttribute("secretSection", secretSection);

		include(renderPagePath, request, response);
		super.render(request, response);
	}
}
