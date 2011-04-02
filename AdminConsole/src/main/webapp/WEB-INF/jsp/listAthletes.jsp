<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="athletes" scope="request" type="java.util.List<ru.swimmasters.domain.MeetAthlete>"/>

<html>
<head>
    <title>Athletes</title>
    <style type="text/css">
        .entries_table {
            /* nop; */
        }
        .entries_athlete_header {
            border: 1px solid black;
        }
        .entries_event_time {
            border: 1px solid rgb(220, 220, 220);
        }
        .entries_event {
            border: 1px solid rgb(220, 220, 220);
        }
        .entries_time {
            border: 1px solid rgb(220, 220, 220);
        }
        .entries_event_delimiter {
            height: 1em;
        }
    </style>
</head>
<body>
<table class="entries_table" cellpadding="0" cellspacing="0">
    <c:forEach items="${athletes}" var="athlete">
        <tr>
            <td colspan="2" class="entries_athlete_header">
                <c:out value="${athlete.lastName}"/>, <c:out value="${athlete.firstName}"/>
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