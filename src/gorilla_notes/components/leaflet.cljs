(ns gorilla-notes.components.leaflet
  (:require
   ["react-leaflet" :refer [Map TileLayer Popup Marker CircleMarker Circle Rectangle Polygon Polyline GeoJSON]]
   ["leaflet" :refer [Icon]]
   [pinkgorilla.ui.config :refer [link-css res-href]]
   [pinkgorilla.dsl.leaflet]))


;; This is an adaptation of Gorilla-UI's leafelt component.
;; https://github.com/pink-gorilla/gorilla-ui/blob/e3a354a70a679221e5d8b2e1ac8f4fd71679d4b0/src/pinkgorilla/ui/data/leaflet.cljs

(def default-options
  {:width  600
   :height 400
   :zoom       10
   :center     [8.5407166 -79.8833319] ; panama, but better to calculate center automatically
   :color      "steelblue"
   :weight     5.0
   :opacity    1.0
   :dash-array "1, 0"
   :line-cap   "butt"
   :line-join  "miter"
   ;; See many options here: https://leaflet-extras.github.io/leaflet-providers/preview/
   :tile-layer {:url "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                :attribution "&copy; <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"}})

(defn- log [s] (.log js/console s))

(defn- feature [data-with-type]
  (let [type (:type data-with-type)
        data (dissoc data-with-type :type)]
    (case type
      :rectangle ^:r [:> Rectangle data] ;  <Rectangle bounds={rectangle} color="black" />
      :circle ^:r [:> Circle data] ;  <Circle center={center} fillColor="blue" radius={200} />
      :line ^:r [:> Polyline data]   ; <Polyline color="lime" positions={polyline} />
      :polygon ^:r [:> Polygon data]  ; <Polygon color="purple" positions={polygon} />
      :marker ^:r [:> Marker data        ;  <Marker position= {position} >  <Marker position= {position} icon= {pointerIcon} >
                   (when (not (nil? (:popup data)))
                     ^:r [:> Popup (:popup data)])]  ; <Popup>A pretty CSS3 popup.<br />Easily customizable.</Popup>
      :circlemarker ^:r [:> CircleMarker data   ;  <CircleMarker center= {[51.51, -0.12]} color= "red" radius= {20} >
                         (when (not (nil? (:popup data)))
                           ^:r [:> Popup (:popup data)])]  ;<Popup>Popup in CircleMarker</Popup>
      :geojson ^:r [:> GeoJSON data] ;<GeoJSON  data={london_postcodes} style={this.geoJSONStyle} onEachFeature={this.onEachFeature}
      (do (log (str "No feature found for: " type))
          nil))))


; Map props:
; bounds: bounds (optional): A rectangle for the map to contain. It will be centered, and the map will zoom in as close as it can while still showing the full bounds. Changes are compared using the 🍃 equals() method of LatLngBounds.
; center: latLng (optional if viewport is provided with a center value): Center of the map. Changes are compared by value, so [51.0, 0.0] is considered the same as {lat: 51, lng: 0}.
; className: string (optional): className property of the <div> container for the map.
;onViewportChange: (viewport: {center: ?[number, number], zoom: ?number}) => void (optional): fired continuously as the viewport changes.
;onViewportChanged: (viewport: {center: ?[number, number], zoom: ?number}) => void (optional): fired after the viewport changed.
; style: Object (optional): style property of the <div> container for the map.
; id: string (optional): The ID of the <div> container for the map.


(defn view? [feature]
  (= :view (:type feature)))

(defn assoc-if-exists [m key val]
  (if (nil? val)
    m
    (assoc m key val)))

(defn view-map-props [view]
  (let [{:keys [width height zoom center useFlyTo]} view
        style (when (or width height)
                (-> {}
                    (assoc-if-exists :width width)
                    (assoc-if-exists :height height)))]
    (-> {}
        (assoc-if-exists :style style)
        (assoc-if-exists :zoom zoom)
        (assoc-if-exists :center center)
        (assoc-if-exists :useFlyTo useFlyTo))))

(defn ^{:category :data}
  leaflet-map
  "displays a map with leaflet.
     example:
    [:p/leaflet
     [{:type :view :center [51.49, -0.08] :zoom 12 :height 600 :width 700}
    {:type :rectangle :bounds rectangle}
      {:type :circle :center center :fillColor :blue :radius 200}
    {:type :polygon :positions polygon :color :purple}
    {:type :polygon :positions multiPolygon :color :purple}
    {:type :line :positions polyline :color :lime}
    {:type :line :positions multi-polyline :color :lime}
    {:type :marker :position [51.505, -0.09]}
    {:type :marker :position [51.51, -0.12] :popup \"wow\"}
    {:type :circlemarker :center [51.52, -0.06] :fillColor :blue :radius 200 :popup \"square the circle\"}
    {:type :geojson :data geojson}]]"

  ([options features-incl-view]
   (let [{:keys [width height zoom center
                 tile-layer]} options
         view (first (filter view? features-incl-view))
         features (remove view? features-incl-view)
         view-map (view-map-props view)]
     [:div.z-10
      [:> Map (merge  {:zoom zoom
                       :center center
                       :style {:width width :height height}
                       :keyboard true ; navigate map with arrows and +-
                       :class "z-10"}
                      view-map)
       [:> TileLayer tile-layer]
       (into  [:<>]
              (map feature features))]]))
  ([features]
   (leaflet-map default-options features)))

