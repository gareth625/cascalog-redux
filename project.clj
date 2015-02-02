(defproject cascalog-redux "0.1.0-SNAPSHOT"
  :description "Worked example for my presentation at the Cambridge NonDysFunctional programmers meetup: http://www.meetup.com/Cambridge-NonDysFunctional-Programmers/events/218678661/."
  :url "https://github.com/gareth625/cascalog-redux"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cascalog "2.1.1"]
                 [clj-time "0.7.0"]
                 [de.javakaffee/kryo-serializers "0.23"]]
  :main ^:skip-aot cascalog-redux.core
  :target-path "target/%s"
  :profiles {:provided {:dependencies [[org.apache.hadoop/hadoop-core "1.0.3"]]}
             :uberjar {:aot :all}}
  :jvm-opts ["-Xmx2g"])
