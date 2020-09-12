(ns gorilla-notes.core
  (:require [gorilla-notes.server :as server]
            [gorilla-notes.communication :as communication]
            [gorilla-notes.state :as state]
            [gorilla-notes.intro :as intro]
            [clojure.java.browse :as browse]
            [gorilla-notes.static-rendering :as static-rendering]))

(def broadcast-content-ids! communication/broadcast-content-ids!)

(def broadcast-options! communication/broadcast-options!)

(defn reset-notes! [& {:keys [broadcast?] :or {broadcast? true}}]
  (state/reset-notes!)
  (when broadcast?
    (communication/broadcast-content-ids!)))

(defn add-note! [extended-hiccup
                 & {:keys [broadcast?] :or {broadcast? true}}]
  (state/add-note! extended-hiccup)
  (when broadcast?
    (communication/broadcast-content-ids!)))

(defn assoc-note! [idx extended-hiccup
                   & {:keys [broadcast?] :or {broadcast? true}}]
  (state/assoc-note! idx extended-hiccup)
  (when broadcast?
    (communication/broadcast-content-ids!)))

(defn drop-tail! [n
                  & {:keys [broadcast?] :or {broadcast? true}}]
  (state/drop-tail! n)
  (when broadcast?
    (communication/broadcast-content-ids!)))

(defn merge-new-options! [new-options
                          & {:keys [broadcast?] :or {broadcast? true}}]
  (state/merge-new-options! new-options)
  (when broadcast?
    (communication/broadcast-options!)))

(defn toggle-option! [k
                      & {:keys [broadcast?] :or {broadcast? true}}]
  (state/toggle-option! k)
  (when broadcast?
    (communication/broadcast-options!)))

(defn start-server! []
  (server/start-server!)
  (add-note! intro/note))

(defn default-url []
  (server/default-url))

(defn browse-default-url []
  (future
   (browse/browse-url (default-url))))

(defn inputs []
  (state/inputs))

(defn watch-inputs! [handler]
  (state/watch-inputs! handler))

(defn render-current-state! [output-path]
  (static-rendering/render-current-state! output-path))

(comment
  (start-server!)

  (browse-default-url)

  (reset-notes!)

  (assoc-note! 0 [:div [:p (rand-int 999)]])

  (render-current-state! "/tmp/index.html")
  (browse/browse-url "/tmp/index.html")

  (do
    (reset-notes!
     :broadcast? false)
    (add-note!
     [:p/slider :abcd {:initial-value -3
                    :min           -9
                    :max           9}]
     :broadcast? false)
    (add-note!
     [:h1 "..........."]
     :broadcast? false)
    (add-note!
     [:p/slider :x {:initial-value -1
                    :min           -9
                    :max           9}]
     :broadcast? false)
    (broadcast-content-ids!))

  (watch-inputs!
   (fn [symbol value]
     (println [symbol value])))

  (inputs)

  (add-note!
   [:p/sparklinespot {:data (repeatedly 30 (partial rand-int 9))
                      :limit 100
                      :svgWidth 100
                      :svgHeight 20
                      :margin 1}])

  (add-note! [:div [:p/code {:code "{:x (+
1 2)}"
                             :zprint {:width 4}}]])

  (do (reset-notes! :broadcast? false)
      (dotimes [_ 5]
        (add-note! [:div [:p (rand-int 999)]]
                   :broadcast? false))
      (Thread/sleep 50)
      (broadcast-content-ids!))

  (assoc-note! 1 [:div [:p (rand-int 999)]])

  (drop-tail! 2)

  (do
    (reset-notes! :broadcast? false)
    (dotimes [_ 20]
      (add-note! [:div [:p (rand-int 999)]]
                 :broadcast? false))
    (dotimes [_ 5]
      (drop-tail! 2 :broadcast? false))
    (broadcast-content-ids!))

  (add-note! [:div [:p (rand-int 999)]]
             :broadcast? false)

  (broadcast-content-ids!)

  (merge-new-options! {:reverse-notes? false
                       :header? false})
  (toggle-option! :reverse-notes?)
  (toggle-option! :header?)
  (toggle-option! :notes-in-cards?)

  (merge-new-options!
   {:custom-header [:div
                    [:big "Hello"]
                    [:hr]]
    :custom-footer [:div
                    [:hr]
                    [:big "Goodbye"]]}))
