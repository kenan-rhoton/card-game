"use strict";
import { Selector } from 'testcafe'; // first import testcafe selectors

async function countCardsInRow (rows) {
    var countedCards = 0;
    for (var i = 0; i < await rows.count; ++i) {
        console.log(i);
        countedCards += await rows.nth(i).find('.card').count;
    }
    return countedCards;
}

export default class GamePage {
    constructor () {
        this.hand = Selector('.hand');
        this.cardsInHand = this.hand.find('.card');
        this.joinLink = Selector('#join-link');
        this.rows = Selector('#my-rows .game-row');
        this.opponentRows = Selector('#opp-rows .game-row');
        this.myScore = Selector('#my-score');
        this.opponentScore = Selector('#opponent-score');
        this.gameStatus = Selector('#game-status');

        this.cardsInMyRow = function (n) {
		return this.rows.nth(n).find('.card')
	}
        this.cardsInOpposingRow = function (n) {
		return this.opponentRows.nth(n).find('.card')
	}
    }

    checkState(testCase, inHand, inPlay){
        return testCase
            .expect(this.cardsInHand.count).eql(inHand)
            .expect(countCardsInRow(this.rows)).eql(inPlay)
            .expect(countCardsInRow(this.opponentRows)).eql(inPlay)
    }

}

