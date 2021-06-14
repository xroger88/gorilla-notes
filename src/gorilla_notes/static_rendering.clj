(ns gorilla-notes.static-rendering
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [gorilla-notes.state :as state]))


;; https://stackoverflow.com/a/19297746
(defn copy [uri file]
  (io/make-parents file)
  (with-open [in  (io/input-stream uri)
              out (io/output-stream file)]
    (io/copy in out)))


(defn static-html [state-str]
  (-> "public/gorilla-notes/index.html"
      io/resource
      slurp
      (string/replace "<!-- state-placeholder -->"
                      (format "<script id=\"state\" type=\"text\">%s</script>"
                              (pr-str state-str)))))

(defn render-current-state! [output-path]
  (io/make-parents output-path)
  (-> @state/*state
      (select-keys [:options :ids :id->content])
      (assoc-in [:options :auto-scroll?] false)
      pr-str
      static-html
      (->> (spit output-path)))
  (let [js-target-path (-> output-path
                           io/file
                           (.getParent)
                           str
                           (str "/gorilla-notes/js/compiled/main.js"))]
    (copy (io/resource "public/gorilla-notes/js/compiled/main.js")
          js-target-path)))

