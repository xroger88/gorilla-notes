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
            [clojure.core.async :as a :refer [<! go timeout]]
            [gorilla-notes.state :as state]))

(def channel-hub
  (atom {}))

(defn broadcast! [event-type data]
  (doseq [channel (keys @channel-hub)]
    (send! channel
           {:status  200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body    (cheshire/generate-string [event-type data])})))

(defn broadcast-content-ids! []
  (broadcast! :gn/content-ids (state/content-ids)))

(defn broadcast-options! []
  (broadcast! :gn/options (pr-str (state/options))))

(comment
  (broadcast! :gn/dummy [1 2 3]))

(defn ws-handler [req]
  (with-channel req ws-channel
    (swap! channel-hub assoc ws-channel req)
    (on-close ws-channel
              (fn [status]
                (swap! channel-hub dissoc ws-channel)))))

(defroutes routes
  (GET "/" req
       (response/content-type
        (response/resource-response "public/index.html")
        "text/html"))
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
  (route/resources "/")
  (route/not-found "Not found"))

(def handler
  (-> #'routes
      (cond-> (environ/env :dev?) (reload/wrap-reload))
      (defaults/wrap-defaults (assoc-in defaults/site-defaults [:security :anti-forgery] false))
      (cors/wrap-cors :access-control-allow-origin [#".*"]
                      :access-control-allow-methods [:get :put :post :delete]
                      :access-control-allow-credentials ["true"])))
