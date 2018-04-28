(ns rules.victory-conditions-test
  (:require [expectations.clojure.test :refer :all]
            [rules.victory-conditions :as victory]))

(def game-state {:cards [{:power 1 :location [:row 0] :owner 0}
                         {:power 2 :location [:row 1] :owner 0}
                         {:power 4 :location [:row 0] :owner 0}
                         {:power 8 :location [:row 1] :owner 1}
                         {:power 16 :location [:row 0] :owner 1}
                         {:power 32 :location [:hand] :owner 1}
                         {:power 64 :location [:row 1] :owner 1}
                         {:power 128 :location [:row 2] :owner 0}
                         {:power 128 :location [:row 2] :owner 1}
                         {:power 256 :location [:row 3] :owner 0}]
                 :rows [{}{}{}{}{}]})

(defexpect finished?

  ; Correctly determines finished games
  (expect
    true
    (victory/finished? {:cards []}))
  
  (expect
    true
    (victory/finished? {:cards [{:location [:row 1]}
                                {:location [:row 2]}]}))
  
  (expect
    false
    (victory/finished? {:cards [{:location [:hand]}]}))
  
  (expect
    false
    (victory/finished? game-state)))

(defexpect points-in-row

  ; Correctly counts points
  (expect
    5
    (victory/points-in-row game-state 0 0))
  
  (expect
    2
    (victory/points-in-row game-state 1 0))
  
  (expect
    16
    (victory/points-in-row game-state 0 1))
  
  (expect
    72
    (victory/points-in-row game-state 1 1)))

(defexpect player-wins-row?

  ; Correctly determines row-winner
  (expect
    false
    (victory/player-wins-row? game-state 0 0))
  
  (expect
    true
    (victory/player-wins-row? game-state 0 1))
  
  (expect
    true
    (victory/player-wins-row? game-state 3 0))
  
  ; No winner for ties
  (expect
    false
    (victory/player-wins-row? game-state 2 0))

  (expect
    false
    (victory/player-wins-row? game-state 2 1)))

(defexpect get-won-rows

  ; Correctly gets won rows
  (expect
    1
    (victory/get-won-rows game-state 0))

  (expect
    2
    (victory/get-won-rows game-state 1)))

(defexpect most-rows

  ; Correctly get current-winner
  (expect
    1
    (victory/most-won-rows game-state))
  
  (expect
    0
    (victory/most-won-rows {:cards [{:power 1 :location [:row 0] :owner 0}]
                            :rows [{}]}))
  
  (expect
    2
    (victory/most-won-rows {:cards [{:power 1 :owner 0}]
                            :rows [{}{}]})))

(defexpect winner

  ; nil when game's not finished
  (expect
    nil
    (victory/winner game-state))
  
  ; winner when game's finnished
  (expect
    0
    (victory/winner {:cards [{:power -1 :location [:row 0] :owner 1}{}]
                     :rows [{}]}))
  
  (expect
    1
    (victory/winner {:cards [{:power 1 :location [:row 1] :owner 1}
                             {:power 1 :location [:row 2] :owner 1}
                             {:power 1 :location [:row 0] :owner 0}]
                     :rows [{}{}{}]}))
  
  (expect
    2
    (victory/winner {:cards [{}]})))
