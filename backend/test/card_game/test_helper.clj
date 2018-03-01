(ns card-game.test-helper
  (:require [card-game.core :as core]
            [configs :as configs]))

(defn play-strategy
  "Plays a strategy"
  [game-state strategy]
  (apply core/play-card game-state strategy))

(defn play-strategies
  "Plays following strategies for both players"
  [game-state strategies-0 strategies-1]
  (if (= (count strategies-0) (count strategies-1))
      (loop [game-state game-state
             strategies-0 strategies-0
             strategies-1 strategies-1]
        (if (empty? strategies-0)
            game-state
            (recur
              (-> game-state
                  (play-strategy (first strategies-0))
                  (play-strategy (first strategies-1)))
              (rest strategies-0)
              (rest strategies-1))))
      ({:error "Use the same number of strategies for each player"})))

(defn end-game
  "Plays until the game ends"
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
