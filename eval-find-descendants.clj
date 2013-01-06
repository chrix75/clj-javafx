(require '[clj-javafx.wrapper :as jfx])

(reset! jfx/_children {})
(jfx/update-children :p1 :p2)
(jfx/update-children :p2 :c1)
(jfx/update-children :p2 :c2)
(jfx/update-children :p2 :c4)
(jfx/update-children :c2 :c3)

(jfx/find-descendants :c4 [])
(jfx/find-descendants :p1 [])

(jfx/descendants :p1)
