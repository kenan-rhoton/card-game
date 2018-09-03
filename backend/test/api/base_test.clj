(ns api.base-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [api.base :as base]
            [configs.hands :as hands]
            [configs.messages :as messages]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defexpect game-creation
  (expect 0 (:game-id (base/create-game)))
  (expect messages/no-opp (:status (base/create-game)))
  (expect nil (:player-ids (base/create-game)))
  (expect true (contains? (base/create-game) :player-id)))

(defexpect join-game

  (let [joined-game (-> (base/create-game) :game-id base/add-player)]
    (expect 0 (:game-id joined-game))
    (expect (count hands/default-hand)
            (count (filter
                     #(= "me" (:owner %))
                     (:cards joined-game))))
    (expect (count hands/default-hand)
            (count (filter
                     #(= "opp" (:owner %))
                     (:cards joined-game))))))

(defexpect join-crowded
  (expect {:error messages/too-many-players}
          (-> (base/create-game)
              :game-id
              base/add-player
              :game-id
              base/add-player)))

(defexpect join-empty
  (expect {:error messages/lobby-not-created}
              (base/add-player 0)))

(defexpect play-card

  (let [game (base/create-game)
        game-id (:game-id game)
        p1 (:player-id game)
        p2 (:player-id (base/add-player game-id))]
    
    (expect {:error messages/no-row}
            (base/play-card-as-player game-id p1 1 99))
    
    (expect messages/wait
            (:status (base/play-card-as-player game-id p1 1 0)))

    (expect {:error messages/out-of-turn}
            (base/play-card-as-player game-id p1 1 0))))
