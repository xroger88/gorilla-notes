(ns gorilla-notes.components.leaflet.providers
  (:require [gorilla-notes.components.leaflet.providers-info :refer [info]]
            [clojure.string :as string]))

(defn hyphened-keyword [& keywords]
  (->> keywords
       (map name)
       (string/join "-")
       keyword))

(def layers-by-name
  (->> info
       (mapcat
        (fn [[layer-name layer-info]]
          (let [layer (merge (select-keys layer-info [:url])
                             (:options layer-info))]
            (->> layer-info
                 :variants
                 (map (fn [[variant-name variant-info]]
                        (println )
                        [(hyphened-keyword layer-name variant-name)
                         (cond (string? variant-info)
                               (assoc layer :variant variant-info)
                               (map? variant-info)
                               (merge layer (:options variant-info)))]))
                 (cons [layer-name layer])))))))

(doseq [[layer-name layer] layers-by-name]
       (eval (list 'def
                   (symbol layer-name)
                   layer)))


