alter table BUSINESS_COMMUNITY add related_project_cuid varchar2(100) ;
-- Create table
create table t_attemp_BUSINESS_COMMUNITY
(
  cuid                     VARCHAR2(172) not null,
  label_cn                 VARCHAR2(256),
  buildings_num            VARCHAR2(256),
  units_num                VARCHAR2(256),
  households_num           VARCHAR2(256),
  maintain_person          VARCHAR2(256),
  maintain_dept            VARCHAR2(256),
  contact_number           VARCHAR2(256),
  construct_dept           VARCHAR2(256),
  create_time              DATE,
  create_user              VARCHAR2(256),
  related_bmclasstype_cuid VARCHAR2(255),
  county                   VARCHAR2(255),
  maintain_site            VARCHAR2(255),
  is_light_changed         VARCHAR2(255),
  is_overlap_community     VARCHAR2(255),
  is_overlapp              VARCHAR2(255),
  is_priority              VARCHAR2(255),
  city                     VARCHAR2(255),
  pre_coverage_threshold   VARCHAR2(255),
  is_key_community         VARCHAR2(255),
  is_optical_community     VARCHAR2(255),
  address                  VARCHAR2(512),
  business_community       VARCHAR2(255),
  warning_value            NUMBER(38),
  is_warning               VARCHAR2(255),
  access_info              VARCHAR2(255),
  code                     VARCHAR2(10),
  cover_area               VARCHAR2(255),
  access_type              VARCHAR2(255),
  cooperation_code         VARCHAR2(255),
  build_type               VARCHAR2(255),
  access_mode              VARCHAR2(255),
  service_type             VARCHAR2(255),
  related_project_cuid     varchar2(100)
);
-- Create/Recreate indexes 
create index attemp_BUSINESS_COMMUNITY_02 on t_attemp_BUSINESS_COMMUNITY (LABEL_CN);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_attemp_BUSINESS_COMMUNITY
  add constraint attemp_PK_1BUSINESS_COMMUNITY primary key (CUID)
  using index ;


-- Create table
create table t_his_BUSINESS_COMMUNITY
(
  cuid                     VARCHAR2(172) not null,
  label_cn                 VARCHAR2(256),
  buildings_num            VARCHAR2(256),
  units_num                VARCHAR2(256),
  households_num           VARCHAR2(256),
  maintain_person          VARCHAR2(256),
  maintain_dept            VARCHAR2(256),
  contact_number           VARCHAR2(256),
  construct_dept           VARCHAR2(256),
  create_time              DATE,
  create_user              VARCHAR2(256),
  related_bmclasstype_cuid VARCHAR2(255),
  county                   VARCHAR2(255),
  maintain_site            VARCHAR2(255),
  is_light_changed         VARCHAR2(255),
  is_overlap_community     VARCHAR2(255),
  is_overlapp              VARCHAR2(255),
  is_priority              VARCHAR2(255),
  city                     VARCHAR2(255),
  pre_coverage_threshold   VARCHAR2(255),
  is_key_community         VARCHAR2(255),
  is_optical_community     VARCHAR2(255),
  address                  VARCHAR2(512),
  business_community       VARCHAR2(255),
  warning_value            NUMBER(38),
  is_warning               VARCHAR2(255),
  access_info              VARCHAR2(255),
  code                     VARCHAR2(10),
  cover_area               VARCHAR2(255),
  access_type              VARCHAR2(255),
  cooperation_code         VARCHAR2(255),
  build_type               VARCHAR2(255),
  access_mode              VARCHAR2(255),
  service_type             VARCHAR2(255),
  related_project_cuid     varchar2(100)
);
-- Create/Recreate indexes 
create index his_BUSINESS_COMMUNITY_02 on t_his_BUSINESS_COMMUNITY (LABEL_CN);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_his_BUSINESS_COMMUNITY
  add constraint his_PK_1BUSINESS_COMMUNITY primary key (CUID)
  using index ;


