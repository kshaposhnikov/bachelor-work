var timerCount = 0;
//var home = "/face-recognizer-service";
var home = "";

var activeCamera = null;
var ws = null;

$(document).ready(function() {
    $('select').material_select();
    $('.modal').modal();
});

function getFaces(cameraId) {
    timerCount += setInterval(function () {
        $.ajax({
            type: "GET",
            url: home + "/webcam/getFace/",
            data: {
                "camId" : cameraId
            },
            success: function (faceResponse) {
                $("#face-list")
                    .append(
                        "<span style=\"/* margin: 5px; *//* position: relative; */display: table-row;background-color: #4db6ac;box-shadow: 0 0 10px 0px rgba(0,0,0,0.5);/* padding: 10px; */height: 40px;\">" +
                            "<div style=\"width: 70px; float: left; /*margin-top: 5px;*/ \">" +
                                "<img class=\"circle\" width=\"64\" height=\"64\" src=\"" + 'data:image/png;base64,' + faceResponse.face + "\"/>" +
                            "</div>" +
                            "<div style=\"float: left; margin-top: 9px;\">" +
                                "<p>" + faceResponse.human.firstName + " " +  faceResponse.human.lastName + "</p>" +
                            "</div>" +
                        "</span>"
                    )
            }
        });
    }, 1000);
}

function closeNewCameraPopup() {
    $('#new-camera-popup').modal('close');
}

function connectToCamera() {
    activeCamera = $("#available-cameras").val();

    if (ws != null) {
        ws.send(activeCamera);
        getFaces(activeCamera);
    } else {
        ws = new SockJS('/video/stream');
        ws.onopen = function () {
            ws.send(activeCamera);
            getFaces(activeCamera);
        };
        ws.onmessage = function (json) {
            var liveVideoCanvas = document.getElementById('liveVideo');
            var ctx = liveVideoCanvas.getContext('2d');
            var frame = document.getElementById('frame');

            var data = JSON.parse(json.data);

            if (data.camId == activeCamera) {
                frame.src = "data:image/jpeg;base64," + data.frame;
                ctx.drawImage(frame, 0, 0, 640, 480);
            }
        };
        ws.onclose = function () {
        };
    }

    closeNewCameraPopup();
}

function disconnectFromCamera() {
    if (ws != null) {
        ws.close();
        ws = null;
    }

    for (var interval = 1; interval <= timerCount; interval++) {
        clearInterval(interval);
    }
    timerCount = 0;
}

function showNewCameraPopup() {
    $('#new-camera-popup').modal('open');

    $.ajax({
        type: "GET",
        url: home + "/webcam/getCameras",
        success: function (json) {
            $("#available-cameras").empty();
            $.each(json, function (i, camera) {
                $("#available-cameras").append($('<option>').text(camera.cameraName).attr('value', camera.cameraId));
            });
            $('select').material_select();
        }
    });
}

function getCameraDescriptionBySelectedCamera() {
    var cameraId = $("#available-cameras").val();

    $.ajax({
        type: "GET",
        url: home + "/webcam/getCamera",
        data: {
            "camId" : cameraId
        },
        success: function (cameraResponse) {
            $("#cameraDescription").text(cameraResponse.cameraDescription);
        }
    });
}