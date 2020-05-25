(ns gorilla-notes.core
  (:require [gorilla-notes.server :as server]
            [gorilla-notes.routes :as routes]
            [gorilla-notes.state :as state]))

(def start-server! server/start-server!)

(defn add-note! [extended-hiccup]
  (state/add-note! extended-hiccup)
  (routes/broadcast-content-ids))
