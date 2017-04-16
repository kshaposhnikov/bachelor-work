var timerCount = 0;

function streamControl(mode) {
    if (mode == 'start') {
        var liveVideoCanvas = document.getElementById('liveVideo');
        var ctx = liveVideoCanvas.getContext('2d');
        var frame = document.getElementById('frame');


        timerCount = setInterval(function () {
            $.ajax({
                type: "GET",
                url: "/face-recognizer-service/webcam/stream/123",
                success: function (rawImage) {
                    //var frame = new Image();
                    frame.src = 'data:image/png;base64,' + rawImage;
                    ctx.drawImage(frame, 0, 0, 640, 480);
                }
            });
        }, 30);
    } else if (mode == 'stop'){
        for (var interval = 1; interval <= timerCount; interval++) {
            clearInterval(interval);
        }
        timerCount = 0;
    }
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