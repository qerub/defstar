(ns defstar.core
  (:use [clojure.core.match :only [match]]))

(defmacro do* [& xs]
  (if (seq xs)
    (let [x (macroexpand (first xs))]
      (if (and (seq? x) (= (first x) 'def))
        `(let [~@(rest x)] (do* ~@(next xs)))
        (if (next xs)
          `(do ~x (do* ~@(next xs)))
          x)))
    `(do)))

;; fn* is used internally in Clojure
(defmacro fn** [args & body]
  `(fn ~args (do* ~@body)))

(defmacro def* [& margs]
  ;; :seq matches (U ISeq Sequential) so vectors are unfortunately also matched

  (match (vec margs)
    [  (name :guard symbol?)               (docs :guard string?) body]          `(def  ~name  ~docs ~body)
    [([(name :guard symbol?) & args] :seq) (docs :guard string?) body0 & body*] `(def  ~name  ~docs (fn** [~@args] ~body0 ~@body*))
    [([([& inner] :seq)      & args] :seq) (docs :guard string?) body0 & body*] `(def* ~inner ~docs (fn** [~@args] ~body0 ~@body*))

    [  (name :guard symbol?)                 body]  `(def  ~name  ~body)
    [([(name :guard symbol?) & args] :seq) & body*] `(def  ~name  (fn** [~@args] ~@body*))
    [([([& inner] :seq)      & args] :seq) & body*] `(def* ~inner (fn** [~@args] ~@body*))

    :else (throw (ex-info "Invalid invocation of def*" {:form (cons 'def* margs)}))))
