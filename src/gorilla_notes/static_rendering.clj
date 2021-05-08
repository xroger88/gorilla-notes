(ns gorilla-notes.static-rendering
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [gorilla-notes.state :as state]))

(defn static-html [state-str]
  (-> "public/index.html"
      io/resource
      slurp
      (string/replace "<!-- state-placeholder -->"
                      (format "<script id=\"state\" type=\"text\">%s</script>"
                              (pr-str state-str)))
      (string/replace
       "<script src=\"js/compiled/main.js\"></script>"
       "<script src=\"https://cdn.statically.io/gh/scicloj/gorilla-notes@master/dist/0.5.12/main.js\"></script>")))

(defn render-current-state! [output-path]
  (-> @state/*state
      (select-keys [:options :ids :id->content])
      (assoc-in [:options :auto-scroll?] false)
      pr-str
      static-html
      (->> (spit output-path))))
