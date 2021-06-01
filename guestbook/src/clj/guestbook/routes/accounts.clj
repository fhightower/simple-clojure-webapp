(ns guestbook.routes.accounts
  (:require
   [guestbook.layout :as layout]
   [guestbook.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]
   [buddy.hashers :as hashers]
   [guestbook.db.core :as db]))

(defn login-valid? [params]
  (hashers/check (get params :pass) (db/get-user-password (get params :user))))

(defn serve-login-page [{:keys [flash] :as request}]
  (layout/render request "login.html" (select-keys flash [:name :message :errors])))

(defn attempt-login [{:keys [params]}]
  (if (login-valid? params)
    (response/found "/")
    (-> (response/found "/login")
        (assoc :flash (assoc params :errors "Invalid login. Please try again.")))))

(defn account-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/login" {:get serve-login-page
              :post attempt-login}]])
