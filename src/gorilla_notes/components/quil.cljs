(ns gorilla-notes.components.quil
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [reagent.core :as r]
            [reagent.dom :as dom]
            [sci.core :as sci]
            [cljs.pprint :as pprint]
            [gorilla-notes.components.code :as code]))

;; https://github.com/yogthos/quil-reagent-demo
;; FIXME added by xroger88 to fix the problem of applet running after stop
;; To workaround of that problem, the applet is removed forcely at component-will-unmount
(defn canvas [{:keys [width height
                      init draw update-state]
               :or {width (/ (.-innerWidth js/window) 2)
                    height (/ (.-innerHeight js/window) 2)}}]

  (let [applet (atom nil)]
    (r/create-class
     {:component-did-mount
      (fn [component]
        (let [node (dom/dom-node component)]
          (reset! applet (q/sketch
                          :host node
                          :draw draw
                          :setup (init width height)
                          :update update-state
                          :size [width height]
                          :middleware [m/fun-mode]))))
      :component-will-unmount
      (fn [component]
        ;(js/alert "unmount!")
        (.remove @applet))
      :render
      (fn [] [:div])})))

(defn ^{:category :ui}
  quil
  ([spec]
   (quil spec nil))
  ([spec {:keys [show-spec?]}]
   (r/with-let [running? (r/atom false)]
     [:div
      (when show-spec?
        [code/code-block (with-out-str (pprint/pprint spec))])
      [:div>button
       {:on-click #(swap! running? not)}
       (if @running? "stop" "start")]
      (when @running?
        [canvas (sci/eval-string
                 (pr-str (list 'do
                               '(require (quote q))
                               spec))
                 {:namespaces {'q (ns-publics 'quil.core)}})])])))
