(ns api.messages-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [api.handler :as handler]
            [api.base :as api]
            [cheshire.core :as json]
            [configs.messages :as messages]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defn request [resource method & params]
  (json/parse-string
    (:body 
      (handler/entry {:request-method method
                :uri resource
                :params (first params)}))
    true))
   

(defexpect no-opp-on-creation
  (let [response (request "/games" :post)
        game-id (:game-id response)
        player-id (:player-id response)]
    (expect
      messages/no-opp
      (:status (request
                 (str "/games/" game-id "/player/" player-id)
                 :get)))
    (expect
      messages/no-opp
      (:status
        (request
          (str "/games/" game-id "/player/" player-id)
          :get)))))

(defexpect play-on-both-players
  (expect
    messages/play
    (let [game (request "/games" :post)]
        (:status (request
                   (str "/games/" (:game-id game))
                   :post)))))

(defexpect wait-on-card-played
  (expect
    messages/wait
    (let [game (request "/games" :post)
          game-id (:game-id game)
          player-id (:player-id game)]
      (do (request (str "/games/" game-id) :post)
          (:status
            (request
              (str "/games/" game-id "/player/" player-id)
              :post
              {:index 0 :rownum 0}))))))

(defexpect play-on-not-played
  (let [game (api/create-game)
        game-id (:game-id game)
        player-id (:player-id game)
        opponent-id (:player-id (api/add-player game-id))]
    (expect
      messages/play
      (do (api/play-card-as-player game-id player-id 0 0)
          (:status
            (api/play-card-as-player game-id opponent-id 0 0))))

    (expect
      messages/play
      (do (api/play-card-as-player game-id opponent-id 0 0)
          (:status
            (api/get-game game-id player-id))))))

(defexpect out-of-turn
  ; Game gives error when playing and shouldn't
  (let [game (api/create-game)
        game-id (:game-id game)
        player-id (:player-id game)]

    (expect
      {:error messages/out-of-turn}
      (api/play-card-as-player game-id player-id 0 0))

    (expect
      {:error messages/out-of-turn}
      (do (api/add-player game-id)
          (api/play-card-as-player game-id player-id 0 0)
          (api/play-card-as-player game-id player-id 0 0)))))


