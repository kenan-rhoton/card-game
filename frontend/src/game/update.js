"use strict";

const fetch = require('node-fetch');
const config = require('config/config.js');
const backend = config.servers["backend"];

var board = require('game/board/board.js');
var hand = require('game/hand.js');
var scores = require('game/scores.js');
var status = require('game/status.js');
const params = require('game/params.js');

function setState(state) {
    hand.setHand(state["cards"]);
    board.setBoard(state["cards"]);
    scores.setScores(state["scores"]);
    scores.setScoresByRow(state["rows"]);

    status.setStatus();
}

var lastnum = 0;
async function updateGame() {
    const res = await fetch( `http://${backend}/games/${params.gameID}/player/${params.playerID}`);
    const json = await res.json();
    var num = json["cards"].filter(card => card.location[0] === 'hand').length;
    if (num !== lastnum){
        lastnum = num;
        setState(json);
    }
}

module.exports = updateGame;
