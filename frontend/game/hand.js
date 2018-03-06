"use strict";

define(function(require) {
    var helper = require('./helper.js');
    return {
        setHand(handState) {
            var hand = document.querySelector(".hand");
            helper.clear(hand);

            const baseCard = document.getElementById("card-template")
                .content.querySelector(".card");

            handState.forEach(function (cardInHand, index) {
                var newCard = baseCard.cloneNode(true);
                newCard.innerHTML = cardInHand["power"];
                newCard.setAttribute("index", index);

                hand.appendChild(newCard);
            });
        },
        dragCard(event) {
            event.dataTransfer.setData("handIndex", event.target.getAttribute("index"));
        }
    }
});
