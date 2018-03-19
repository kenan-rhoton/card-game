(ns rules.alter-card)

(defn alter-card
  "Alters a cards' values, merging the new values with existing ones"
  [game-state path new-values]
  (update-in game-state path #(merge % new-values)))

(defn relative-power
  "Alters a cards' power, by adding the passed value to it"
  [game-state path increase]
  (let [power (get-in game-state (conj path :power))]
    (alter-card game-state path {:power (+ power increase)})))
