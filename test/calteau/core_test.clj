(ns calteau.core-test
  (:require [calteau.core :as calteau])
  (:use clojure.test))

(deftest simple-program []
  (is (= (calteau/src-to-clj "Input X\nDisp X\nDisp 6")
         '[(def X (read-line)) (println X) (println 6)])))
