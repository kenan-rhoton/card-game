"use strict";

const config = require('config/config.js');
const fetch = require('node-fetch');
const backend = config.servers["backend"];

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");

async function joinGame() {
    const res = await fetch(`http://${backend}/games/${gameID}`, { method: 'POST' });
    const json = await res.json();
    console.log(json);
    const playerID = json["player-id"];
    const url = `/game.html?gameID=${gameID}&playerID=${playerID}`;

    //Use replace to remove ourselves from browser history
    window.location.replace(url);
}

joinGame();
