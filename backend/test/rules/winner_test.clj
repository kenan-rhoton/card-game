(ns rules.winner-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.victory-conditions :as victory-conditions]
            [configs.hands :as hands]
            [test-helper :as helper]))

(defexpect finished-game
  ; We can tell if a game is finished
  (expect
    #(not (victory-conditions/finished? %))
    (create-game/new-game))

  (expect
    #(victory-conditions/finished? %)
    (helper/play-as-rules
      (fn [i] 0)
      (fn [i] 0))))

(defexpect winning-player
  ; Winner isn't set if game hasn't ended
  (expect
    nil
    (-> (create-game/new-game)
        (victory-conditions/winner)))

  ; Winner returns the winning player on a finished game, or 2 on a tie
  (expect
    #(= (victory-conditions/winner %) 0)
    (helper/play-as-rules
      (fn [i] (mod i 4))
      (fn [i] 0)))
  (expect
    #(= (victory-conditions/winner %) 1)
    (helper/play-as-rules
      (fn [i] 0)
      (fn [i] (mod i 4))))
  (expect
    #(= (victory-conditions/winner %) 2)
    (helper/play-as-rules
      (fn [i] 0)
      (fn [i] 0))))
