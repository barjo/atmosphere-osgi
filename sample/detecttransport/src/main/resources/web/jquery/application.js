$(function () {
    "use strict";

    var transports = new Array();
    transports[0] = "websocket";
    transports[1] = "sse";
    transports[2] = "jsonp";
    transports[3] = "long-polling";
    transports[4] = "streaming";
    transports[5] = "ajax";
    var socket = $.atmosphere;

    var detect = $("#content");

    $.each(transports, function (index, transport) {
        var req = new $.atmosphere.AtmosphereRequest();

        req.url = 'http://' + document.location.host.toString() + '/atmosphere/detect';
        req.contentType = "application/json";
        req.transport = transport;
        req.headers = { "negotiating": "true" };

        req.onOpen = function (response) {
            detect.append('<p><span style="color:blue">' + transport
                + ' supported: ' + '</span>' + (response.transport == transport));
        }

        req.onReconnect = function (request) {
            request.close();
        }
        socket.subscribe(req)
    });
});
