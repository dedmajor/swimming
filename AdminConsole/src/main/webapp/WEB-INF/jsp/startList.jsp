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
<div>
    <p><a href="listEvents.html?meet=${event.meet.id}">К другим стартовым протоколам</a></p>
    <c:choose>
    <c:when test="${event.rankingsTimestamp != null}">
        <p>Результаты по группам сформированы ${event.rankingsTimestamp}, версия: TODO</p>
        <p><a href="showAgeRankings.html?event=${event.id}">Посмотреть</a></p>
    </c:when>
    <c:otherwise>
        <p>Результаты по группам не сформированы</p>
    </c:otherwise>
    </c:choose>
    <c:choose>
    <c:when test="${event.rankingsTimestamp != null}">
        <p><a href="prepareAgeRankings.html?event=${event.id}">Сформировать результаты по группам повторно</a></p>
    </c:when>
    <c:otherwise>
        <p><a href="prepareAgeRankings.html?event=${event.id}">Сформировать результаты по группам</a></p>
    </c:otherwise>
    </c:choose>
</div>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:forEach items="${event.startListHeats.heatsOrderedByNumber}" var="heat">
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
        <c:forEach items="${heat.entries.all}" var="entry">
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
</table>
<p>
    Всего участников: <c:out value="${fn:length(event.startListEntries.all)}" />
</p>
<p>
    Стартовый протокол сформирован <c:out value="${event.startListTimestamp}" />, версия: TODO
</p>
</body>
</html>