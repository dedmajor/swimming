<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="heat" scope="request" type="ru.swimmasters.domain.Heat"/>

<html>
<head>
    <title>Заплыв <c:out value="${heat.absoluteNumber}"/></title>
    <link rel="stylesheet" type="text/css" href="css/light-console.css" media="all">
</head>
<body>
<h1>
    Заплыв <c:out value="${heat.absoluteNumber}"/>
</h1>
<h3>
    ${heat.number} / ${heat.totalHeatsInEvent}
</h3>
<form:form>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:forEach items="${heat.entries.all}" var="entry" varStatus="status">
        <tr class="entries_athlete_time">
            <td class="entries_event">${entry.lane}</td>
            <td class="entries_event">${entry.athlete.athlete.fullName}</td>
            <td class="entries_event">${entry.athlete.athlete.birthDate}</td>
            <td class="entries_event">${entry.ageGroup}</td>
            <td class="entries_time">${entry.entryTime != null ? entry.entryTime : 'NA'}</td>
            <td>
                    <form:hidden path="all[${status.index}].laneNumber" />
                    <form:input path="all[${status.index}].finalTime" />
            </td>
        </tr>
    </c:forEach>
</table>
<input type="submit"/>
</form:form>
</body>
</html>