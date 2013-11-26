(ns jsite.views.contact
  (:use [net.cgrand.enlive-html :as enlive]))

(defn make-contact-page
  []
  (enlive/emit*
   (enlive/at (enlive/html-resource "jsite/views/layout.html")
              [:div#main]
              (enlive/content (enlive/html-resource "jsite/views/contact.html"))
              [:li#home-tab]
              (enlive/set-attr :class nil)
              [:li#contact-tab]
              (enlive/set-attr :class "active"))))
