"use strict";
const config = require('config/config.js');

var status = require('game/status.js');
var play = require('game/play/play.js');

module.exports = {
    clickRow(event) {
        if (status.clickedCard !== undefined) {
            event.preventDefault();

            var rownum = event.target.getAttribute("rownum");
            status.clickedCard.setAttribute("row-played", rownum);
            if (status.clickedCard.getAttribute("add-power") == "undefined") {
                play.playCard();
            } else {
                status.clickedCard.style.background = "yellow";
            }
        }
    },
    allowDrop(event) {
        event.preventDefault();
    },
    dropOnRow(event) {
        event.preventDefault();
        const rownum = event.target.getAttribute("rownum");
        const cardindex = event.dataTransfer.getData("handIndex");

        status.onGetStatus(function(status) {
            if (status === config.messages["play"]) {
                play.playCard(rownum, cardindex);
            }
        })
    }
}
