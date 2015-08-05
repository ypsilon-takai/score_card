(ns score_card.dbif
  (:use [score_card.sqldata.sqldata])
  (:require [clojure.java.jdbc :as sql]))

(def table-def "resources/dbdata/table_def.sql")
(def initial-data "resources/dbdata/init_data.sql")

(defn- get-dbspec []
  (or (System/getenv "DATABASE_URL")           ; for heroku
      {:classname "org.postgresql.Driver"
       :subprotocol "postgresql"
       :subname "//localhost:5432/GolfTest"
       :user "postgres"
       :password "postgres"})) ; for dev

;;;;;;;;;;;;;;;;
;; init funcs
(defn create-tables []
  (sql/with-connection (get-dbspec)
    (sql/do-commands (slurp table-def))))

(defn init-data [filename]
  (sql/with-connection (get-dbspec)
    (sql/do-commands (slurp initial-data))))

(defn run-sqlcommand [input]
  (sql/with-connection (get-dbspec)
    (sql/with-query-results result
      input 
      (into [] result ))))

;;;;;;;;;;;;;;;
;; retrieve something from db

(defn get-scorecard-data [card-id]
  "returns map:
   {:personid <num>, :fullname <str>, :nickname <str>, :hdcp <num>,
    :groupid <str>, :groupuname <str>,
    :compeid <str>, :compename <str>, :daydate <Date>,
    :siteid <num> :sitename <str>}"
  (sql/with-connection (get-dbspec)
    (sql/with-query-results result
       [(select-scorecard-info card-id)] 
       (assoc (first (into [] result)) :cardid card-id))))

(defn get-group-list [compeid]
  (sql/with-connection (get-dbspec)
    (sql/with-query-results result
      ["select group_id,group_name from roundgroup where compe_id = ?" compeid] 
      (into [] result ))))

(defn get-group-member [gid]
    (sql/with-connection (get-dbspec)
    (sql/with-query-results result
      ["select id from score where group_id = ?" gid] 
      (into [] (map :id result) ))))

(defn get-group-info [gid]
    (sql/with-connection (get-dbspec)
    (sql/with-query-results result
      ["select * from score where group_id = ?" gid] 
      (into {} result))))


(defn get-compe-attendee [compeid]
  "returs info of compe attendee."
  (sql/with-connection (get-dbspec)
    (sql/with-query-results result
      ["SELECT person.nick_name,person.hdcp,score.id as cardid
         FROM score,person,compe,roundgroup
         WHERE 
           score.player = person.p_id AND
           score.group_id = roundgroup.group_id AND
           roundgroup.compe_id = compe.compe_id AND
           roundgroup.compe_id = ?;" compeid]
      (into [] result))))

(defn str-to-numv [str]
  (vec (map #(Integer/parseInt %1) (.split str "," -1))))

(defn get-site-info [site-id]
  "returns map:
   {:name <str>,
    :holepar_out [<1-9>], :holepar_in [<10-18>],
    :hdcp_out [<1-9>], :hdcp_in [<10-18>],
    :length_out [<1-9>], :length_in [<10-18>],
    :l_length_out [<1-9>], :l_length_in [<10-18>],
    :s_length_out [<1-9>], :s_length_in [<10-18>],"
  (sql/with-connection (get-dbspec)
    (sql/with-query-results result
      ["select * from siteinfo where id = ?" site-id]
      (let [r (first result)]
        (loop [entries (seq r) res {}]
          (if (empty? entries)
            res
            (let [[tag val] (first entries)]
              (recur (next entries)
                     (assoc res tag (if (some #(= tag %) [:id :name])
                                      val
                                      (str-to-numv val)))))))))))

(defn get-score [card-id]
  "retuns vector:
  [[shot put] [shot put] ... [x x]]"
  (sql/with-connection (get-dbspec)
    (sql/with-query-results result
      ["select hole_num,total,putt from score_hole where score_card = ?" card-id]
      (loop [hdata (into [] result) res (vec (repeat 18 [0 0]))]
        (if (empty? hdata)
          res
          (recur (next hdata)
                 (assoc res
                   (dec (:hole_num  (first hdata)))
                   [(:total (first hdata)) (:putt (first hdata))])))))))



;;;;;;;;;;;;;;;;;;;;
;; put somthing to db
(defn put-score [card-id hole-num total putt]
  (sql/with-connection (get-dbspec)
    (sql/insert-records
     "score_hole"
     {:score_card card-id, :hole_num hole-num, :total total, :putt putt})))