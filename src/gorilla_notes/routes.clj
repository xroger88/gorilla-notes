(ns gorilla-notes.routes
  (:require [ring.middleware.defaults :as defaults]
            [ring.middleware.reload :as reload]
            [ring.middleware.cors :as cors]
            [ring.util.response :as response]
            [environ.core :as environ]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [taoensso.sente  :as sente]
            [taoensso.sente.server-adapters.http-kit :refer [sente-web-server-adapter get-sch-adapter]]
            [gorilla-notes.state :as state]
            [gorilla-notes.util :refer [uuid]]))

(declare channel-socket)

(defn start-websocket []
  (defonce channel-socket
    (sente/make-channel-socket!
     sente-web-server-adapter
     {:user-id-fn (fn [_] (uuid))})))

(defroutes routes
  (GET "/" req (response/content-type
                (response/resource-response "public/index.html")
                "text/html"))
  (GET "/status" req (str "Running: " (pr-str @(:connected-uids channel-socket))))
  (GET "/chsk" req ((:ajax-get-or-ws-handshake-fn channel-socket) req))
  (POST "/chsk" req ((:ajax-post-fn channel-socket) req))
  (route/resources "/")
  (route/not-found "Not found"))

(def handler
  (-> #'routes
      (cond-> (environ/env :dev?) (reload/wrap-reload))
      (defaults/wrap-defaults (assoc-in defaults/site-defaults [:security :anti-forgery] false))
      (cors/wrap-cors :access-control-allow-origin [#".*"]
                      :access-control-allow-methods [:get :put :post :delete]
                      :access-control-allow-credentials ["true"])))

(defmulti event :id)

(defmethod event :default [{:as ev-msg :keys [event]}]
  (println "Unhandled event: " event))

(defmethod event :chsk/uidport-open [{:keys [uid client-id]}]
  (println "New connection:" uid client-id))

(defmethod event :chsk/uidport-close [{:keys [uid]}]
  (println "Disconnected:" uid))

(defmethod event :chsk/ws-ping [_])

(defn start-router []
  (defonce router
    (sente/start-chsk-router! (:ch-recv channel-socket) event)))

(defn broadcast-content-ids []
  (doseq [uid (:any @(:connected-uids channel-socket))]
    ((:send-fn channel-socket) uid [:chsk/state (state/content-ids)])))
