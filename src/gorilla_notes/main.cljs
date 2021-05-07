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

(defn main! []
  (mount view/main)
  (if-let [state-str (some-> js/document
                             (.getElementById "state")
                             .-text
                             read-string)]
    ;; State is already specified.
    (do (println "Using predefined state.")
        (reset-state! state-str))
    ;; No state is specified. Start communication for an interactive session.
    (do (println "No predefined state. Starting communication.")
        (communication/start!)))
  (some-> js/document
          (.getElementById "loading")
          (.remove))
  (println "Ready"))

