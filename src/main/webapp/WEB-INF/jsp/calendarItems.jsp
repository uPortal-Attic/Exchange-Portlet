<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@page import="java.util.Date, java.text.SimpleDateFormat"%>
<div class="ecsportlet ui-tabs ui-widget ui-widget-content ui-corner-all">
  <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
    <!-- This href hash seems to work in expanded view only -->
    <li class="ui-state-default ui-corner-top"><a href="<portlet:renderURL><portlet:param name="action" value="inboxView"/></portlet:renderURL>"><span>Inbox</span></a></li>
    <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#exchcalendar"><span>Today&rsquo;s Calendar</span></a></li>
  </ul>
  <portlet:actionURL var="actionUrl">
      <portlet:param name="action" value="calendarView"/>
  </portlet:actionURL>
  <!-- Dave, rework this interaction to conform to Andrew's IA -->
  <form method="post" action="${actionUrl}">
      <label>Calendar</label>
      <select name="calId">
          <option value="calendar" <c:out value="${empty calendarId ? 'selected=\"selected\"' : ''}" escapeXml="false"/>>Default Calendar</option>
        <c:forEach items="${calList}" var="calListItem">
          <option value="${calListItem.id}" <c:out value="${calListItem.id eq calendarId.id ? 'selected=\"selected\"' : ''}" escapeXml="false"/>>${calListItem.displayName}</option>
        </c:forEach>
       </select>
                                                                        <!-- Dave format this spacing w/css for Andrew's IA -->
       <input type="submit" value="Get Calendar" /><span class="fmtDate"><%= new SimpleDateFormat("EEE, MMM d, yyyy").format(new Date()) %></span>
  </form>
  <div id="exchcalendar" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
    <table border="0" cellpadding="4">
       <thead>
       <tr>
          <th>Time</th>
          <th>Event</th>
          <th>Location</th>
       </tr>
       </thead>
       <tbody>
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
       </tbody>
    </table>
       <!-- Note, I took out the old mowa sso links before revision 317 -->
       <c:url value="https://mail.uvic.ca/owa" var="calUrl">
         <c:param name="ae" value="Folder" />
         <c:param name="t" value="IPF.Appointment" />
         <c:if test="${! empty calendarId }">
            <c:param name="id" value="${calendarId.owaId}" />
         </c:if>
       </c:url>
       <c:url value="${ssoUrl}" var="casUrl">
         <c:param name="destination" value="${calUrl}" />
       </c:url>
    <a href='<c:out value="${casUrl}"/>' target="_blank">Open Calendar</a>
  </div>
</div>