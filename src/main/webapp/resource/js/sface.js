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
