(ns gorilla-notes.components.math
  (:require
   [pinkie.jsrender :refer [render-js]]))

(defn render-math [dom-node data-js]
  (let [katex (.-katex js/window)]
    (.render katex
             data-js
             dom-node
             #js {:throwOnError false})))

(defn ^{:category :ui}
  math
  "displays mathematical formulae"
  [data-clj]
  [render-js {:f render-math :data data-clj}])

