(ns gorilla-notes.styles.highlight-js
  (:require [clojure.string :as string]))


(def styles
  ;; https://github.com/highlightjs/highlight.js/tree/main/src/styles
  ["nord" "pojoaque" "github-dark-dimmed" "nnfx-light" "androidstudio" "nnfx-dark" "gradient-light"
   "googlecode" "hybrid" "sunburst" "kimbie-light" "lightfair" "lioshi" "color-brewer" "tomorrow-night-bright"
   "night-owl" "dark" "github-dark" "ir-black" "idea" "atom-one-dark" "far" "rainbow" "a11y-dark" "routeros"
   "mono-blue" "brown-papersq.png" "isbl-editor-dark" "obsidian" "school-book" "grayscale" "shades-of-purple"
   "vs2015" "stackoverflow-dark" "github" "codepen-embed" "isbl-editor-light" "base16/atelier-forest-light"
   "base16/embers" "base16/brewer" "base16/nord" "base16/papercolor-light" "base16/material"
   "base16/windows-high-contrast-light" "base16/edge-light" "base16/vulcan" "base16/material-palenight"
   "base16/cupertino" "base16/ros-pine-moon" "base16/windows-95-light" "base16/atelier-dune-light"
   "base16/snazzy" "base16/darcula" "base16/tomorrow" "base16/atelier-cave" "base16/fruit-soda"
   "base16/horizon-dark" "base16/black-metal-bathory" "base16/google-light" "base16/3024"
   "base16/black-metal-burzum" "base16/hardcore" "base16/eva" "base16/black-metal-nile"
   "base16/gigavolt" "base16/grayscale-light" "base16/black-metal-gorgoroth" "base16/solarized-dark"
   "base16/classic-light" "base16/classic-dark" "base16/ia-light" "base16/atelier-cave-light"
   "base16/phd" "base16/atelier-lakeside" "base16/ir-black" "base16/darkmoss" "base16/dirtysea"
   "base16/ia-dark" "base16/windows-10" "base16/outrun-dark" "base16/synth-midnight-terminal-dark"
   "base16/marrakesh" "base16/solarized-light" "base16/edge-dark" "base16/humanoid-dark"
   "base16/black-metal-venom" "base16/darktooth" "base16/twilight" "base16/brush-trees-dark"
   "base16/apprentice" "base16/atelier-sulphurpool" "base16/onedark" "base16/atelier-savanna-light"
   "base16/dark-violet" "base16/atelier-seaside-light" "base16/nova" "base16/pasque" "base16/summercamp"
   "base16/gruvbox-dark-pale" "base16/framer" "base16/dracula" "base16/cupcake" "base16/unikitty-light"
   "base16/solar-flare" "base16/paraiso" "base16/gruvbox-light-hard" "base16/atelier-forest"
   "base16/helios" "base16/railscasts" "base16/default-dark" "base16/mellow-purple" "base16/nebula"
   "base16/eva-dim" "base16/gruvbox-light-medium" "base16/harmonic16-light" "base16/materia"
   "base16/ashes" "base16/silk-light" "base16/summerfruit-dark" "base16/london-tube" "base16/tango"
   "base16/windows-nt" "base16/material-lighter" "base16/spacemacs" "base16/atelier-plateau"
   "base16/equilibrium-gray-light" "base16/ros-pine-dawn" "base16/default-light" "base16/github"
   "base16/heetch-light" "base16/atelier-sulphurpool-light" "base16/atelier-dune" "base16/windows-10-light"
   "base16/bespin" "base16/black-metal-dark-funeral" "base16/windows-high-contrast" "base16/pico"
   "base16/brush-trees" "base16/equilibrium-gray-dark" "base16/humanoid-light" "base16/kimber"
   "base16/ros-pine" "base16/atelier-savanna" "base16/zenburn" "base16/solar-flare-light"
   "base16/eighties" "base16/atelier-plateau-light" "base16/black-metal-marduk" "base16/black-metal-khold"
   "base16/black-metal-immortal" "base16/icy-dark" "base16/atlas" "base16/gruvbox-dark-medium"
   "base16/mocha" "base16/material-vivid" "base16/gruvbox-dark-hard" "base16/espresso" "base16/danqing"
   "base16/gruvbox-dark-soft" "base16/circus" "base16/green-screen" "base16/unikitty-dark" "base16/pop"
   "base16/equilibrium-light" "base16/heetch-dark" "base16/brogrammer" "base16/horizon-light" "base16/silk-dark"
   "base16/decaf" "base16/atelier-estuary-light" "base16/colors" "base16/atelier-seaside" "base16/chalk"
   "base16/xcode-dusk" "base16/black-metal" "base16/macintosh" "base16/equilibrium-dark" "base16/shapeshifter"
   "base16/windows-95" "base16/bright" "base16/flat" "base16/oceanicnext" "base16/papercolor-dark"
   "base16/apathy" "base16/woodland" "base16/google-dark" "base16/atelier-heath" "base16/sagelight"
   "base16/atelier-estuary" "base16/windows-nt-light" "base16/harmonic16-dark" "base16/sandcastle"
   "base16/atelier-heath-light" "base16/gruvbox-light-soft" "base16/mexico-light" "base16/rebecca"
   "base16/porple" "base16/codeschool" "base16/ocean" "base16/isotope" "base16/monokai" "base16/one-light"
   "base16/seti-ui" "base16/qualia" "base16/black-metal-mayhem" "base16/tender" "base16/grayscale-dark"
   "base16/material-darker" "base16/tomorrow-night" "base16/atelier-lakeside-light" "base16/summerfruit-light"
   "base16/hopscotch" "base16/synth-midnight-terminal-light" "monokai-sublime" "kimbie-dark" "default"
   "paraiso-dark" "arduino-light" "purebasic" "gml" "a11y-light" "magula" "arta" "atom-one-light" "docco"
   "xcode" "atom-one-dark-reasonable" "qtcreator-light" "pojoaque.jpg" "qtcreator-dark" "foundation"
   "agate" "paraiso-light" "gradient-dark" "xt256" "an-old-hope" "stackoverflow-light" "srcery" "vs"
   "ascetic" "monokai" "devibeans" "tomorrow-night-blue" "brown-paper"])

(defn ->symbol [string]
  (-> string
      (string/split #"/")
      (->> (string/join "-"))
      symbol))

(doseq [s styles]
  (intern *ns* (->symbol s) s))


