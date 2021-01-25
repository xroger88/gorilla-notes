(ns gorilla-notes.server
  (:require
    [org.httpkit.server :as server]
    [gorilla-notes.communication :as communication]
    [gorilla-notes.state :as state]))

(defn start-server! [options]
  (when options
    (swap! state/*state assoc :options options))
  (let [port (state/port)]
    (println "Server starting...")
    (let [stop-server (server/run-server #'communication/handler
                                         {:port port})]
      (println "Ready at port" port ".")
      stop-server)))

(defn base-http-url []
  (str "http://localhost:" (state/port)))
