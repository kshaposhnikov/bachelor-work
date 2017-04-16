var timerCount = 0;

function startStream() {
    var liveVideoCanvas = document.getElementById('liveVideo');
    var ctx = liveVideoCanvas.getContext('2d');
    var frame = document.getElementById('frame');


    timerCount = setInterval(function () {
        $.ajax({
            type: "GET",
            url: "/face-recognizer-service/webcam/stream/123",
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
        url: "/face-recognizer-service/webcam/stop/123",
    });
}

function closeNewCameraPopup() {
    $('#new-camera-popup').hide();
}

function showNewCameraPopup() {
    $('#new-camera-popup').show();
}

function onLoadNewCameraPopup() {
    alert("test");
}