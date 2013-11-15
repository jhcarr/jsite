(ns jsite.web-test
  (:use ring.mock.request)
  (:require [clojure.test :refer :all]
            [jsite.web :refer :all]))

(deftest test-handle-root-req
  (testing "input root page request: expect index.html page"
    (is (= {:uri "/index.html"} (wrap-error-page {:uri "/"})))))

(deftest test-app
  (testing "input root route: expect 200 status"
    (let [response (app (request :get "/"))]
      (is (= 200 (response :status)))))
  (testing "input invalid route: expect 400 status"
    (let [response (app (request :get "/herp-a-derp"))]
      (is (= 404 (response :status))))))
