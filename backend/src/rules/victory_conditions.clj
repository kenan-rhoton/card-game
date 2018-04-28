(ns rules.victory-conditions
  (:require [rules.count-cards :as count-cards]))

(defn finished?
  "Tells us if a game has finished"
  [game-state]
  (= 0 (count-cards/count-cards game-state {:location [:hand]})))

(defn points-in-row
  "Tells us how many points the cards of a row has for a certain player"
  [game-state row-id player-id]
  (count-cards/count-cards game-state
                           {:location [:row row-id]
                            :owner player-id}
                           :power))

(defn player-wins-row?
  "Tells us if a player is winning a row"
  [game-state row-id player-id]
  (let [opponent-id (mod (inc player-id) 2)]
    (> (points-in-row game-state row-id player-id)
       (points-in-row game-state row-id opponent-id))))

(defn get-won-rows
  "Tells us how many rows a player is winning"
  [game-state player-id]
  (loop [won 0 row 0]
    (if (>= row (count (:rows game-state)))
      won
      (recur
        (if (player-wins-row? game-state row player-id)
          (inc won)
          won)
        (inc row)))))

(defn most-won-rows
  "Which player is winning the most rows?"
  [game-state]
  (let [one (get-won-rows game-state 0)
        two (get-won-rows game-state 1)]
    (cond
      (> one two) 0
      (< one two) 1
      :else 2)))

(defn winner
  "Tells us if there's a winner and if so, who it is"
  [game-state]
  (if (finished? game-state)
    (most-won-rows game-state)
    nil))
