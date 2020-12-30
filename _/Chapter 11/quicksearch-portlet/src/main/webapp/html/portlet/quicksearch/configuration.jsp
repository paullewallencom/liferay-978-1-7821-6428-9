<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="liferay-portlet" uri="http://liferay.com/tld/portlet"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %>

<c:choose>
	<c:when test="${ showNoValidLayoutsError }">
		<div class="portlet-msg-error">
			<liferay-ui:message key="search-results-portlet-was-not-found-in-this-site"/>
		</div>
	</c:when>
	<c:otherwise>

		<liferay-portlet:actionURL portletConfiguration="true" var="configurationActionURL" />

		<aui:form action="${ configurationActionURL }" method="post" name="fm">
			<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

			<aui:select name="preferences--targetPlid--" label="target-layout">

				<c:if test="${ not empty publicTargetLayouts }">
					<optgroup label='<liferay-ui:message key="public-layouts" />'>
						<c:forEach items="${ publicTargetLayouts }" var="layout">
							<aui:option label="${ layout.right }" value="${ layout.left }" selected="${ layout.left == targetPlid }" />
						</c:forEach>
					</optgroup>
				</c:if>

				<c:if test="${ not empty privateTargetLayouts }">
					<optgroup label='<liferay-ui:message key="private-layouts" />'>
						<c:forEach items="${ privateTargetLayouts }" var="layout">
							<aui:option label="${ layout.right }" value="${ layout.left }" selected="${ layout.left == targetPlid }" />
						</c:forEach>
					</optgroup>
				</c:if>

			</aui:select>

			<aui:button-row>
				<aui:button type="submit" />
			</aui:button-row>
		</aui:form>
	</c:otherwise>
</c:choose>