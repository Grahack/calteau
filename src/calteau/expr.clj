(ns calteau.expr
  "Tools for parsing the structure of math expressions"
  (:gen-class))

(defstruct ^:private operator :name :precedence :associativity)

(def ^:private operators
     #{(struct operator + '1 'left)
       (struct operator - '1 'left)
       (struct operator * '2 'left)
       (struct operator / '2 'left)
       (struct operator '\^ 3 'right)})

(defn- lookup-operator
  [symb]
  (first (filter (fn [x] (= (:name x) symb)) operators)))

(defn- operator?
  [op]
  (not (nil? (lookup-operator op))))

(defn- op-compare
  [op1 op2]
  (let [operand1 (lookup-operator op1)
        operand2 (lookup-operator op2)]
    (or
      (and (= 'left (:associativity operand1))
           (<= (:precedence operand1) (:precedence operand2)))
      (and (= 'right (:associativity operand1))
           (<= (:precedence operand1) (:precedence operand2))))))

(defn- open-bracket? [op]
  (= op \())

(defn- close-bracket? [op]
  (= op \)))

(defn shunting-yard
  "Convert an array of symbols (infix) into a list (RPN) using the famous algo

   Taken from <http://www.fatvat.co.uk/2009/06/shunting-yard-algorithm.html>.

   Changes before inclusion in the repo:

   * Quote `^` in `operators`.
   * Remove trailing whitespace."
  ([expr]
     (shunting-yard expr []))
  ([expr stack]
     (if (empty? expr)
       (if-not (some (partial = \() stack)
         stack
         (assert false))
       (let [token (first expr)
             remainder (rest expr)]
         (cond

           (number? token)
             (lazy-seq (cons token (shunting-yard remainder stack)))

           (operator? token)
             (if (operator? (first stack))
               (lazy-seq
                 (concat (take-while (partial op-compare token) stack)
                         (shunting-yard remainder
                                        (cons token
                                              (drop-while (partial op-compare token) stack)))))
               (shunting-yard remainder (cons token stack)))

           (open-bracket? token)
             (shunting-yard remainder (cons token stack))

           (close-bracket? token)
             (let [ops (take-while (comp not open-bracket?) stack)
                   ret (drop-while (comp not open-bracket?) stack)]
               (assert (= (first ret) \())
               (lazy-seq
                 (concat ops (shunting-yard remainder (rest ret)))))

           :else (assert false))))))

(defn rpn2pn
  "Convert a list of symbols (RPN) into a Clojure expression (PN)

   Taken from <http://eddmann.com/posts/infix-calculator-in-clojure/>.

   Changes before inclusion in the repo:

   * `{\"+\" +, ...}]` -> `#{'+ '- '* '/}`, because we have symbols not strings
   * add `list` before `(ops token)`, because we don't eval, we build up
   * `(ops token)` -> `token`, because of the first change
   * `(read-string token)` -> `token`, because we have symbols not strings"
  [tokens]
  (let [ops #{'+ '- '* '/}]
    (first
      (reduce
        (fn [stack token]
          (if (contains? ops token)
            (cons (list token (second stack) (first stack)) (drop 2 stack))
            (cons token stack)))
        [] tokens))))