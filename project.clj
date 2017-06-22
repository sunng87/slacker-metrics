(defproject slacker/slacker-metrics "0.1.0-SNAPSHOT"
  :description "A metrics extension for slacker rpc"
  :url "http://github.com/sunng87/slacker-metrics"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[metrics-clojure "2.9.0"]
                 [slacker "0.15.0-SNAPSHOT"]]
  :profiles {:example {:source-paths ["examples"]}
             :dev {:dependencies [[org.clojure/clojure "1.8.0"]]}}
  :deploy-repositories {"releases" :clojars}
  :aliases {"run-example" ["trampoline" "with-profile" "default,example" "run" "-m" "slacker-metrics.example.main"]})
