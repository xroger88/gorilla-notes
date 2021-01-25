(ns gorilla-notes.components.html)

(defn ^{:category :gorilla-notes}
  html
  ([raw-html]
   [:div
    {:dangerouslySetInnerHTML
     {:__html raw-html}}]))

