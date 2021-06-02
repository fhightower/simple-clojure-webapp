(ns guestbook.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [guestbook.dev-middleware :refer [wrap-dev]]
    [guestbook.db.core :as db]))

(defn create-sample-data []
  (db/create-user! {:id 1 :first_name "Jane" :last_name "Doe" :email "jane@example.com" :pass "abc"}))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (create-sample-data)
     (log/info "\n-=[guestbook started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[guestbook has shut down successfully]=-"))
   :middleware wrap-dev})
