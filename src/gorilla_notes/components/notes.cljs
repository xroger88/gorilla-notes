(ns gorilla-notes.components.notes
  (:require [pinkie.pinkie :refer [tag-inject]]
            [gorilla-notes.components.code :as code]
            [gorilla-notes.components.collapsible :as collapsible]))

(defn ^{:category :gorilla-notes}
  Note [{:keys [id note]}]
  ^{:key (str "panel#" id)}
  [collapsible/panel
   [:div (tag-inject note)]])

(defn ^{:category :gorilla-notes}
  NoteCard [{:keys [id idx note]
             :as data}]
  [:div {:class "card"}
   [:div {:class "card-header"}
    (str "#" idx)]
   [:div {:class "card-body"}
    [code/code-block
     {:code     (pr-str note)
      :bg-class "bg-light"
      :zprint   {:width 60}}]
    [Note data]]])

(defn ^{:category :gorilla-notes}
  Header [{:keys [notes]}]
  [:small
   [:p "Currently showing " [:big [:big (count notes)]] " notes."]])

