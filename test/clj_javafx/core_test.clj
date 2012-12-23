(ns clj-javafx.core-test
  (:use clojure.test)
  (:require [clj-javafx.wrapper :as jfx])
  (:import javafx.scene.control.Button javafx.scene.Group))

(deftest ^:integration show-window-with-button
  (jfx/launch :width 500 :height 500)

  (jfx/with-javafx-let
    [btn (Button.)]

    (doto btn
        (.setLayoutX 100)
        (.setLayoutY 150)
        (.setText "Hello World!"))
      
      (jfx/add-child :primary-root ::my-button btn)
    (jfx/show))

  (jfx/wait-stopping))


(deftest test-launching-initialization
  (jfx/launch)
  (is (not (nil? (jfx/component :primary-root))))
  (is (not (nil? (jfx/component :primary-scene))))
  (is (not (nil? (jfx/component :primary-stage)))))

;; In the following test, I use the promise add-done? to mark when the button is really added in the root.
;; It's necessary for the is-tests because like the with-javafx-let is managed by the JavaFX thread in the future, we want to test the add-child result when it's really done.
(deftest test-add-child
  (jfx/launch)
  (let [add-done? (promise)]
    (jfx/with-javafx-let
      [btn (Button.)]
      (doto btn
        (.setLayoutX 100)
        (.setLayoutY 150)
        (.setText "Button"))

      (jfx/add-child :primary-root ::my-button btn)
      (deliver add-done? true))

    (if @add-done?
      (do 
        (is (= 1 (count (.getChildren (jfx/component :primary-root)))))
        (is (= 1 (count (jfx/children :primary-root))))
        (is (= ::my-button (first (jfx/children :primary-root))))
        (is (= "Button" (.getText (first (.getChildren (jfx/component :primary-root))))))
        (is (= "Button" (.getText (jfx/component ::my-button)))))
      
      (throw (Error. "Invalid state")))))


(deftest test-descendants
  (jfx/launch)
  (let [add-done? (promise)]
    (jfx/with-javafx-let
      [p1 (Group.) p2 (Group.) p3 (Group.) c1 (Button.) c2 (Button.) c3 (Button.)]
      (jfx/add-child :primary-root ::p1 p1)
      (jfx/add-child ::p1 ::p2 p2)
      (jfx/add-child ::p2 ::p3 p3)
      (jfx/add-child ::p2 ::c1 c1)
      (jfx/add-child ::p3 ::c2 c2)
      (jfx/add-child ::p2 ::c3 c3)
      (deliver add-done? true))

    (if @add-done?
      (is (= 5 (count (jfx/descendants ::p1)))))))
