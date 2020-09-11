(ns gorilla-notes.components
  (:require [gorilla-notes.components.code :as code]
            [gorilla-notes.components.input :as input]
            [pinkie.pinkie :refer-macros [register-component]]))

(register-component :p/code code/Code)
(register-component :p/slider input/Slider)
