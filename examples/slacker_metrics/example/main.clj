(ns slacker-metrics.example.main
  (:require [slacker.server :as ss]
            [slacker.client :as sc]
            [slacker-metrics.core :as sm]
            [metrics.core :as mc]
            [metrics.timers :as timers]
            [metrics.meters :as meters]))

(def client (sc/slackerc "127.0.0.1:2334"))

(sc/defn-remote client f1
  :remote-ns "slacker-metrics.example.api")

(sc/defn-remote client f2
  :remote-ns "slacker-metrics.example.api")

(defn -main [& args]
  (let [reg (mc/new-registry)
        fns {"slacker-metrics.example.api" {"f1" (fn [] "this is f1")
                                            "f2" (fn [] (throw (Exception. "this is f2")))}}
        server (ss/start-slacker-server fns 2334
                                        :interceptors (sm/metric-interceptor reg))]

    (dotimes [_ (+ (rand-int 100) 50)]
      (f1))
    (dotimes [_ (+ (rand-int 100) 50)]
      (try (f2) (catch Exception _)))

    (sc/shutdown-slacker-client-factory)
    (ss/stop-slacker-server server)

    (doseq [[name timer] (mc/timers reg)]
      (println name (timers/rate-one timer)))
    (doseq [[name meter] (mc/meters reg)]
      (println name (meters/rate-one meter)))

    (shutdown-agents)))
