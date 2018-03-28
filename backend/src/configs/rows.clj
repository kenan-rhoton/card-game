(ns configs.rows)


(def default-limits
  (vec (repeat 5 4))) ; Set to 9000 before frontend can handle limits

(def limitless
  (vec (repeat 5 9000)))
