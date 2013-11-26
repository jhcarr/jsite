(ns jsite.views.resume
  (:use [net.cgrand.enlive-html :as enlive]))

(defn make-resume-page
  []
  (enlive/emit*
   (enlive/at (enlive/html-resource "jsite/views/layout.html")
              [:div#main]
              (enlive/content (enlive/html-resource "jsite/views/resume.html"))
              [:li#home-tab]
              (enlive/set-attr :class nil)
              [:li#resume-tab]
              (enlive/set-attr :class "active"))))
