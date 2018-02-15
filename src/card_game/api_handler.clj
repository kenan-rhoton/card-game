(ns card-game.api-handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

(defroutes handler
  (GET "/" [] "<h1>Hello World!</h1>")
  (route/not-found "<h1>Page not found</h1>"))
