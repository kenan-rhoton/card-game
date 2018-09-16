"use strict";

const fetch = require('node-fetch');
var params = require('game/params.js');
var status = require('game/status.js');
const config = require('config/config.js');
const backend = config.servers["backend"];

module.exports = {
    playCard(card) {
        if (!card.hasAttribute("target") &&
            card.getAttribute("add-power") !== "undefined") {
            card.style.background = "yellow";
            status.clickedCard = card;
            document.getElementById("game-status").innerHTML = "Select a target";
        } else {
            if (!card.hasAttribute("target")) {
                card.setAttribute("target", [0, 0]);
            }
            const playData = {
                index: card.getAttribute("index"),
                row: card.getAttribute("row-played"),
                targetrownum: card.getAttribute("target").split(',')[0],
                targetindex: card.getAttribute("target").split(',')[1],
            };
            card.style.background = "green";

            fetch(
                `http://${backend}/games/${params.gameID}/player/${params.playerID}`,
                {
                    method: 'POST',
                    body: JSON.stringify(playData),
                    headers: { 'Content-Type': 'application/json' }
                }
            )
            .then(async (resp) => {
                const json = await resp.json();
                status.setStatus()
            })
            .catch(error => console.log(error))
        }
    }
};
