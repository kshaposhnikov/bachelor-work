var timerCount = 0;
//var home = "/face-recognizer-service";
var home = "";

function startStream() {
    var liveVideoCanvas = document.getElementById('liveVideo');
    var ctx = liveVideoCanvas.getContext('2d');
    var frame = document.getElementById('frame');


    timerCount = setInterval(function () {
        $.ajax({
            type: "GET",
            url: home + "/webcam/stream/123",
            success: function (rawImage) {
                frame.src = 'data:image/png;base64,' + rawImage;
                ctx.drawImage(frame, 0, 0, 640, 480);
            }
        });
    }, 30);
}

function stopStream() {
    for (var interval = 1; interval <= timerCount; interval++) {
        clearInterval(interval);
    }
    timerCount = 0;

    $.ajax({
        type: "POST",
        url: home + "/webcam/stop/123"
    });
}

function closeNewCameraPopup() {
    $('#new-camera-popup').hide();
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
