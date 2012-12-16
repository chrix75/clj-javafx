(ns clj-javafx.application
  (:gen-class
   :name javafx.clojure.FXApplication
   :extends javafx.application.Application
   :methods [#^{:static true} [getCurrentStage [] javafx.stage.Stage]
             #^{:static true} [isStopped [] Boolean]]
   :exposes-methods {stop parentStop}))

(def current-stage (promise))
(def stopped (promise))

(defn -getCurrentStage
  []
  @current-stage)

(defn -isStopped
  []
  @stopped)

(defn -start [this stage]
  (deliver current-stage stage))


(defn -stop [this]
  (.parentStop this)
  (deliver stopped true))