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
    <title>Title</title>
</head>
<body>
    <p>Face Recognition Service</p>

    <div id="playerWindow">
        <canvas id="liveVideo" width="640" height="480" style="display: inline"></canvas>
        <button id="startStream" type="button" onclick="streamControl('start')">Start</button>
        <button id="stopStream" type="button" onclick="streamControl('stop')">Stop</button>
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
