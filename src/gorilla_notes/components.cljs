(ns gorilla-notes.components
  (:require [gorilla-notes.components.code :as code]
            [pinkie.pinkie :refer-macros [register-component]]))

(register-component :p/code code/Code)
