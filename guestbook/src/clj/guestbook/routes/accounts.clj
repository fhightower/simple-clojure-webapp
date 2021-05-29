(ns guestbook.routes.accounts
  (:require
   [guestbook.layout :as layout]
   [guestbook.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]
   [buddy.hashers :as hashers]))

;; todo - update this to pull password from database
(defn login-valid? [params]
  (hashers/check (get params :pass) "bcrypt+sha512$86601e02c305a929834a291a2c6c3ab9$12$ad8d73baf30358bb4d7566a8e3cdcb6113898490c0a9c2d6"))

(defn serve-login-page [{:keys [flash] :as request}]
  (layout/render request "login.html" (select-keys flash [:name :message :errors])))

(defn attempt-login [{:keys [params]}]
  (if (login-valid? params)
    (do
     (println "Logged in!")
     (response/found "/"))
    (-> (response/found "/login")
        (assoc :flash (assoc params :errors "Invalid login. Please try again.")))))

(defn account-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/login" {:get serve-login-page
              :post attempt-login}]])
