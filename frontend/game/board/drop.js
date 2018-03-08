"use strict";

define(function (require) {
    var helper = require('../helper.js');

    return {
        allow(event) {
            event.preventDefault();
        },
        onRow(event) {
            event.preventDefault();

            helper.playCard(event.target.getAttribute("rownum"),
                            event.dataTransfer.getData("handIndex"));
        }
    }
});
