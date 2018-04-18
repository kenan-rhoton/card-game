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
            play.playCard();
        }
    },
    allowDrop(event) {
        event.preventDefault();
    },
    dropOnRow(event) {
        event.preventDefault();
        status.clickedCard = document.querySelector('.card[index="'+
                event.dataTransfer.getData("handIndex")+
                '"]');
        status.clickedCard.setAttribute("row-played",
                event.target.getAttribute("rownum"));

        status.onGetStatus(function(status) {
            if (status === config.messages["play"]) {
                play.playCard();
            }
        })
    }
}
