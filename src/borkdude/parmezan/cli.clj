(ns borkdude.parmezan.cli
  (:require [babashka.cli :as cli]
            [borkdude.parmezan :as parmezan]))

(def cli-spec {:spec {:file {:desc "The file you want to fix"}
                      :write {:desc "Modify file in place"}}
               :aliases {:f :file
                         :w :write}
               :args->opts [:file]})

(defn exec [{:keys [file write]}]
  (when-not file
    (println "Missing --file option")
    (println "Options:")
    (println (cli/format-opts cli-spec))
    (throw (ex-info nil {:babashka/exit 1})))
  (let [s (slurp file)
        new-s (parmezan/parmezan s)]
    (when-not (= s new-s)
      (if write
        (spit file new-s)
        (println new-s))
      ;; in case of changes, return exit code 1
      (System/exit 1))))

(defn -main [& args]
  (exec (cli/parse-opts args cli-spec)))
