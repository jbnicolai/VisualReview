;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Copyright 2015 Xebia B.V.
;
; Licensed under the Apache License, Version 2.0 (the "License")
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;     http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns com.xebia.visualreview.core
  (:require [slingshot.slingshot :refer [try+]]
            [com.xebia.visualreview.starter :as starter]
            [com.xebia.visualreview.config :as config]
            [com.xebia.visualreview.io :as io]
            [com.xebia.visualreview.persistence.database :as db]
            [taoensso.timbre :as timbre])
  (:gen-class))

(defn- config-settings []
  (try+
    (config/init!)
    (catch [:type :com.xebia.visualreview.validation/invalid] exception
      (timbre/log :fatal (str "Server configuration error: " (exception :message))))))

(defn -main [& _]
  (when-let [{:keys [server-port db-uri db-user db-password screenshots-dir]} (config-settings)]
    (try
      (io/init-screenshots-dir! screenshots-dir)
      (db/init! db-uri db-user db-password)
      (starter/start-server server-port)
      :ok
      (catch Exception e
        (timbre/log :fatal (str "Error initializing: " (.getMessage e)))
        (starter/stop-server)
        (db/close-connection)
        (System/exit 1)))))
