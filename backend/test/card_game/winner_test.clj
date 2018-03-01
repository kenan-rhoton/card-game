(ns card-game.winner-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]
            [card-game.victory-conditions :as victory-conditions]
            [configs :as configs]))

(defn play-a-game-helper
  [strategy1 strategy2]
  (loop [game-state (core/new-game)
         iteration (count (configs/ini-hand))]
      (if (= 0 iteration)
        game-state
        (recur
          (-> game-state
              (core/play-card 0 0 (strategy1 iteration))
              (core/play-card 1 0 (strategy2 iteration)))
          (dec iteration)))))

(defexpect finished-game
  ; We can tell if a game is finished
  (expect
    #(not (victory-conditions/finished? %))
    (core/new-game))

  (expect
    #(victory-conditions/finished? %)
    (play-a-game-helper
      (fn [i] 0)
      (fn [i] 0))))

(defexpect winning-player
  ; Winner isn't set if game hasn't ended
  (expect
    nil
    (-> (core/new-game)
        (victory-conditions/winner)))

  ; Winner returns the winning player on a finished game, or 2 on a tie
  (expect
    #(= (victory-conditions/winner %) 0)
    (play-a-game-helper
      (fn [i] (mod i 4))
      (fn [i] 0)))
  (expect
    #(= (victory-conditions/winner %) 1)
    (play-a-game-helper
      (fn [i] 0)
      (fn [i] (mod i 4))))
  (expect
    #(= (victory-conditions/winner %) 2)
    (play-a-game-helper
      (fn [i] 0)
      (fn [i] 0))))
