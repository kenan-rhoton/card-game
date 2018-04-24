"use strict";
const config = require('config/config.js');

var status = require('game/status.js');
var play = require('game/play/play.js');

function clearBackground() {
    document.querySelectorAll(".card").forEach(function(element) {
        element.style.background = "white"
    });
}

module.exports = {
    clickCard(card) {
        status.onGetStatus(function(game_status) {
            if (game_status === config.messages["play"]) {
                clearBackground();
                if (status.clickedCard === card) {
                    status.clickedCard = undefined;
                } else {
                    status.clickedCard = card;
                    card.style.background = "red";
                }

                document.getElementById("game-status").innerHTML = config.messages["play"];
            }
        })
    }  
}
