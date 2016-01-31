(ns calteau.math
  (:require [blancas.kern.core :refer :all]))

(def number
  "Parses a number."
  (<$> read-string
       (<+> (many1 digit))))

(def variable
  "Parses a variable."
  (<$> #(symbol (str %))
       upper))

(def expr
  "Parses a math expression."
  (<|> number variable))
