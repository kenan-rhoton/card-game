(ns card-game.api-winner-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.api.base :as api]
            [configs :as configs]))

(defexpect win-conditions
  ; Both stack all cards on one row -> tie
  (expect
    #(= :tie (:winner %))
    (loop [game (api/create-game)
           opponent (api/add-player (:game-id game))
           iteration (count (configs/ini-hand))]
      (if (= 0 iteration)
        (api/get-game (:game-id game) (:player-id game))
        (recur
          (api/play-card-as-player (:game-id game) (:player-id game) 0 0)
          (api/play-card-as-player (:game-id game) (:player-id opponent) 0 0)
          (dec iteration)))))

  ; Opponent stacks all cards on one row and I spread -> I win
  (expect
    #(= :me (:winner %))
    (loop [game (api/create-game)
           opponent (api/add-player (:game-id game))
           iteration (count (configs/ini-hand))]
      (if (= 0 iteration)
        (api/get-game (:game-id game) (:player-id game))
        (recur
          (api/play-card-as-player (:game-id game) (:player-id game) 0 (mod iteration 4))
          (api/play-card-as-player (:game-id game) (:player-id opponent) 0 0)
          (dec iteration)))))

  ; I stack all cards on one row and opponent spreads -> Opponent wins
  (expect
    #(= :opponent (:winner %))
    (loop [game (api/create-game)
           opponent (api/add-player (:game-id game))
           iteration (count (configs/ini-hand))]
      (if (= 0 iteration)
        (api/get-game (:game-id game) (:player-id game))
        (recur
          (api/play-card-as-player (:game-id game) (:player-id game) 0 0)
          (api/play-card-as-player (:game-id game) (:player-id opponent) 0 (mod iteration 4))
          (dec iteration))))))
