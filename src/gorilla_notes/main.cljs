(ns gorilla-notes.main
  (:require [reagent.dom :as rdom]
            [cljs.reader :refer [read-string]]
            [pinkgorilla.ui.default-renderer]
            [gorilla-notes.view :as view]
            [gorilla-notes.state :as state]
            [gorilla-notes.communication :as communication]))

(enable-console-print!)

(defn mount [app]
  (rdom/render [app]
               (.getElementById js/document "app")))

(defn reload! []
  (mount view/main)
  (print "Hello reload!"))

(defn reset-state! [state-str]
  (reset! state/*state (read-string state-str)))

(defn main!
  ([communication? state-str]
   (mount view/main)
   (when communication?
     (communication/start!))
   (when state-str
     (reset-state! state-str))
   (println "Ready")))

(main! true nil)
