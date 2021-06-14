(ns gorilla-notes.components.markdown
  (:require ["react-markdown" :as ReactMarkdown]
            ["remark-gfm" :as gfm]
            ["remark-math" :as math]
            ["react-katex" :refer [InlineMath BlockMath]]
            ["react" :as React]))

(def renderers
  #js {:inlineMath (fn [v]
                     (React/createElement InlineMath #js {:math (.-value v)}))
       :math       (fn [{:keys [value]}]
                     (React/createElement BlockMath #js {:math value}))})

(defn ^{:category :ui}
  markdown
  "reagent markdown render component
   usage:
    [markdown markdown-string]"
  [md]
  [:div
   [:> ReactMarkdown
    {:plugins #js [math gfm]
     :renderers renderers
     :children md}]])
