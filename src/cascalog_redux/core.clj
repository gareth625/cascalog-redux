(ns cascalog-redux.core
  "This contains -main, although really this is just the canonical starting point for any Clojure project."
  (:require [cascalog.api :refer :all]
            [cascalog-redux.taps :as bike-trip-data])
  (:gen-class))

(def bike-trip-data-sample
  "A smaller version of the data set which is useful for experiments where you want to check the processing of each line."
  "data/bike_trip_data_sample.csv")

(def bike-trip-data-full
  "The full data set. 16MB, 114,015 rows and 11 columns."
  "data/bike_trip_data.csv")

(defn -main
  "Streams the input bike trip data file to stdout. You probably don't want to stream the whole file ;)"
  [input & args]
  (let [bad-rows-sink (hfs-textline "br")]
    (?- (bike-trip-data/out-sink "stdout")
        (bike-trip-data/bike-trip-data input bad-rows-sink)
        (:trap bad-rows-sink))))
