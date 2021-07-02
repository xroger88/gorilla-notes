(ns gorilla-notes.styles.bootswatch)

(def styles
  ["cerulean"
   "cosmo"
   "cyborg"
   "darkly"
   "flatly"
   "journal"
   "lumen"
   "paper"
   "readable"
   "sandstone"
   "simplex"
   "slate"
   "spacelab"
   "superhero"
   "united"
   "yeti"])

(doseq [k styles]
  (intern *ns* (symbol (name k)) k))

