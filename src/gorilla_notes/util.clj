(ns gorilla-notes.util
  (:import java.util.UUID))

(defn uuid []
  (UUID/randomUUID))
