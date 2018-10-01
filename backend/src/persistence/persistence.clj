(ns persistence.persistence
  (:require [taoensso.faraday :as far]))

(def creds
  {:access-key (or (System/getenv "CARD_GAME_DB_ACCESS_KEY") "")
   :secret-key (or (System/getenv "CARD_GAME_DB_SECRET_KEY") "")
   :endpoint (or (System/getenv "CARD_GAME_DB_ENDPOINT")
                 "http://localhost:8000")})

(defn next-id
  "Returns the next available game id"
  []
  (let [id (or
             (:next (far/get-item creds :info {:subject "game-ids"}))
             0)]
    (far/put-item creds :info {:subject "game-ids" :next (inc id)})
    id))

(defn save-game
  "Saves a game"
  [game]
  (far/put-item creds :games {:id (:game-id game) :data game})
  game)

(defn fetch-game
  [id] (:data (far/get-item creds :games {:id id})))
