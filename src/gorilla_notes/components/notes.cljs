(ns gorilla-notes.components.notes
  (:require [pinkie.pinkie :refer [tag-inject]]
            [gorilla-notes.components.code :as code]))

(defn ^{:category :gorilla-notes}
  NoteCard [{:keys [idx note]}]
  [:div {:class "card"}
   [:div {:class "card-header"}
    (str "#" idx)]
   [:div {:class "card-body"}
    [code/Code {:code     (pr-str note)
           :bg-class "bg-light"
           :zprint   {:width 60}}]
    (tag-inject note)]])

(defn ^{:category :gorilla-notes}
  Note [{:keys [note]}]
  [:div (tag-inject note)])

(defn ^{:category :gorilla-notes}
  Header [{:keys [notes]}]
  [:small
   [:p "Currently showing " [:big [:big (count notes)]] " notes."]])

