(ns card-game.effects-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :refer :all]))

(defexpect card-altering
  ; Altering a card in hand works
  (expect
    [15 10 10 10 10 10 10 10 10 10 10 15]
    (map :power
         (-> (new-game)
             (alter-card [:players 0 :hand 0] {:power 15})
             (alter-card [:players 0 :hand 11] {:power 15})
             :players
             (nth 0)
             :hand)))

  ; Playing altered card goes as expected
  (expect
    {:power 15}
    (in
      (-> (new-game)
          (alter-card [:players 0 :hand 0] {:power 15})
          (play-card 0 0 0)
          (play-card 1 1 1)
          :rows (get 0) (get 0))))
  (expect
    [10 10 10 10 10 10 10 10 10 10 10]
    (map :power
         (-> (new-game)
             (alter-card [:players 0 :hand 0] {:power 15})
             (play-card 0 0 0)
             (play-card 1 1 1)
             :players
             (nth 0)
             :hand)))

  ; Altering a card in a row works
  (expect
    {:power 25}
    (in
      (-> (new-game)
          (play-card 0 0 0)
          (play-card 1 1 1)
          (alter-card [:rows 0 0] {:power 25})
          :rows (get 0) (get 0)))))