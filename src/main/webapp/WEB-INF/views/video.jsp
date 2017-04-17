<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Kirill
  Date: 06.04.2017
  Time: 22:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <!--<c:url var="home" value="/" scope="request" />-->

        <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery-3.2.0.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/materialize.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/sface.js"></script>

        <link type="text/css" href="${pageContext.request.contextPath}/resource/css/materialize.min.css" rel="stylesheet">
        <link type="text/css" href="${pageContext.request.contextPath}/resource/css/sface.css" rel="stylesheet">

        <title>SFace - Video</title>
    </head>
    <body>

        <div id="root" class="root">
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

            <div id="active-camera-list" class="active-camera-list-style">
                <p id="camera-list-title">Activities</p>
            </div>

            <div id="sface-cente-block" class="sface-center-block">
                <div id="player-window" class="player">
                    <div id="video-show">
                        <div style="display: none">
                            <canvas id="liveVideo" width="640" height="480" style="display: inline">
                            </canvas>
                        </div>
                        <img id="frame" width="640" height="480"/>

                    </div>
                    <div id="control-panel" class="control-panel">
                        <a class="waves-effect waves-light btn" id="startStream" onclick="startStream()">Start</a>
                        <a class="waves-effect waves-light btn" id="stopStream" onclick="stopStream()">Stop</a>
                    </div>
                </div>

                <div class="fixed-action-btn">
                    <a class="btn-floating btn-large waves-effect waves-light red accent-4" onclick="showNewCameraPopup()">
                        <img id="fab_video" class="material-icons" src="${pageContext.request.contextPath}/resource/img/ic_add_white.png">
                    </a>
                </div>
            </div>

            <div id="recognized-faces" class="recognized-list">
                <p id="recognized-faces-title">Recognized</p>
            </div>

            <div id="new-camera-popup" class="select-camera">
                <form>
                    <select title="Name" id="available-cameras" size="5">
                        <option value="Default">Default</option>
                    </select>
                </form>
                <a class="waves-effect waves-light btn" onclick="closeNewCameraPopup()">OK</a>
            </div>
        </div>
    </body>
</html>
