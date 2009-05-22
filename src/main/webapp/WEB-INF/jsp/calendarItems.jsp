<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@page import="java.util.Date, java.text.SimpleDateFormat"%>
<h1>Calendar Items</h1>
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
   <c:url value="https://mail.uvic.ca/owa" var="calUrl">
     <c:param name="ae" value="Folder" />
     <c:param name="t" value="IPF.Appointment" />
   </c:url>
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

     <td><a href='<c:out value="${calUrl}" />'><c:out value="${item.subject}" /></a></td>
     <td><c:out value="${item.location}" /></td>
   </tr>
        </c:forEach>
      </c:otherwise>
   </c:choose>
</table>
<a href='<c:out value="${calUrl}" />'>Open Calendar</a>