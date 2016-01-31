(ns calteau.core
  "Entry point to parse and convert source code from calculators"
  (:require [blancas.kern.core :refer :all]
            [calteau.math :as math])
  (:gen-class))

(def t-user-input
  "Parses a line like `Input X`."
  (bind [_ (token* "Input ")
         var upper]
    (return (list 'def (symbol (str var)) '(read-line)))))

(def t-expr
  "Parses an expression."
  math/expr)

(def t-display
  "Parses a line like `Display ...`."
  (bind [_ (token* "Disp ")
         expr t-expr]
    (return (list 'println expr))))

(def t-instruction
  "Parses a line of a t-program."
  (bind [instr (<|> t-user-input
                    t-display)
         _ (<|> new-line* eof)]
    (return instr)))

(def t-prg
  "Parses a T program."
  (many t-instruction))

(defn src-to-clj
  "Convert a string containing a program to Clojure code.

   For now it's only for T programs."
  [src]
  (let [st (parse t-prg src)]
    (if (:ok st)
      (:value st)
      (with-out-str (print-error st)))))

(defn -main
  "Placeholder for future use..."
  []
  (println "Sorry, it's only a library!"))
