//var home = "/face-recognizer-service";
var home = "";

var selectedCameras = null;
var selectedHumans = null;

$(document).ready(function() {
    $('select').material_select();
    $('.modal').modal();

    $("#page-number").keyup(function(event){
        if(event.keyCode == 13){
            getHistory($("#page-number").val());
        }
    });
});

function closeUpdateCameraPopup() {
    $('#update-camera-popup').modal('close');
}

function closeUpdateHumanPopup() {
    $('#update-human-popup').modal('close');
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
            $('#camera_success_call_popup').val(json.successCall);
            $('#camera_error_call_popup').val(json.erroneousCall);
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
    getPageCount();
    getHistory(1);
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

function getPageCount() {
    $.ajax({
        type: "GET",
        url: home + "/settings/getPageCount",
        data: {
            "size": 50
        },
        success: function (count) {
            var label = "from " + count;
            $("#page-count").text(label);
            $("#page-number").attr("max", count).val(1);
        }
    });
}

function getHistory(pageNumber) {
    $.ajax({
        type: "GET",
        url: home + "/settings/getHistory",
        data: {
            "page": pageNumber,
            "size": 50
        },
        success: function (json) {
            $("#history-body").empty();
            $.each(json, function (i, history) {
                $("#history").find('tbody').append(
                    $('<tr>').append(
                        $('<td>').text(new Date(history.visitDate).toUTCString())
                    ).append(
                        $('<td>').text(history.humanId)
                    ).append(
                        $('<td>').text(history.firstName)
                    ).append(
                        $('<td>').text(history.lastName)
                    ).append(
                        $('<td>').text(history.cameraId)
                    ).append(
                        $('<td>').text(history.cameraName)
                    )
                );
            });
        }
    });
}