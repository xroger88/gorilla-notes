(ns gorilla-notes.main
  (:require [reagent.dom :as rdom]
            [pinkgorilla.ui.default-renderer]
            [gorilla-notes.view :as view]
            [gorilla-notes.state]
            [gorilla-notes.websockets]))

(enable-console-print!)

(defn mount [app]
  (rdom/render [app]
               (.getElementById js/document "app")))

(defn reload! []
  (mount view/main)
  (print "Hello reload!"))

(defn main! []
  (mount view/main)
  (print "Hello Main"))


