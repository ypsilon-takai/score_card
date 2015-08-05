(ns score_card.sqldata.sqldata
  (:require [clojure.string :as string]))

(declare sql-scorecard-info sql-compe-info)

(defn select-scorecard-info [person-id]
  (string/replace sql-scorecard-info #"__SCOREID__" (str person-id)))

(defn select-site-info [site-id]
  (str "select * from siteinfo " "where id = " (str site-id)))

(def sql-scorecard-info
  "SELECT
  person.p_id as personid,
  person.name as fullname, 
  person.nick_name as nickname,
  person.hdcp,
  roundgroup.group_id as groupid, 
  roundgroup.group_name as groupuname, 
  compe.compe_id as compeid, 
  compe.compe_name as compename, 
  compe.daydate,
  siteinfo.id as siteid, 
  siteinfo.name as sitename
FROM 
  public.score, 
  public.person, 
  public.roundgroup, 
  public.compe, 
  public.siteinfo
WHERE 
  person.p_id = score.player AND
  roundgroup.group_id = score.group_id AND
  compe.compe_id = roundgroup.compe_id AND
  siteinfo.id = compe.site_id AND
  score.id = __SCOREID__;")

