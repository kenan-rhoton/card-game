"use strict";

define(function(require) {
    return {
        setScores() {
            var myScore = document.getElementById("my-score");
	    var opponentScore = document.getElementById("opponent-score");
	    myScore.innerHTML = "0";
            opponentScore.innerHTML = "0";
        }
    }
});
