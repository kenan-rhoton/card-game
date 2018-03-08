"use strict";

define(function(require) {
    var helper = require('./helper.js');
    
    var clickedCard = -1;

    function clearBackground() {
        document.querySelectorAll(".card").forEach(function(element) {
            element.style.background = "white"
        });
    }

    return {
        setHand(handState) {
            var hand = document.querySelector(".hand");
            helper.clearChildren(hand);

            handState.forEach(function (cardInHand, index) {
                var newCard = helper.baseCard.cloneNode(true);
                newCard.innerHTML = cardInHand["power"];
                newCard.setAttribute("index", index);

                hand.appendChild(newCard);
            });

            clickedCard = -1;
        },
        dragCard(event) {
            event.dataTransfer.setData("handIndex", event.target.getAttribute("index"));
        },
        clickCard(event) {
            clearBackground();
            if (clickedCard == event.target) {
                clickedCard = -1;
            } else {
                clickedCard = event.target;
                event.target.style.background = "red";
            }
        }
    }
});
