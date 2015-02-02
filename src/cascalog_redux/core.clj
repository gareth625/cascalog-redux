(ns cascalog-redux.core
  (:require [cascalog.api :refer :all]
            [cascalog-redux.taps :as bike-trip-data])
  (:gen-class))

(defn -main
  ""
  [input & args]
  (let [bad-rows-sink (hfs-textline "br")]
    (?- (bike-trip-data/out-sink "stdout")
        (bike-trip-data/bike-trip-data input bad-rows-sink)
        (:trap bad-rows-sink))))
