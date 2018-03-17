"use strict";

module.exports = {
    setScores(scoresState) {
        document.getElementById("my-score").innerHTML = scoresState[0];
        document.getElementById("opponent-score").innerHTML = scoresState[1];
    }
}
