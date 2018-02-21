(ns card-game.api-test
  (:use expectations
        card-game.api))

(expect
  #(contains? % :game-id)
  (create-game))

(expect
  #(contains? % :player-id)
  (create-game))

(expect
  #(= 12 (count (:hand %)))
  (create-game))

(expect
  #(= 5 (count (:rows %)))
  (create-game))

; Both stack all cards on one row -> tie
(expect
  #(= :tie (:winner %))
  (loop [game (create-game)
         opponent (add-player (:game-id game))
         iteration 12]
    (if (= 0 iteration)
      (get-game (:game-id game) (:player-id game))
      (recur
        (play-card-as-player (:game-id game) (:player-id game) 0 0)
        (play-card-as-player (:game-id game) (:player-id opponent) 0 0)
        (dec iteration)))))

; Opponent stacks all cards on one row and I spread -> I win
(expect
  #(= :me (:winner %))
  (loop [game (create-game)
         opponent (add-player (:game-id game))
         iteration 12]
    (if (= 0 iteration)
      (get-game (:game-id game) (:player-id game))
      (recur
        (play-card-as-player (:game-id game) (:player-id game) 0 (mod iteration 4))
        (play-card-as-player (:game-id game) (:player-id opponent) 0 0)
        (dec iteration)))))

; I stack all cards on one row and opponent spreads -> Opponent wins
(expect
  #(= :opponent (:winner %))
  (loop [game (create-game)
         opponent (add-player (:game-id game))
         iteration 12]
    (if (= 0 iteration)
      (get-game (:game-id game) (:player-id game))
      (recur
        (play-card-as-player (:game-id game) (:player-id game) 0 0)
        (play-card-as-player (:game-id game) (:player-id opponent) 0 (mod iteration 4))
        (dec iteration)))))

(expect
  #(empty? (filter (fn [e] (nil? (:power e))) %))
  (-> (create-game)
      :hand))

; Cards are removed from hands upon being played
(expect
  #(= 11 (count (:hand %)))
  (let [game (create-game)
        opponent (add-player (:game-id game))]
    (play-card-as-player (:game-id game) (:player-id opponent) 0 0)
    (play-card-as-player (:game-id game) (:player-id game) 0 0)))

; Card is not removed if opponent has not yet played
(expect
  #(= 12 (count (:hand %)))
  (let [game (create-game)
        opponent (add-player (:game-id game))]
    (do
      (play-card-as-player (:game-id game) (:player-id game) 0 0)
      (get-game (:game-id game) (:player-id game)))))

; Fetching the game as a player returns one less card after a play
(expect
  #(= 11 (count (:hand %)))
  (let [game (create-game)
        opponent (add-player (:game-id game))]
    (do
      (play-card-as-player (:game-id game) (:player-id opponent) 0 0)
      (play-card-as-player (:game-id game) (:player-id game) 0 0)
      (get-game (:game-id game) (:player-id game)))))

; card played is owned by self
(expect
  #(= :me (get-in % [:rows 0 0 :owner]))
  (let [game (create-game)
        opponent (add-player (:game-id game))]
    (do
      (play-card-as-player (:game-id game) (:player-id opponent) 1 1)
      (play-card-as-player (:game-id game) (:player-id game) 0 0))))

; opponent's card is owned by him
(expect
  #(= :opponent (get-in % [:rows 1 0 :owner]))
  (let [game (create-game)
        opponent (add-player (:game-id game))]
    (do
      (play-card-as-player (:game-id game) (:player-id opponent) 1 1)
      (play-card-as-player (:game-id game) (:player-id game) 0 0))))

(expect
  #(contains? % :player-id)
  (-> (create-game)
      :game-id
      (add-player)))

(expect
  #(not (contains? % :error))
  (-> (create-game)
      :game-id
      (add-player)))

(expect
  #(= % (:game-id (add-player %)))
  (-> (create-game)
      :game-id))

; A third player causes an error
(expect
  {:error "Too many players"}
  (-> (create-game)
      :game-id
      (add-player)
      :game-id
      (add-player)))

(expect
  #(not (= (:player-id %) (:player-id (add-player (:game-id %)))))
  (create-game))

; Game is Waiting for Opponent until two players had joined
(expect
  "Waiting for Opponent"
  (:status (create-game)))
(expect
  "Playing"
  (-> (create-game)
      :game-id
      (add-player)
      :status))
(expect
  "Playing"
  (let [game (create-game)]
    (-> (:game-id game)
        (add-player)
        :game-id
        (play-card-as-player (:player-id game) 0 0)
        :status)))
(expect
  "Waiting for Opponent"
  (let [game (create-game)]
       (:status (get-game (:game-id game) (:player-id game)))))
