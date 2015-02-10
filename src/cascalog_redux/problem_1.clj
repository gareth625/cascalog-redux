(ns cascalog-redux.problem-1
  "What was the average total time (in minutes) used by a bicycle in the data?

  Extension: What does the distribution of the journey times look like? as a frequency distribution?"
  (:require [cascalog.api :refer :all]
            [cascalog.logic.ops :as c]
            [cascalog-redux.taps :as bike-trip-data]
            [clj-time.core :as t]))

(defn- get-interval
  [t1 t2]
  (if (t/before? t1 t2)
    (t/interval t1 t2)
    (t/interval t2 t1)))

(defn problem-1
  [input output]
  (let [dest (bike-trip-data/out-sink output)
        br-sink (hfs-textline "br")
        trip-data (bike-trip-data/bike-trip-data input br-sink)]
    (?<- dest
         [?avg_trip_duration_minutes]

         (trip-data :>> bike-trip-data/bike-trip-data-fields)

         (get-interval !start_date !end_date :> ?trip_duration)
         (t/in-minutes ?trip_duration :> ?trip_duration_minutes)

         (c/avg ?trip_duration_minutes :> ?avg_trip_duration_minutes)

         (:trap br-sink))))
