<%@ include file="/WEB-INF/jsp/include.jsp" %>
<h1>Recent Inbox Messages</h1>

<table border="0" cellpadding="4">
   <tr>
      <th>From: </th>
      <th>Subject: </th>
   </tr>
   <c:forEach items="${messages}" var="msg">
     <tr>
        <td><c:out value="${msg.fromMailboxName}" /></td>
        <td><a href="https://mail.uvic.ca/owa/?ae=Item&t=IPM.Note&id=<c:out value="${msg.owaId}" />"><c:out value="${msg.subject}" /></a></td>
     </tr>
   </c:forEach>
</table>