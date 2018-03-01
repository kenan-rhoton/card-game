(ns card-game.test-helper
  (:require [card-game.core :as core]
            [configs :as configs]))

(defn end-game
  [game-state]
   (loop [game-state game-state]
     (if (empty? (get-in game-state [:players 0 :hand]))
         game-state
         (recur
           (-> game-state
               (core/play-card 0 0 0)
               (core/play-card 1 0 0))))))

(defn ini-hand-power
  [x]
  (get-in (configs/ini-hand) [x :power]))
