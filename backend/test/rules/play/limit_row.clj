(ns rules.play.limit-row
  (:require [expectations.clojure.test :refer :all]
            [rules.play-card :as play-card]
            [configs.messages :as messages]))

(defexpect row-limit-error
  (expect messages/row-limit
    (play-card/play-card {:next-play [nil nil]
                          :rows [:limit 0]}
                         0 0 0)))
