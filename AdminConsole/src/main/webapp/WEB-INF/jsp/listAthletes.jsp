<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="athletes" scope="request" type="java.util.List<ru.swimmasters.domain.MeetAthlete>"/>

<%-- TODO: let IDE autocomplete css filename --%>

<html>
<head>
    <title>Athletes</title>
    <link rel="stylesheet" type="text/css" href="css/light-console.css" media="all">
</head>
<body>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:forEach items="${athletes}" var="athlete">
        <tr>
            <td colspan="2" class="entries_athlete_header">
                <c:out value="${athlete.fullName}"/>
            </td>
        </tr>
        <c:forEach items="${athlete.entries.all}" var="entry">
            <tr class="entries_event_time">
                <td class="entries_event">${entry.event.swimStyle.name}</td>
                <td class="entries_time">${entry.entryTime != null ? entry.entryTime : 'NA'}</td>
            </tr>
        </c:forEach>
        <tr><td colspan="2" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
</table>
</body>
</html>