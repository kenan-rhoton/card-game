"use strict";

define(function(require) {
    return {
        setScores(scoresState) {
            var myScore = document.getElementById("my-score");
	    var opponentScore = document.getElementById("opponent-score");
	    myScore.innerHTML = scoresState[0];
            opponentScore.innerHTML = scoresState[1];
        }
    }
});
