(ns example-session
  (:require [gorilla-notes.core :as gn]))

(defonce server (gn/start-server!))

(gn/add-note!
 [:div [:p "hello"]])

(gn/reset-notes!)

(gn/add-note!
 gorilla-notes.intro/note)

(gn/add-note!
 [:p/vega
  {:$schema     "https://vega.github.io/schema/vega-lite/v4.json"
   :description "A scatter plot."
   :data        {:values (for [i (range 4)]
                           {:x i
                            :y (+ i (* i 4 (rand-int 5)))})}
   :mark        :point
   :encoding    {:x {:field :x :type :quantitative}
                 :y {:field :y :type :quantitative}}}])


