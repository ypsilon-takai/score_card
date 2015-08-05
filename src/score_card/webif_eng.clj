(ns score_card.webif
  (:require [hiccup.form-helpers :as form] 
            [hiccup.page-helpers :as page]
            [hiccup.core :as hcore]
            [clojure.java.io :as io]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;  login
(defn login []
  (page/html5
   [:head
    [:title "Score card : Login"]
    (page/include-css "/css/common.css")]
   [:body {:class "login"}
    [:div {:id "content"}  "Input your score card number."] [:br]
    [:form {:action "/web/post" :method "POST" :enctype "multipart/form-data"}
     (form/text-field "cardnum")
     [:br]
     (form/submit-button "login")]]))

;; select display
(defn selection [cardnum]
  (page/html5
   [:head
    [:title "Score Card : Folder"]
    (page/include-css "/css/common.css")]
   [:body {:class "select"}
    [:form {:action "/web/select" :method "GET" :enctype "multipart/form-data"}
     [:p "Select what to be displayed."] [:br]
     (form/radio-button "select" true "ScoreCard") "Your score card."[:br]
     (form/label "holenuml" "Hole num to display: ")
     (form/text-field "holenum" 1) [:br]
     [:br][:br]
     (form/radio-button "select" false "ScoareBoard") "Score board of this compe."[:br]
     (form/hidden-field "cardnum" cardnum)
     [:br] [:br]
     (form/submit-button "set")]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; main windows

(defn- set-color [act par]
  (cond (= act 0) "None"
        (> (- act par) 2) "TrippleBogey"
        (= (- act par) 2) "DoubleBogey"
        (= (- act par) 1) "Bogey"
        (= act par) "Par"
        (= (- act par) -1) "Birdy"
        (< (- act par) -1) "Eagle"))

(defn- person-data-1 [name score par-out par-in]
  (let [[out in] (split-at 9 score)]
    (list
     [:tr {:class "outscores"}
      [:td {:class "name" } name]
      [:td {:class "subtotal" } (reduce #(+ %1 (first %2)) 0 out)]
      (map #(vector :td {:class (set-color %1 %2)} (if (= %1 0) "-" %1))
           (map first out)
           par-out)]
     [:tr {:class "inscores"}
      [:td {:class "total"} (reduce + (map first score))]
      [:td {:class "subtotal"} (reduce #(+ %1 (first %2)) 0 in)]
      (map #(vector :td {:class (set-color %1 %2)} (if (= %1 0) "-" %1))
           (map first in)
           par-in)])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; score board
(declare board-score-list board-head)

(defn display-board [board-info]
  (page/html5
   [:head
    [:meta {:http-equiv "Content-type"
            :content "text/html;charset=utf-8"}]
    [:title (str "Score Board: "(:compename board-info))]
    (page/include-css "/css/common.css")
    (page/include-css "/css/score_table.css")]
   [:body {:class "board"}
    [:table {:class "board"}
     (concat
      (board-head board-info)
      (board-score-list board-info))]]))

(defn board-score-list [{groupscores :groupscores siteinfo :siteinfo}]
  (for [[group-name score-list] groupscores]
    (list
     [:tr 
      [:td {:class "groupname"} group-name]]
     (for [[pname score] score-list]
      (person-data-1 pname score (:holepar_out siteinfo) (:holepar_in siteinfo) )))))

(defn board-head [boardinfo]
  (let [compe-name (:compename boardinfo)
        datestring (:daydate boardinfo)
        site-name (:name (:siteinfo boardinfo))
        par-out (:holepar_out (:siteinfo boardinfo))
        par-in (:holepar_in (:siteinfo boardinfo))]
    (list
     [:tr 
      [:td {:class "compename" :colspan 5} compe-name]
      [:td {:class "daydate" :colspan 6} datestring]]
     [:tr 
      [:td {:class "sitename" :colspan 11} site-name]]
     [:tr 
      [:td {:class "parlabel" :colspan 2} "Out:"]
      (map #(vector :td {:class "holepar"} %) par-out)]
     [:tr 
      [:td {:class "parlabel" :colspan 2} "In:"]
      (map #(vector :td {:class "holepar"} %) par-in)])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; score card
(declare score-card-body score-card-buddylist)

(defn display-card [cardinfo holenum]
  (page/html5
   [:head
    [:meta {:http-equiv "Content-type"
            :content "text/html;charset=utf-8"}]
    [:title "Score card : Scratch!"]
    (page/include-css "/css/common.css")
    (page/include-css "/css/score_card.css")
    (page/include-css "/css/score_table.css")
    (page/include-js "/js/scorecard.js")]
   [:body {:class "scorecard"}
    (score-card-body cardinfo holenum)
    (score-card-buddylist cardinfo)]))

(defn- score-card-body [carddata holenum]
  [:table {:class "input"}
   [:tr 
    [:td {:class "name" :colspan 2} (:nickname carddata)]
    [:td {:class "label" :colspan 4} ""]]
   [:form {:action "/web/shots" :method "POST" :enctype "multipart/form-data"}
    (form/hidden-field "cardnum" (:cardid carddata))
    (form/hidden-field "holenum" holenum)
    [:tr 
     [:td {:class "holenum"} (str "Hole: " holenum)]
     [:td {:class "holescore" :colspam 2 :rowspan 2}
      (let [score (first (nth (:score-list carddata) (dec holenum)))
            par (nth (concat (:holepar_out (:siteinfo carddata))
                             (:holepar_in (:siteinfo carddata)))
                     (dec holenum))
            disp-score (if (= 0 score) par score)]
        (form/text-field {:id "shots"} "shots" disp-score ))]
     [:td {:class "shotdec" :rowspan 2}
      [:input {:id "shotdec"
               :type "button" :name "shotdec"
               :onClick "ShotDown('shots')"
               :value "D"}]]
     [:td {:class "shotinc" :rowspan 2}
      [:input {:id "shotinc"
               :type "button" :name "shotinc"
               :onClick "ShotUp('shots')"
               :value "U"}]]
     [:td {:class "submit" :colspan 2 :rowspan 2}
      (form/submit-button {:id "submit"} "Set")]]]
   [:tr
    [:td {:class "holenum"}
     (str "PAR: "
          (nth (concat (:holepar_out (:siteinfo carddata))
                       (:holepar_in (:siteinfo carddata)))
               (dec holenum)))]]
   [:tr 
     [:form {:action "/web/card" :method "GET" :enctype "multipart/form-data"}
      (form/hidden-field "cardnum" (:cardid carddata))
      (form/hidden-field "holenum" (if (<= holenum 1) 1 (dec holenum) ))
      [:td {:class "prevbutton" :colspan 2}
      [:input {:class "prev"
               :type "submit" :name "prev" :value "prev"}]]]
     [:td {:class "label"} ""]
     [:td {:class "label"} ""]
     [:form {:action "/web/card" :method "GET" :enctype "multipart/form-data"}
      (form/hidden-field "cardnum" (:cardid carddata))
      (form/hidden-field "holenum" (if (>= holenum 18) 18 (inc holenum) ))
      [:td {:class "nextbutton" :colspan 2}
      [:input {:class "next"
               :type "submit" :name "next" :value "next"}]]]]])

(defn- score-card-buddylist [carddata]
  [:table {:class "board"}
   (let [scorelist (cons [(:nickname carddata) (:score-list carddata)]
                         (:buddyscores carddata))
         par-out (:holepar_out (:siteinfo carddata))
         par-in (:holepar_in (:siteinfo carddata)) ]
     (list
      [:tr 
       [:td {:class "holenumlist" :colspan 2} "Hole:"]
       (map #(vector :td {:class "holenum"} %) (range 1 10))]
      [:tr 
       [:td {:class "holenumlist" :colspan 2} ""]
       (map #(vector :td {:class "holenum"} %) (range 10 19))]
      (for [[pname score] scorelist]
        (person-data-1 pname score par-out par-in))
      [:tr 
       [:td {:class "parlabel" :colspan 2} "Out:"]
       (map #(vector :td {:class "holepar"} %) par-out)]
      [:tr 
       [:td {:class "parlabel" :colspan 2} "In:"]
       (map #(vector :td {:class "holepar"} %) par-in)]))])


;;;;;;;;;;;;;;;;;;;;;;;;;;
;; admin tool
(defn display-admin-tool [outtext]
  (page/html5
   [:head
    [:meta {:http-equiv "Content-type"
            :content "text/html;charset=utf-8"}]
    [:title "Score Board: ADMIN"]]
   [:body
    [:div {:class "adminput"
           :width "80%"}
     [:form {:action "/admin/yosi" :method "POST" :enctype "multipart/form-data"}
      (form/text-area {:width "100%"} "input" "")
      [:br]
      (form/submit-button "Exec")]]
    [:div {:class "admout"
           :width "80%" }
     [:p {:class "admout"
          :width "00%"}
      (hcore/escape-html outtext)]]]) )
