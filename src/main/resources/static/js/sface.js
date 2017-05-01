var timerCount = 0;
//var home = "/face-recognizer-service";
var home = "";

var activeCamera = null;
var ws = null;

$(document).ready(function() {
    $('select').material_select();
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
                        "<div>" +
                            "<img class=\"circle\" width=\"64\" height=\"64\" src=\"" + 'data:image/png;base64,' + faceResponse.face + "\"/>" +
                            "<div style='float: right; margin-top: 10px;'>" + faceResponse.human.firstName + ' ' +  faceResponse.human.lastName + "</div>" +
                        "</div>"
                    )
            }
        });
    }, 1000);
}

function closeNewCameraPopup() {
    $('#new-camera-popup').hide();
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
    $('#new-camera-popup').show();

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

function startCamera(camId) {
    // This Function dynamically inject to table with list of the cameras on Settings page
    $.ajax({
        type: "POST",
        url: home + "/settings/camera/start/",
        data: {
            "cameraId" : camId
        }
    });
}

function stopCamera(camId) {
    // This Function dynamically inject to table with list of the cameras on Settings page
    $.ajax({
        type: "POST",
        url: home + "/settings/camera/stop/",
        data: {
            "cameraId" : camId
        }
    });
}

function removePerson(personId) {
    // This Function dynamically inject to table with list of the cameras on Settings page
    $.ajax({
        type: "POST",
        url: home + "/settings/removePerson",
        data: {
            "personId" : personId
        }
    });
}

function onLoadSettingsPage() {
    loadListCamerasForSettings();
    loadListPersonForSettings();
}

function loadListCamerasForSettings() {
    $.ajax({
        type: "GET",
        url: home + "/settings/getCameras",
        success: function (json) {
            $.each(json, function (i, camera) {
                $("#list_cameras").find('tbody').append(
                    $('<tr>').append(
                        $('<td>').text(camera.objectId)
                    ).append(
                        $('<td>').text(camera.name)
                    ).append(
                        $('<td>').text(camera.address)
                    ).append(
                        $('<td>').text(camera.description)
                    ).append(
                        $('<td>').append(
                            $('<a>')
                                .attr('class', 'waves-effect waves-light btn add_button')
                                .attr('onclick', 'startCamera(\''+ camera.objectId +'\')')
                                .text('Start')
                        ).append(
                            $('<a>')
                                .attr('class', 'waves-effect waves-light btn add_button')
                                .attr('onclick', 'stopCamera(\''+ camera.objectId +'\')')
                                .text('Stop')
                        )
                    )
                );
            });
        }
    });
}

function loadListPersonForSettings() {
    $.ajax({
        type: "GET",
        url: home + "/settings/getHumans",
        success: function (json) {
            $.each(json, function (i, person) {
                $("#list_person").find('tbody').append(
                    $('<tr>').append(
                        $('<td>').text(person.humanId)
                    ).append(
                        $('<td>').text(person.firstName)
                    ).append(
                        $('<td>').text(person.lastName)
                    ).append(
                        $('<td>').text(person.email)
                    ).append(
                        $('<td>').text(person.phone)
                    ).append(
                        $('<td>').append(
                            $('<a>')
                                .attr('class', 'waves-effect waves-light btn add_button')
                                .attr('onclick', 'removePerson(\''+ person.objectId +'\')')
                                .text('Remove')
                        )
                    )
                );
            });
        }
    });
}