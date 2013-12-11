(ns jsite.web
  (:use [net.cgrand.enlive-html]
        [jsite.views.resume :only (make-resume-page)]
        [jsite.views.contact :only (make-contact-page)])
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.middleware.stacktrace :as trace]
            [ring.middleware.session :as session]
            [ring.middleware.session.cookie :as cookie]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.basic-authentication :as basic]
            [cemerick.drawbridge :as drawbridge]
            [environ.core :refer [env]]))

(defn- authenticated? [user pass]
  ;; TODO: heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]
  (= [user pass] [(env :repl-user false) (env :repl-password false)]))

(def ^:private drawbridge
  (-> (drawbridge/ring-handler)
      (session/wrap-session)
      (basic/wrap-basic-authentication authenticated?)))

(defn default-layout
  "Wraps an argument with a valid site page."
  [body]
  (->> body
       (html)
       (content)
       (at (html-resource "jsite/views/layout.html") [:#main])
       (emit*)))

(defn html-layout
  "Wraps html arguments with a valid site page."
  [path-to-html]
  (->> path-to-html
       (html-resource)
       (content)
       (at (html-resource "jsite/views/layout.html") [:#main])
       (emit*)))


(defn tab-layout
  [path-to-html selector]
  (emit* (at (html-resource "jsite/views/layout.html")
             [:li#home-tab]
             (set-attr :class nil)
             [:div#main]
             (content (html-resource path-to-html))
             [selector]
             (set-attr :class "active"))))

(defroutes app
  (ANY "/repl" {:as req}
       (drawbridge req))
  (GET "/" [] (html-layout "jsite/views/home.html"))
  (GET "/schedule" [] (html-layout "jsite/views/schedule.html"))
  (GET "/contact" [] (tab-layout "jsite/views/contact.html" :li#contact-tab))
  (GET "/resume" [] (tab-layout "jsite/views/resume.html" :li#resume-tab))
  (GET "/about" [] (tab-layout "jsite/views/about.html" :li#about-tab))
  (GET "/3dsp" [] (tab-layout "jsite/views/3dsp.html" :li#projects-tab))
  (GET "/dujour" [] (tab-layout "jsite/views/dujour.html" :li#projects-tab))
  (route/resources "/")
  (route/not-found (slurp (io/resource "404.html")))
  )

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
         (catch Exception e
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (slurp (io/resource "500.html"))}))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))
        ;; TODO: heroku config:add SESSION_SECRET=$RANDOM_16_CHARS
        store (cookie/cookie-store {:key (env :session-secret)})]
    (jetty/run-jetty (-> #'app
                         ((if (env :production)
                            wrap-error-page
                            trace/wrap-stacktrace))
                         (site {:session {:store store}}))
                     {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
