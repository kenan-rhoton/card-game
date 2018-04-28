(ns rules.create-game-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [configs.hands :as hands]
            [configs.rows :as rows]))
  
(defexpect locate-in-hand

  ; Cards recieve the correct location
  (expect 
    [{:attr 0 :location [:hand] :owner "-#$-"} {:location [:hand] :owner "-#$-"}]
    (create-game/locate-in-hand [{:attr 0} {}] "-#$-"))
  
  (expect
    [{:power -1 :add-power 3 :location [:hand] :owner "fitipaldi"} {:power 9 :location [:hand] :owner "fitipaldi"}]
    (create-game/locate-in-hand [{:power -1 :add-power 3} {:power 9}] "fitipaldi")))

(defexpect new-game
  
  ; Game can be created
  (expect
    #(some? %)
    (create-game/new-game))
  
  ; Game has cards
  (expect
    #(not (empty? %))
    (:cards (create-game/new-game)))
  
  ; Game has rows
  (expect
    #(not (empty? %))
    (:rows (create-game/new-game)))
  
  ; Game uses config
  (expect
    [{:p 0 :location [:hand] :owner "pip"} {:attr 12 :sometext "" :location [:hand] :owner "pop"}]
    (:cards (create-game/new-game {:hands [[{:p 0}][{:attr 12 :sometext ""}]] :player-ids ["pip" "pop"]})))
  
  (expect
     [{:limit 0} {:limit 3}]
    (:rows (create-game/new-game {:limits [0 3]})))
  
  ; Game uses default config
  (expect
    (concat hands/default-hand hands/default-hand)
    (map #(apply dissoc % [:location :owner]) (:cards (create-game/new-game))))
  
  (expect
    rows/default-limits
    (vec (map :limit (:rows (create-game/new-game)))))
  
  (expect
    "p0"
    (get-in (create-game/new-game)
            [:cards 0 :owner]))
  
  (expect
    "p1"
    (get-in (create-game/new-game)
            [:cards (count hands/default-hand) :owner])))
