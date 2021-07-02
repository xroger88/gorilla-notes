(ns gorilla-notes.communication
  (:require [ring.middleware.defaults :as defaults]
            [ring.middleware.reload :as reload]
            [ring.middleware.cors :as cors]
            [ring.util.response :as response]
            [environ.core :as environ]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [org.httpkit.server :refer [send! on-close with-channel]]
            #_[chord.http-kit :refer [wrap-websocket-handler]]
            [cheshire.core :as cheshire]
            [clojure.core.async :as async :refer [<! go go-loop timeout chan]]
            [gorilla-notes.state :as state]
            [gorilla-notes.page :as page]))

(def channel-hub
  (atom {}))

(defonce message-counter (atom 0))

(defn broadcast! [event-type data]
  (doseq [channel (keys @channel-hub)]
    (send! channel
           {:status  200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body    (cheshire/generate-string [(swap! message-counter inc)
                                                event-type
                                                data])})))

(defn broadcast-content-ids! []
  (broadcast! :gn/content-ids (pr-str (state/content-ids))))

(defn broadcast-options! []
  (broadcast! :gn/options (pr-str (state/options))))

(defn broadcast-refresh-page! []
  (broadcast! :gn/refresh-page nil))

(comment
  (broadcast! :gn/dummy [1 2 3]))

(defonce last-ws-event-time
  (atom (System/currentTimeMillis)))

(defn ws-handler [req]
  (reset! last-ws-event-time (System/currentTimeMillis))
  (with-channel req ws-channel
    (swap! channel-hub assoc ws-channel req)
    (on-close ws-channel
              (fn [status]
                (swap! channel-hub dissoc ws-channel)))))

(defroutes routes
  (GET "/" req
       (page/page))
  (GET "/status" req (str "Running."))
  (GET "/ws" [] #'ws-handler)
  (GET "/ids" req (-> @state/*state
                      :ids
                      pr-str))
  (GET "/content/:id" [id]
       (-> @state/*state
           :id->content
           (get id)
           pr-str))
  (GET "/options" req (-> @state/*state
                          :options
                          pr-str))
  (POST "/input-update" [symbol value]
        (state/assoc-input! (keyword symbol)
                            (read-string value))
        {:status 200})
  (GET "/gn-files/:local-path" [local-path]
       (println [:local-path local-path])
       (slurp local-path))
  (route/files "/")
  (route/not-found "Not found"))

(def handler
  (-> #'routes
      (cond-> (environ/env :dev?) (reload/wrap-reload))
      (defaults/wrap-defaults (assoc-in defaults/site-defaults [:security :anti-forgery] false))
      (cors/wrap-cors :access-control-allow-origin [#".*"]
                      :access-control-allow-methods [:get :put :post :delete]
                      :access-control-allow-credentials ["true"])))

;; (defn refresh []
;;   (when (-> (System/currentTimeMillis)
;;             (- @last-ws-event-time)
;;             (> 1000))
;;     (broadcast-content-ids!)))

#_(defonce periodically-refresh
  (async/go-loop []
    (async/<! (async/timeout 1000))
    (refresh)
    (recur)))
