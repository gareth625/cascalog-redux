(ns cascalog-redux.util
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))

;; The (try-)as-long functions I've pinched from my day job. Thanks Ray.
(defn as-long
  [x]
  "Coerce the argument `x` to a long."
  (cond
   (= (class x) java.lang.Long) x
   (= (class x) java.lang.String) (Long/parseLong x)
   :else (long x)))

(defn try-as-long
  "Attempt to coerce the argument `x` to a long. Return nil on error."
  [x]
  (try (as-long x) (catch Exception _ nil)))

(def ^:private month-day-year-hour-minute (f/formatter "MM/dd/yy HH:mm"))

(defn try-as-date-time
  "Attempt to coerce the argument 'x' to a date-time object.

  Expected time format: MM-dd-yy HH:MM e.g. 8/29/13 14:13."
  [x]
  (try (f/parse month-day-year-hour-minute x) (catch Exception _ nil)))
