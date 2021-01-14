(ns gorilla-notes.static-rendering
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [gorilla-notes.state :as state]))

(defn static-html [state-str]
  (-> "public/index.html"
      io/resource
      slurp
      (string/replace
       "gorilla_notes.main.main_BANG_(true, null)",
       (format "gorilla_notes.main.main_BANG_(false, %s)", (pr-str state-str)))
      (string/replace
       "<script src=\"js/compiled/main.js\"></script>"
       "<script src=\"https://cdn.jsdelivr.net/gh/scicloj/gorilla-notes@master/dist/0.5.0/main.js\"></script>")))

(defn render-current-state! [output-path]
  (->> (select-keys @state/*state [:options :ids :id->content])
       pr-str
       static-html
       (spit output-path)))
