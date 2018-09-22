(ns configs.hands
  (:require [rules.abilities :as ability]))

(def simple-hand
  "Simple hand for autoplay"
  (vec (repeat 42 {:power 10})))

(def default-hand
  "Default hand (can't be empty)"
  [{:power 10}
   {:power 9}
   {:power 8}
   {:power 7}
   {:power 6 :ability (ability/add-power 1)}
   {:power 6 :ability (ability/add-power -1)}
   {:power 4 :ability (ability/add-power 2)}
   {:power 4 :ability (ability/add-power -2)}])

(def default-hands
  "Default hands"
  [default-hand default-hand])
