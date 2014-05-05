# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table action (
  id                        integer not null,
  startdate                 timestamp,
  agent_username            varchar(255),
  description               varchar(255),
  incident_id               integer,
  constraint pk_action primary key (id))
;

create table address (
  id                        integer not null,
  address1                  varchar(255),
  address2                  varchar(255),
  city                      varchar(255),
  state                     varchar(255),
  zipcode                   varchar(255),
  company_id                integer,
  constraint pk_address primary key (id))
;

create table agent (
  username                  varchar(255) not null,
  password                  varchar(255),
  fullname                  varchar(255),
  email                     varchar(255),
  active                    boolean,
  constraint pk_agent primary key (username))
;

create table category (
  id                        integer not null,
  type                      varchar(255),
  constraint pk_category primary key (id))
;

create table company (
  id                        integer not null,
  name                      varchar(255),
  notes                     varchar(255),
  website                   varchar(255),
  active                    boolean,
  constraint pk_company primary key (id))
;

create table contact (
  id                        integer not null,
  email                     varchar(255),
  fullname                  varchar(255),
  phone                     varchar(255),
  active                    boolean,
  company_id                integer,
  constraint pk_contact primary key (id))
;

create table incident (
  id                        integer not null,
  owner_username            varchar(255),
  category_id               integer,
  subject                   varchar(255),
  description               varchar(255),
  startdate                 timestamp,
  enddate                   timestamp,
  priority                  integer,
  status                    varchar(255),
  requester_id              integer,
  constraint pk_incident primary key (id))
;

create sequence action_seq;

create sequence address_seq;

create sequence agent_seq;

create sequence category_seq;

create sequence company_seq;

create sequence contact_seq;

create sequence incident_seq;

alter table action add constraint fk_action_agent_1 foreign key (agent_username) references agent (username) on delete restrict on update restrict;
create index ix_action_agent_1 on action (agent_username);
alter table action add constraint fk_action_incident_2 foreign key (incident_id) references incident (id) on delete restrict on update restrict;
create index ix_action_incident_2 on action (incident_id);
alter table address add constraint fk_address_company_3 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_address_company_3 on address (company_id);
alter table contact add constraint fk_contact_company_4 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_contact_company_4 on contact (company_id);
alter table incident add constraint fk_incident_owner_5 foreign key (owner_username) references agent (username) on delete restrict on update restrict;
create index ix_incident_owner_5 on incident (owner_username);
alter table incident add constraint fk_incident_category_6 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_incident_category_6 on incident (category_id);
alter table incident add constraint fk_incident_requester_7 foreign key (requester_id) references contact (id) on delete restrict on update restrict;
create index ix_incident_requester_7 on incident (requester_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists action;

drop table if exists address;

drop table if exists agent;

drop table if exists category;

drop table if exists company;

drop table if exists contact;

drop table if exists incident;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists action_seq;

drop sequence if exists address_seq;

drop sequence if exists agent_seq;

drop sequence if exists category_seq;

drop sequence if exists company_seq;

drop sequence if exists contact_seq;

drop sequence if exists incident_seq;

