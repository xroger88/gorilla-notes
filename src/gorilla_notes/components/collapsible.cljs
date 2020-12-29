(ns gorilla-notes.components.collapsible
  (:require [reagent.core :as reagent]
            [gorilla-notes.state :as state]))

;; Inspired by: https://purelyfunctional.tv/lesson/collapsible-panel/

(defn panel [child]
  (let [s (reagent/atom {:open (-> @state/*state
                                   :options
                                   :initially-collapse?
                                   not)})]
    (fn [& children]
      (let [open? (:open @s)
            child-height (:child-height @s)]
        [:div
         {:on-click #(swap! s update :open not)
          :style    {;:background-color "#eee"
                     :padding          "0 1em"}}
         [:div {:style {:float "right"}}
          [:big (if open? "" "(+)")]]
          [:div {:style {:overflow   "hidden"
                         :transition "max-height 0.8s"
                         :max-height (if open?
                                       child-height
                                       (min child-height 50))
                         :opacity (if open?
                                    1
                                    0.4)}}
           [:div {:ref   #(when %
                            (swap! s assoc :child-height (.-clientHeight %)))
                  :style {;:background-color "#fff"
                          :padding          "0 1em"}}
            child]]]))))
