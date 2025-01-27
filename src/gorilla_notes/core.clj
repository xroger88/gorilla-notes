(ns gorilla-notes.core
  (:require [gorilla-notes.server :as server]
            [gorilla-notes.communication :as communication]
            [gorilla-notes.state :as state]
            [gorilla-notes.intro :as intro]
            [clojure.java.browse :as browse]
            [gorilla-notes.static-rendering :as static-rendering]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [gorilla-notes.styles.bootswatch :as bootswatch]
            [gorilla-notes.styles.highlight-js :as highlight-js]))

(def broadcast-content-ids! communication/broadcast-content-ids!)

(def broadcast-options! communication/broadcast-options!)

(def broadcast-refresh-page! communication/broadcast-refresh-page!)

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
    (communication/broadcast-options!)
    (when (:page new-options)
      ;; page options require page refresh to take place
      (broadcast-refresh-page!))))

(defn toggle-option! [k
                      & {:keys [broadcast?] :or {broadcast? true}}]
  (state/toggle-option! k)
  (when broadcast?
    (communication/broadcast-options!)))

(defn start-server!
  ([]
   (start-server! nil))
  ([options]
   (let [stop-server (server/start-server! options)]
     (add-note! intro/note)
     stop-server)))

(defn base-http-url []
  (server/base-http-url))

(defn browse-http-url []
  (future
    (browse/browse-url (base-http-url))))

(defn inputs []
  (state/inputs))

(defn watch-inputs! [handler]
  (state/watch-inputs! handler))

(defn render-current-state! [output-path]
  (static-rendering/render-current-state! output-path))



(comment

  (def stop-server (start-server!))
  (stop-server)

  (def stop-server (start-server! {:port 1904}))
  (stop-server)

  (browse-http-url)

  (reset-notes!)

  (do
    (reset-notes!)
    (add-note!
     [:div
      [:a {:name "abcd"}]
      [:p "abcd"]])
    (dotimes [i 20]
      (add-note!
       [:p (rand-int 10)]))
    (add-note!
     [:a {:href "#abcd"}
      "to abcd"]))

  (assoc-note!
   0
   [:p/code
    (pr-str
     {:a (range 9)
      :b {:c {:d [3 1 {:e [1 3]}]}}})])

  (broadcast-refresh-page!)

  (assoc-note!
   0
   [:p/frisk
    {:a (range 9)
     :b {:c {:d [3 1 {:e [1 3]}]}}}])

  (assoc-note!
   0
   (let [stylesheet    [{:selector "node"
                         :style    {:width  20
                                    :height 10
                                    :shape  "rectangle"}}
                        {:selector "edge"
                         :style    {:width 5}}]
         layout-random {:name "random"}
         el            [{:data {:id "a" :label "apple"} :position {:x 0 :y 0}}
                        {:data {:id "b" :label "banana"} :position {:x 100 :y 0}}
                        {:data {:id "c" :label "cherry"} :position {:x 200 :y 0}}
                        {:data {:id "ab" :source "a" :target "b"}}]]
     [:p/cytoscape   {:stylesheet stylesheet
                      :elements   el
                      :layout     layout-random
                      :style      {:border "9px solid #39b"
                                   :width  "100px"
                                   :height "100px"}}]))


  (assoc-note!
   0
   [:p/frisk
    {:a (range 9)
     :b {:c {:d [3 1 {:e [1 3]}]}}}])

  (add-note! [:div [:p (rand-int 999)]])

  (assoc-note! 0 [:div [:p (rand-int 999)]])

  (assoc-note!
   0 [:p/quil
      '{:draw         (fn [{:keys [circles]}]
                        (q/background 255)
                        (doseq [{[x y] :pos [r g b] :color} circles]
                          (q/fill r g b)
                          (q/ellipse x y 10 10)))
        :update-state (fn [{:keys [width height] :as state}]
                        (update state :circles conj {:pos   [(+ 20 (rand-int (- width 40)))
                                                             (+ 20 (rand-int (- height 40)))]
                                                     :color (repeatedly 3 #(rand-int 250))}))
        :init         (fn [width height]
                        (fn []
                          {:width   width
                           :height  height
                           :circles []}))}
      #_{:show-spec? true}])

  (add-note!
   [:p/sci '[:div [:h1 (+ 1 2)]]])

  (add-note!
   [:p/markdown "# A hi! $a^2$"])

  (do
    (spit "resources/public/tmp.txt" "hi!")
    (add-note!
     [:div
      [:a {:href "tmp.txt"}
       "hi!"]]))

  (add-note!
   [:p/math "(ax ^2 + bx + c = 0 )"])

  (add-note!
   [:p/vega
    {:description "A simple bar chart with embedded data."
     :data        {:values [{:a "A" :b 28} {:a "B" :b 55} {:a "C" :b 43}
                            {:a "D" :b 91} {:a "E" :b 81} {:a "F" :b 53}
                            {:a "G" :b 19} {:a "H" :b 87} {:a "I" :b 52}]}
     :mark        :bar
     :encoding    {:x {:field :a :type :nominal :axis {:labelAngle 0}}
                   :y {:field :b :type :quantitative}}}])

  (add-note! (into [:div]
                   (repeatedly 9 (fn []
                                   [:p (rand-int 999)]))))

  (add-note! [:p/html "<h1>....</h1>"])

  (def columnDefs [{:headerName "Make" :field "make"}
                   {:headerName "Model" :field "model"}
                   {:headerName "Price" :field "price"}])

  (def 	rowData [{:make "Toyota" :model "Celica" :price 35000}
                 {:make "Ford" :model "Mondeo" :price 32000}
                 {:make "Porsche" :model "Boxter" :price 72000}])

  (add-note!
   [:div {:class "ag-theme-balham"
          :style {:height "150px"
                  :width  "600px"}}
    [:p/dataset {:columnDefs columnDefs
                  :rowData    rowData}]])

  (render-current-state! "/tmp/gn/gn.html")
  (clojure.java.shell/sh "firefox" "file:///tmp/gn/gn.html")

  (require '[gorilla-notes.components.leaflet.providers :as leaflet-providers])

  (add-note!
   [:p/leafletmap
    {:tile-layer leaflet-providers/Stamen-TonerLite}
    [{:type   :view
      :center [51.49, -0.08]
      :zoom   12
      :height 600
      :width  700}
     {:type   :rectangle
      :bounds [[51.49, -0.08]
               [51.5, -0.06]]}
     {:type      :circle
      :center    [51.505, -0.09]
      :fillColor :blue
      :radius    200}
     {:type      :polygon
      :positions [[51.515, -0.09]
                  [51.52, -0.1]
                  [51.52, -0.12]]
      :color     :purple}
     {:type      :polygon
      :positions [[[51.51, -0.12]
                   [51.51, -0.13]
                   [51.53, -0.13]]
                  [[51.51, -0.05]
                   [51.51, -0.07]
                   [51.53, -0.07]]]
      :color     :purple}
     {:type      :line
      :positions [[51.505, -0.09]
                  [51.51, -0.1]
                  [51.51, -0.12]]
      :color     :lime}
     {:type      :line
      :positions [[[51.5, -0.1]
                   [51.5, -0.12]
                   [51.52, -0.12]]
                  [[51.5, -0.05]
                   [51.5, -0.06]
                   [51.52, -0.06]]]
      :color     :lime}
     {:type     :marker
      :position [51.505, -0.09]}
     {:type     :marker
      :position [51.51, -0.12]
      :popup    "wow"}
     {:type      :circlemarker
      :center    [51.52, -0.06]
      :fillColor :blue
      :radius    200
      :popup     "square the circle"}]])

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
   [:p/sparklinespot {:data      (repeatedly
                                  30
                                  (partial rand-int 9))
                      :limit     100
                      :svgWidth  100
                      :svgHeight 20
                      :margin    1}])

  (add-note! [:div [:p/code "{:x (+ 1 2)}"]])

  (add-note! [:div [:p/code "{:x
 (+ 1 2)}"]])

  (add-note! [:div [:p/code "{:x (+ 1 2)}" {}]])

  (add-note! [:div [:p/code {:code   "{:x (+ 1 2)}"
                             :zprint {:width 4}}]])

  (add-note! [:div [:p/code {:code   "def f(x):
  return x+1;"
                             :language :python}]])

  (add-note! [:div [:p/code
                    "def f(x):
  return x+1;"
                    {:language :python}]])

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


  (assoc-note! 0 [:p/code (pr-str '(map inc (range 9)))])

  (require 'gorilla-notes.defaults
           '[gorilla-notes.styles.bootswatch :as bootswatch]
           '[gorilla-notes.styles.highlight-js :as highlight-js])
  (merge-new-options! {:page (:page gorilla-notes.defaults/options)})
  (merge-new-options! {:page {:bootswatch-style bootswatch/darkly
                              :highlight-js-theme highlight-js/rainbow}})
  (merge-new-options! {:page {:bootswatch-style   bootswatch/slate}})
  (broadcast-refresh-page!)


  (merge-new-options! {:main-div-class :container})
  (merge-new-options! {:main-div-class :container-fluid})
  (merge-new-options! {:main-div-class nil})

  (merge-new-options! {:reverse-notes? false
                       :header?        false})
  (toggle-option! :reverse-notes?)
  (toggle-option! :header?)
  (toggle-option! :notes-in-cards?)
  (toggle-option! :initially-collapse?)
  (toggle-option! :auto-scroll?)

  (communication/broadcast-options!)

  (merge-new-options!
   {:custom-header [:div
                    [:big "Hello"]
                    [:hr]]
    :custom-footer [:div
                    [:hr]
                    [:big "Goodbye"]]})

  (render-current-state! "/tmp/a.html"))

