"use strict";
import { Selector } from 'testcafe'; // first import testcafe selectors

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

    checkState(testCase, inHand, inPlay, myRow, opposingRow){
        return testCase
            .expect(this.cardsInHand.count).eql(inHand)
            .expect(this.cardsInMyRow(myRow).count).eql(inPlay)
            .expect(this.cardsInOpposingRow(opposingRow).count).eql(inPlay)
    }

}

