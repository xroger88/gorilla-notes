(ns gorilla-notes.state
  (:require [gorilla-notes.util :refer [next-id]]))

(def *state (atom {:options {:reverse-notes? true
                             :header? true
                             :notes-in-cards? true
                             :initially-collapse? false}
                   :ids []
                   :id->content {}
                   :inputs {}
                   :input-handlers []}))

(defn ->timestamp []
  (pr-str (java.util.Date.)))

(defn add-note! [extended-hiccup]
  (let [id (next-id)]
    (swap! *state
     (fn [state]
       (-> state
           (assoc-in [:id->content id] extended-hiccup)
           (update :ids conj id))))))

(defn assoc-note! [idx extended-hiccup]
  (let [id (next-id)]
    (swap! *state
           (fn [state]
             (-> state
                 (update :id->content
                         (fn [id->content]
                           (-> id->content
                               (dissoc (-> state (get-in [:ids idx])))
                               (assoc id extended-hiccup))))
                 (update :ids assoc idx id))))))

(defn drop-tail! [n]
  (let [n-remaining (-> @*state :ids count (- n) (max 0))]
    (println n-remaining)
    (swap! *state
           (fn [state]
             (-> state
                 (update :id->content
                         (fn [id->content]
                           (dissoc id->content
                                   (-> state
                                       :ids
                                       (subvec n-remaining)))))
                 (update :ids #(subvec % 0 n-remaining)))))))

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

(defn watch-inputs! [handler]
  (swap! *state update :input-handlers #(conj % handler)))

(defn assoc-input! [symbol value]
  (swap! *state assoc-in [:inputs symbol] value)
  (doseq [handler (:input-handlers @*state)]
    (handler symbol value)))

(defn inputs []
  (:inputs @*state))
