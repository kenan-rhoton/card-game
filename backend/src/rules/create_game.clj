(ns rules.create-game
  (:require [configs.hands :as hands]
            [configs.rows :as rows]))

(defn new-player
  "Creates a new player object"
[hand player]
{
 :hand (loop [final-hand []
              base-hand hand
              id (* 1000 player)]
         (if (empty? base-hand)
           final-hand
           (recur
             (conj final-hand
                   (assoc (first base-hand) :id id))
             (rest base-hand)
             (inc id))))
})

(defn new-game
  "Creates a new game object"
  ([] (new-game {}))
  ([ini-config]
   {

    :players (let [hands (:hands ini-config hands/default-hands)]
               [(new-player (first hands) 0)
                (new-player (second hands) 1)])

    :rows (vec (reduce
                 #(concat %1 [{:limit %2 :cards []}])
                 []
                 (:limits ini-config rows/default-limits)))
    :next-play [nil nil]
   }))
