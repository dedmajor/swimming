<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="event" scope="request" type="ru.swimmasters.domain.Event"/>

<html>
<head>
    <title><c:out value="${event.swimStyle.name}"/>, <c:out value="${event.eventGender}"/> - Стартовый протокол</title>
    <link rel="stylesheet" type="text/css" href="css/light-console.css" media="all">
</head>
<body>
<h1>
    Стартовый протокол
</h1>
<h3>
    <c:out value="${event.date}"/> <br />
    <c:out value="${event.number}: ${event.swimStyle.name}"/>
    (<c:out value="${event.eventGender}, ${event.swimStyle.relayCount} x ${event.swimStyle.distance} m, ${event.swimStyle.stroke}" />)
</h3>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:set var="totalHeats" value="${fn:length(event.entries.heatsOrderedByNumber)}" />
    <c:forEach items="${event.entries.heatsOrderedByNumber}" var="heat">
        <tr><td colspan="5" class="entries_athlete_header">Heat #${heat.number} / ${totalHeats}</td></tr>
        <c:forEach items="${heat.entries.all}" var="entry">
            <tr class="entries_athlete_time">
                <td class="entries_event">${entry.lane}</td>
                <td class="entries_event">${entry.athlete.fullName}</td>
                <td class="entries_event">${entry.athlete.birthDate}</td>
                <td class="entries_event">${entry.ageGroup}</td>
                <td class="entries_time">${entry.entryTime != null ? entry.entryTime : 'NA'}</td>
            </tr>
        </c:forEach>
        <tr><td colspan="5" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
</table>
<p>
    Стартовый протокол сформирован: <c:out value="${event.startListTimestamp}" />
</p>
</body>
</html>