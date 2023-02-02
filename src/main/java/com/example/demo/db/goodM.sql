SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS diary;
DROP TABLE IF EXISTS like_list;
DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS general_board;
DROP TABLE IF EXISTS info_board;
DROP TABLE IF EXISTS map;
DROP TABLE IF EXISTS test_result;
DROP TABLE IF EXISTS users;




/* Create Tables */

CREATE TABLE diary
(
	did int NOT NULL AUTO_INCREMENT,
	uid varchar(20) NOT NULL,
	title varchar(128) NOT NULL,
	content varchar(128),
	modtime datetime NOT NULL,
	isDeleted int NOT NULL,
	files varchar(100),
	PRIMARY KEY (did)
);


CREATE TABLE general_board
(
	genbid int NOT NULL AUTO_INCREMENT,
	uid varchar(20) NOT NULL,
	title varchar(128) NOT NULL,
	content varchar(128),
	modtime datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
	viewCount int DEFAULT 0 NOT NULL,
	replyCount int DEFAULT 0 NOT NULL,
	isDeleted int DEFAULT 0 NOT NULL,
	files varchar(100),
	PRIMARY KEY (genbid)
);


CREATE TABLE info_board
(
	infobid int NOT NULL AUTO_INCREMENT,
	uid varchar(20) NOT NULL,
	title varchar(128) NOT NULL,
	content varchar(128),
	modtime datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
	viewCount int DEFAULT 0 NOT NULL,
	isDeleted int DEFAULT 0 NOT NULL,
	files varchar(100),
	PRIMARY KEY (infobid)
);


CREATE TABLE like_list
(
	likeCount int DEFAULT 0 NOT NULL,
	uid varchar(20) NOT NULL,
	genbid int NOT NULL,
	infobid int NOT NULL,
	PRIMARY KEY (likeCount)
);


CREATE TABLE map
(
	orgName varchar(50) NOT NULL,
	orgType varchar(50) NOT NULL,
	addr varchar(200) NOT NULL,
	homepage varchar(200)
);


CREATE TABLE reply
(
	rid int NOT NULL AUTO_INCREMENT,
	content varchar(128) NOT NULL,
	regDate datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
	isMine int NOT NULL,
	uid varchar(20) NOT NULL,
	genbid int NOT NULL,
	PRIMARY KEY (rid)
);


CREATE TABLE test_result
(
	uid varchar(20) NOT NULL,
	result varchar(200) NOT NULL
);


CREATE TABLE users
(
	uid varchar(20) NOT NULL,
	pwd char(60) NOT NULL,
	uname varchar(20) NOT NULL,
	email varchar(32),
	regDate datetime DEFAULT (CURRENT_DATE),
	isDeleted int DEFAULT 0 NOT NULL,
	PRIMARY KEY (uid)
);



/* Create Foreign Keys */

ALTER TABLE like_list
	ADD FOREIGN KEY (genbid)
	REFERENCES general_board (genbid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE reply
	ADD FOREIGN KEY (genbid)
	REFERENCES general_board (genbid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE like_list
	ADD FOREIGN KEY (infobid)
	REFERENCES info_board (infobid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE diary
	ADD FOREIGN KEY (uid)
	REFERENCES users (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE general_board
	ADD FOREIGN KEY (uid)
	REFERENCES users (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE like_list
	ADD FOREIGN KEY (uid)
	REFERENCES users (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE reply
	ADD FOREIGN KEY (uid)
	REFERENCES users (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE test_result
	ADD FOREIGN KEY (uid)
	REFERENCES users (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;