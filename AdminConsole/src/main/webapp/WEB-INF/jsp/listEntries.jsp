<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="athletes" scope="request" type="java.util.List<ru.swimmasters.domain.Athlete>"/>

<html>
<head><title>Entries</title></head>
<body>
    <c:forEach items="${athletes}" var="athlete">
        L: <c:out value="${athlete.firstName}" /> <br />
    </c:forEach>
</body>
</html>