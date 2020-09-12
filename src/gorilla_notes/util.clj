(ns gorilla-notes.util
  (:import java.util.UUID))

(defonce *current-id (atom 0))

(defn next-id []
  (str (swap! *current-id inc)))


