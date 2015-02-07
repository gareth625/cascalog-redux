(ns cascalog-redux.core
  (:require [cascalog.api :refer :all]
            [cascalog-redux.taps :as bike-trip-data])
  (:gen-class))

(def bike-trip-data-sample
  "data/bike_trip_data_sample.csv")

(def bike-trip-data-full
  "data/bike_trip_data.csv")

(defn -main
  "Streams the input bike trip data file to stdout. You probably don't want to stream the whole file ;)"
  [input & args]
  (let [bad-rows-sink (hfs-textline "br")]
    (?- (bike-trip-data/out-sink "stdout")
        (bike-trip-data/bike-trip-data input bad-rows-sink)
        (:trap bad-rows-sink))))
