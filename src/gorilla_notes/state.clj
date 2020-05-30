(ns gorilla-notes.state
  (:require [gorilla-notes.util :refer [uuid]]))

(def *state (atom {:ids []
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

(defn content-ids []
  (:ids @*state))

(comment
  (add-note! [:div [:h1 ".."]]))


