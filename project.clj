(defproject jsite "1.0.0-SNAPSHOT"
  :description "Personal website"
  :url "http://jcarr.herokuapp.com"
  :license {:name "FIXME: choose"
            :url "http://example.com/FIXME"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [ring/ring-devel "1.1.0"]
                 [ring-basic-authentication "1.0.1"]
                 [environ "0.2.1"]
                 [com.cemerick/drawbridge "0.0.6"]
                 ;; json 
                 [cheshire "5.2.0"]
                 ;; Routing library
                 [compojure "1.1.5"]
                 ;; Templating for Cojure
                 [enlive "1.1.1"]
                 ;; HTML in Clojure
                 [hiccup "1.0.4"]
                 ;; HTML->Hiccup
                 [hickory "0.4.1"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.2.1"]]
  :hooks [environ.leiningen.hooks]
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]}
             :production {:env {:production true}}})
