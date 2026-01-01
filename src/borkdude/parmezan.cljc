(ns borkdude.parmezan
  (:require [clojure.string :as str]
            [edamame.core :as e]))

(defn- split-at-pos [s pos]
  (if (> pos (count s))
    [s nil]
    [(subs s 0 pos) (subs s pos)]))

(defn- replace-char-at-pos [s pos char]
  (str (subs s 0 pos) char (subs s (inc pos))))

(defn parmezan [s]
  (loop [s s]
      (let [[status s]
            (try (e/parse-string-all s {:all true
                                        :features #?(:bb
                                                     ;; wait for new bb release to remove this
                                                     #{:clj :cljs :bb}
                                                     :default identity)
                                        :read-cond :allow
                                        :auto-resolve name
                                        :readers (fn [_tag]
                                                   (fn [val] val))})
                 [::success s]
                 (catch #?(:clj clojure.lang.ExceptionInfo
                           :cljs ExceptionInfo) e
                   (if-let [expected-delimiter (:edamame/expected-delimiter (ex-data e))]
                     (let [{:keys [row col]} (ex-data e)
                           lines (str/split-lines s)
                           [before-rows after-rows] (split-at (dec row) lines)
                           [row & after-rows] after-rows
                           row (if (zero? (count expected-delimiter))
                                 (replace-char-at-pos row (dec col) "")
                                 (let [[before-cols after-cols] (split-at-pos (or row "") (dec col))
                                       row (str before-cols expected-delimiter after-cols)]
                                   row))
                           new-s (str/join \newline (concat before-rows [row] after-rows))]
                       [::recur new-s])
                     (throw e))))]
        (case status
          ::success s
          ::recur (recur s)))))

(comment
  (parmezan "#js [")
  )
