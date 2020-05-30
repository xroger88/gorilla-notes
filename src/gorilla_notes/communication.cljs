(ns gorilla-notes.communication
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require [cljs.core.async :as async :refer (<! >! put! chan timeout)]
            [chord.client :refer [ws-ch]]
            [cljs-http.client :as http]
            [cljs.reader :refer [read-string]]
            [gorilla-notes.config :as config]
            [gorilla-notes.state :as state]))

(defn base-http-url []
  (str "http://localhost:" config/port))

(defn ws-url []
  (str "ws://localhost:" config/port "/ws"))

(defn handle-content-ids [content-ids]
  (let [new-ids (->> content-ids
                     (filter (complement (set (:ids @state/*state)))))]
    (doseq [id new-ids]
      (go (let [response (<! (http/get (str (base-http-url) "/content/" id)))]
            (when (-> response :status (= 200))
              (swap! state/*state
                     assoc-in [:id->content id]
                     (-> response :body read-string))))))
    (state/reset-ids! content-ids)))

(defn handle [[event-type data]]
  (case event-type
    "gn/content-ids" (handle-content-ids data)))

(go-loop []
  (let [{:keys [ws-channel]}    (<! (ws-ch (ws-url)))
        {:keys [message error]} (<! ws-channel)]
    (if error
      (js/console.log "Uh oh:" error)
      (do (js/console.log "Recieved:" (pr-str message))
          (handle message))))
  (recur))

(go (let [response (<! (http/get (str (base-http-url) "/ids")))]
      (when (-> response :status (= 200))
        (-> response
            :body
            read-string
            handle-content-ids))))
