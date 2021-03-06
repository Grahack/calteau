(ns calteau.expr-test
  (:require [calteau.expr :as expr])
  (:use clojure.test))

(defn pretty-print
  [expr]
  (map (fn [x]
         (cond
           (= x +) '+
           (= x -) '-
           (= x /) '/
           (= x *) '*
           :else x)) expr))

(deftest test-shuntingyard
  (is (= (pretty-print (expr/shunting-yard [100 + 200])) '(100 200 +)))
  (is (= (pretty-print (expr/shunting-yard [100 * 200])) '(100 200 *)))
  (is (= (pretty-print (expr/shunting-yard [100 / 200])) '(100 200 /)))
  (is (= (pretty-print (expr/shunting-yard [100 - 200])) '(100 200 -)))
  (is (= (pretty-print (expr/shunting-yard [4 + 5 + \( 6 * 7 \)] '(4 5 + 6 7 * +)))))
  (is (= (pretty-print (expr/shunting-yard [\( \( \( 6 * 7 \) \) \)] '(6 7 *)))))
  (is (= (pretty-print (expr/shunting-yard [3 + 4 * 5])) '(3 4 5 * +))))

(deftest test-rpn2pn
  (is (= (expr/rpn2pn '(1 1 +)) '(+ 1 1)))
  (is (= (expr/rpn2pn '(A B *)) '(* A B)))
  (is (= (expr/rpn2pn '(3 4 5 * 3 2 + / +)) '(+ 3 (/ (* 4 5) (+ 3 2))))))
