(ns card-game.api.helper
  (:require [card-game.core :as core]
            [card-game.victory-conditions :as victory-conditions]
            [card-game.persistence :as persistence]))

(defn player-num
  "Translates a Player id into an internal player representation"
  [game-state id] (.indexOf (:player-ids game-state) id))

(defn translate-player
  "Translates an internal player to a useful representation"
  [game-state internal me]
  (cond
    (nil? internal) nil
    (>= internal 2) :tie
    (= me (get-in game-state [:player-ids internal])) :me
    :else :opponent))

(defn get-game-as-player
  "Returns the part that ought to be visible to the player"
  [game-state player-id]
  {:game-id (:game-id game-state)
   :player-id player-id
   :hand (get-in game-state [:players (player-num game-state player-id) :hand])
   :rows (mapv #(mapv (fn [card]
                        (assoc card :owner
                          (translate-player game-state (:owner card) player-id)))
                      %)
               (:rows game-state))
   :winner (translate-player game-state (victory-conditions/winner game-state) player-id)})

(defn define-status
  "Returns the status of the game"
  [game-state player-id]
  (cond (= (count (:player-ids game-state)) 1) "Waiting for an opponent"
        (nil? (get-in game-state [:next-play (player-num game-state player-id)])) "Playing"
        :else "Waiting for opponent's play"))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn create-player
  [game]
  (loop [id (uuid)]
    (if (some #{id} (:player-ids game))
      (recur (uuid))
      (update game :player-ids #(vec (conj % id))))))