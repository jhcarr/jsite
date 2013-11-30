(ns jsite.web-test
  (:use ring.mock.request
        [net.cgrand.enlive-html]
        [jsite.views.contact :only (make-contact-page)])
  (:require [clojure.test :refer :all]
            [jsite.web :refer :all]))

(deftest test-default-layout
  (testing "input string: expect valid site page"
    (let [test-string "Hello, World."]
      (is (= (default-layout test-string)
             (->> test-string
                  (html)
                  (content)
                  (at (html-resource "jsite/views/layout.html") [:#main])
                  (emit*)))))))

(deftest test-html-layout
  (testing "input file of html elems: expect valid site page"
    (let [test-page "jsite/views/home.html"]
      (is (= (html-layout test-page)
             (->> test-page
                  (html-resource)
                  (content)
                  (at (html-resource "jsite/views/layout.html") [:#main])
                  (emit*)))))))

(deftest test-tab-layout
  (testing "input tuple of page source and nil value: expect default site page."
    (is (= (tab-layout "jsite/views/contact.html" :li#contact-tab)
           (make-contact-page)))))

(deftest test-app
  (testing "input all valid routes: expect 200 status"
    (let [home (app (request :get "/"))
          projects (app (request :get "/projects"))
          schedule (app (request :get "/schedule"))
          resume (app (request :get "/resume"))
          about (app (request :get "/about"))]
      (is (= 200 (home :status)))
      (is (= 200 (schedule :status)))
      (is (= 200 (about :status)))      
      (is (= 200 (resume :status)))))
  (testing "input invalid route: expect 400 status"
    (let [response (app (request :get "/herp-a-derp"))]
      (is (= 404 (response :status))))))
