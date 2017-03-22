alter table gpon_cover add related_project_cuid varchar2(100) ;
-- Create table
create table t_attemp_GPON_COVER
(
  objectid              NUMBER(19),
  create_time           DATE,
  last_modify_time      DATE,
  gt_version            NUMBER(19),
  isdelete              NUMBER(19),
  build_name            VARCHAR2(255),
  project_no            VARCHAR2(255),
  ontinaddr             VARCHAR2(255),
  remark                VARCHAR2(255),
  use_property          VARCHAR2(100),
  cover_range           VARCHAR2(255),
  device_type           VARCHAR2(50),
  longitude             NUMBER(19,6),
  pro_right             VARCHAR2(100),
  related_ne_cuid       VARCHAR2(255),
  related_district_cuid VARCHAR2(255),
  maincomp              VARCHAR2(50),
  latitude              NUMBER(19,6),
  device_name           VARCHAR2(255),
  cuid                  VARCHAR2(172),
  cover_info            VARCHAR2(255),
  standard_addr         VARCHAR2(255),
  is_valid              VARCHAR2(2),
  valid_reason          VARCHAR2(512),
  regiontype1           VARCHAR2(255),
  regiontype2           VARCHAR2(255),
  related_cab_cuid      VARCHAR2(255),
  business_type         NUMBER(1),
  related_project_cuid     varchar2(100)
);
-- Create/Recreate indexes 
create index attemp_GPON_COVER_ADDR on t_attemp_GPON_COVER (STANDARD_ADDR);
create index attemp_GPON_COVER_INDEX2 on t_attemp_GPON_COVER (RELATED_NE_CUID);
create index attemp_GPON_COVER_INDEX3 on t_attemp_GPON_COVER (RELATED_NE_CUID, STANDARD_ADDR);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_attemp_GPON_COVER
  add constraint attemp_UNIQUE_1GPON_COVER unique (CUID)
  using index ;
  
  -- Create table
create table t_his_GPON_COVER
(
  objectid              NUMBER(19),
  create_time           DATE,
  last_modify_time      DATE,
  gt_version            NUMBER(19),
  isdelete              NUMBER(19),
  build_name            VARCHAR2(255),
  project_no            VARCHAR2(255),
  ontinaddr             VARCHAR2(255),
  remark                VARCHAR2(255),
  use_property          VARCHAR2(100),
  cover_range           VARCHAR2(255),
  device_type           VARCHAR2(50),
  longitude             NUMBER(19,6),
  pro_right             VARCHAR2(100),
  related_ne_cuid       VARCHAR2(255),
  related_district_cuid VARCHAR2(255),
  maincomp              VARCHAR2(50),
  latitude              NUMBER(19,6),
  device_name           VARCHAR2(255),
  cuid                  VARCHAR2(172),
  cover_info            VARCHAR2(255),
  standard_addr         VARCHAR2(255),
  is_valid              VARCHAR2(2),
  valid_reason          VARCHAR2(512),
  regiontype1           VARCHAR2(255),
  regiontype2           VARCHAR2(255),
  related_cab_cuid      VARCHAR2(255),
  business_type         NUMBER(1),
  related_project_cuid     varchar2(100)
);
-- Create/Recreate indexes 
create index his_GPON_COVER_ADDR on t_his_GPON_COVER (STANDARD_ADDR);
create index his_GPON_COVER_INDEX2 on t_his_GPON_COVER (RELATED_NE_CUID);
create index his_GPON_COVER_INDEX3 on t_his_GPON_COVER (RELATED_NE_CUID, STANDARD_ADDR);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_his_GPON_COVER
  add constraint his_UNIQUE_1GPON_COVER unique (CUID)
  using index ;

