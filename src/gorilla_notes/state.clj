(ns gorilla-notes.state
  (:require [gorilla-notes.util :refer [uuid]]))

(def *state (atom {:options {:reverse-notes? true
                             :header? true
                             :notes-in-cards? true}
                   :ids []
                   :id->content {}}))

(defn ->timestamp []
  (pr-str (java.util.Date.)))

(defn add-note! [extended-hiccup]
  (let [id (uuid)]
    (swap! *state
     (fn [state]
       (-> state
           (assoc-in [:id->content id] extended-hiccup)
           (update :ids conj id))))))

(defn reset-notes! []
  (swap! *state
         assoc
         :ids []
         :id->content {}))

(defn merge-new-options! [new-options]
  (swap! *state
         update
         :options
         #(merge % new-options)))

(defn toggle-option! [k]
  (swap! *state
         update-in
         [:options k]
         not))

(defn content-ids []
  (:ids @*state))

(defn options []
  (:options @*state))
