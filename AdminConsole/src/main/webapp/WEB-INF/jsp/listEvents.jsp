<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="meet" scope="request" type="ru.swimmasters.domain.Meet"/>

<html>
<head>
    <title>Стартовые протоколы</title>
    <link rel="stylesheet" type="text/css" href="css/light-console.css" media="all">
</head>
<body>
<h1>Стартовые протоколы</h1>
<div>
    <p><a href="listAthletes.html?meet=${meet.id}">Вернуться к мандатной комиссии</a></p>
    <p><a href="prepareAllStartLists.html?meet=${meet.id}">Сформировать все стартовые протоколы</a></p>
</div>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:forEach items="${meet.sessions.all}" var="session">
        <tr>
            <td class="entries_event_delimiter" colspan="2">
                <h2><c:out value="${session.date}"/></h2>
            </td>
        </tr>
    <c:forEach items="${session.events.all}" var="event">
        <tr>
            <td class="entries_athlete_header table_header_left">
                Дисциплина #<c:out value="${event.number}"/>
            </td>
            <td colspan="2" class="entries_athlete_header" style="border-left:0; border-right:0;">
                ${event.swimStyle.name}
                <br />
                <c:out value="${event.eventGender}, ${event.swimStyle.relayCount} x ${event.swimStyle.distance} m, ${event.swimStyle.stroke}" />
            </td>
            <td class="entries_athlete_header table_header_middle">
                <c:choose>
                    <c:when test="${event.startListPrepared}">
                        <a href="<c:url value="/startList.html?event=${event.id}" />">стартовый протокол</a>
                        <br />
                        <c:out value="${event.startListTimestamp}" />
                    </c:when>
                    <c:otherwise>не сформирован</c:otherwise>
                </c:choose>

            </td>
            <td class="entries_athlete_header table_header_right">
            <c:choose>
            <c:when test="${event.rankingsTimestamp != null}">
                <a href="showAgeRankings.html?event=${event.id}">результаты</a>
                <br />
                ${event.rankingsTimestamp}
            </c:when>
            <c:otherwise>
                &nbsp;
            </c:otherwise>
            </c:choose>
            </td>
        </tr>
        <c:forEach items="${event.entries.allSortedByAthleteName}" var="entry"  varStatus="entryStatus">
            <tr class="entries_athlete_time">
                <td class="entries_event ${entry.athlete.approvalStatus == 'APPROVED' ? '' : 'entries_athlete_not_approved'}">
                ${entryStatus.index + 1}. ${entry.athlete.athlete.fullName}
                </td>
                <td class="entries_time">
                ${entry.athlete.athlete.club.name} &nbsp;
                </td>
                <td class="entries_time">
                ${entry.ageGroup} &nbsp;
                </td>
                <td class="entries_time">${entry.entryTime != null ? entry.entryTime : 'NA'}</td>
                <td class="entries_time">
                    <c:choose>
                    <c:when test="${event.startListPrepared}">
                        ${entry.heat.number} ${entry.lane}
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        <tr><td colspan="2" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
    </c:forEach>
</table>
</body>
</html>