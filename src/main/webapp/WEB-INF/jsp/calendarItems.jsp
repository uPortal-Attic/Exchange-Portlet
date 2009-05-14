<%@ include file="/WEB-INF/jsp/include.jsp" %>
<h1>Recent Calendar Items</h1>

<table border="0" cellpadding="4">
   <tr>
      <th>Organizer: </th>
      <th>Subject: </th>
   </tr>
   <c:choose>
      <c:when test="${empty calItems}">
        <c:forEach items="${calItems}" var="item">
     <tr>
        <td><c:out value="${item.organizerMailboxName}" /></td>
        <td><a href="https://mail.uvic.ca/owa/?ae=Item&t=IPM.Note&id=<c:out value="${item.owaId}" />"><c:out value="${msg.subject}" /></a></td>
     </tr>
        </c:forEach>
      </c:when>
      <c:otherwise>
     <tr>
        <td>Empty - N/A</td>
        <td>Empty - N/A</td>
     </tr>
      </c:otherwise>
   </c:choose>
</table>