(defproject card-game "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [bidi "2.1.4"]
                 [com.taoensso/faraday "1.9.0"]
                 [io.forward/yaml "1.0.7"]
                 [expectations "2.2.0-rc3" :scope "test"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.22.0"]
            [lein-expectations "0.0.8"]
            [lein-cloverage "1.0.13"]
            [jonase/eastwood "0.2.9"]
            [venantius/yagni "0.1.4"]]
  :cloverage {:ns-exclude-regex [#"persistence.*" #"api\.handler"]
              :fail-threshold 95}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
