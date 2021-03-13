(ns gorilla-notes.components.sci
  (:require
   [pinkie.jsrender :refer [render-js]]
   [sci.core :as sci]))

(defn ^{:category :ui}
  sci
  "interpret code"
  [code]
  (sci/eval-string (pr-str code)))
