(ns gorilla-notes.page
  (:require [clojure.java.io :as io]
            [selmer.parser]
            [gorilla-notes.state :as state]))

(defn page []
  (-> "public/gorilla-notes/index.html"
      io/resource
      slurp
      (selmer.parser/render (state/page-options))))

