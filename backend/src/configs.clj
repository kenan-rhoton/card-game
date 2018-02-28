(ns configs)

(def out-of-turn "Out of turn play")

(def no-opp "Waiting for an opponent")

(def play "Playing")

(def wait "Waiting for opponent's play")

(def too-players "Too many players")

(defn ini-hand
  "Generates de initial hand (can't be empty)"
  []
  (vec (repeat 13 {:power 11})))
