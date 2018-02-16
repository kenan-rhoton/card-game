(ns card-game.api-test
  (:use expectations
        card-game.api
        card-game.core))

(expect
  #(contains? % :game-id)
  (create-game))

(expect
  #(contains? % :player-id)
  (create-game))

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

(expect
  #(contains? % :error)
  (-> (create-game)
      :game-id
      (add-player)
      :game-id
      (add-player)))
