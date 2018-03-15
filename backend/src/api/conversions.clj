(ns api.conversions)

(defn player-num
  "Translates a Player id into an internal player representation"
  [game-state id] (.indexOf (:player-ids game-state) id))

(defn translate-player
  "Translates an internal player to a human-readable representation"
  [game-state internal me]
  (cond
    (nil? internal) nil
    (>= internal 2) :tie
    (= me (get-in game-state [:player-ids internal])) :me
    :else :opponent))

(defn ^:private uuid [] (str (java.util.UUID/randomUUID)))

(defn create-player
  [game]
  (loop [id (uuid)]
    (if (some #{id} (:player-ids game))
      (recur (uuid))
      (update game :player-ids #(vec (conj % id))))))
