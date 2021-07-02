(ns gorilla-notes.defaults
  (:require [gorilla-notes.styles.bootswatch :as bootswatch]
            [gorilla-notes.styles.highlight-js :as highlight-js]))

(def options
  {:reverse-notes?      false
   :header?             false
   :notes-in-cards?     false
   :initially-collapse? false
   :auto-scroll?        false
   :port                1903
   :main-div-class :container-fluid
   :page {:bootswatch-style bootswatch/sandstone
          :highlight-js-theme highlight-js/base16-solarized-light}})

