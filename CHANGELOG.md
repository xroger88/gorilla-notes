# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [0.5.13-SNAPSHOT]
- paths reorganizations (making index.html, etc. have a unique location to avoid conflicts with other tools)
- deps version update
- code cleanup

## [0.5.12] - 2020-05-08
- updated deps (shadow-cljs, gorilla-ui)
- a more standard setup of index.html and static rendering
- fixed communication through websockets: debouncing

## [0.5.11] - 2020-03-16
- periodically boardcasting for eventual sync

## [0.5.10] - 2020-03-15
- experimental quil support

## [0.5.9] - 2020-03-14
- changed markdown rendering to react-markdown, for math support

# [0.5.8] - 2020-03-13
- rebuild after npm cleanup and some version updates

# [0.5.7] - 2020-03-13
- rebuild after npm cleanup and vega version update

## [0.5.6] - 2020-03-13
- added sci component
- support for including external files
- css changes (brought the margins back)

## [0.5.5] - 2020-02-17
- changed default config
- bugfix in options update on server start
- using KaTeX for math rendering

## [0.5.3] - 2020-01-25
- fixed the configurable port support (now the port is communicated to the client as required)
- added support for raw html hiccup tags
- made autoscroll off by default
- switched to the Statically CDN service

## [0.5.2] - 2020-01-21
- stoppable server, configurable port

## [0.5.1] - 2020-01-20
- made auto-scroll configurable, turned off on on static rendering

## [0.5.0] - 2020-01-14
- updated core.async dep version
- took care of ag-grid css
- added leaflet component (a bit more flexible than the Gorilla-UI one)
- some internal renaming
- added a catalog of leaflet tile providers
- made code blocks components flexible in syntax and supporting different laguages
- server state cleanup for efficiency

## [0.4.0] - 2020-09-21
- changes in aesthetics, configurability
- some api extensions for more control on state
- support for interactive input elements
- static rendering
- added "Loading" message

## [0.3.0-SNAPSHOT] - 2020-08-16
- started supporting some convenience pinkie tags (e.g., `:p/code` for styling code)
- build setup changes (distinguishing dev from release)
- more careful client state updates
- added some some convenience functions regarding the default URL
- changed default port
- support for changing a single note
- more careful client state updates 
- minor changes in view
- bugfixes

## [0.2.0-SNAPSHOT] - 2020-08-16
- refactoring for modularity and finer control over rendering options

## [0.1.0-SNAPSHOT] - 2020-07-23
- initial version
