(ns clj-javafx.wrapper
  (:import java.lang.Thread
           javafx.clojure.FXApplication 
           [javafx.application Application Platform]
           [javafx.scene Scene Group]))

(defonce primary-stage (promise))
(defonce primary-root (promise))
(defonce primary-scene (promise))
(defonce fxapp (promise))

(defmacro with-javafx 
  "Runs a body inside the JavaFX thread."
  [& body]
  `(let [f# (fn [] ~@body)]
     (Platform/runLater f#)))

(defmacro with-javafx-let 
  "Runs a code inside the JavaFX thread. You can bind variables that are used inside the body."
  [bindings & body]
  `(let ~bindings
     (with-javafx ~@body)))

(defn wait-stopping
  "Checks if the application is stopped."
  []
  (FXApplication/isStopped))

(defn wait-launching
  "Tests if the JavaFX application is launched."
  []
  @primary-scene
  @primary-stage
  @primary-root)

(defn start-fxapplication
  "Starts the JavaFX thread. This function should not be called directly.\n
   Don't forget you cann launch the JavaFX Application more one time."
  []
  (deliver fxapp (future (Application/launch javafx.clojure.FXApplication (into-array String []))))
  (javafx.clojure.FXApplication/getCurrentStage)
)

(defn launch-app
  "Launches a basic JavaFX application. This function lays on the FXApplication to work.\n
   You can't call this function more one time. The second time, nothing will happen."
  ([]
     (start-fxapplication)

     (with-javafx
         (deliver primary-root (Group.))
         (deliver primary-scene (Scene. @primary-root))
         (deliver primary-stage (javafx.clojure.FXApplication/getCurrentStage))
         (.setScene @primary-stage @primary-scene)))

  ([width height]
     (start-fxapplication)

     (let [w (if (nil? width) 0 width)
           h (if (nil? height) 0 height)]

       (with-javafx
         (deliver primary-root (Group.))
         (deliver primary-scene (Scene. @primary-root (double w) (double h)))
         (deliver primary-stage (javafx.clojure.FXApplication/getCurrentStage))
         (.setScene @primary-stage @primary-scene))))
  

  ([width height depth-buffer paint]
     (start-fxapplication)

     (let [db (not (nil? depth-buffer))
           w (if (nil? width) 0 width)
           h (if (nil? height) 0 height)]

       (with-javafx
         (deliver primary-root (Group.))
         (deliver primary-scene (Scene. @primary-root (double w) (double h) (boolean db)))
         (deliver primary-stage (javafx.clojure.FXApplication/getCurrentStage))

         (if-not (nil? paint)
           (.setFill @primary-scene paint))

         (.setScene @primary-stage @primary-scene))))
  )

(defn launch
  "A facility function to launch an JavaFX application. In a call, you can give the original size of your main window (with arguments :width and :height), the depth-buffer value or a default fill color (with :paint)."
  [&  {:keys [width height depth-buffer paint]}]
  (cond 
   (not (nil? depth-buffer)) (launch-app width height depth-buffer paint)
   (not (nil? paint)) (launch-app width height depth-buffer paint)
   (and (nil? width) (nil? height) (nil? depth-buffer) (nil? paint)) (launch-app)
   :else (launch-app width height)))

(defn show
  "Displays on the screen the main window."
  []
  (with-javafx (.show @primary-stage)))


(defn add-child
  "Adds a node to a group."
  [group node]
  (-> (.getChildren group) (.add node)))