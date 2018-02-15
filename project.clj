(defproject card-game "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.0"]
                 [expectations "2.2.0-rc3" :scope "test"]]
  :plugins [[lein-autoexpect "1.9.0"]
            [lein-expectations "0.0.8"]
            [lein-ring "0.12.1"]]
  :ring {:handler card-game.api-handler/handler}
  :main ^:skip-aot card-game.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
