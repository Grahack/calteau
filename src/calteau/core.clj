(ns calteau.core
  (:require [blancas.kern.core :refer :all])
  (:gen-class))

(def t-user-input
  "Parses a line like `Input X`."
  (bind [_ (token* "Input ")
         var upper]
    (return (list 'def var '(read-line)))))

(def t-expr
  "Parses an expression."
  (<+> (many any-char)))

(def t-display
  "Parses a line like `Display ...`."
  (bind [_ (token* "Disp ")
         var t-expr]
    (return (list 'println var))))

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
  (println (src-to-clj "Input X\nDisp X")))
