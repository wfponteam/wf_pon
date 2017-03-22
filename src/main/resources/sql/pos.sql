-- Create table
create table t_attemp_AN_POS
(
  related_pon_cuid       VARCHAR2(172),
  idn                    VARCHAR2(255),
  objectid               NUMBER(38),
  create_time            DATE,
  related_upne_cuid      VARCHAR2(172),
  last_modify_time       DATE,
  gt_version             NUMBER(38),
  isdelete               NUMBER(38),
  is_permit_sys_del      NUMBER(1),
  fdn                    VARCHAR2(255),
  userlabel              VARCHAR2(255),
  related_template_name  VARCHAR2(60),
  abbreviation           VARCHAR2(60),
  related_ems_cuid       VARCHAR2(43),
  native_ems_name        VARCHAR2(255),
  live_cycle             NUMBER(19),
  use_state              NUMBER(19),
  model                  VARCHAR2(60),
  hard_version           VARCHAR2(60),
  soft_version           VARCHAR2(60),
  location               VARCHAR2(255),
  related_district_cuid  VARCHAR2(60),
  setup_time             DATE,
  seqno                  VARCHAR2(60),
  label_cn               VARCHAR2(255),
  related_room_cuid      VARCHAR2(37),
  related_site_cuid      VARCHAR2(37),
  related_vendor_cuid    VARCHAR2(46),
  real_longitude         NUMBER(38,6),
  real_latitude          NUMBER(38,6),
  longitude              NUMBER(38,6),
  latitude               NUMBER(38,6),
  ration                 VARCHAR2(60),
  related_olt_cuid       VARCHAR2(46),
  related_port_cuid      VARCHAR2(36),
  related_cab_cuid       VARCHAR2(200),
  remark                 VARCHAR2(255),
  label_dev              VARCHAR2(100),
  related_access_point   VARCHAR2(255),
  related_project_cuid   VARCHAR2(255),
  maint_mode             NUMBER(19),
  last_modify_user       VARCHAR2(60),
  creator_name           VARCHAR2(60),
  creattime              DATE,
  ownership              NUMBER(19) default 1,
  repair_time            DATE,
  preserver              VARCHAR2(100),
  standard_name          VARCHAR2(255),
  is_closenet            NUMBER(1) default 0,
  ownership_man          VARCHAR2(100),
  back_network_time      DATE,
  dev_cuid               VARCHAR2(60),
  access_type            NUMBER(19),
  livemodify_time        DATE,
  can_allocate_to_user   NUMBER(1) default 0,
  cuid                   VARCHAR2(172),
  install_location_type  NUMBER(19) default 0,
  pos_type               NUMBER(1),
  cover_info             VARCHAR2(255),
  cover_user_count       NUMBER(10),
  project_state          NUMBER(19),
  covertype              NUMBER(19),
  iswiring               NUMBER(10),
  construction           VARCHAR2(255),
  related_upne_port_cuid VARCHAR2(255),
  related_port2_cuid     VARCHAR2(255),
  data_quality_person    VARCHAR2(255),
  maint_person           VARCHAR2(255),
  rmuid                  VARCHAR2(255),
  construct_unit         VARCHAR2(255),
  sec_related_port_cuid  VARCHAR2(255)
);
-- Create/Recreate indexes 
create index attemp_AN_POS_CAB on t_attemp_AN_POS (RELATED_CAB_CUID);
create index attemp_INDEX_AN_POS_DEV_CUID on t_attemp_AN_POS (DEV_CUID);
create index attemp_INDEX_AN_POS_FDN on t_attemp_AN_POS (FDN);
create index attemp_INDEX_AN_POS_LABEL_CN on t_attemp_AN_POS (LABEL_CN);
create index attemp_INDEX_AN_POS_OLT on t_attemp_AN_POS (RELATED_OLT_CUID);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_attemp_AN_POS
  add constraint attemp_UNIQUE_1AN_POS unique (CUID)
  using index ;

  
-- Create table
create table t_his_AN_POS
(
  related_pon_cuid       VARCHAR2(172),
  idn                    VARCHAR2(255),
  objectid               NUMBER(38),
  create_time            DATE,
  related_upne_cuid      VARCHAR2(172),
  last_modify_time       DATE,
  gt_version             NUMBER(38),
  isdelete               NUMBER(38),
  is_permit_sys_del      NUMBER(1),
  fdn                    VARCHAR2(255),
  userlabel              VARCHAR2(255),
  related_template_name  VARCHAR2(60),
  abbreviation           VARCHAR2(60),
  related_ems_cuid       VARCHAR2(43),
  native_ems_name        VARCHAR2(255),
  live_cycle             NUMBER(19),
  use_state              NUMBER(19),
  model                  VARCHAR2(60),
  hard_version           VARCHAR2(60),
  soft_version           VARCHAR2(60),
  location               VARCHAR2(255),
  related_district_cuid  VARCHAR2(60),
  setup_time             DATE,
  seqno                  VARCHAR2(60),
  label_cn               VARCHAR2(255),
  related_room_cuid      VARCHAR2(37),
  related_site_cuid      VARCHAR2(37),
  related_vendor_cuid    VARCHAR2(46),
  real_longitude         NUMBER(38,6),
  real_latitude          NUMBER(38,6),
  longitude              NUMBER(38,6),
  latitude               NUMBER(38,6),
  ration                 VARCHAR2(60),
  related_olt_cuid       VARCHAR2(46),
  related_port_cuid      VARCHAR2(36),
  related_cab_cuid       VARCHAR2(200),
  remark                 VARCHAR2(255),
  label_dev              VARCHAR2(100),
  related_access_point   VARCHAR2(255),
  related_project_cuid   VARCHAR2(255),
  maint_mode             NUMBER(19),
  last_modify_user       VARCHAR2(60),
  creator_name           VARCHAR2(60),
  creattime              DATE,
  ownership              NUMBER(19) default 1,
  repair_time            DATE,
  preserver              VARCHAR2(100),
  standard_name          VARCHAR2(255),
  is_closenet            NUMBER(1) default 0,
  ownership_man          VARCHAR2(100),
  back_network_time      DATE,
  dev_cuid               VARCHAR2(60),
  access_type            NUMBER(19),
  livemodify_time        DATE,
  can_allocate_to_user   NUMBER(1) default 0,
  cuid                   VARCHAR2(172),
  install_location_type  NUMBER(19) default 0,
  pos_type               NUMBER(1),
  cover_info             VARCHAR2(255),
  cover_user_count       NUMBER(10),
  project_state          NUMBER(19),
  covertype              NUMBER(19),
  iswiring               NUMBER(10),
  construction           VARCHAR2(255),
  related_upne_port_cuid VARCHAR2(255),
  related_port2_cuid     VARCHAR2(255),
  data_quality_person    VARCHAR2(255),
  maint_person           VARCHAR2(255),
  rmuid                  VARCHAR2(255),
  construct_unit         VARCHAR2(255),
  sec_related_port_cuid  VARCHAR2(255)
);
-- Create/Recreate indexes 
create index his_AN_POS_CAB on t_his_AN_POS (RELATED_CAB_CUID);
create index his_INDEX_AN_POS_DEV_CUID on t_his_AN_POS (DEV_CUID);
create index his_INDEX_AN_POS_FDN on t_his_AN_POS (FDN);
create index his_INDEX_AN_POS_LABEL_CN on t_his_AN_POS (LABEL_CN);
create index his_INDEX_AN_POS_OLT on t_his_AN_POS (RELATED_OLT_CUID);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_his_AN_POS
  add constraint his_UNIQUE_1AN_POS unique (CUID)
  using index ;