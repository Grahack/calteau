(defproject calteau "0.0.1-SNAPSHOT"
  :description "A Clojure lib to parse and convert source code from calculators"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.blancas/kern "0.7.0"]]
  :profiles {:dev {:plugins [[lein-codox "0.9.1"]]}
             :uberjar {:aot :all}}
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :codox {:metadata {:doc "FIXME: write docs"
                     :doc/format :markdown}
          :source-uri "https://github.com/grahack/calteau/blob/{version}/{filepath}#L{line}"}
  :main calteau.core)
