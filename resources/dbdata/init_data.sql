
insert into siteinfo (id, name, holepar_in, holepar_out)
values(1,'水戸レイクス','5,4,3,4,4,5,3,4,4','5,3,4,4,4,3,4,5,4');

insert into siteinfo (id, name, 
                      holepar_out, holepar_in, 
                      hdcp_out, hdcp_in, 
                      length_out, length_in, 
                      l_length_out, l_length_in, 
                      s_length_out, s_length_in )
values(2,'館山カントリークラブ 東西',
          '5,4,4,3,4,3,4,4,5', '5,4,3,4,3,4,5,4,4',
	  '8,2,14,10,6,18,12,16,4','11,13,3,7,17,5,1,15,9',
          '511,364,352,153,357,127,319,314,505','513,388,201,386,115,345,534,370,402',
          '526,430,377,166,382,136,345,335,559','532,406,221,401,132,360,555,394,418',
          '420,260,295,125,314,110,294,264,395','438,272,136,292,107,335,413,275,347');

insert into siteinfo (id, name, 
                      holepar_out, holepar_in, 
                      hdcp_out, hdcp_in, 
                      length_out, length_in, 
                      l_length_out, l_length_in, 
                      s_length_out, s_length_in )
values(3,'館山カントリークラブ 中東',
          '4,4,3,5,3,5,4,4,4','5,4,4,3,4,3,4,4,5',
          '9,3,13,7,17,1,11,15,5','8,2,14,10,6,18,12,16,4',
          '352,263,141,519,119,548,354,288,365','511,364,352,153,357,127,319,314,505',
          '367,273,154,537,132,565,367,298,378','526,430,377,166,382,136,345,335,559',
          '315,253,100,408,110,410,300,267,315,2478','420,260,295,125,314,110,294,264,395');



insert into compe (compe_id, compe_name, site_id, daydate)
values(100, '第10回 本田杯 キャンセル', 1, 'March 10, 2012');

insert into compe (compe_id, compe_name, site_id, daydate)
values(101, '第10回 本田杯 1日目', 2, 'June 9, 2012');

insert into compe (compe_id, compe_name, site_id, daydate)
values(102, '第10回 本田杯 2日目', 3, 'June 10, 2012');

insert into compe (compe_id, compe_name, site_id, daydate)
values(1, 'テスト コンペ', 1, 'Feb 10, 2012');


insert into person (p_id, name, nick_name, hdcp)
values(1, '本田 博之', '本田', 24)
;
insert into person (p_id, name, nick_name, hdcp)
values(2, '田上', '田上', 8)
;
insert into person (p_id, name, nick_name, hdcp)
values(3, '沼田', '沼田', 19)
;
insert into person (p_id, name, nick_name, hdcp)
values(4, '関根', '関根', 6)
;
insert into person (p_id, name, nick_name, hdcp)
values(5, '新井 健', '新井', 16)
;
insert into person (p_id, name, nick_name, hdcp)
values(6, '柏原 達', '柏原', 24)
;
insert into person (p_id, name, nick_name, hdcp)
values(7, '服部 半蔵', '服部', 24)
;
insert into person (p_id, name, nick_name, hdcp)
values(8, '神田 正樹', '神田', 27)
;
insert into person (p_id, name, nick_name, hdcp)
values(9, '高井 克孝', '高井', 30)
;
insert into person (p_id, name, nick_name, hdcp)
values(10, '中野 サンプラザ', '中野', 40)
;
insert into person (p_id, name, nick_name, hdcp)
values(11, '加藤 清正', '加藤清', 40)
;
insert into person (p_id, name, nick_name, hdcp)
values(12, '高木 ブー', '高木', 40)
;
insert into person (p_id, name, nick_name, hdcp)
values(13, '高岡 早紀', '高岡', 40)
;


insert into roundgroup (group_id, group_name, compe_id)
values(101, '1組', 100)
;
insert into roundgroup (group_id, group_name, compe_id)
values(102, '2組', 100)
;
insert into roundgroup (group_id, group_name, compe_id)
values(103, '3組', 100)
;
insert into roundgroup (group_id, group_name, compe_id)
values(104, '4組', 100)
;


insert into roundgroup (group_id, group_name, compe_id)
values(105, '1組', 101)
;
insert into roundgroup (group_id, group_name, compe_id)
values(106, '2組', 101)
;
insert into roundgroup (group_id, group_name, compe_id)
values(107, '3組', 101)
;
insert into roundgroup (group_id, group_name, compe_id)
values(108, '4組', 101)
;


insert into roundgroup (group_id, group_name, compe_id)
values(109, '1組', 102)
;
insert into roundgroup (group_id, group_name, compe_id)
values(110, '2組', 102)
;
insert into roundgroup (group_id, group_name, compe_id)
values(111, '3組', 102)
;
insert into roundgroup (group_id, group_name, compe_id)
values(112, '4組', 102)
;

insert into roundgroup (group_id, group_name, compe_id)
values(1, 'Test 1組', 1)
;
insert into roundgroup (group_id, group_name, compe_id)
values(2, 'Test 2組', 1)
;


insert into score (id, group_id, player)
values(1, 101, 6)  
;
insert into score (id, group_id, player)
values(2, 101, 13)  
;
insert into score (id, group_id, player)
values(3, 101, 8)  
;
insert into score (id, group_id, player)
values(4, 102, 1)  
;
insert into score (id, group_id, player)
values(5, 102, 9)  
;
insert into score (id, group_id, player)
values(6, 102, 12)  
;
insert into score (id, group_id, player)
values(7, 103, 3)  
;
insert into score (id, group_id, player)
values(8, 103, 5)  
;
insert into score (id, group_id, player)
values(9, 103, 10)  
;
insert into score (id, group_id, player)
values(10, 104, 2)  
;
insert into score (id, group_id, player)
values(11, 104, 4)  
;
insert into score (id, group_id, player)
values(12, 104, 11)  
;
insert into score (id, group_id, player)
values(13, 104, 7)  
;


insert into score (id, group_id, player)
values(27, 105, 3)  
;
insert into score (id, group_id, player)
values(24, 105, 1)  
;
insert into score (id, group_id, player)
values(25, 105, 9)  
;
insert into score (id, group_id, player)
values(22, 105, 13)  
;

insert into score (id, group_id, player)
values(28, 106, 5)  
;
insert into score (id, group_id, player)
values(33, 106, 7)  
;
insert into score (id, group_id, player)
values(23, 106, 8)  
;
insert into score (id, group_id, player)
values(26, 106, 12)  
;

insert into score (id, group_id, player)
values(31, 107, 4)  
;
insert into score (id, group_id, player)
values(30, 107, 2)  
;
insert into score (id, group_id, player)
values(21, 107, 6)  
;
insert into score (id, group_id, player)
values(29, 107, 10)  
;



insert into score (id, group_id, player)
values(44, 109, 1)  
;
insert into score (id, group_id, player)
values(48, 109, 5)  
;
insert into score (id, group_id, player)
values(46, 109, 12)  
;
insert into score (id, group_id, player)
values(49, 109, 10)  
;

insert into score (id, group_id, player)
values(47, 110, 3)  
;
insert into score (id, group_id, player)
values(41, 110, 6)  
;
insert into score (id, group_id, player)
values(45, 110, 9)  
;
insert into score (id, group_id, player)
values(53, 110, 7)  
;


insert into score (id, group_id, player)
values(51, 111, 4)  
;
insert into score (id, group_id, player)
<<<<<<< HEAD
values(50, 111, 2)  
;
insert into score (id, group_id, player)
values(42, 111, 13)  
;
insert into score (id, group_id, player)
values(43, 111, 8)  
;


-- Test
-- select * from siteinfo;
-- select * from compe;
-- select * from person;
-- select * from roundgroup;
-- select * from score order by player;

