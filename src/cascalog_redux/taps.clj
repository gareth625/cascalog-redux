(ns cascalog-redux.taps
  (:require [cascalog.api :refer :all]
            [cascalog-redux.util :as util]))

(def bike-trip-data-sample
  "A smaller version of the data set which is useful for experiments where you want to check the processing of each line."
  "data/bike_trip_data_sample.csv")

(def bike-trip-data-full
  "The full data set. 16MB, 114,015 rows and 11 columns."
  "data/bike_trip_data.csv")

(defn out-sink
  "Defines the output sink as a location on a HDFS file system.

   This will not append to an existing directory so it is up-to the caller to
   ensure it is unique.

   If 'stdout' is given as the argument the stdout sink is return."
  [out]
  (if (not= out "stdout")
    (hfs-textline out :sinkmode :keep)
    (stdout)))

(def ^:private bike-trip-data-fields-and-classes
  ;; The order must match the column ordering in the CSV file in order to
  ;; correctly parse the columns.
  (array-map "!trip_id" util/as-long
             "!duration" util/as-long
             "!start_date" util/as-date-time
             "!start_station" identity
             "!start_terminal" util/as-long
             "!end_date" util/as-date-time
             "!end_station" identity
             "!end_terminal" util/as-long
             "!bike_num" util/as-long
             "!subscription_type" identity
             "!zip_code" identity))

(def bike-trip-data-fields
  "Vector of the Cascalog variables to assign to each of the bike trip columns."
  (keys bike-trip-data-fields-and-classes))

 (defn- parse-line
  "Splits the given line based on a regular expression delimiter and, optionally, applies a function to the read field.

   The input should contain only a single line to parse. It will ignore all
   other lines in the input."
  [line & {:keys [delimiter classes]}]
  (let [n-columns 11
        parsed-line (clojure.string/split line delimiter n-columns)]
    (if-not (empty? classes)
      (map #(%1 %2) classes parsed-line) ;; Applies the predicate from index i
                                         ;; to the ith entry in the parsed-line
                                         ;; tuple performing requested transform
      parsed-line)))

(defn bike-trip-data
  [data-file bad-rows-sink]
  (let [bike-trip-data-classes (vals bike-trip-data-fields-and-classes)]
    (<- bike-trip-data-fields
        ; I believe that as the source tap associates names with each tuple
        ; element (:outfields) the name ordering doesn't matter for the
        ; :>> output. However it's the same as the input so no need to worry.
        ; The order of the input must match the file column ordering.
        ((hfs-textline data-file) ?line)
        (parse-line ?line :delimiter #"," :classes bike-trip-data-classes
                    :>> bike-trip-data-fields)
        (:trap bad-rows-sink))))
