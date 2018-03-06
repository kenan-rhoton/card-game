"use strict";

let params = new URLSearchParams(document.location.search.substring(1));

define(function() {
    return {
        clear(elem) {
            while (elem.firstChild) {
                elem.removeChild(elem.firstChild);
            }
        },
        gameID: params.get("gameID"),
        playerID: params.get("playerID")
    }
})
