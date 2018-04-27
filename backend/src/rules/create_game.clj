(ns rules.create-game
  (:require [configs.hands :as hands]
            [configs.rows :as rows]))

(defn locate-in-hand
  "Creates location for a vec of cards on player's hand"
  [hand player]
  (vec (map #(assoc % :location [:hand] :owner player)
            hand))) 

(defn new-game
  "Creates a new game object"
  ([] (new-game {}))
  ([ini-config]
   {
    :cards (let [hands (:hands ini-config hands/default-hands)]
             (vec (concat (locate-in-hand (first hands) 0)
                          (locate-in-hand (second hands) 1))))
    :rows (vec (reduce
                 #(concat %1 [{:limit %2}])
                 []
                 (:limits ini-config rows/default-limits)))
    :next-play [nil nil]
   }))
