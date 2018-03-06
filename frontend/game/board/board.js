"use strict";

define(function (require) {
    var helper = require('../helper.js');
    var builder = require('./builder.js');
    var drop = require('./drop.js');

    function fetchRow(finder, index) {
        return document.querySelectorAll(finder)[index];
    }

    return {
        buildRows: builder.buildRows,
        setBoard(boardState) {
            const baseCard = document.getElementById("card-template")
                .content.querySelector(".card");

            var gameRows = document.querySelectorAll("game-row");
            gameRows.forEach(function (gameRow) {
                helper.clear(gameRow);
            })

            boardState.forEach(function (row, rownum) {
                row.forEach(function (cardInRow) {
                    var newCard = baseCard.cloneNode(true);
                    newCard.setAttribute("draggable","false");
                    newCard.classList.remove("col-1");
                    newCard.classList.add("col-2");
                    newCard.innerHTML = cardInRow["power"];

                    if (cardInRow["owner"] === "me") {
                        fetchRow("#my-rows .game-row", rownum).appendChild(newCard);
                    } else {
                        fetchRow("#opp-rows .game-row", rownum).appendChild(newCard);
                    }
                });
            });
        },
        allowDrop: drop.allow,
        dropOnRow: drop.onRow,
    }
});
