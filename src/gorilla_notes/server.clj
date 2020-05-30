(ns gorilla-notes.server
  (:require
    [org.httpkit.server :as server]
    [gorilla-notes.communication :as communication]
    [gorilla-notes.config :as config]))


(defn start-server! [& {:keys [port]
                        :or   {port config/port}}]
  (println "Server starting...")
  (server/run-server #'communication/handler
                     {:port port})
  (println "Ready at port" port "."))

