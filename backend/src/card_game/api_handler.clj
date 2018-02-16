(ns card-game.api-handler
  (:use card-game.api
        compojure.core
        cheshire.core
        ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]))

(defroutes app-routes
  (GET "/" [] (response (create-game)))
  (route/not-found "<h1>Page not found</h1>"))

(def entry
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
