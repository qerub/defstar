(ns defstar.core-test
  (:require [clojure.test :refer :all]
            [defstar.core :refer :all]
            [clojure.walk :refer [macroexpand-all]]))

(defmethod assert-expr 'macroexpand= [msg form]
  (let [form* `(= ~(nth form 1) (macroexpand-all ~(nth form 2)))]
    (assert-predicate msg form*)))

(deftest def*-test
  (is (macroexpand= `(def n 42)
                    ; <=
                    `(def* n 42)))

  (is (macroexpand= `(def id (fn* ([x] x)))
                    ; <=
                    `(def* (id x) x)))

  (is (macroexpand= `(def adder (fn* ([x] (fn* ([y] (+ x y))))))
                    ; <=
                    `(def* ((adder x) y) (+ x y))))

  (is (macroexpand= `(def f (fn* ([] (let* [n 42] (* n n)))))
                    ; <=
                    `(def* (f) (def n 42) (* n n))))

  )
