```clj
;; def*                  (pronounced defstar)
;;
;; An experiment in macrology and in an
;; alternative definition form for Clojure
;; based on Racket's define
;;
;; Not to be confused with
;; https://bitbucket.org/eeeickythump/defstar
;;
;; - - - - - - - - - - - - - - - - - - - - - -

(use 'defstar.core)

;; It can define anything:

(def* n 42)

;; It can define functions:

(def* (f x) (+ x n))

;; It can define curried functions:

(def* ((adder x) y) (+ x y))

((adder 1) 2) ; => 3

;; It supports local definitions:

(def* (f x)
  (def n 42)
  (* n x))

(def* (f x)
  (def* (a x) (+ x 2))
  (def* (b x) (* x 2))
  (->> x a b))

;; - - - - - - - - - - - - - - - - - - - - - -
;;
;; Copyright (c) 2014 Christoffer Sawicki
```
