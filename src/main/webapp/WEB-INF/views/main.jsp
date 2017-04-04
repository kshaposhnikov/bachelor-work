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

    <link type="text/css" href="${pageContext.request.contextPath}/resource/css/materialize.min.css" rel="stylesheet">
    <link type="text/css" href="${pageContext.request.contextPath}/resource/css/sface.css" rel="stylesheet">

    <title>SFace</title>

    <nav>
        <div class="nav-wrapper">
            <a href="#" class="brand-logo">SFace</a>
            <ul class="right hide-on-med-and-down">
                <li><a>Login</a></li>
            </ul>
        </div>
    </nav>

</head>

<body>
    <div id="root">
        <div id="player-window" class="player">
            <div id="video-show">
                <%--<canvas id="liveVideo" width="640" height="480" style="display: inline">--%>
                <img src="${pageContext.request.contextPath}/resource/img/test.png" width="640" height="480" alt="test"/>
                <%--</canvas>--%>
            </div>
            <div id="control-panel" class="control-panel">
                <a class="waves-effect waves-light btn" id="startStream" onclick="streamControl('start')">Start</a>
                <a class="waves-effect waves-light btn" id="stopStream" onclick="streamControl('stop')">Stop</a>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        var timer = 0;
        function streamControl(mode) {
            if (mode == 'start') {
                var liveVideoCanvas = $("#liveVideo");
                var ctx = liveVideoCanvas.get()[0].getContext('2d');

                timer = setInterval(function () {
                    $.ajax({
                        type: "GET",
                        url: "${home}/webcam/stream/123",
                        success: function (rawImage) {
                            var frame = new Image();
                            frame.src = 'data:image/png;base64,' + rawImage;
                            ctx.drawImage(frame, 0, 0, 640, 480);
                        }
                    });
                }, 30);
            } else if (mode == 'stop'){
                for (var interval = 1; interval <= timer; interval++) {
                    clearInterval(interval);
                }
                timer = 0;
            }
        }
    </script>

</body>
</html>
