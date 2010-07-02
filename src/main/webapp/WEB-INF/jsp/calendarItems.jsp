<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@page import="java.util.Date, java.text.SimpleDateFormat"%>
<h1><c:out value="${calendarId.displayName}" default="Calendar" /></h1>
<portlet:actionURL var="actionUrl">
    <portlet:param name="action" value="calendarView"/>
</portlet:actionURL>
<form method="post" action="${actionUrl}">
    <label>Calendar</label>
    <select name="calId">
        <option value="calendar" <c:out value="${empty calendarId ? 'selected=\"selected\"' : ''}" escapeXml="false"/>>Default Calendar</option>
      <c:forEach items="${calList}" var="listItem">
        <option value="${listItem.id}" <c:out value="${listItem.id eq calendarId.id ? 'selected=\"selected\"' : ''}" escapeXml="false"/>>${listItem.displayName}</option>
      </c:forEach>
     </select>
     <input type="submit" value="Get Calendar" />
</form>
<p>
Today&rsquo;s Events - <%= new SimpleDateFormat("EEE, MMM d, yyyy").format(new Date()) %>
</p>
<a href="<portlet:renderURL><portlet:param name="action" value="inboxView"/></portlet:renderURL>">Inbox</a>
<table border="0" cellpadding="4">
   <tr>
      <th>Time</th>
      <th>Event</th>
      <th>Location</th>
   </tr>
   <c:choose>
      <c:when test="${empty calItems}">
   <tr>
      <td>Empty - N/A</td>
      <td>Empty - N/A</td>
      <td>Empty - N/A</td>
   </tr>
      </c:when>
      <c:otherwise>
        <c:forEach items="${calItems}" var="item">
   <tr>
     <td><c:out value="${item.eventTimePeriod}" /></td>

     <td><c:out value="${item.subject}" /></td>
     <td><c:out value="${item.location}" /></td>
   </tr>
        </c:forEach>
      </c:otherwise>
   </c:choose>
</table>
   <!-- Note, I took out the old mowa sso links before revision 317 -->
   <c:url value="https://mail.uvic.ca/owa" var="calUrl">
     <c:param name="ae" value="Folder" />
     <c:param name="t" value="IPF.Appointment" />
     <c:if test="${! empty calendarId }">
        <c:param name="id" value="${calendarId.owaId}" />
     </c:if>
   </c:url>
<a href='<c:out value="${calUrl}" />' target="_blank">Open Calendar</a>