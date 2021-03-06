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
            <td class="entries_athlete_header table_header_left" colspan="2">
                Дисциплина #<c:out value="${event.number}"/>
            </td>
            <td colspan="3" class="entries_athlete_header" style="border-left:0; border-right:0;">
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
    <c:forEach items="${event.startListHeats.allSortedByNumber}" var="heat">
        <!-- TODO: FIXME: remove copypaste -->
        <tr>
            <td colspan="4" class="entries_athlete_header" style="border-right:0;">
            Заплыв # ${heat.absoluteNumber}
            <br />
            ${heat.number} / ${heat.totalHeatsInEvent}
            </td>
            <td class="entries_athlete_header" style="border-left:0;border-right:0;">
                <c:choose>
                <c:when test="${heat.raceStatus == 'IN_PROGRESS'}">
                    ${heat.startTimestamp}
                    <br />
                    <span class="race_in_progress_status">
                        <a class="race_in_progress_status" href="<c:url value="/editRace.html?heat=${heat.id}" />"><c:out value="${heat.raceStatus}"/></a>
                    </span>
                </c:when>
                <c:when test="${heat.raceStatus == 'FINISHED'}">
                    ${heat.startTimestamp}
                    <br />
                    <span class="race_finished_status">
                        <c:out value="${heat.raceStatus}"/>
                    </span>
                </c:when>
                <c:otherwise>
                    <span class="race_not_started_status">
                        <c:out value="${heat.raceStatus}"/>
                    </span>
                </c:otherwise>
                </c:choose>
            </td>
            <td class="entries_athlete_header" style="border-left:0;">
                <c:choose>
                <c:when test="${heat.raceStatus == 'NOT_STARTED'}">
                    <a href="runRace.html?heat=${heat.id}">Старт!</a>
                </c:when>
                <c:otherwise>
                    ${heat.finishTimestamp}
                    <br />
                    <a href="runRace.html?heat=${heat.id}">Рестарт</a>
                </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <c:forEach items="${heat.entries.allSortedByLane}" var="entry">
            <tr class="entries_athlete_time">
                <td class="entries_event">${entry.lane}</td>
                <td class="entries_event">${entry.athlete.athlete.fullName}</td>
                <td class="entries_event">${entry.athlete.athlete.birthDate}</td>
                <td class="entries_event">${entry.ageGroup}</td>
                <td class="entries_time">${entry.entryTime != null ? entry.entryTime : 'NA'}</td>
                <td class="entries_time">
                    <c:choose>
                    <c:when test="${entry.result != null}">
                        ${entry.result.swimTime} / ${entry.result.status}
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        <tr><td colspan="6" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
        <tr><td colspan="2" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
    </c:forEach>
</table>
</body>
</html>