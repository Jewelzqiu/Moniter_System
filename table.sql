CREATE TABLE MEMBER_NAME
(
cdate	data(yymmdd),
in		varchar(8),
out		varchar(8) DEFAULT '00:00:00',
PRIMARY KEY (cdate)
)