var timerCount = 0;
//var home = "/face-recognizer-service";
var home = "";

function startStream() {
    var liveVideoCanvas = document.getElementById('liveVideo');
    var ctx = liveVideoCanvas.getContext('2d');
    var frame = document.getElementById('frame');
    var camId = "58f91ff1341801c374bc9520";

    timerCount = setInterval(function () {
        $.ajax({
            type: "GET",
            url: home + "/webcam/start/" + camId,
            success: function (rawImage) {
                frame.src = 'data:image/png;base64,' + rawImage;
                ctx.drawImage(frame, 0, 0, 640, 480);
            }
        });
    }, 30);

    getFaces(camId);
}

function stopStream() {
    for (var interval = 1; interval <= timerCount; interval++) {
        clearInterval(interval);
    }
    timerCount = 0;

    $.ajax({
        type: "POST",
        url: home + "/webcam/stop/58f91ff1341801c374bc9520"
    });
}

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
                        "<div>" +
                            "<img width=\"64\" height=\"64\" src=\"" + 'data:image/png;base64,' + faceResponse.face + "\"/>" +
                            "<div>" + faceResponse.human.firstName + ' ' +  faceResponse.human.lastName + "</div>" +
                        "</div>"
                    )
            }
        });
    }, 1000 * 30);
}

function closeNewCameraPopup() {
    $('#new-camera-popup').hide();
}

function activateCamera() {
    var cameraId = $("#available-cameras").val();

    $.ajax({
        type: "POST",
        url: home + "/webcam/activate",
        data: {
            "camId" : cameraId
        }
    });
    closeNewCameraPopup();
}

function showNewCameraPopup() {
    $('#new-camera-popup').show();

    $.ajax({
        type: "GET",
        url: home + "/webcam/getCameras",
        success: function (json) {
            $("#available-cameras").empty();
            $.each(json, function (i, camera) {
                $("#available-cameras").append($('<option>').text(camera.cameraName).attr('value', camera.cameraId));
            })
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

function updateRecognizer() {
    $.ajax({
        type: "POST",
        url: home + "/settings/updateRecognizer",
        success: function (result) {
            alert(result);
        }
    });
}
