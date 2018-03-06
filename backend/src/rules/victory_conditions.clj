(ns rules.victory-conditions)

(defn finished?
  "Tells us if a game has finished"
  [game-state]
  (and
    (= 0 (count (get-in game-state [:players 0 :hand])))
    (= 0 (count (get-in game-state [:players 1 :hand])))))

(defn points-in-row
  "Tells us how many points a certain row contains for a certain player"
  [game-state row player]
  (let [row-data (get-in game-state [:rows row])]
    (reduce
      #(if (= (:owner %2) player)
         (+ %1 (:power %2))
         %1)
      0
      row-data)))

(defn get-points
  "Tells us how many points each player has on each row"
  ; Only used on tests
  [game-state]
  (loop [row 0
         points []]
    (if (> row 4)
      points
      (recur
        (inc row)
        (conj 
          points 
          [(points-in-row game-state row 0)
           (points-in-row game-state row 1)])))))

(defn get-won-rows
  "Tells us how many rows a player is winning"
  [game-state player]
  (loop [won 0
         row 0]
    (if (= row 5)
      won
      (recur
        (if (> (points-in-row game-state row player)
               (points-in-row game-state row (mod (inc player) 2)))
            (inc won)
            won)
        (inc row))))) 

(defn most-points
  "Which player has the most points?"
  [game-state]
  (let [one (get-won-rows game-state 0)
        two (get-won-rows game-state 1)]
    (cond
      (> one two) 0
      (< one two) 1
      :else 2)))

(defn winner
  "Tells us if there's a winner and if so, who it is"
  [game-state]
  (if (finished? game-state)
    (most-points game-state)
    nil))
