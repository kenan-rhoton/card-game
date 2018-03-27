(ns rules.create-game
  (:require [configs.hands :as hands]
            [configs.rows :as rows]))

(defn ^:private new-player
  "Creates a new player object"
  [hand]
  {
   :hand hand
  })

(defn new-game
  "Creates a new game object"
  [& ini-config]
  {
   :players (let [hands (:hands (first ini-config) hands/default-hands)]
              [(new-player (first hands))
               (new-player (second hands))])
   :rows (vec (reduce
                #(concat %1 [{:limit %2 :cards []}])
                []
                (:limits (first ini-config) rows/default-limits)))
   :next-play [nil nil]
   })
