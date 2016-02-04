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
