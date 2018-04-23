"use strict";

const play = require('game/play/play.js');

test('Playing cards changes background as intended', () => {
    var card = document.createElement("div");

    card.setAttribute("add-power", "undefined");
    play.playCard(card);
    expect(card.style.background).toBe("green");
    
    card.setAttribute("add-power", 1);
    play.playCard(card);
    expect(card.style.background).toBe("yellow");

    card.setAttribute("target", 1);
    play.playCard(card);
    expect(card.style.background).toBe("green");
});
