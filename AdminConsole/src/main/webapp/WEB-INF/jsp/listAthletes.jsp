<%@ page import="ru.swimmasters.time.RealTimeClock" %>
<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="meet" scope="request" type="ru.swimmasters.domain.Meet"/>

<html>
<head>
    <title>Заявки на участие в соревновании - Мандатная комиссия</title>
    <link rel="stylesheet" type="text/css" href="css/light-console.css" media="all">
</head>
<body>
<h1>Мандатная комиссия</h1>
<div>
    <p>Соревнования: ${meet.name} (${meet.city}, ${meet.course}, ${meet.startDate} - ${meet.endDate})</p>
    <p>Место проведения: ${meet.pool.name} (${meet.pool.address})</p>
    <p>Текущее время: <c:out value="<%=new RealTimeClock().now()%>" /></p>
    <p><a href="approveAllAthletes.html?meet=${meet.id}">Подтвердить всех участников</a></p>
    <p><a href="listEvents.html?meet=${meet.id}">Перейти к стартовым протоколам</a></p>
</div>
<h3>Заявки на участие в соревновании</h3>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:forEach items="${meet.meetAthletes.allSortedByAthleteName}" var="athlete">
        <tr>
            <td class="entries_athlete_header" style="border-right:0;">
                <c:out value="${athlete.athlete.fullName}"/> <br />
                <c:out value="${athlete.athlete.club.name}"/>
            </td>
            <td class="entries_athlete_header" style="border-left:0;border-right:0;">
                <c:out value="${athlete.athlete.birthYear}"/> <br />
            </td>
            <td class="entries_athlete_header" style="border-left:0;">
                <c:choose>
                <c:when test="${athlete.approvalStatus == 'REJECTED'}">
                    <span class="approval_rejected_status">
                        <a  class="approval_rejected_status" href="<c:url value="/approveAthlete.html?athlete=${athlete.id}" />"><c:out value="${athlete.approvalStatus}"/></a>
                    </span>
                </c:when>
                <c:when test="${athlete.approvalStatus == 'APPROVED'}">
                    <span class="approval_approved_status">
                        <a  class="approval_approved_status" href="<c:url value="/rejectAthlete.html?athlete=${athlete.id}" />"><c:out value="${athlete.approvalStatus}"/></a>
                    </span>
                </c:when>
                <c:otherwise>
                    <c:if test="${fn:length(athlete.entries.all) > 0}">
                        <a href="<c:url value="/approveAthlete.html?athlete=${athlete.id}" />"><c:out value="${athlete.approvalStatus}"/></a>
                    </c:if>
                    &nbsp;
                </c:otherwise>
                </c:choose>
                <br />
                <c:out value="${athlete.approvalTimestamp}" />
            </td>
        </tr>
        <c:forEach items="${athlete.entries.all}" var="entry">
            <tr class="entries_athlete_time">
                <td class="entries_event">${entry.event.swimStyle.name}</td>
                <td class="entries_time">
                    <c:choose>
                    <c:when test="${entry.validAge}">
                        ${entry.ageGroup}
                    </c:when>
                    <c:otherwise>
                        <span class="approval_rejected_status">
                            ${entry.athlete.athlete.birthYear}
                        </span>
                    </c:otherwise>
                    </c:choose>
                </td>
                <td class="entries_time">${entry.entryTime != null ? entry.entryTime : 'NA'}</td>
            </tr>
        </c:forEach>
        <tr><td colspan="2" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
</table>
<div>Всего участников: <c:out value="${fn:length(meet.meetAthletes.all)}"/></div>
</body>
</html>