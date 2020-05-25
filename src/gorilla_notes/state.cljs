(ns gorilla-notes.state
  (:require
    [reagent.core :as reagent]))

(def *state
  (reagent/atom
   {:content [{:id      0
               :content [:div
                         [:p "Hello! Please add notes using "
                          [:code "gorilla-notes.core/add-note!"]
                          " at your Clojure REPL."]
                         [:p "For example:"]
                         [:code {:class "prettyprint lang-clj"}
                          "(gorilla-notes.core/add-note! [:h1 \"hello\"])"]]}
              {:id 1
               :content [:div
                         [:p/vega
                          {:$schema     "https://vega.github.io/schema/vega-lite/v4.json"
                           :description "A scatter plot."
                           :data        {:values (for [i (range 5)]
                                                   {:x i
                                                    :y (+ i (* i 9 (rand-int 5)))})}
                           :mark        :point
                           :encoding    {:x {:field :x :type :quantitative}
                                         :y {:field :y :type :quantitative}}}]]}]}))




(defn set-uid! [uid]
  (swap! *state assoc :uid uid))
