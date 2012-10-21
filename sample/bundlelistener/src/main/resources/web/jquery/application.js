$(function () {
    "use strict";

    var header = $('#header');
    var content = $('#content');
    var input = $('#input');
    var status = $('#status');
    var myName = false;
    var author = null;
    var logged = false;
    var socket = $.atmosphere;
    var subSocket;
    var transport = 'sse';

    // We are now ready to cut the request
    var request = { url: 'http://' + document.location.host.toString() + '/atmosphere/event',
        contentType : "application/json",
        logLevel : 'debug',
        shared : true,
        transport : transport ,
        trackMessageLength : true,
        fallbackTransport: 'long-polling'};


    request.onOpen = function(response) {
        content.html($('<p>', { text: 'Atmosphere connected using ' + response.transport }));
        transport = response.transport;
    };


    request.onReconnect = function (request, response) {
        socket.info("Reconnecting")
    };

    request.onMessage = function (response) {

        var message = response.responseBody;
        try {
            var json = jQuery.parseJSON(message);
        } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message.data);
            return;
        }
         var date = typeof(json.time) == 'string' ? parseInt(json.time) : json.time;
         addMessage(json.name, json.type, json.id, new Date(date));
    };

    request.onClose = function(response) {
        logged = false;
    }

    request.onError = function(response) {
        content.html($('<p>', { text: 'Sorry, but there\'s some problem with your '
            + 'socket or the server is down' }));
    };

    subSocket = socket.subscribe(request);

    function addMessage(name, type, id, datetime) {
        content.append('<p>'
        + (datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime.getHours()) + ':'
        + (datetime.getMinutes() < 10 ? '0' + datetime.getMinutes() : datetime.getMinutes()) + ':'
        + (datetime.getSeconds() < 10 ? '0' + datetime.getSeconds() : datetime.getSeconds())
        + '| [' +id+ '] '+name+': '+ type + '</p>');
    }
});
