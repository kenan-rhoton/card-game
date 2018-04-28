(ns api.player-view-functions
  (:require [rules.victory-conditions :as victory]
            [api.conversions :as conversions]
            [configs.messages :as messages]))

(defn get-cards
  "Returns cards as seen by a player"
  [game-state player-id]
  (vec (map
         #(if (= (:owner %) player-id)
            %
            {:location (:location %)
             :owner (:owner %)})
         (:cards game-state))))

(defn get-rows
  "Return the rows info as seen by the player"
  [game-state player-id]
  (let [player-ids (:player-ids game-state)
        opp-id (if (= (first player-ids) player-id)
                 (second player-ids)
                 (first player-ids))]
    (loop [rows (:rows game-state)
           row 0]
      (if (= row (count (:rows game-state)))
        rows
        (recur
          (assoc-in
            rows
            [row :scores]
            [(victory/points-in-row game-state row player-id)
             (victory/points-in-row game-state row opp-id)])
          (inc row))))))

(defn get-scores
  "Return the scores as seen by the player"
  [game-state player-id]
  (let [player-ids (:player-ids game-state)
        opp-id (if (= (first player-ids) player-id)
                 (second player-ids)
                 (first player-ids))]
    [(victory/get-won-rows game-state player-id)
     (victory/get-won-rows game-state opp-id)]))

(defn get-winner
  "Return the winner as seen by the player"
  [game-state]
  (victory/winner game-state))

(defn get-status
  "Returns the status of the game from a player's perspective"
  [game-state player-id]
  (cond (= (count (:player-ids game-state)) 1) messages/no-opp
        (nil? (get-in game-state [:next-play (conversions/player-num game-state player-id)])) messages/play
        :else messages/wait))
