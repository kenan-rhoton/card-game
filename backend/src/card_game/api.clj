(ns card-game.api
  (:use card-game.core))

(defn create-game
  "Creates a new instance of a game"
  [] {:game-id 1
      :player-id 1})

(defn add-player
  "Adds a player to a game"
  [game-id] {:game-id 1
             :player-id 1})
