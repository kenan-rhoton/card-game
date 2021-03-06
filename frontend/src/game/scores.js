"use strict";

module.exports = {
    setScores(scoresState) {
        document.getElementById("my-score").innerHTML = scoresState[0];
        document.getElementById("opponent-score").innerHTML = scoresState[1];
    },
    setScoresByRow(scoresByRowState) {
        scoresByRowState.forEach(function (row, rownum) {
            document.querySelectorAll("#row-scores .scores-row")[rownum].innerText = row[0] + " - " + row[1];
        });
    }
};
