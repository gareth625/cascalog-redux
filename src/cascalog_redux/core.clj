(ns cascalog-redux.core
  "This contains -main, although really this is just the canonical starting point for any Clojure project."
  (:require [cascalog.api :refer :all]
            [cascalog-redux.taps :as bike-trip-data])
  (:gen-class))

(defn stream-file-to-stdout
  [input]
  (let [bad-rows-sink (hfs-textline "br")]
    (?- (bike-trip-data/out-sink "stdout")
        (bike-trip-data/bike-trip-data input bad-rows-sink)
        (:trap bad-rows-sink))))

(defn stream-file
  "Streams the input bike trip data file to stdout. You probably don't want to stream the whole file ;)"
  [input output]
  (let [dest (bike-trip-data/out-sink output)
        bad-rows-sink (hfs-textline "br")]
    (?- dest
        (bike-trip-data/bike-trip-data input bad-rows-sink)
        (:trap bad-rows-sink))))

(defn -main
  "Streams the input bike trip data file to stdout. You probably don't want to stream the whole file ;)"
  [input & args]
  (stream-file-to-stdout input))
