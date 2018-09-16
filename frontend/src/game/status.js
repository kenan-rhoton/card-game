"use strict";

const fetch = require('node-fetch');
const config = require('config/config.js');
const params = require('game/params.js');
const backend = config.servers["backend"];

async function onGetStatus (action) {
    const res = await fetch(`http://${backend}/games/${params.gameID}/player/${params.playerID}`);
    const json = await res.json();
    action(json["status"]);
}

module.exports = {
    clickedCard: undefined,
    onGetStatus,
    setStatus() {
        onGetStatus(function(status) {
            document.getElementById("game-status").innerHTML = status;
        })
    }
};
