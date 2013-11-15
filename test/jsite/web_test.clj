(ns jsite.web-test
  (:use ring.mock.request)
  (:require [clojure.test :refer :all]
            [jsite.web :refer :all]))

(deftest test-app
  (testing "site root route"
    (let [response (app (request :get "/"))]
      (is (= 200 (response :status)))))
  (testing "site not found"
    (let [response (app (request :get "/herp-a-derp"))]
      (is (= 404 (response :status))))))
