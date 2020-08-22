(ns gorilla-notes.components.code
  (:require [zprint.core :as zprint]))

(defn ^{:category :gorilla-notes}
  Code [{:keys [code bg-class zprint]}]
  (let [maybe-zprint (if zprint
                       #(-> %
                            (zprint/zprint (:width zprint) {:parse-string? true})
                            with-out-str)
                       identity)]
    [:div (when bg-class
            {:class bg-class})
     [:pre
      [:code {:dangerouslySetInnerHTML
              {:__html (-> code
                           maybe-zprint
                           (->> (js/hljs.highlight "clojure"))
                           (.-value))}}]]]))

