(ns slacker-metrics.core
  (:require [metrics.timers :as timers]
            [metrics.meters :as meters]))

(defn metric-interceptor [mreg & {:keys [timer-name-fn
                                         error-meter-name-fn]}]
  (let [timer-name-fn (or timer-name-fn (fn [fname] (str fname ":timer")))
        error-meter-name-fn (or error-meter-name-fn (fn [fname] (str fname ":error")))]
    {:before (fn [req]
               (let [timer-name (timer-name-fn (:fname req))
                     timer (timers/timer mreg timer-name)
                     timer-ctx (timers/start timer)]
                 (assoc req ::timer timer-ctx)))
     :after (fn [req]
              (when-let [timer-ctx (::timer req)]
                (timers/stop timer-ctx))
              (when (= :exception (:code req))
                (meters/mark! (meters/meter mreg (error-meter-name-fn (:fname req)))))
              (dissoc req ::timer))}))
