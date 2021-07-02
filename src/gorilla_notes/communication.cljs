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

(defn refresh-page []
  (.reload js/location))

(defn handle [[_ event-type data]]
  (println event-type)
  (when event-type
    (case event-type
      "gn/content-ids"  (handle-content-ids data)
      "gn/options"      (handle-options data)
      "gn/refresh-page" (refresh-page))))

(defn post-input [symbol value]
  (http/post "/input-update"
             {:form-params {:symbol (name symbol)
                            :value  (pr-str value)}}))

(defn pass-valid-messages [in out]
  (async/go-loop []
    (let [{:keys [message error]} (<! in)]
      (if error
        (println "Error:" error)
        (async/>! out message)))
    (recur)))

;; https://stackoverflow.com/a/33621605
(defn batch-messages [in out max-time max-count]
  (let [lim-1 (dec max-count)]
    (async/go-loop [buf []
                    t (async/timeout max-time)]
      (let [[v p] (async/alts! [in t])]
        (cond

          (= p t)
          (do
            (async/>! out buf)
            (recur [] (async/timeout max-time)))

          (nil? v)
          (if (seq buf)
            (async/>! out buf))

          (== (count buf) lim-1)
          (do
            (async/>! out (conj buf v))
            (recur [] (async/timeout max-time)))

          :else
          (recur (conj buf v) t))))))

(defn cleanup-messages [in out]
  (async/go-loop []
    (->> in
         async/<!
         (group-by (fn [[_ event-type _]]
                     event-type))
         (map (fn [[_ messages]]
                (->> messages
                     (sort-by (fn [[message-counter _ _]]
                                message-counter))
                     last)))
         (async/>! out))
    (recur)))

(defn handle-messages [in]
  (async/go-loop []
    (let [messages (async/<! in)]
      (run! handle messages))
    (recur)))

(defn start! []
  (go
    (let [response (<! (http/get "/options"))]
      (when (-> response :status (= 200))
        (-> response
            :body
            handle-options)))
    (let [response (<! (http/get "/ids"))]
      (when (-> response :status (= 200))
        (-> response
            :body
            handle-content-ids)))
    (let [{:keys [ws-channel]}   (<! (ws-ch
                                      (ws-url)
                                      {:read-ch (async/chan
                                                 (async/sliding-buffer 100))}))
          messages-channel (async/chan 100)
          batched-messages-channel (async/chan 1)
          clean-messages-channel   (async/chan 1)]
      (pass-valid-messages ws-channel messages-channel)
      (batch-messages messages-channel batched-messages-channel 200 100)
      (cleanup-messages batched-messages-channel clean-messages-channel)
      (handle-messages clean-messages-channel))))
