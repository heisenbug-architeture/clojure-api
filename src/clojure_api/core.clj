(ns clojure-api.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [ring.util.response :refer [response]]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]])
  (:gen-class))

(defn simple-body-page
  [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})

(defn request-example [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->>
             (pp/pprint req)
             (str "Request Object: " req))})

(def people-collection (atom []))

(defn addperson [firstname surname]
  (swap! people-collection conj {:firstname (str/capitalize firstname) :surname (str/capitalize surname)}))

(defn people-handler
  [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    @people-collection})

(defn hello-name [req]
  (print "teste")
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body {:message (str "Hello " (:name (:params req)))}})

(defn addperson-handler
  [first-name surname]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body   (addperson first-name surname)})

(defroutes app-routes
  (GET "/" [] simple-body-page)
  (GET "/request" [] request-example)
  (GET "/hello" [] hello-name)
  (POST "/people" req
        (let [first-name (get-in req [:body "first_name"])
              surname (get-in req [:body "surname"])]
          (println "\n\n\n")
          (addperson-handler first-name surname)))

  (route/not-found "Error, page not found!"))

(def app
  (-> app-routes
      wrap-json-body
      wrap-json-response))

(addperson "Functional" "Human")
(addperson "Micky" "Mouse")

(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (server/run-server (wrap-defaults app (assoc-in site-defaults [:security :anti-forgery] false)) {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
