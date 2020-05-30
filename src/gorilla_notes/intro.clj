(ns gorilla-notes.intro)

(def note
  [:div
   [:p "Hello! This is a note."]
   [:p "You can add notes at your REPL using the function "
    [:code "add-note!"]
    " of the namespace "
    [:code "gorilla-notes.core"]
    ". For example:"]
   [:p [:code "(require '[gorilla-notes.core :as gn])\n"]]
   [:p [:code "(gn/add-note! [:h1 \"hello\"])"]]
   [:p "You can empty the collection of notes using the function "
    [:code "reset-notes!"]"."]])
