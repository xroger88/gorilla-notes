(ns gorilla-notes.state
  (:require [gorilla-notes.util :refer [uuid]]))

(def *state (atom {}))

(defn ->timestamp []
  (pr-str (java.util.Date.)))

(defn update-content! [f & args]
  (swap! *state
         (fn [state]
           (as-> state s
             (assoc s :timestamp (->timestamp))
             (apply update s :content f args)))))

(defn add-note! [extended-hiccup]
  (update-content! conj {:content   extended-hiccup
                         :timestamp (->timestamp)
                         :id (uuid)}))

(defn content-ids []
  (->> @*state
       :content
       (map :id)))

(comment
  (add-note! [:div [:h1 ".."]]))
