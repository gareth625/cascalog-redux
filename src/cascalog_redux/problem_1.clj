(ns cascalog-redux.problem-1
  "What was the average total time (in minutes) used by a bicycle in the data?

  Extension: What does the distribution of the journey times look like? as a frequency distribution?"
  (:require [cascalog.api :refer :all]
            [cascalog.logic.ops :as c]
            [cascalog-redux.taps :as bike-trip-data]
            [cascalog-redux.util :as u]
            [clj-time.core :as t]))

(defn- get-trip-time-in-minutes
  [trip-data br-sink]
  (<- [?trip_duration_minutes]

      (trip-data :>> bike-trip-data/bike-trip-data-fields)

      (u/get-interval !start_date !end_date :> ?trip_duration)
      (t/in-minutes ?trip_duration :> ?trip_duration_minutes)))

(defn problem-1
  [input output]
  (let [dest (bike-trip-data/out-sink output)
        br-sink (hfs-textline "br")
        trip-data (bike-trip-data/bike-trip-data input br-sink)]
    (?<- dest
         [?avg_trip_duration_minutes]

         ((get-trip-time-in-minutes trip-data br-sink) :> ?trip_duration_minutes)
         (c/avg ?trip_duration_minutes :> ?avg_trip_duration_minutes)

         (:trap br-sink))))

(defn problem-1-hist
  [input output]
  (let [dest (bike-trip-data/out-sink output)
        br-sink (hfs-textline "br")
        trip-data (bike-trip-data/bike-trip-data input br-sink)]
    (?<- dest
         [?trip_duration_minutes ?count]

         ((get-trip-time-in-minutes trip-data br-sink) :> ?trip_duration_minutes)
         (c/count ?count)

         (:trap br-sink))))
