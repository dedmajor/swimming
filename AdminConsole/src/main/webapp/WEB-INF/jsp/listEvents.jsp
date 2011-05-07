<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- TODO: replace with Domain Collection --%>
<jsp:useBean id="events" scope="request" type="java.util.List<ru.swimmasters.domain.Event>"/>

<html>
<head>
    <title>Events</title>
    <link rel="stylesheet" type="text/css" href="css/light-console.css" media="all">
</head>
<body>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:forEach items="${events}" var="event">
        <tr>
            <td class="entries_athlete_header" style="border-right:0;">
                <c:out value="${event.date}"/> <br />
                <c:out value="${event.number}: ${event.swimStyle.name}"/>
                (<c:out value="${event.eventGender}, ${event.swimStyle.relayCount} x ${event.swimStyle.distance} m, ${event.swimStyle.stroke}" />)
            </td>
            <td class="entries_athlete_header" style="border-left:0;">
                <a href="<c:url value="/startList.html?event=${event.id}" />">стартовый протокол</a>
                <br />
                <c:out value="${event.startListTimestamp}" />
            </td>
        </tr>
        <c:forEach items="${event.entries.all}" var="entry">
            <tr class="entries_athlete_time">
                <td class="entries_event ${entry.athlete.approvalStatus == 'APPROVED' ? '' : 'entries_athlete_not_approved'}">
                ${entry.athlete.fullName}
                </td>
                <td class="entries_time">${entry.entryTime != null ? entry.entryTime : 'NA'}</td>
            </tr>
        </c:forEach>
        <tr><td colspan="2" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
</table>
</body>
</html>