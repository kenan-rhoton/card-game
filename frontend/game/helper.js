"use strict";

let params = new URLSearchParams(document.location.search.substring(1));

const gameID = params.get("gameID");
const playerID = params.get("playerID");

define(function() {
    return {
        clearChildren(elem) {
            while (elem.firstChild) {
                elem.removeChild(elem.firstChild);
            }
        },
        clickedCard: undefined,
        gameID: gameID,
        playerID: playerID,
        baseCard: document.getElementById("card-template")
                .content.querySelector(".card"),
        playCard(rownum, cardindex) {
            var req = new XMLHttpRequest();
            req.open("POST", `http://backend:3000/games/${gameID}/player/${playerID}`);
            req.setRequestHeader("Content-type", "application/json");
            req.responseType = "json";

            const playData = {
                index: cardindex,
                row: rownum,
            };

            req.send(JSON.stringify(playData));
        }
    }
})
