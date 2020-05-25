(ns example-session
  (:require [gorilla-notes.core :refer [start-server! add-note!]]
            [gorilla-notes.routes :as routes]))

(defonce server (start-server!))

(add-note!
 [:div [:h1 "hello"]])

(add-note!
 [:div
  [:p/vega
   {:$schema     "https://vega.github.io/schema/vega-lite/v4.json"
    :description "A scatter plot."
    :data        {:values (for [i (range 99)]
                            {:x i
                             :y (+ i (* i 9 (rand)))})}
    :mark        :point
    :encoding    {:x {:field :x :type :quantitative}
                  :y {:field :y :type :quantitative}}}]])
