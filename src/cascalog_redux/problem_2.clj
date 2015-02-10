(ns cascalog-redux.problem-2
  "What was the most popular day by trip frequency?"
  (:require [cascalog.api :refer :all]
            [cascalog.logic.ops :as c]
            [cascalog-redux.taps :as bike-trip-data]))
