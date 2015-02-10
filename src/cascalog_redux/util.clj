(ns cascalog-redux.util
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))

;; The as-long functions I've pinched, and modified, from my day job. Thanks Ray.
(defn as-long
  "Attempt to coerce the argument `x` to a long. Throws if conversion fails."
  [x]
  (try (cond
        (= (class x) java.lang.Long) x
        (= x "") nil
        (= (class x) java.lang.String) (Long/parseLong x)
        :else (long x))
       (catch Exception e (throw
                           (Exception. (str "Could not parse: "
                                            x
                                            " as a long.\n"
                                            (.getMessage e)))))))

(def ^:private month-day-year-hour-minute (f/formatter "MM/dd/yy HH:mm"))

(defn as-date-time
  "Attempt to coerce the argument 'x' to a date-time object.

  Expected time format: MM-dd-yy HH:mm e.g. 8/29/13 14:13.

  Throws if the conversion fails."
  [x]
  (try (when-not (= x "")
         (f/parse month-day-year-hour-minute x))
       (catch Exception e (throw (Exception. (str "Could not parse: "
                                                  x
                                                  " as data time \"MM-dd-yy HH:mm\" e.g. \"8/29/13 14:13\".\n"
                                                  (.getMessage e)))))))

(defn get-interval
  "Returns a JODA time interval object between two date-time instances."
  [t1 t2]
  (if (t/before? t1 t2)
    (t/interval t1 t2)
    (t/interval t2 t1)))
