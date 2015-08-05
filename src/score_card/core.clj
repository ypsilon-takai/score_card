(ns score_card.core
  (:use [compojure.core :only [defroutes GET POST context]])
  (:require [score_card.logic :as logic]
            [score_card.webif :as webif]
            [score_card.appif :as appif]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as resp]
            [ring.adapter.jetty :as ring]
            [ring.util.response :as ringutil]
            ))

;; session data store
(def session-data (atom {}))

(defn get-s-data [sid]
  (get @session-data sid nil))

(defn set-s-data! [sid cardid]
  (swap! session-data assoc sid cardid))

(defn release-s-data! [sid]
  (do
    (dissoc @session-data sid)
    (logic/release-card )))

;; disp
(defn display-score [cardnum holenum]
  (let [scorecard-info (logic/get-scorecard-info cardnum)]
    (webif/display-card scorecard-info holenum)))

(defn display-board [cardnum]
  (let [boardinfo (logic/get-board-info cardnum)]
    (webif/display-board boardinfo)))

(defn display-ranking [cardnum]
  (let [rankinfo (logic/get-ranking-info cardnum)]
    (webif/display-rank rankinfo)))

(defn admin-tool [intext]
  (webif/display-admin-tool intext))

;; routes

(defroutes routes
  (context "/web/:request" [request]
           (GET "/" {session :session param :params}
;                (println "parms ->" param " session->" session " request->" request)
                (cond (or (= request "login")
                          (empty? session))
                      ,,(webif/login)
                      (or (= request "card")
                          (= (:select param) "ScoreCard"))
                      ,,(display-score (Integer/parseInt (:cardnum param))
                                       (Integer/parseInt (str (:holenum param))))
                      (or (= request "board")
                          (= (:select param) "ScoareBoard"))
                      ,,(display-board (:cardnum session))
                      (or (= request "rank")
                          (= (:select param) "RankingBoard"))
                      ,,(display-ranking (:cardnum session))                      ))
           (POST "/" {params :params session :session}
                 (cond (= request "shots")
                       ,, (let [cardnum (Integer/parseInt (:cardnum params))
                                holenum (Integer/parseInt (:holenum params))
                                shots   (Integer/parseInt (:shots params))]
                            (if (logic/get-card-data cardnum)
                              (logic/set-new-score cardnum holenum shots 0))
                            (display-score cardnum (if (>= holenum 18) 18 (inc holenum))))
                       :t
                       ,, (let [cardnum (Integer/parseInt (:cardnum params))]
                            (if (logic/get-card-data cardnum)
                              (let [sessionid (logic/new-sessionid cardnum)]
                                (do (set-s-data! sessionid cardnum)
                                    (-> (resp/render (webif/selection cardnum) nil)
                                        (assoc ,, :session {:cardnum sessionid}))))
                              (ringutil/redirect "/web/login")))))
           )
  (context "/admin/:request" [request]
           (GET "/" {params :params}
                (if (= request "yosi")
                  (admin-tool "")))
           (POST "/" {params :params}
                 (admin-tool (logic/run-sqlcommand (vector (:input params))))))
  (route/resources "/")
  (route/files "/")
  (route/not-found "NOT FOUND"))

(def my-site
  (handler/site routes))

;; for on the fly
;; (use 'ring.util.serve)
;; (serve my-site)
;; (stop-server)

(defn -main [port]
  (ring/run-jetty my-site {:port (Integer/parseInt port) :join? false}))


