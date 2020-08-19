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

(defn assoc-note! [idx extended-hiccup]
  (let [id (uuid)]
    (swap! *state
           (fn [state]
             (-> state
                 (assoc-in [:id->content id] extended-hiccup)
                 (update :ids assoc idx id))))))

(defn remove-tail! [n]
  (let [n-to-remove (min n (-> @*state :ids count))]
    (swap! *state
           (fn [state]
             (update state :ids #(subvec % n-to-remove))))))

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
