"use strict";

function clear(elem) {
    while (elem.firstChild) {
        elem.removeChild(elem.firstChild);
    }
}

function fetchRow(finder, index) {
    return document.querySelectorAll(finder)[index];
}

function setState(state) {
    var hand = document.querySelector(".hand");
    clear(hand);

    const baseCard = document.getElementById("card-template")
        .content.querySelector(".card");

    state["hand"].forEach(function (cardInHand, index) {
        var newCard = baseCard.cloneNode(true);
        newCard.innerHTML = cardInHand["power"];
        newCard.setAttribute("index", index);

        hand.appendChild(newCard);
    });

    var gameRows = document.querySelectorAll("game-row");
    gameRows.forEach(function (gameRow) {
        clear(gameRow);
    })

    state["rows"].forEach(function (row, rownum) {
        row.forEach(function (cardInRow) {
            var newCard = baseCard.cloneNode(true);
            newCard.innerHTML = cardInRow["power"];

            if (cardInRow["owner"] === "me") {
                fetchRow("#my-rows .game-row", rownum).appendChild(newCard);
            } else {
                fetchRow("#opp-rows .game-row", rownum).appendChild(newCard);
            }
        });
    });
}

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");
const playerID = params.get("playerID");
var lastnum = 0

function updateGame() {
    var req = new XMLHttpRequest();
    req.open("GET", `http://backend:3000/games/${gameID}/player/${playerID}`);
    req.responseType = "json";
    
    req.onload = function () {
        var num = req.response["hand"].length;
        if (num !== lastnum){
            lastnum = num;
            setState(req.response);
        }
    }

    req.send();
}

document.addEventListener('DOMContentLoaded', function() {

    const baseRow = document.getElementById("row-template")
        .content.querySelector("div");
    const myRows = document.getElementById("my-rows");
    const oppRows = document.getElementById("opp-rows");

    for (var i = 0; i < 5; i++) {
        var myNewRow = baseRow.cloneNode(true);
        myNewRow.setAttribute("rownum", i);
        
        myRows.appendChild(myNewRow);

        var oppRow = baseRow.cloneNode(true);
        oppRows.appendChild(oppRow);
    }
    setInterval(updateGame, 1000);
});

function allowDrop(event) {
    event.preventDefault();
}

function dropOnRow(event) {
    event.preventDefault();
    var req = new XMLHttpRequest();
    req.open("POST", `http://backend:3000/games/${gameID}/player/${playerID}`);
    req.setRequestHeader("Content-type", "application/json");
    req.responseType = "json";
    
    const data = event.dataTransfer.getData("handIndex");
    const playData = {
        index: data,
        row: event.target.getAttribute("rownum")
    };

    req.onload = function () {
        console.log(req.response);
    }
    req.send(JSON.stringify(playData));

}

function dragCardFromHand(event) {
    event.dataTransfer.setData("handIndex", event.target.getAttribute("index"));
}
