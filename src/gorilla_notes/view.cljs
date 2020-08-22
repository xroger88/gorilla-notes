(ns gorilla-notes.view
  (:require
   [gorilla-notes.state :as state]
   [gorilla-notes.components.notes :as notes]
   [pinkie.pinkie :refer [tag-inject]]
   [cljsjs.highlight]
   [cljsjs.highlight.langs.clojure]))

(defn main []
  [:div {:class "container"}
   (let [{:keys [ids id->content options]}     @state/*state
         {:keys [reverse-notes? header? notes-in-cards?
                 custom-header custom-footer]} options
         notes                                 (map id->content ids)]
     (into [:div]
           (concat [(tag-inject custom-header)
                    (tag-inject (when header?
                                  [notes/Header {:notes notes}]))]
                   (->> notes
                        (map-indexed
                         (fn [idx note]
                           (when note
                             (if notes-in-cards?
                               [notes/NoteCard {:idx  idx
                                                :note note}]
                               [notes/Note {:note note}]))))
                        ((if reverse-notes?
                           reverse
                           identity)))
                   [(tag-inject custom-footer)])))])


