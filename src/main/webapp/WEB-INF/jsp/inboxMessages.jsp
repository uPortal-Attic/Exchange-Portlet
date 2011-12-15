<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="ecsportlet ui-tabs ui-widget ui-widget-content ui-corner-all">
  <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
    <!-- This hash href seems to work in expanded view only -->
    <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#exchemail"><span>Inbox</span></a></li>
    <li class="ui-state-default ui-corner-top"><a href="<portlet:renderURL><portlet:param name="action" value="calendarView"/></portlet:renderURL>"><span>Today&rsquo;s Calendar</span></a></li>
  </ul>
  <div id="exchemail" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
    <table border="0" cellpadding="4">
       <thead>
       <tr>
          <th>From</th>
          <th>Subject</th>
          <th>Received</th>
       </tr>
       </thead>
       <tbody>
       <c:choose>
         <c:when test="${empty messages}">
       <tr>
          <td>Empty - N/A</td>
          <td>Empty - N/A</td>
          <td>Empty - N/A</td>
       </tr>
          </c:when>
          <c:otherwise>
          <!--
            <c:url value="https://mail.uvic.ca/owa" var="mowaBaseUrl" />
              -->
            <c:url value="${oUrl}" var="mowaBaseUrl" />
            <c:url value="${ssoUrl}" var="casBaseUrl" />
            <c:url value="${casBaseUrl}" var="combinedBaseUrl" >
                <c:param name="destination" value="${mowaBaseUrl}"/>
            </c:url>
            <!-- Note, I took out the old mowa sso links before revision 317 -->
            <c:forEach items="${messages}" var="msg">
       <tr <c:out value="${msg.isRead == false ? 'class=\"unread\"' : ''}" escapeXml="false"/>>
          <td><c:out value="${msg.fromMailboxName}" /></td>
               <c:url value="${mowaBaseUrl}" var="mowaUrl">
                 <c:param name="ae" value="Item"/>
                 <c:param name="t" value="IPM.Note"/>
                 <c:param name="id" value="${msg.owaId}" />
               </c:url>
               <c:url value="${casBaseUrl}" var="inboxUrl">
                 <c:param name="destination" value="${mowaUrl}"/>
               </c:url>
          <td><a href='<c:out value="${inboxUrl}" />' target="_blank"><c:out value="${empty msg.subject ? '[No Subject]' : msg.subject}" /></a></td>
          <td><a href='<c:out value="${inboxUrl}" />' target="_blank"><c:out value="${msg.dateTimeCreatedMonthDay}" /></a></td>
       </tr>
            </c:forEach>
         </c:otherwise>
       </c:choose>
       </tbody>
    </table>
    <a href='<c:out value="${combinedBaseUrl}" />' target="_blank">Open Inbox</a>
  </div>
</div>