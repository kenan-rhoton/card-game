(ns card-game.winner-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]
            [card-game.victory-conditions :as victory-conditions]
            [card-game.test-helper :as helper]))

(defexpect finished-game
  ; We can tell if a game is finished
  (expect
    #(not (victory-conditions/finished? %))
    (core/new-game))

  (expect
    #(victory-conditions/finished? %)
    (helper/end-game (core/new-game))))

(defexpect winning-player
  ; Winner isn't set if game hasn't ended
  (expect
    nil
    (-> (core/new-game)
        (victory-conditions/winner)))

  ; Winner returns the winning player on a finished game, or 2 on a tie
  (expect
    0
    (-> (core/new-game)
        (core/play-card 0 0 3)
        (core/play-card 1 0 2)
        (core/play-card 0 0 0)
        (core/play-card 1 0 2)
        (helper/end-game)
        (victory-conditions/winner)))
  (expect
    1
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 0 1)
        (core/play-card 0 0 0)
        (core/play-card 1 0 2)
        (helper/end-game)
        (victory-conditions/winner)))

  (expect
    2
    (-> (core/new-game)
        (helper/end-game)
        (victory-conditions/winner))))
