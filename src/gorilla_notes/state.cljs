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

(defn update-content! [i id content]
  (swap! *state
         (fn [state]
           (-> state
               (assoc-in [:id->content id]
                         content)
               (assoc-in [:ids i]
                         id)))))

(defn cleanup-content! []
  (swap! *state
         (fn [state]
           (let [obsolete-ids (->> state
                                   :id->content
                                   keys
                                   (filter (complement (set (:ids state)))))]
             (-> state
                 (update :id->content #(apply dissoc % obsolete-ids)))))))

(defn port []
  (-> @*state :options :port))
