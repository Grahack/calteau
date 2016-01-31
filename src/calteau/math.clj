(ns calteau.math
  (:require [blancas.kern.core :refer :all]))

(def number
  "Parses a number."
  (<+> (many1 digit)))

(def variable
  "Parses a variable."
  upper)

(def expr
  "Parses a math expression."
  (<|> number variable))