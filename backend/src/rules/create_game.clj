(ns rules.create-game
  (:require [configs.hands :as hands]
            [configs.rows :as rows]))

(defn ^:private new-player
  "Creates a new player object"
  ([hand]
   {
    :hand hand
    }))

(defn new-game
  "Creates a new game object"
  ([] (new-game {}))
  ([ini-config]
   {
    :players (vec (repeat 2 (new-player (:hand ini-config hands/default-hand))))
    :rows (vec (repeat 5 {:limit (:limit ini-config rows/default-limit)
                          :cards []}))
    :next-play [nil nil]
    }))
