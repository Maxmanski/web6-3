# --- !Ups

create table if not exists user (
  username                  varchar(255) not null,
  password                  varchar(255),
  firstname                 varchar(255),
  lastname                  varchar(255),
  birthdate                 varchar(255),
  gender                    integer,
  avatarid                  varchar(255),
  constraint ck_user_gender check (gender in (0,1)),
  constraint pk_user primary key (username))
;

create sequence user_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists user_seq;

