<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Kirill
  Date: 23.03.2017
  Time: 1:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <c:url var="home" value="/" scope="request" />

    <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery-3.2.0.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/materialize.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/sface.js"></script>

    <link type="text/css" href="${pageContext.request.contextPath}/resource/css/materialize.min.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/resource/css/sface.css" rel="stylesheet">

    <title>SFace</title>
</head>

<body>
    <nav>
        <div class="nav-wrapper teal lighten-1">
            <a href="#" class="brand-logo">SFace</a>
            <ul class="right hide-on-med-and-down">
                <li><a href="${pageContext.request.contextPath}/image">Image</a></li>
                <li><a href="${pageContext.request.contextPath}/video">Video</a></li>
                <li><a href="new_person.jsp">New Person</a></li>
                <li><a>Login</a></li>
            </ul>
        </div>
    </nav>

    <div id="root">
    </div>

</body>
</html>
