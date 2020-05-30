(ns gorilla-notes.core
  (:require [gorilla-notes.server :as server]
            [gorilla-notes.communication :as communication]
            [gorilla-notes.state :as state]
            [gorilla-notes.intro :as intro]))

(defn reset-notes! []
  (state/reset-notes!)
  (communication/broadcast-content-ids!))

(defn add-note! [extended-hiccup]
  (state/add-note! extended-hiccup)
  (communication/broadcast-content-ids!))

(defn start-server! []
  (server/start-server!)
  (add-note! intro/note))

