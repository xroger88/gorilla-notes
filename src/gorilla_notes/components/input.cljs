(ns gorilla-notes.components.input
  (:require [reagent.core :as r]
            [gorilla-notes.communication :as communication]))

(defn slider [symbol {:keys [initial-value min max]}]
  (let [value (r/atom initial-value)
        id    (rand-int 999999)]
    (communication/post-input symbol initial-value)
    (fn []
      ^{:key id}
      [:div
       [:p (name symbol) " = " @value]
       [:input {:type      "range"
                :value     @value
                :min       min
                :max       max
                :style     {:width "100%"}
                :on-change (fn [e]
                             (let [new-value (js/parseInt (.. e -target -value))]
                               (reset! value new-value)
                               (communication/post-input symbol new-value)))}]])))
