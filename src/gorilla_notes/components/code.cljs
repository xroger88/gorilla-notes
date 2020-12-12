(ns gorilla-notes.components.code
  (:require [zprint.core :as zprint]))

(defn ^{:category :gorilla-notes}
  code-block
  ([arg]
   ;; handle a single argument by its type
   (cond
     ;; arg is the code
     (string? arg)
     (code-block {:code arg})
     ;; arg is the options map
     (map? arg)
     (code-block nil arg)))
  ([content {:keys [code bg-class zprint language]
             :or {language :clojure}}]
   (let [maybe-zprint (if zprint
                        #(-> %
                             (zprint/zprint (:width zprint) {:parse-string? true})
                             with-out-str)
                        identity)]
     [:div (when bg-class
             {:class bg-class})
      [:pre
       [:code {:dangerouslySetInnerHTML
               {:__html (-> content
                            (or code)
                            maybe-zprint
                            (->> (js/hljs.highlight (name language)))
                            (.-value))}}]]])))

