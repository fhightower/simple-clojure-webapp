(ns guestbook.handler-test
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [guestbook.handler :refer :all]
    [guestbook.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'guestbook.config/env
                 #'guestbook.handler/app-routes)
    (f)))

(deftest test-routes
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response)))))

  (testing "login route"
    (let [response ((app) (request :get "/login"))]
      (is (= 200 (:status response)))
      (.contains (:body response) "Welcome, please login"))))
