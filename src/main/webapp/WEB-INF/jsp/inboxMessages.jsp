<%@ include file="/WEB-INF/jsp/include.jsp" %>
<h1>Inbox Messages</h1>
<a href="<portlet:renderURL><portlet:param name="action" value="calendarView"/></portlet:renderURL>">Calendar</a>
<table border="0" cellpadding="4">
   <tr>
      <th>From</th>
      <th>Subject</th>
      <th>Received</th>
   </tr>
   <c:choose>
     <c:when test="${empty messages}">
   <tr>
      <td>Empty - N/A</td>
      <td>Empty - N/A</td>
      <td>Empty - N/A</td>
   </tr>
      </c:when>
      <c:otherwise>
        <!-- Note, I took out the old mowa sso links before revision 317 -->
        <c:forEach items="${messages}" var="msg">
   <tr>
      <td><c:out value="${msg.fromMailboxName}" /></td>
           <c:url value="https://mail.uvic.ca/owa" var="inboxUrl">
           </c:url>
      <td><a href='<c:out value="${inboxUrl}" />' target="_blank"><c:out value="${msg.subject}" /></a></td>
      <td><c:out value="${msg.dateTimeCreatedMonthDay}" /></td>
   </tr>
        </c:forEach>
     </c:otherwise>
   </c:choose>
</table>
