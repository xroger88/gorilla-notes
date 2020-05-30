(ns gorilla-notes.util
  (:import java.util.UUID))

(defn uuid []
  (.toString (UUID/randomUUID)))
