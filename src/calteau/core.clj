(ns calteau.core
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
  "A line of a t-program."
  (bind [instr (<|> t-user-input
                    t-display)
         _ (<|> new-line* eof)]
    (return instr)))

(def t-prg
  "Parses a T program."
  (many t-instruction))

(defn src-to-clj
  [src]
  (let [st (parse t-prg src)]
    (if (:ok st)
      (:value st)
      (with-out-str (print-error st)))))

(defn -main
  []
  (println "Sorry, it's only a library!"))
