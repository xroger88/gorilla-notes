(ns gorilla-notes.components
  (:require [gorilla-notes.components.code :as code]
            [gorilla-notes.components.input :as input]
            [gorilla-notes.components.dataset :as dataset]
            [gorilla-notes.components.leaflet :as leaflet]
            [pinkie.pinkie :refer-macros [register-component]]))

(register-component :p/code code/code-block)
(register-component :p/slider input/slider)
(register-component :p/dataset dataset/dataset-view)
(register-component :p/leaflet leaflet/leaflet-map)
