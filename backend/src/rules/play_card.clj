(ns rules.play-card
  (:require [configs.messages :as messages]
            [rules.alter-card :as alter-card]))

(defn move-card
  "Moves a card to the specified row"
  [game-state card-id row-id]
  (assoc-in game-state [:cards card-id :location] [:row row-id]))

(defn apply-add-power
  "Applies an add-power ability"
  [game-state play]
  (let [add-power (get-in game-state
                          [:cards (:card-id play) :add-power])
        target (:target play)]
    (if (nil? add-power)
        game-state
        (alter-card/add-power game-state target add-power))))

(defn apply-play
  "Applies an entire play"
  [game-state play]
  (-> game-state
      (apply-add-power play)
      (move-card (:card-id play) (:row-id play))))

(defn apply-all-plays
  "Plays all cards waiting to be played"
  [game-state]
    (-> game-state
        (apply-play (get-in game-state [:next-play 0]))
        (apply-play (get-in game-state [:next-play 1]))
        (assoc :next-play [nil nil])))

(defn ^:private count-owned-cards
  "Returns number of cards owned by a player in row-cards"
  [row-cards player-id]
  (reduce #(if (= (:owner %2) player-id)
               (inc %1)
               %1)
          0
          row-cards))

(defn ^:private crowded-row?
  "True if a row has :limit cards for player-id"
  [row-data player-id]
  (<= (:limit row-data 9000)
      (count-owned-cards (:cards row-data) player-id)))

(defn play-card
  "Takes a playing of a card from hand onto a game row and makes it wait until both players had played"
  [game-state player-id index row-id & target]
  ; Uses stored :next-play to know who is supposed to play
  (cond 
    (some? (get-in game-state [:next-play player-id]))
    {:error messages/out-of-turn}

    (crowded-row? (get-in game-state [:rows row-id]) player-id)
    {:error messages/row-limit}

    (every? nil? (:next-play game-state))
    (assoc-in game-state [:next-play player-id] {:player player-id :index index :row row-id :target (first target)})

    :else
    (-> game-state
        (assoc-in [:next-play player-id] {:player player-id :index index :row row-id :target (first target)})
        (apply-all-plays))))
