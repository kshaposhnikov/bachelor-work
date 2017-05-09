var timerCount = 0;
//var home = "/face-recognizer-service";
var home = "";

var activeCamera = null;
var ws = null;

var selectedCameras = null;
var selectedHumans = null;

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
                        "<div style=\"position: relative; display: table-row;\">" +
                            "<div style=\"width: 70px; float: left; margin-top: 5px;\">" +
                                "<img class=\"circle\" width=\"64\" height=\"64\" src=\"" + 'data:image/png;base64,' + faceResponse.face + "\"/>" +
                            "</div>" +
                            "<div style=\"float: left; margin-top: 9px;\">" +
                                "<p>" + faceResponse.human.firstName + ' ' +  faceResponse.human.lastName + "</p>" +
                            "</div>" +
                        "</div>"
                    )
            }
        });
    }, 1000);
}

function closeNewCameraPopup() {
    $('#new-camera-popup').modal('close');
}

function closeUpdateCameraPopup() {
    $('#update-camera-popup').modal('close');
}

function closeUpdateHumanPopup() {
    $('#update-human-popup').modal('close');
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

function showUpdateCameraPopup(cameraId) {
    $('#update-camera-popup').modal('open');

    $.ajax({
        type: "GET",
        url: home + "/settings/getCamera",
        data: {
            "cameraId" : cameraId
        },
        success: function (json) {
            $('#camera_name_popup').val(json.name);
            $('#camera_address_popup').val(json.address);
            $('#camera_description_popup').val(json.description);
            $('#camera_object_id_popup').val(json.objectId);
            Materialize.updateTextFields();
        }
    });
}

function showUpdateHumanPopup(humanId) {
    // This Function dynamically inject to table with list of the cameras on Settings page
    $('#update-human-popup').modal('open');

    $.ajax({
        type: "GET",
        url: home + "/settings/getHuman",
        data: {
            "humanId" : humanId
        },
        success: function (json) {
            $('#first_name_popup').val(json.firstName);
            $('#last_name_popup').val(json.lastName);
            $('#email_popup').val(json.email);
            $('#phone_popup').val(json.phone);
            $('#human_object_id_popup').val(json.objectId);
            $('#human_id_popup').val(json.humanId);
            Materialize.updateTextFields();
        }
    });
}

function startCamera() {
    for (var i = 0; i < selectedCameras.length; i++) {
        $.ajax({
            type: "POST",
            url: home + "/settings/camera/start/",
            data: {
                "cameraId": selectedCameras[i]
            }
        });
    }
}

function stopCamera() {
    for (var i = 0; i < selectedCameras.length; i++) {
        $.ajax({
            type: "POST",
            url: home + "/settings/camera/stop/",
            data: {
                "cameraId": selectedCameras[i]
            }
        });
    }
}

function removePerson() {
    for (var i = 0; i < selectedHumans.length; i++) {
        $.ajax({
            type: "POST",
            url: home + "/settings/removePerson",
            data: {
                "personId": selectedHumans[i]
            }
        });
    }
}

function removeCamera() {
    for (var i = 0; i < selectedCameras.length; i++) {
        $.ajax({
            type: "POST",
            url: home + "/settings/removeCamera",
            data: {
                "cameraId": selectedCameras[i]
            }
        });
    }
}

function updateSelectedPersons(humanId) {
    // This Function dynamically inject to table with list of the cameras on Settings page
    if (selectedHumans == null) {
        selectedHumans = [];
    }
    updateSelectedItems(selectedHumans, humanId);
}

function updateSelectedCameras(cameraId) {
    // This Function dynamically inject to table with list of the cameras on Settings page
    if (selectedCameras == null) {
        selectedCameras = [];
    }
    updateSelectedItems(selectedCameras, cameraId)
}

function updateSelectedItems(list, element) {
    var exists = -1;
    for (var i = 0; i < list.length; i++) {
        if (list[i] == element) {
           exists = i;
        }
    }

    if (exists == -1) {
        list.push(element);
    } else {
        list.splice(exists, 1);
    }
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
                        $('<td>').append(
                            $('<input>')
                                .attr('id', 'camera_checkbox_' + camera.objectId)
                                .attr('type', 'checkbox')
                                .attr('onchange', 'updateSelectedCameras(\'' + camera.objectId + '\')')
                        ).append(
                            $('<label>')
                                .attr('for', 'camera_checkbox_' + camera.objectId)
                        )
                    ).append(
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
                                .attr('onclick', 'showUpdateCameraPopup(\''+ camera.objectId +'\')')
                                .text('Update')
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
                        $('<td>').append(
                            $('<input>')
                                .attr('id', 'human_checkbox_' + person.objectId)
                                .attr('type', 'checkbox')
                                .attr('onchange', 'updateSelectedPersons(\'' + person.objectId + '\')')
                        ).append(
                            $('<label>')
                                .attr('for', 'human_checkbox_' + person.objectId)
                        )
                    ).append(
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
                                .attr('onclick', 'showUpdateHumanPopup(\''+ person.objectId +'\')')
                                .text('Update')
                        )
                    )
                );
            });
        }
    });
}