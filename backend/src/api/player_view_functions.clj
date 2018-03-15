(ns api.player-view-functions
  (:require [rules.victory-conditions :as victory]
            [configs.messages :as messages]))

(defn player-num
  "Translates a Player id into an internal player representation"
  [game-state id] (.indexOf (:player-ids game-state) id))

(defn ^:private translate-player
  "Translates an internal player to a human-readable representation"
  [game-state internal me]
  (cond
    (nil? internal) nil
    (>= internal 2) :tie
    (= me (get-in game-state [:player-ids internal])) :me
    :else :opponent))

(defn get-hand
  "Return hand as seen by the player"
  [game-state player-id]
  (get-in game-state [:players (player-num game-state player-id) :hand]))

(defn get-rows
  "Return the rows as seen by the player"
  [game-state player-id]
  (mapv #(mapv (fn [card]
                 (assoc card :owner
                   (translate-player game-state (:owner card) player-id)))
               %)
        (:rows game-state)))

(defn get-rows-power
  "Return the powers of each row as seen by the player"
  [game-state player-id]
  (let [player (player-num game-state player-id)
        opponent (mod (inc player) 2)]
    (loop [rows-power []
           rows (:rows game-state)]
      (if (empty? rows)
        rows-power
        (recur
          (conj rows-power
                [(victory/points-in-row (first rows) player)
                 (victory/points-in-row (first rows) opponent)])
          (rest rows))))))

(defn get-scores
  "Return the scores as seen by the player"
  [game-state player-id]
  (let [player (player-num game-state player-id)
        opponent (mod (inc player) 2)]
    [(victory/get-won-rows game-state player)
     (victory/get-won-rows game-state opponent)]))

(defn get-winner
  "Return the winner as seen by the player"
  [game-state player-id]
  (translate-player game-state (victory/winner game-state) player-id))

(defn get-status
  "Returns the status of the game from a player's perspective"
  [game-state player-id]
  (cond (= (count (:player-ids game-state)) 1) messages/no-opp
        (nil? (get-in game-state [:next-play (player-num game-state player-id)])) messages/play
        :else messages/wait))