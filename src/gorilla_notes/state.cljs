(ns gorilla-notes.state
  (:require
    [reagent.core :as reagent]))

(def *state
  (reagent/atom
   {:ids []
    :id->content {}}))

(defn reset-ids! [ids]
  (swap! *state assoc :ids ids))

(defn set-uid! [uid]
  (swap! *state assoc :uid uid))

(defn reset-options! [options]
  (swap! *state assoc :options options))

(defn reset-with-options-and-notes! [options notes]
  (let [ids (-> notes count range vec)]
    (reset! *state
            {:ids ids
             :id->content (zipmap ids notes)
             :options options}))
  (println @*state))
