(ns gorilla-notes.server
  (:require
    [org.httpkit.server :as server]
    [gorilla-notes.routes :as routes]
    [gorilla-notes.config :as config]))

(defn start-server! [& {:keys [port]
                        :or   {port config/port}}]
  (println "Server starting...")
  (routes/start-websocket)
  (routes/start-router)
  (server/run-server #'routes/handler
                     {:port port})
  (println "Ready at port" port "."))

