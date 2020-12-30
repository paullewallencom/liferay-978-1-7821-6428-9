package com.liferay.guru.portlet.quicksearch.util;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

public class PreferencesUtil {

	private static final boolean DEFAULT_BOOLEAN = GetterUtil.DEFAULT_BOOLEAN;

	private static final long DEFAULT_LONG = GetterUtil.DEFAULT_LONG;

	public static boolean getBoolean(PortletRequest request, String name)
			throws PortalException, SystemException {
		return getBoolean(request, name, DEFAULT_BOOLEAN);
	}

	public static boolean getBoolean(PortletRequest request, String name,
			boolean defaultValue) throws PortalException, SystemException {
		PortletPreferences prefs = getPreferences(request);
		return getBoolean(prefs, name, defaultValue);
	}

	public static boolean getBoolean(PortletPreferences preferences, String name) {
		return getBoolean(preferences, name, DEFAULT_BOOLEAN);
	}

	public static boolean getBoolean(PortletPreferences preferences,
			String name, boolean defaultValue) {
		return GetterUtil.getBoolean(preferences.getValue(name, null),
				defaultValue);
	}

	public static long getLong(PortletRequest request, String name)
			throws PortalException, SystemException {
		return getLong(request, name, DEFAULT_LONG);
	}

	public static long getLong(PortletRequest request, String name,
			long defaultValue) throws PortalException, SystemException {
		PortletPreferences prefs = getPreferences(request);
		return getLong(prefs, name, defaultValue);
	}

	public static long getLong(PortletPreferences preferences, String name) {
		return getLong(preferences, name, DEFAULT_LONG);
	}

	public static long getLong(PortletPreferences preferences, String name,
			long defaultValue) {
		return GetterUtil.getLong(preferences.getValue(name, null),
				defaultValue);
	}

	public static PortletPreferences getPreferences(PortletRequest request)
			throws PortalException, SystemException {
		PortletPreferences preferences = request.getPreferences();
		String portletResource = ParamUtil
				.getString(request, "portletResource");

		if (Validator.isNotNull(portletResource)) {
			preferences = PortletPreferencesFactoryUtil.getPortletSetup(
					request, portletResource);
		}
		return preferences;
	}
}
