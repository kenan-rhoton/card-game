(ns test-helper
  (:require [configs.hands :as hands]))

(defn ini-hand-power
  [x]
  (get-in hands/default-hand [x :power]))
