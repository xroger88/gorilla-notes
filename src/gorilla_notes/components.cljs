(ns gorilla-notes.components
  (:require [gorilla-notes.components.code :as code]
            [gorilla-notes.components.html :as html]
            [gorilla-notes.components.input :as input]
            [gorilla-notes.components.dataset :as dataset]
            [gorilla-notes.components.leaflet :as leaflet]
            [gorilla-notes.components.math :as math]
            [gorilla-notes.components.sci :as sci]
            [pinkie.pinkie :refer-macros [register-component]]))

(register-component :p/code code/code-block)
(register-component :p/html html/html)
(register-component :p/slider input/slider)
(register-component :p/dataset dataset/dataset-view)
(register-component :p/leafletmap leaflet/leaflet-map)
(register-component :p/math math/math)
(register-component :p/sci sci/sci)
