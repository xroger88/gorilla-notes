(defproject gorilla-notes "0.1.0-SNAPSHOT"
  :description "basic example using gorilla-ui"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/core.async "0.4.500"]
                 [org.clojure/core.memoize "0.7.2"]
                 [environ "1.1.0"]
                 [http-kit "2.3.0"]
                 [juji/chord "0.8.2"]
                 [compojure "1.6.1"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-cors "0.1.13"]
                 [ring/ring-devel "1.7.1"]
                 [cheshire "5.8.1"]]
  :repl-options {:init-ns gorilla-notes.core})


