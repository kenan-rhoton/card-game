(ns api.scores-test
  (:require [expectations.clojure.test :refer :all]
            [api.base :as api]
            [test-helper :as helper]
            [configs.hands :as hands]))

(defexpect rows-won
  (expect
    #(= [3 1] 
        (:scores %))
    (helper/play-as-api
      (fn [i] (mod i 4))
      (fn [i] 0)))
  (expect
    #(= [2 2]
        (:scores %))
    (helper/play-as-api
      (fn [i] (mod i 2))
      (fn [i] (+ (mod i 2) 2)))))

(defexpect power-in-rows
  ; Game begins with scores at 0
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (let [game-state (api/create-game)
          game-id (:game-id game-state)
          player-id (:player-id game-state)
          opponent-id (:player-id (api/add-player game-id))]
      (:rows-power (api/get-game game-id player-id))))
  (expect
    [[(helper/default-hand-power 0) (helper/default-hand-power 1)]
     [0 0] [0 0] [0 0] [0 0]]
    (let [game-state (api/create-game)
          game-id (:game-id game-state)
          player-id (:player-id game-state)
          opponent-id (:player-id (api/add-player game-id))]
      (do
        (api/play-card-as-player game-id player-id 0 0)
        (api/play-card-as-player game-id opponent-id 1 0))
      (:rows-power (api/get-game game-id player-id))))
  (expect
    [[0 (+ (helper/default-hand-power 0) (helper/default-hand-power 1))]
     [(helper/default-hand-power 0) 0]
     [(helper/default-hand-power 1) 0]
     [0 0] [0 0]]
    (let [game-state (api/create-game)
          game-id (:game-id game-state)
          player-id (:player-id game-state)
          opponent-id (:player-id (api/add-player game-id))]
      (do
        (api/play-card-as-player game-id player-id 0 0)
        (api/play-card-as-player game-id opponent-id 0 1)
        (api/play-card-as-player game-id player-id 0 0)
        (api/play-card-as-player game-id opponent-id 0 2)
      (:rows-power (api/get-game game-id opponent-id))))))
