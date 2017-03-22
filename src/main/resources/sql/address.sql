alter table T_ROFH_FULL_ADDRESS add related_project_cuid varchar2(100) ;
-- Create table
create table t_attemp_T_ROFH_FULL_ADDRESS
(
  label_cn                 VARCHAR2(255),
  province                 VARCHAR2(255),
  city                     VARCHAR2(255),
  county                   VARCHAR2(255),
  town                     VARCHAR2(255),
  villages                 VARCHAR2(255),
  building                 VARCHAR2(255),
  unit_no                  VARCHAR2(255),
  floor_no                 VARCHAR2(255),
  room_no                  VARCHAR2(255),
  longitude                NUMBER(19,6),
  latitude                 NUMBER(19,6),
  abbreviation             VARCHAR2(255),
  pinyin                   VARCHAR2(255),
  postcode                 VARCHAR2(16),
  approve_result           VARCHAR2(255),
  related_community_cuid   VARCHAR2(255),
  related_bmclasstype_cuid VARCHAR2(255),
  cuid                     VARCHAR2(172),
  village_alias            VARCHAR2(255),
  road_number              VARCHAR2(255),
  road                     VARCHAR2(255),
  community                VARCHAR2(255),
  last_modify_time         DATE,
  create_time              DATE,
  spellabbreviation        VARCHAR2(100),
  flag                     NUMBER(2) default 0,
  address_level            VARCHAR2(255),
  object_id                NUMBER(12) not null,
  regiontype2              VARCHAR2(255),
  regiontype1              VARCHAR2(255),
  related_project_cuid     varchar2(100)
);
-- Create/Recreate indexes 
create index attemp_T_FULL_ADDRESS_COUNTY on t_attemp_T_ROFH_FULL_ADDRESS (COUNTY);
create index attemp_T_FULL_ADDRESS_NAME on t_attemp_T_ROFH_FULL_ADDRESS (LABEL_CN);
create index attemp_T_FULL_ADDRESS_PY on t_attemp_T_ROFH_FULL_ADDRESS (PINYIN);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_attemp_T_ROFH_FULL_ADDRESS
  add constraint attemp_PK_T_FULL_ADDRESS primary key (OBJECT_ID)
  using index ;
alter table t_attemp_T_ROFH_FULL_ADDRESS
  add constraint attemp_UNIQUE_1T_FULL_ADDRESS unique (CUID)
  using index ;

  
  -- Create table
create table t_his_T_ROFH_FULL_ADDRESS
(
  label_cn                 VARCHAR2(255),
  province                 VARCHAR2(255),
  city                     VARCHAR2(255),
  county                   VARCHAR2(255),
  town                     VARCHAR2(255),
  villages                 VARCHAR2(255),
  building                 VARCHAR2(255),
  unit_no                  VARCHAR2(255),
  floor_no                 VARCHAR2(255),
  room_no                  VARCHAR2(255),
  longitude                NUMBER(19,6),
  latitude                 NUMBER(19,6),
  abbreviation             VARCHAR2(255),
  pinyin                   VARCHAR2(255),
  postcode                 VARCHAR2(16),
  approve_result           VARCHAR2(255),
  related_community_cuid   VARCHAR2(255),
  related_bmclasstype_cuid VARCHAR2(255),
  cuid                     VARCHAR2(172),
  village_alias            VARCHAR2(255),
  road_number              VARCHAR2(255),
  road                     VARCHAR2(255),
  community                VARCHAR2(255),
  last_modify_time         DATE,
  create_time              DATE,
  spellabbreviation        VARCHAR2(100),
  flag                     NUMBER(2) default 0,
  address_level            VARCHAR2(255),
  object_id                NUMBER(12) not null,
  regiontype2              VARCHAR2(255),
  regiontype1              VARCHAR2(255),
  related_project_cuid     varchar2(100)
);
-- Create/Recreate indexes 
create index his_T_FULL_ADDRESS_COUNTY on t_his_T_ROFH_FULL_ADDRESS (COUNTY);
create index his_T_FULL_ADDRESS_NAME on t_his_T_ROFH_FULL_ADDRESS (LABEL_CN);
create index his_T_FULL_ADDRESS_PY on t_his_T_ROFH_FULL_ADDRESS (PINYIN);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_his_T_ROFH_FULL_ADDRESS
  add constraint his_PK_T_FULL_ADDRESS primary key (OBJECT_ID)
  using index ;
alter table t_his_T_ROFH_FULL_ADDRESS
  add constraint his_UNIQUE_1T_FULL_ADDRESS unique (CUID)
  using index ;
