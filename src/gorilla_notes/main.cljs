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

(defn reset-content! [content-str]
  (let [{:keys [options notes]} (read-string content-str)]
    (state/reset-with-options-and-notes! options notes)))

(defn main!
  ([communication? content-str]
   (mount view/main)
   (when communication?
     (communication/start!))
   (when content-str
     (reset-content! content-str))
   (println "Ready")))
