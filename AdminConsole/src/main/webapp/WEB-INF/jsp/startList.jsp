<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="event" scope="request" type="ru.swimmasters.domain.Event"/>

<html>
<head>
    <title>Start List: <c:out value="${event.swimStyle.name}"/>, <c:out value="${event.eventGender}"/></title>
    <link rel="stylesheet" type="text/css" href="css/light-console.css" media="all">
</head>
<body>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="5">
            Start List: <c:out value="${event.swimStyle.name}"/>, <c:out value="${event.eventGender}"/>
        </td>
    </tr>
    <c:set var="totalHeats" value="${fn:length(event.entries.heatsOrderedByNumber)}" />
    <c:forEach items="${event.entries.heatsOrderedByNumber}" var="heat">
        <tr><td colspan="5" class="entries_athlete_header">${heat.number} / ${totalHeats}</td></tr>
        <c:forEach items="${heat.entries.all}" var="entry">
            <tr class="entries_event_time">
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
</body>
</html>