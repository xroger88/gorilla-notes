(ns gorilla-notes.communication
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require [cljs.core.async :as async :refer (<! >! put! chan timeout)]
            [chord.client :refer [ws-ch]]
            [cljs-http.client :as http]
            [cljs.reader :refer [read-string]]
            [gorilla-notes.state :as state]))

(defn ws-url []
  (str "ws://localhost:" (state/port) "/ws"))

(defn handle-content-ids [content-ids-str]
  (let [content-ids (read-string content-ids-str)
        new-id? (complement (set (:ids @state/*state)))
        new-ids-by-idx (->> content-ids
                            (map-indexed
                             (fn [idx id]
                               [idx id]))
                            (filter (comp new-id? second)))]
    (go
      (doseq [[i id] new-ids-by-idx]
        (let [response (<! (http/get (str "/content/" id)))]
          (when (-> response :status (= 200))
            (state/update-content! i id (-> response :body read-string)))))
      (state/reset-ids! content-ids)
      (state/cleanup-content!))))

(defn handle-options [options-str]
  (state/reset-options! (read-string options-str)))

(defn handle [[event-type data]]
  (when event-type
    (case event-type
    "gn/content-ids" (handle-content-ids data)
    "gn/options" (handle-options data))))

(defn post-input [symbol value]
  (http/post "/input-update"
             {:form-params {:symbol (name symbol)
                            :value  (pr-str value)}}))

(defn start! []
  (go (let [response (<! (http/get "/options"))]
        (when (-> response :status (= 200))
          (-> response
              :body
              handle-options)))
      (let [response (<! (http/get "/ids"))]
        (when (-> response :status (= 200))
          (-> response
              :body
              handle-content-ids)))
      (loop []
        (let [{:keys [ws-channel]}    (<! (ws-ch (ws-url)))
              {:keys [message error]} (<! ws-channel)]
          (if error
            (js/console.log "Uh oh:" error)
            (do (js/console.log "Recieved:" (pr-str message))
                (handle message))))
        (recur))))
