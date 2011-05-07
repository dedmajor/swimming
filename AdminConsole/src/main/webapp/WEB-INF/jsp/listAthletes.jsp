<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- TODO: replace with Domain Collection --%>
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
            <td class="entries_athlete_header" style="border-right:0;">
                <c:out value="${athlete.fullName}, ${athlete.birthYear}, ${athlete.club.name}"/>
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
                        <a href="<c:url value="/approveAthlete.html?athlete=${athlete.id}" />">не подтвержден</a>
                    </c:if>
                    &nbsp;
                </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <c:forEach items="${athlete.entries.all}" var="entry">
            <tr class="entries_athlete_time">
                <td class="entries_event">${entry.event.swimStyle.name}</td>
                <td class="entries_time">${entry.entryTime != null ? entry.entryTime : 'NA'}</td>
            </tr>
        </c:forEach>
        <tr><td colspan="2" class="entries_event_delimiter">&nbsp;</td></tr>
    </c:forEach>
</table>
</body>
</html>