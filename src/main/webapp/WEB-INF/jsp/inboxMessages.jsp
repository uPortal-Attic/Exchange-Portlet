<%@ include file="/WEB-INF/jsp/include.jsp" %>
<h1>Recent Inbox Messages</h1>

<table border="0" cellpadding="4">
   <tr>
      <th>From: </th>
      <th>Subject: </th>
   </tr>
   <c:choose>
     <c:when test="${empty messages}">
   <tr>
      <td>Empty - N/A</td>
      <td>Empty - N/A</td>
   </tr>
      </c:when>
      <c:otherwise>
        <c:forEach items="${messages}" var="msg">
   <tr>
      <td><c:out value="${msg.fromMailboxName}" /></td>
      <td><a href="https://mail.uvic.ca/owa/?ae=Item&t=IPM.Note&id=<c:out value="${msg.owaId}" />"><c:out value="${msg.subject}" /></a></td>
   </tr>
        </c:forEach>
     </c:otherwise>
   </c:choose>
</table>
<a href="<portlet:renderURL><portlet:param name="action" value="calendarView"/></portlet:renderURL>">View Calendar</a>