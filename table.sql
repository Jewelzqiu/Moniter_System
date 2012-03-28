CREATE TABLE radlab
(
cdate	date(yymmdd),
name	varchar(20),
in		varchar(8),
out		varchar(8) DEFAULT '00:00:00',
PRIMARY KEY (cdate, name)
)