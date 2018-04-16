"use strict";

var cleanup = require('game/cleanup.js');
var templates = require('game/templates.js');
var status = require('game/status.js');
var play = require('game/play/hand.js');

module.exports = {
    setHand(handState) {
        var hand = document.querySelector(".hand");
        cleanup.clearChildren(hand);

        handState.forEach(function (cardInHand, index) {
            var newCard = templates.baseCard.cloneNode(true);
            newCard.innerHTML = cardInHand["power"]
            var addpower = cardInHand["add-power"]
            if (addpower != undefined) {
                newCard.innerHTML += " ("
                if (addpower > 0) newCard.innerHTML += "+"
                newCard.innerHTML += addpower + ")"
            }
            newCard.setAttribute("index", index);
            newCard.addEventListener('click', function(){play.clickCard(newCard)});

            hand.appendChild(newCard);
        });

        status.clickedCard = undefined;
    },
    dragCard(event) {
        event.dataTransfer.setData("handIndex", event.target.getAttribute("index"));
    }
}
