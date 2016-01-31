(ns calteau.math-test
  (:require [calteau.math :as math]
            [blancas.kern.core :as kern])
  (:use clojure.test))

(deftest number-42 []
  (is (= (kern/value math/number "42") 42)))

(deftest var-X []
  (is (= (kern/value math/variable "X") 'X)))

(deftest expr-42 []
  (is (= (kern/value math/expr "42") 42)))

(deftest expr-X []
  (is (= (kern/value math/expr "X") 'X)))
