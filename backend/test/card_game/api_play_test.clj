(ns card-game.api-play-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.api.base :as api]
            [configs :as configs]))

(defexpect playing-cards
  (expect
    #(empty? (filter (fn [e] (nil? (:power e))) %))
    (-> (api/create-game)
        :hand))

  ; Cards are removed from hands upon being played
  (expect
    #(= (count (:hand %)) (dec (count (configs/ini-hand))))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (api/play-card-as-player (:game-id game) (:player-id opponent) 0 0)
      (api/play-card-as-player (:game-id game) (:player-id game) 0 0)))

  ; Card is not removed if opponent has not yet played
  (expect
    #(= (count (:hand %)) (count (configs/ini-hand)))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (do
        (api/play-card-as-player (:game-id game) (:player-id game) 0 0)
        (api/get-game (:game-id game) (:player-id game)))))

  ; Fetching the game as a player returns one less card after a play
  (expect
    #(= (count (:hand %)) (dec (count (configs/ini-hand))))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (do
        (api/play-card-as-player (:game-id game) (:player-id opponent) 0 0)
        (api/play-card-as-player (:game-id game) (:player-id game) 0 0)
        (api/get-game (:game-id game) (:player-id game)))))

  ; card played is owned by self
  (expect
    #(= :me (get-in % [:rows 0 0 :owner]))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (do
        (api/play-card-as-player (:game-id game) (:player-id opponent) 1 1)
        (api/play-card-as-player (:game-id game) (:player-id game) 0 0))))

  ; opponent's card is owned by him
  (expect
    #(= :opponent (get-in % [:rows 1 0 :owner]))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (do
        (api/play-card-as-player (:game-id game) (:player-id opponent) 1 1)
        (api/play-card-as-player (:game-id game) (:player-id game) 0 0)))))
