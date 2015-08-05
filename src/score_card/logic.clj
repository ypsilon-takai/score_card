(ns score_card.logic
  (:require [score_card.dbif :as dbif]))

;; cashe
(def cards (atom {}))

;; session-id
;;  temprary just returns arg 
(defn new-sessionid [seed]
  seed)

(defn get-cardnum [sessionid]
  sessionid)

;; card data
(defn create-new-card []
  false)

(defn release-card [cardid]
  (swap! cards dissoc cardid))

(defn get-card-data [cardid]
  (or (get @cards cardid false)
      (if-let [cardinfo (dbif/get-scorecard-data cardid)]
        (do (swap!  cards assoc cardid cardinfo)
            cardinfo)
        false)))

;; card info
;;  get datas for display score card
(defn get-score [cardid]
  (dbif/get-score cardid))

(defn get-scorecard-info [cardid]
  (let [carddata (get-card-data cardid)
        nickname (:nickname carddata)
        score-list (dbif/get-score cardid)
        site-data (dbif/get-site-info (:siteid carddata))
        buddy-scores (for [cdid (dbif/get-group-member (:groupid carddata))
                           :when (not= cdid cardid)]
                       (let [name (:nickname (get-card-data cdid))
                             score (dbif/get-score cdid)]
                         [name score]))]
    {:cardid cardid
     :nickname nickname
     :siteinfo site-data
     :score-list score-list
     :buddyscores buddy-scores}))


(defn get-board-info [cardid]
  (let [carddata (get-card-data cardid)
        site-data (dbif/get-site-info (:siteid carddata))
        grouplist (dbif/get-group-list (:compeid carddata))
        groupscores (for [gdata grouplist]
                      [(:group_name gdata)
                       (for [cdid (dbif/get-group-member (:group_id gdata))]
                         [(:nickname (get-card-data cdid))
                          (dbif/get-score cdid)])])]
    {:compename (:compename carddata)
     :daydate (.toString (:daydate carddata))
     :siteinfo site-data
     :groupscores groupscores}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ranking board

(defn convert-to-parbase-l [score-list hole-par]
  (map #(vector (- (first %1) %2) (second %1))
       score-list hole-par))

(defn convert-to-parbase-s [score-list hole-par]
  (map #(if (nil? %1) %1 (- %1 %2))
       score-list hole-par))


(defn hdcped-score [score-list person-hdcp hole-hdcp-list hole-par-list]
  (let [p-hdcp-base (int (/ person-hdcp 18))
        p-hdcp-rem (rem person-hdcp 18)]
    (map (fn [[shot put] h-hdcp par]
           (if (zero? shot)
             nil
             (- (if (<= h-hdcp p-hdcp-rem)
                  (- shot 1)
                  shot)
                p-hdcp-base
                par)))
         score-list hole-hdcp-list hole-par-list)))


(defn ranking-add-scores [hole-par-list hole-hdcp-list attendee-list]
  (map (fn [attendee-info]
         (let [score-list (dbif/get-score (:cardid attendee-info))
               person-hdcp (:hdcp attendee-info)
               hdcpscore (hdcped-score score-list person-hdcp hole-hdcp-list hole-par-list)]
           (assoc attendee-info
             :scorelist score-list
             :hdcped-score hdcpscore
             :hd-total (reduce #(if %2 (+ %1 %2) %1) 0 hdcpscore))))
       attendee-list))

(defn get-ranking-info [cardid]
  (let [carddata (get-card-data cardid)
        site-data (dbif/get-site-info (:siteid carddata))
        hole-par (concat (:holepar_out site-data) (:holepar_in site-data))
        hole-hdcp (concat (:hdcp_out site-data) (:hdcp_in site-data))
        score-ranking (->> (dbif/get-compe-attendee (:compeid carddata))
                           (ranking-add-scores hole-par hole-hdcp ,,)
                           (sort #(compare (:hd-total %1) (:hd-total %2)) ,,))]
    {:compename (:compename carddata)
     :daydate (.toString (:daydate carddata))
     :siteinfo site-data
     :ranking score-ranking}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; input

(defn set-new-score [cardid hole shots putts]
  (dbif/put-score cardid hole shots putts))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; admintool

(defn run-sqlcommand [sqlcmd]
  (try
    (dbif/run-sqlcommand sqlcmd)
    (catch Exception e (.toString e))))