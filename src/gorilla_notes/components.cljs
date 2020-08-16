(ns gorilla-notes.components
  (:require [pinkie.pinkie :refer [tag-inject]
             :refer-macros [register-component]]
            [zprint.core :as zprint]))

(defn ^{:category :gorilla-notes}
  Code [{:keys [code width]
         :or   {width 60}}]
  [:div {:class "bg-light"}
   [:pre
    [:code {:dangerouslySetInnerHTML
            {:__html (-> code
                         (zprint/zprint width {:parse-string? true})
                         with-out-str
                         (->> (js/hljs.highlight "clojure"))
                         (.-value))}}]]])

(defn ^{:category :gorilla-notes}
  NoteCard [{:keys [idx note]}]
  [:div {:class "card"}
   [:div {:class "card-header "}
    (str "#" idx)]
   [:div {:class "card-body"}
    [Code {:code (pr-str note)}]
    note]])

(defn ^{:category :gorilla-notes}
  Header [{:keys [notes]}]
  [:small
   [:p "Currently showing " [:big [:big (count notes)]] " notes."]])

(register-component :p/code Code)
(register-component :p/note-card NoteCard)
(register-component :p/header Header)

