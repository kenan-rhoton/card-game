"use strict";

define(function (require) {
    var play = require('../play.js');

    return {
        allow(event) {
            event.preventDefault();
        },
        onRow(event) {
            event.preventDefault();

            play.playCard(event.target.getAttribute("rownum"),
                          event.dataTransfer.getData("handIndex"));
        }
    }
});
