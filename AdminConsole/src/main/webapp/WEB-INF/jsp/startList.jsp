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
    <p><a href="listEvents.html?meet=${event.meet.id}">Назад к другим стартовым протоколам</a></p>
    <p><a href="prepareRankings.html?event=${event.id}">Сформировать результаты</a></p>
    <p><a href="showRankings.html?event=${event.id}">Посмотреть результаты</a> (TODO: version / timestamp)</p>
</div>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:forEach items="${event.startListHeats.heatsOrderedByNumber}" var="heat">
        <tr>
            <td colspan="4" class="entries_athlete_header" style="border-right:0;">
            Заплыв # ${heat.absoluteNumber}
            <br />
            ${heat.number} / ${heat.totalHeatsInEvent}
            </td>
            <td class="entries_athlete_header" style="border-left:0;">
                <c:choose>
                <c:when test="${heat.raceStatus == 'IN_PROGRESS'}">
                    <span class="race_in_progress_status">
                        <a  class="race_in_progress_status" href="<c:url value="/editRace.html?heat=${heat.id}" />"><c:out value="${heat.raceStatus}"/></a>
                    </span>
                    <br />
                    <a href="runRace.html?heat=${heat.id}">Рестарт</a>
                </c:when>
                <c:when test="${heat.raceStatus == 'FINISHED'}">
                    <span class="race_finished_status">
                        <a  class="race_finished_status" href="<c:url value="/viewRace.html?heat=${heat.id}" />"><c:out value="${heat.raceStatus}"/></a>
                    </span>
                    <br />
                    <a href="runRace.html?heat=${heat.id}">Рестарт</a>
                </c:when>
                <c:otherwise>
                    <span class="race_not_started_status">
                        <c:out value="${heat.raceStatus}"/>
                    </span>
                    <br />
                    <a href="runRace.html?heat=${heat.id}">Старт!</a>
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
            </tr>
        </c:forEach>
        <tr><td colspan="5" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
</table>
<p>
    Всего участников: <c:out value="${fn:length(event.startListEntries.all)}" />
</p>
<p>
    Стартовый протокол сформирован: <c:out value="${event.startListTimestamp}" />, версия: TODO
</p>
</body>
</html>