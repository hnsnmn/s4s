create table FILE_METADATA (
        FILE_METADATA_ID int not null,
        SERVICE_ID varchar(100),
        FILE_NAME varchar(200) not null,
        FILE_LENGTH int,
        MIMETYPE varchar(250) not null,
        UPLOAD_TIME date not null,
        FILE_ID varchar(200) not null
);

ALTER TABLE FILE_METADATA add CONSTRAINT FILE_METADATA_PK PRIMARY KEY(FILE_METADATA_ID);

CREATE unique INDEX FILE_METADATA_FILE_ID ON FILE_METADATA (FILE_ID);

create sequence FILE_METADATA_SEQ start with 1 increment by 1;