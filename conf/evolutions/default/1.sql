# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table agent (
  username                  varchar(255) not null,
  password                  varchar(255),
  firstname                 varchar(255),
  lastname                  varchar(255),
  email                     varchar(255),
  constraint pk_agent primary key (username))
;

create sequence agent_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists agent;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists agent_seq;

