(ns gorilla-notes.core
  (:require [gorilla-notes.server :as server]
            [gorilla-notes.communication :as communication]
            [gorilla-notes.state :as state]
            [gorilla-notes.intro :as intro]
            [clojure.java.browse :as browse]))

(defn reset-notes! []
  (state/reset-notes!)
  (communication/broadcast-content-ids!))

(defn add-note! [extended-hiccup]
  (state/add-note! extended-hiccup)
  (communication/broadcast-content-ids!))

(defn assoc-note! [idx extended-hiccup]
  (state/assoc-note! idx extended-hiccup)
  (communication/broadcast-content-ids!))

(defn merge-new-options! [new-options]
  (state/merge-new-options! new-options)
  (communication/broadcast-options!))

(defn toggle-option! [k]
  (state/toggle-option! k)
  (communication/broadcast-options!))

(defn start-server! []
  (server/start-server!)
  (add-note! intro/note))

(defn default-url []
  (server/default-url))

(defn browse-default-url []
  (future
   (browse/browse-url (default-url))))

(comment
  (start-server!)

  (browse-default-url)

  (reset-notes!)

  (add-note! [:div [:p (rand-int 999)]])
  (add-note! [:div [:p (rand-int 999)]])
  (add-note! [:div [:p (rand-int 999)]])

  (assoc-note! 1 [:div [:p (rand-int 999)]])

  (merge-new-options! {:reverse-notes? false
                       :header? false})
  (toggle-option! :reverse-notes?)
  (toggle-option! :header?)
  (toggle-option! :notes-in-cards?))

