-- Create table
create table T_ATTEMP_AN_ONU
(
  objectid               NUMBER(38),
  idn                    VARCHAR2(255),
  create_time            DATE,
  related_upne_cuid      VARCHAR2(172),
  last_modify_time       DATE,
  gt_version             NUMBER(38),
  isdelete               NUMBER(38),
  is_permit_sys_del      NUMBER(1) default 0,
  fdn                    VARCHAR2(255),
  state                  NUMBER(19),
  userlabel              VARCHAR2(255),
  related_template_name  VARCHAR2(60),
  abbreviation           VARCHAR2(60),
  capacity               NUMBER(19),
  related_ems_cuid       VARCHAR2(43),
  native_ems_name        VARCHAR2(255),
  live_cycle             NUMBER(19),
  use_state              NUMBER(19),
  model                  VARCHAR2(60),
  hard_version           VARCHAR2(60),
  soft_version           VARCHAR2(60),
  location               VARCHAR2(255),
  related_district_cuid  VARCHAR2(60),
  related_upne_port_cuid VARCHAR2(50),
  setup_time             DATE,
  seqno                  VARCHAR2(60),
  related_pon_port_cuid  VARCHAR2(70),
  label_cn               VARCHAR2(255),
  related_vendor_cuid    VARCHAR2(46),
  dev_ip                 VARCHAR2(25),
  fttx                   NUMBER(19),
  auth_type              NUMBER(19),
  onu_type               NUMBER(19),
  onu_id                 VARCHAR2(80),
  related_access_point   VARCHAR2(44),
  real_longitude         NUMBER(38,6),
  real_latitude          NUMBER(38,6),
  longitude              NUMBER(38,6),
  latitude               NUMBER(38,6),
  related_olt_cuid       VARCHAR2(46),
  password               VARCHAR2(60),
  remark                 VARCHAR2(255),
  label_dev              VARCHAR2(100),
  related_room_cuid      VARCHAR2(37),
  related_site_cuid      VARCHAR2(37),
  related_project_cuid   VARCHAR2(255),
  last_modify_user       VARCHAR2(60),
  creator_name           VARCHAR2(60),
  creattime              DATE,
  ownership              NUMBER(19) default 1,
  maint_mode             NUMBER(19) default 1,
  is_closenet            NUMBER(1) default 0,
  standard_name          VARCHAR2(255),
  related_pos_cuid       VARCHAR2(200),
  mac_addr               VARCHAR2(30),
  related_olt_port_cuid  VARCHAR2(36),
  ownership_man          VARCHAR2(10),
  back_network_time      DATE,
  dev_cuid               VARCHAR2(60),
  access_type            NUMBER(19),
  oper_state             NUMBER(19),
  onu_rtt                NUMBER(19),
  livemodify_time        DATE,
  cuid                   VARCHAR2(172),
  logicid                VARCHAR2(100),
  related_pos_port_cuid  VARCHAR2(36),
  cover_info             VARCHAR2(255),
  cover_user_count       NUMBER(10),
  preserver              VARCHAR2(200),
  data_quality_person    VARCHAR2(255),
  maint_person           VARCHAR2(255),
  port_number            NUMBER(19)
);
-- Create/Recreate indexes 
create index attemp_AN_ONU_ACCESS on T_ATTEMP_AN_ONU (RELATED_ACCESS_POINT);
create index attemp_INDEX_AN_ONU_DEV_CUID on T_ATTEMP_AN_ONU (DEV_CUID);
create index attemp_INDEX_AN_ONU_DEV_IP on T_ATTEMP_AN_ONU (DEV_IP);
create index attemp_INDEX_AN_ONU_FDN on T_ATTEMP_AN_ONU (FDN);
create index attemp_INDEX_AN_ONU_LABEL_CN on T_ATTEMP_AN_ONU (LABEL_CN);
create index attemp_ONU_OLT_PORT_CUID on T_ATTEMP_AN_ONU (RELATED_OLT_PORT_CUID);
create index attemp_ONU_RELATED_OLT_CUID on T_ATTEMP_AN_ONU (RELATED_OLT_CUID);
create index attemp_ONU_RELATED_POS_CUID on T_ATTEMP_AN_ONU (RELATED_POS_CUID);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_attemp_AN_ONU
  add constraint attemp_UNIQUE_1AN_ONU unique (CUID)
  using index ;

  
-- Create table
create table T_his_AN_ONU
(
  objectid               NUMBER(38),
  idn                    VARCHAR2(255),
  create_time            DATE,
  related_upne_cuid      VARCHAR2(172),
  last_modify_time       DATE,
  gt_version             NUMBER(38),
  isdelete               NUMBER(38),
  is_permit_sys_del      NUMBER(1) default 0,
  fdn                    VARCHAR2(255),
  state                  NUMBER(19),
  userlabel              VARCHAR2(255),
  related_template_name  VARCHAR2(60),
  abbreviation           VARCHAR2(60),
  capacity               NUMBER(19),
  related_ems_cuid       VARCHAR2(43),
  native_ems_name        VARCHAR2(255),
  live_cycle             NUMBER(19),
  use_state              NUMBER(19),
  model                  VARCHAR2(60),
  hard_version           VARCHAR2(60),
  soft_version           VARCHAR2(60),
  location               VARCHAR2(255),
  related_district_cuid  VARCHAR2(60),
  related_upne_port_cuid VARCHAR2(50),
  setup_time             DATE,
  seqno                  VARCHAR2(60),
  related_pon_port_cuid  VARCHAR2(70),
  label_cn               VARCHAR2(255),
  related_vendor_cuid    VARCHAR2(46),
  dev_ip                 VARCHAR2(25),
  fttx                   NUMBER(19),
  auth_type              NUMBER(19),
  onu_type               NUMBER(19),
  onu_id                 VARCHAR2(80),
  related_access_point   VARCHAR2(44),
  real_longitude         NUMBER(38,6),
  real_latitude          NUMBER(38,6),
  longitude              NUMBER(38,6),
  latitude               NUMBER(38,6),
  related_olt_cuid       VARCHAR2(46),
  password               VARCHAR2(60),
  remark                 VARCHAR2(255),
  label_dev              VARCHAR2(100),
  related_room_cuid      VARCHAR2(37),
  related_site_cuid      VARCHAR2(37),
  related_project_cuid   VARCHAR2(255),
  last_modify_user       VARCHAR2(60),
  creator_name           VARCHAR2(60),
  creattime              DATE,
  ownership              NUMBER(19) default 1,
  maint_mode             NUMBER(19) default 1,
  is_closenet            NUMBER(1) default 0,
  standard_name          VARCHAR2(255),
  related_pos_cuid       VARCHAR2(200),
  mac_addr               VARCHAR2(30),
  related_olt_port_cuid  VARCHAR2(36),
  ownership_man          VARCHAR2(10),
  back_network_time      DATE,
  dev_cuid               VARCHAR2(60),
  access_type            NUMBER(19),
  oper_state             NUMBER(19),
  onu_rtt                NUMBER(19),
  livemodify_time        DATE,
  cuid                   VARCHAR2(172),
  logicid                VARCHAR2(100),
  related_pos_port_cuid  VARCHAR2(36),
  cover_info             VARCHAR2(255),
  cover_user_count       NUMBER(10),
  preserver              VARCHAR2(200),
  data_quality_person    VARCHAR2(255),
  maint_person           VARCHAR2(255),
  port_number            NUMBER(19)
);
-- Create/Recreate indexes 
create index his_AN_ONU_ACCESS on T_his_AN_ONU (RELATED_ACCESS_POINT);
create index his_INDEX_AN_ONU_DEV_CUID on T_his_AN_ONU (DEV_CUID);
create index his_INDEX_AN_ONU_DEV_IP on T_his_AN_ONU (DEV_IP);
create index his_INDEX_AN_ONU_FDN on T_his_AN_ONU (FDN);
create index his_INDEX_AN_ONU_LABEL_CN on T_his_AN_ONU (LABEL_CN);
create index his_ONU_OLT_PORT_CUID on T_his_AN_ONU (RELATED_OLT_PORT_CUID);
create index his_ONU_RELATED_OLT_CUID on T_his_AN_ONU (RELATED_OLT_CUID);
create index his_ONU_RELATED_POS_CUID on T_his_AN_ONU (RELATED_POS_CUID);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_his_AN_ONU
  add constraint his_UNIQUE_1AN_ONU unique (CUID)
  using index ;
