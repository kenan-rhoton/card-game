(ns api.handler
  (:gen-class
    :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:require [api.base :as api]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [clojure.java.io :as io]
            [bidi.bidi :as bidi]))


(defn ^:private str->int [number-string]
  (try (Integer/parseInt number-string)
       (catch Exception e nil)))

(comment
(defn ^:private play-action
  [game-id player-id action]
  (api/play-card-as-player
    game-id
    player-id
    (str->int (:index action))
    (str->int (:row action))))

  (def^:private app-routes
    (compojure/routes
      (compojure/POST "/games" [] (api/create-game))
      (compojure/POST "/games/:id{[0-9]+}"
                      [id]
                      (api/add-player (str->int id)))
      (compojure/GET "/games/:id{[0-9]+}/player/:player"
                     [id player]
                     (api/get-game (str->int id) player))
      (compojure/POST "/games/:id{[0-9]+}/player/:player"
                      [id player :as {body :body}]
                      (play-action (str->int id) player body))
      (route/not-found "<h1>Page not found</h1>")))
  )

(def routes
  ["/"
   {"games" :create-game
    ["games/" [#"\d+" :game]] :add-player
    ["games/" [#"\d+" :game] "/player/" :player] :get-game-or-play-action}])

(defn exec-route [match method]
  {:status 200
   :body {}})

(defn handle-event [event]
  (let [result (exec-route
                 (bidi/match-route routes (:path event))
                 (:httpMethod event))]
    {:isBase64Encoded false
     :status (:status result)
     :headers {}
     :body (json/write-str (:body result))}))

(defn -handleRequest [this input output context]
  (let [w (io/writer output)]
    (-> (json/read (io/reader input))
        (handle-event)
        (json/write w))
    (.flush w)))
