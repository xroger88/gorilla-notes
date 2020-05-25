(ns gorilla-notes.view
  (:require
   [gorilla-notes.state :as state]
   [pinkgorilla.ui.pinkie :refer [tag-inject]]
   [zprint.core :as zprint]
   [clojure.string :as string])
  (:import
   [goog.crypt Md5]))

(defn code->hiccup [code]
  [:pre {:class "prettyprint lang-clj"}
   (-> code
       (zprint/zprint 72 {:parse-string? true})
       with-out-str)])

(defn Note [extended-hiccup]
  [:div
   [:div {:style {:background-color "#f2f0f4"}}
    (-> extended-hiccup
        pr-str
        code->hiccup)]
   [:div
    (tag-inject extended-hiccup)]])

(defn main []
  [:div
   (let [notes (->> @state/*state
                    :content
                    (map :content))]
    [:div
     (->> notes
          (map-indexed
           (fn [i extended-hiccup]
             [:div
              [:h2 "Note #" (inc i)]
              [Note extended-hiccup]]))
          (into [:div
                 [:div
                  [:h1 {:style {:color "#604020"}}
                   [:b [:i "gn"]]]
                  [:small
                   "Currently showing " [:big [:big (count notes)]] " notes."]
                  [:hr]]]))])])
