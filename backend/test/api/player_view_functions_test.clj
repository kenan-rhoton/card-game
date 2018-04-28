(ns api.player-view-functions-test
  (:require [expectations.clojure.test :refer :all]
            [api.player-view-functions :as functions]
            [configs.messages :as messages]))

(defexpect get-cards
  
  ; Gives cards as seen by a player
  (expect
    [{:location [:hand] :owner "opp"}
     {:location [:row 0] :owner "opp"}
     {:power 1 :attr "kill" :location [:hand] :owner "me"}
     {:power 100 :location [:row 1] :owner "me"}]
    (functions/get-cards {:cards [{:power 99 :location [:hand] :owner "opp_name"}
                                  {:power -1 :location [:row 0] :owner "opp_name" :secret-attr "who knows?"}
                                  {:power 1 :attr "kill" :location [:hand] :owner "me_name"}
                                  {:power 100 :location [:row 1] :owner "me_name"}]}
                        "me_name")))

(defexpect get-rows

  ; Gives rows with scores
  (expect
    [{:limit 1 :scores [0 0]}
     {:limit 4 :scores [1 2]}
     {:scores [12 0]}]
    (functions/get-rows {:cards [{:power 1 :location [:row 1] :owner "I"}
                                 {:power 2 :location [:row 1] :owner "U"}
                                 {:power 4 :location [:row 2] :owner "I"}
                                 {:power 8 :location [:row 2] :owner "I"}]
                         :rows [{:limit 1}{:limit 4}{}]
                         :player-ids ["I" "U"]}
                        "I")))

(defexpect get-scores

  ; Gives global scores
  (expect
    [2 1]
    (functions/get-scores {:cards [{:power 10 :location [:row 0] :owner "jo"}
                                   {:power 1 :location [:row 0] :owner "tu"}
                                   {:power 3 :location [:row 1] :owner "jo"}
                                   {:power 4 :location [:row 1] :owner "jo"}
                                   {:power 5 :location [:row 1] :owner "tu"}
                                   {:power 9 :location [:row 2] :owner "tu"}]
                           :rows [{}{}{}{}]
                           :player-ids ["jo" "tu"]}
                          "jo")))

(defexpect get-winner

  ; No winner
  (expect
    nil
    (functions/get-winner {:cards [{:location [:hand]}]
                           :player-ids ["john" "jihn"]} "john"))

  ; Tie
  (expect
    ""
    (functions/get-winner {:cards [{}] :rows[{}{}]
                           :player-ids ["jihn" "john"]} "john"))

  ; Gets winner
  (expect
    "me"
    (functions/get-winner {:cards [{:power 1 :location [:row 1] :owner "winner"}]
                           :rows [{}{}]
                           :player-ids ["winner" "someone"]}
                          "winner"))
  (expect
    "opp"
    (functions/get-winner {:cards [{:power 1 :location [:row 1] :owner "winner"}]
                           :rows [{}{}]
                           :player-ids ["winner" "someone"]}
                          "someone")))
