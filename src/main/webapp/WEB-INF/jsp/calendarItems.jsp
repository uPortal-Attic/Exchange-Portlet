<%@ include file="/WEB-INF/jsp/include.jsp" %>
<h1>Recent Calendar Items</h1>

<table border="0" cellpadding="4">
   <tr>
      <th>Organizer: </th>
      <th>Subject: </th>
   </tr>
   <c:choose>
      <c:when test="${empty calItems}">
   <tr>
      <td>Empty - N/A</td>
      <td>Empty - N/A</td>
   </tr>
      </c:when>
      <c:otherwise>
        <c:forEach items="${calItems}" var="item">
   <tr>
     <td><c:out value="${item.organizerMailboxName}" /></td>
     <td><a href="https://mail.uvic.ca/owa/?ae=Folder&t=IPF.Appointment"><c:out value="${item.subject}" /></a></td>
   </tr>
        </c:forEach>
      </c:otherwise>
   </c:choose>
</table>
<a href="<portlet:renderURL><portlet:param name="action" value="inboxView"/></portlet:renderURL>">View Inbox</a>
