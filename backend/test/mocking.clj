(ns mocking
  (:require [persistence.persistence :as persistence]
            [taoensso.carmine :as car]))

(def mock-game (atom nil))

(defn mock-set
  [pkey data]
  (when (not= pkey "next-game-id")
    (reset! mock-game data)))

(defn mock-get
  [pkey]
  (if (= pkey "next-game-id") 0 @mock-game))

(defn mock-persistence
  [tests]
    (with-redefs
      [persistence/wcar* identity
       car/parse-int identity
       car/set mock-set
       car/get mock-get]
      (tests))
    (reset! mock-game nil))
