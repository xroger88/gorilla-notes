(ns gorilla-notes.components.dataset
  (:require ["ag-grid-react" :as rs :refer [AgGridReact]]))

;; Inspired by gorilla-ui aggrid component

(defn ^{:category :gorilla-notes}
  dataset-view
  "displays data in a table, uses ag-grid
   [aggrid {:columnDefs [{:headerName \"Make\" :field \"make\"}
                         {:headerName \"Model\" :field \"model\"}
                         {:headerName \"Price\" :field \"price\"}]
            :rowData [{:make \"Toyota\" :model \"Celica\" :price 35000}
                      {:make \"Ford\" :model \"Mondeo\" :price 32000}
                      {:make \"Porsche\" :model \"Boxter\" :price 72000}]}]"
  [data]
  [:<>
   [:> AgGridReact data]])
