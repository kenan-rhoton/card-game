"use strict";

const fetch = require('node-fetch');
var params = require('game/params.js');
var status = require('game/status.js');
const config = require('config/config.js');
const backend = config.servers["backend"];

module.exports = {
    playCard() {
        if (!status.clickedCard.hasAttribute("target") &&
            status.clickedCard.getAttribute("add-power") !== "undefined") {
            status.clickedCard.style.background = "yellow";    
        } else {
            const playData = {
                index: status.clickedCard.getAttribute("index"),
                row: status.clickedCard.getAttribute("row-played"),
            };
            status.clickedCard.style.background = "green";

            fetch(
                `http://${backend}/games/${params.gameID}/player/${params.playerID}`,
                {
                    method: 'POST',
                    body: JSON.stringify(playData),
                    headers: { 'Content-Type': 'application/json' }
                }
            )
                .then( () => status.setStatus() )
                .catch(error => console.log(error))
        }
    }
}
