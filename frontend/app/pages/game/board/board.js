"use strict";

var cleanup = require('game/cleanup.js');
var templates = require('game/templates.js');
var builder = require('game/board/builder.js');
var play = require('game/play/board.js');

function fetchRow(finder, index) {
    return document.querySelectorAll(finder)[index];
}

module.exports = {
    buildRows: builder.buildRows,
    setBoard(cards) {
        var gameRows = document.querySelectorAll(".game-row");

        gameRows.forEach(function (gameRow) {
            cleanup.clearChildren(gameRow);
        });

        cards.forEach(function (card, index) {
            const [owner, rowNum] = card.location;
            if(owner === 'row') {
                var newCard = builder.buildCard(templates.baseCard, card);
                newCard.setAttribute("rownum", rowNum);
                newCard.setAttribute("index", index);

                let rowSide = "#opp-rows .game-row";
                if (card["owner"] === "me") {
                  rowSide = "#my-rows .game-row";
                }
                fetchRow(rowSide, rowNum).appendChild(newCard);
            }
          // document.querySelectorAll("#limits .scores-row")[rownum].innerText = "(lim: " + row["limit"] + ")"
        });
    },
    allowDrop: play.allowDrop,
    dropOnRow: play.dropOnRow,
    clickRow: play.clickRow
};
