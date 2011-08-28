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
    Результаты
</h1>
<h3>
    <c:out value="${event.date}"/> <br />
    <c:out value="${event.number}: ${event.swimStyle.name}"/>
    (<c:out value="${event.eventGender}, ${event.swimStyle.relayCount} x ${event.swimStyle.distance} m, ${event.swimStyle.stroke}" />)
</h3>
<div>
    <p><a href="startList.html?event=${event.id}">К стартовому протоколу</a></p>
    <p><a href="listEvents.html?meet=${event.meet.id}">К другим протоколам</a></p>
</div>

<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:forEach items="${event.ageRankings.all}" var="ageRanking">
        <tr>
            <td colspan="8" class="entries_athlete_header">${ageRanking.ageGroup}</td>
        </tr>
        <c:forEach items="${ageRanking.groupRankings.all}" var="groupRanking">
            <tr class="entries_athlete_time">
                <td class="entries_event">${groupRanking.place}</td>
                <td class="entries_event">${groupRanking.result.athlete.athlete.fullName}</td>
                <td class="entries_event">${groupRanking.result.athlete.athlete.birthYear}</td>
                <td class="entries_event"><!-- TODO: city --> &nbsp; </td>
                <td class="entries_event">${groupRanking.result.athlete.athlete.club.name} &nbsp;</td>
                <td class="entries_event">${groupRanking.result.swimTime}</td>
                <td class="entries_event">${groupRanking.result.status}</td>
                <td class="entries_event">${groupRanking.result.points}</td>
            </tr>
        </c:forEach>
        <tr><td colspan="8" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
</table>
<p>
    Результаты сформированы <c:out value="${event.rankingsTimestamp}" />, версия: TODO
</p>
</body>
</html>