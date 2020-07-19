(ns gorilla-notes.view
  (:require
   [gorilla-notes.state :as state]
   [pinkie.pinkie :refer [tag-inject]]
   [zprint.core :as zprint]
   [cljsjs.highlight]
   [cljsjs.highlight.langs.clojure])
  (:import
   [goog.crypt Md5]))

(defn Code [code]
  [:div {:class "bg-light"}
   [:pre
   [:code {:dangerouslySetInnerHTML
           {:__html (-> code
                        (zprint/zprint 60 {:parse-string? true})
                        with-out-str
                        (->> (js/hljs.highlight "clojure"))
                        (.-value))}}]]])

(defn Note [note]
  [:div
   [:div
    (-> note
        pr-str
        Code)]
   [:div
    (tag-inject note)]])

(defn NoteCard [i note]
  [:div {:class "card"}
   [:div {:class "card-header "}
    (str "#" (inc i))]
   [:div {:class "card-body"}
    [Note note]]])

(defn main []
  [:div {:class "container"}
   (let [{:keys [ids id->content]} @state/*state
         notes                     (->> ids
                                        (map id->content))]
    [:div
     (->> notes
          (map-indexed
           (fn [i note]
             (when note
               [NoteCard i note])))
          reverse
          (into
           [:div
            [:div
             [:small
              [:p "Currently showing " [:big [:big (count notes)]] " notes."]]]]))])])
