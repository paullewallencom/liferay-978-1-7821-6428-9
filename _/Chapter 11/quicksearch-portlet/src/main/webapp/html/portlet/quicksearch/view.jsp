<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="liferay-theme" uri="http://liferay.com/tld/theme"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>

<portlet:defineObjects />
<liferay-theme:defineObjects />

<c:choose>
	<c:when test="${ showInvalidConfigInfo }">
		<div class="portlet-msg-info">
			<liferay-ui:message
				key="configure-this-portlet-to-access-search-form" />
		</div>
	</c:when>
	<c:otherwise>
		<aui:form action="${ searchURL }" name="fm">
			<input id="<portlet:namespace />keywords" name="keywords" size="30"
				type="text" label="" value="${ keywords }" />
			<input align="absmiddle" border="0"
				src="${ themeDisplay.getPathThemeImages()}/common/search.png"
				title="<liferay-ui:message key="search" />" type="image" />
		</aui:form>

		<aui:script use="autocomplete-plugin,autocomplete-sources">

	A.one('#<portlet:namespace />keywords').plug(
		A.Plugin.AutoComplete,
		{
			minQueryLength: 3,
			render: true,
			resultListLocator: function (response) {
				return (response && response.suggestions) || [];
			},
			source: '<portlet:resourceURL />&keywords={query}'
		}
	);
</aui:script>
	</c:otherwise>
</c:choose>