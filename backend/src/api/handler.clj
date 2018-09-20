(ns api.handler
  (:gen-class
    :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:require [api.base :as api]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [bidi.bidi :as bidi]))


(defn ^:private str->int [number-string]
  (try (Integer/parseInt number-string)
       (catch Exception e nil)))

(defn ^:private play-action
  [game-id player-id action]
  (api/play-card-as-player
    game-id
    player-id
    (str->int (:index action))
    (str->int (:row action))))

(def routes
  ["/card-game/"
   {"games" :create-game
    ["games/" [#"\d+" :game]] :add-player
    ["games/" [#"\d+" :game] "/player/" :player] :get-game-or-play-action}])

(defn exec-route [match method]
  (let [handler (:handler match)
        game (str->int (:game (:route-params match)))
        player (:player (:route-params match))]
    (pprint [handler method match])
    (cond
      (= [handler method] [:create-game "POST"])
      {:status 200 :body (api/create-game)}

      (= [handler method] [:add-player "POST"])
      {:status 200 :body (api/add-player game)}

      (= [handler method] [:get-game-or-play-action "POST"])
      {:status 200 :body (play-action game player)}

      (= [handler method] [:get-game-or-play-action "GET"])
      {:status 200 :body (api/get-game game player)}

      :else {:status 404 :body "<h1>404 Not Found</h1>"})))

(defn handle-event [event]
  (let [result (exec-route
                 (bidi/match-route routes (:path event))
                 (:httpMethod event))]
    (pprint result)
    {:isBase64Encoded false
     :statusCode (:status result)
     :headers {}
     :body (json/write-str (:body result))}))

(defn -handleRequest [this input output context]
  (let [w (io/writer output)]
    (-> (json/read (io/reader input) :key-fn keyword)
        (handle-event)
        (json/write w))
    (.flush w)))
