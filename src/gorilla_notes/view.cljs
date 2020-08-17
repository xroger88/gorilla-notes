(ns gorilla-notes.view
  (:require
   [gorilla-notes.state :as state]
   [gorilla-notes.components :as components]
   [pinkie.pinkie :refer [tag-inject]]
   [cljsjs.highlight]
   [cljsjs.highlight.langs.clojure]))

(defn main []
  [:div {:class "container"}
   #_(pr-str @state/*state)
   (let [{:keys [ids id->content options]}      @state/*state
         {:keys [reverse-notes? header? notes-in-cards?]} options
         notes                                  (map id->content ids)]
     (->> notes
          (map-indexed
           (fn [idx note]
             (println (pr-str [:idx idx :note note]))
             (when note
               (if notes-in-cards?
                 [components/NoteCard {:idx  idx
                                :note note}]
                 [components/Note {:note note}]))))
          ((if reverse-notes?
             reverse
             identity))
          (into
           [:div
            (tag-inject
             (when header?
               [components/Header {:notes notes}]))])))])
