"use strict";

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");
const playerID = params.get("playerID");
const gameURL = `/game/?gameID=${gameID}&playerID=${playerID}`;

var config = {}

var req = new XMLHttpRequest();
req.open("GET", "/config/data/config.yml");
req.responseType = "text";
req.onload = function () {
    config = jsyaml.load(req.response);
}
req.send();


function checkForOpponent() {
    var req = new XMLHttpRequest();
    req.open("GET", `http://backend:3000/games/${gameID}/player/${playerID}`);
    req.responseType = "json";
    
    req.onload = function () {
        const status = req.response["status"];
        
        if (status !== config.messages["no-opp"]) {
            window.location.href = gameURL;
        }
    }

    req.send();
}

document.addEventListener('DOMContentLoaded', function () {
    var joinLink = document.getElementById("joinTemplate")
        .content.querySelector('#join-link');

    var text = document.createTextNode(`http://frontend:8080/lobby/join/?gameID=${gameID}`);
    joinLink.appendChild(text);
    
    document.getElementById("waiting-for-opponent").appendChild(joinLink);

    setInterval(checkForOpponent, 1000);
});
