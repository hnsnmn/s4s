create database s4smeta CHARACTER SET=utf8;

CREATE USER 's4smetauser'@'localhost' IDENTIFIED BY 's4smetapw';
GRANT ALL PRIVILEGES ON s4smeta.* TO 's4smetauser'@'localhost';

CREATE USER 's4smetauser'@'%' IDENTIFIED BY 's4smetapw';
GRANT ALL PRIVILEGES ON s4smeta.* TO 's4smetauser'@'%';

-- FILE METADATA
create table FILE_METADATA (
	FILE_METADATA_ID int not null auto_increment,
	SERVICE_ID varchar(100),
	FILE_NAME varchar(200) not null,
	FILE_LENGTH int,
	MIMETYPE varchar(250) not null,
	UPLOAD_TIME datetime not null,
	FILE_ID varchar(200) not null,
	constraint FILE_METADATA primary key (FILE_METADATA_ID),
	unique index FILE_METADATA_FILE_ID (FILE_ID)
) ENGINE=InnoDB CHARACTER SET=utf8;
