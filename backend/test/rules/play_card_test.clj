(ns rules.play-card-test
  (:require [expectations.clojure.test :refer :all]
            [rules.play-card :as play-card]
            [configs.messages :as messages]))

  (def game-state {:cards [{:power 0 :location [:hand]} {:power 10 :add-power -1} {:power 100 :add-power -100}] :next-play [{:card-id 0 :row-id 0} {:card-id 1 :row-id 3 :target 2}]})

(defexpect move-card

  ; Moves a card to a chosen row
  (expect
    {:my-card "yes" :location [:row 3]}
    (get-in (play-card/move-card {:cards [{}{:my-card "yes"}]} 1 3)
            [:cards 1]))
  
  (expect
    {:location [:row 0]}
    (get-in (play-card/move-card {:cards [{:location "nowhere"}]} 0 0)
            [:cards 0])))

(defexpect apply-add-power

  ; Does nothing when play doesn't have add-power
  (expect
    game-state
    (play-card/apply-add-power game-state {:card-id 0}))
  
  ; Changes power
  (expect
    99
    (get-in (play-card/apply-add-power game-state {:card-id 1 :target 2})
            [:cards 2 :power]))
  
  (expect
    -100
    (get-in (play-card/apply-add-power game-state {:card-id 2 :target 0})
            [:cards 0 :power])))

(defexpect apply-play
  
  ; Moves cards correctly
  (expect
    [:row 1]
    (get-in (play-card/apply-play game-state {:card-id 0 :row-id 1})
            [:cards 0 :location]))
  
  ; Applies abilities correctly
  (expect
    [{:power -100 :location [:hand]} {:power 10 :add-power -1} {:power 100 :add-power -100 :location [:row 1]}]
    (:cards (play-card/apply-play game-state {:card-id 2 :row-id 1 :target 0}))))

(defexpect apply-all-plays
  
  (expect
    {:cards [{:power 0 :location [:row 0]} {:power 10 :add-power -1 :location [:row 3]} {:power 99 :add-power -100}] :next-play [nil nil]}
    (play-card/apply-all-plays game-state)))
