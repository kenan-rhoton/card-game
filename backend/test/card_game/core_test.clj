(ns card-game.core-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]
            [card-game.victory-conditions :as victory-conditions]
            [configs :as configs]
            [card-game.test-helper :as helper]))

(defexpect basic.game
  ; Game can be created
  (expect
    #(not (nil? %))
    (core/new-game))

  ; Game contains two players
  (expect
    #(count (:players %))
    (core/new-game))

  ; Hand's not empty
  (expect
    false
    (-> (core/new-game)
        (get-in [:players 0 :hand])
        (empty?))))

(defexpect finished-game
  ; We can tell if a game is finished
  (expect
    #(not (victory-conditions/finished? %))
    (core/new-game))

  (expect
    #(victory-conditions/finished? %)
    (helper/end-game (core/new-game))))

(defexpect points-on-rows
  ; We can get the current amount of points on different rows
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (-> (core/new-game)
        (victory-conditions/get-points)))

  (expect
    [[(helper/ini-hand-power 0) 0]
     [0 (helper/ini-hand-power 1)]
     [0 0] [0 0] [0 0]]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        (victory-conditions/get-points)))

  (expect
    [[(+ (helper/ini-hand-power 0) (helper/ini-hand-power 1))
      (helper/ini-hand-power 0)]
     [0 0] [0 0] 
     [0 (helper/ini-hand-power 1)] [0 0]]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 0 0)
        (core/play-card 0 0 0)
        (core/play-card 1 0 3)
        (victory-conditions/get-points))))

; Winner isn't set if game hasn't ended
(expect
  nil
  (-> (core/new-game)
      (victory-conditions/winner)))

(defexpect winning-player
  ; Winner returns the winning player on a finished game, or 2 on a tie
  (expect
    0
    (-> (core/new-game)
        (core/play-card 0 0 3)
        (core/play-card 1 0 2)
        (core/play-card 0 0 0)
        (core/play-card 1 0 2)
        (helper/end-game 2)
        (victory-conditions/winner)))
  (expect
    1
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 0 1)
        (core/play-card 0 0 0)
        (core/play-card 1 0 2)
        (helper/end-game 2)
        (victory-conditions/winner)))

  (expect
    2
    (-> (core/new-game)
        (helper/end-game)
        (victory-conditions/winner))))

(defexpect ot-of-turn
  ; Can't play a card when you were not supposed to
  (expect
    {:error configs/out-of-turn}
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 0 0 0))))

(defexpect next-play
  ; Stores next-play
  (expect
    {:player 0 :index 0 :row 0}
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (get-in [:next-play 0])))
  (expect
    {:player 1 :index 2 :row 3}
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        (core/play-card 1 2 3)
        (get-in [:next-play 1])))
  ; Stores nil when a play is expected
  (expect
    [nil nil]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        :next-play))
  (expect
    [nil nil]
    (-> (core/new-game)
        :next-play))
  (expect
    nil
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (get-in [:next-play 1])))
  (expect
    nil
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        (core/play-card 1 2 3)
        (get-in [:next-play 0]))))


(defexpect update-only-when-both-play
  ; Doesn't updates game-state until both players played a card
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (victory-conditions/get-points))))
