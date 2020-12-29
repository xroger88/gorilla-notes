(ns gorilla-notes.state
  (:require
    [reagent.core :as reagent]))

(def *state
  (reagent/atom
   {:ids []
    :id->content {}
    :options {}}))

(defn reset-ids! [ids]
  (swap! *state assoc :ids ids))

(defn set-uid! [uid]
  (swap! *state assoc :uid uid))

(defn reset-options! [options]
  (swap! *state assoc :options options))
