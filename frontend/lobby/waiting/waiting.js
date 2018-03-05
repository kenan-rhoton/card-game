"use strict";

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");
const playerID = params.get("playerID");

function checkForOpponent() {
    var req = new XMLHttpRequest();
    req.open("GET", `http://backend:3000/games/${gameID}/player/${playerID}`);
    req.responseType = "json";
    
    req.onload = function () {
        const url = `/game/?gameID=${gameID}&playerID=${playerID}`;
        const status = req.response["status"];
        
        if (status !== "Waiting for an opponent") {
            window.location.href = url;
        }
    }

    req.send();
}

document.addEventListener('DOMContentLoaded', function () {
    var joinTemplate = document.getElementById("joinTemplate")
    var joinLink = joinTemplate.content.querySelector('#join-link');

    var text = document.createTextNode(`http://frontend:8080/lobby/join/?gameID=${gameID}`);
    joinLink.appendChild(text);
    
    document.getElementById("waiting-for-opponent").appendChild(joinLink);

    setInterval(checkForOpponent, 1000);
});
