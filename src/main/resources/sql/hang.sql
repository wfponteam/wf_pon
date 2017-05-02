
-- Create table
create table T_WF_FTTX_GC
(
  id               VARCHAR2(255) not null,
  regioncode       VARCHAR2(255),
  devicename       VARCHAR2(60),
  oltfactory       VARCHAR2(60),
  dnsubnecode      VARCHAR2(60),
  dnpackagecode    VARCHAR2(60),
  oltslot          VARCHAR2(60),
  portno           NUMBER,
  village_name     VARCHAR2(255),
  village_id       VARCHAR2(255),
  prjcode          VARCHAR2(255),
  prjname          VARCHAR2(255),
  parentprjcode     VARCHAR2(255),
  olttelenetipaddr VARCHAR2(255),
  oltidinems       VARCHAR2(255),
  upbasip          VARCHAR2(255),
  upbasslot        VARCHAR2(255),
  upbaspackage     VARCHAR2(255),
  upbasport        VARCHAR2(255),
  oltsvlan         VARCHAR2(60),
  oltcvlan         VARCHAR2(60),
  splitratio       VARCHAR2(10),
  splitlevel       VARCHAR2(40),
  parent_name      VARCHAR2(255),
  parent_obdcode   VARCHAR2(255),
  password         VARCHAR2(255),
  device_type      VARCHAR2(60),
  operationtype    NUMBER(2),
  operationtime    DATE,
  result           NUMBER(2),
  opt_power        VARCHAR2(60),
  faildesc         VARCHAR2(255),
  call_result      NUMBER(2),
  ponportname      VARCHAR2(255)
);
-- Add comments to the columns 
comment on column T_WF_FTTX_GC.regioncode
  is '区域编码';
comment on column T_WF_FTTX_GC.devicename
  is 'OLT设备名称';
comment on column T_WF_FTTX_GC.oltfactory
  is 'OLT厂家';
comment on column T_WF_FTTX_GC.dnsubnecode
  is 'OLT设备机框号';
comment on column T_WF_FTTX_GC.dnpackagecode
  is 'OLT设备板卡号';
comment on column T_WF_FTTX_GC.oltslot
  is 'OLT槽位号';
comment on column T_WF_FTTX_GC.portno
  is 'OLT设备端口号';
comment on column T_WF_FTTX_GC.village_name
  is '小区名称';
comment on column T_WF_FTTX_GC.village_id
  is '小区ID';
comment on column T_WF_FTTX_GC.prjcode
  is '设备所属工程编码';
comment on column T_WF_FTTX_GC.prjname
is '设备所属主工程名称';
comment on column T_WF_FTTX_GC.parentprjcode
  is '设备所属父工程编码';
comment on column T_WF_FTTX_GC.olttelenetipaddr
  is '所属OLT的TELENETIP地址';
comment on column T_WF_FTTX_GC.oltidinems
  is '所属OLT网管标识';
comment on column T_WF_FTTX_GC.upbasip
  is '上联BASIP';
comment on column T_WF_FTTX_GC.upbasslot
  is '上联BAS槽位号';
comment on column T_WF_FTTX_GC.upbaspackage
  is '上联BAS子槽号';
comment on column T_WF_FTTX_GC.upbasport
  is '上联BAS端口';
comment on column T_WF_FTTX_GC.oltsvlan
  is 'OLTPON口外层VLAN';
comment on column T_WF_FTTX_GC.oltcvlan
  is 'OLTPON口内层VLAN';
comment on column T_WF_FTTX_GC.splitratio
  is '分光比';
comment on column T_WF_FTTX_GC.splitlevel
  is '分光器级别';
comment on column T_WF_FTTX_GC.parent_name
  is '分光器名称';
comment on column T_WF_FTTX_GC.parent_obdcode
  is '末级分光器端口';
comment on column T_WF_FTTX_GC.device_type
  is '设备类型1、工程挂测
2、存量挂测
3.在网用户挂测
';
comment on column T_WF_FTTX_GC.operationtype
  is '1：新增
2：删除
3:修改
';
comment on column T_WF_FTTX_GC.operationtime
  is '操作时间';
comment on column T_WF_FTTX_GC.result
  is '结果标识0：待处理
1：成功
-1：失败
-2：处理中
';
comment on column T_WF_FTTX_GC.opt_power
  is '光功率 挂测具体光功率值';
comment on column T_WF_FTTX_GC.faildesc
  is '异常原因';
comment on column T_WF_FTTX_GC.call_result
  is '拨号结果1：成功 2：失败';
comment on column T_WF_FTTX_GC.ponportname
  is 'pon口名称'; 
-- Create/Recreate primary, unique and foreign key constraints 
alter table T_WF_FTTX_GC
  add primary key (ID)
  using index ;
-- Grant/Revoke object privileges 
grant select, update on nm_irms352_0913.T_WF_FTTX_GC to wangyou;

  
  
  create sequence seq_WF_FTTX_GC
minvalue 10000000
maxvalue 9999999999999
start with 10000000
increment by 1
nocache;



create or replace view v_fttx_gc as
select olt.related_district_cuid as REGIONCODE,
         olt.label_cn as DEVICENAME,
         (select label_cn
            from device_vendor
           where device_vendor.cuid = olt.related_vendor_cuid) as OLTFACTORY,
         substr(p.fdn,
                instr(p.fdn, 'rack=') + 5,
                instr(p.fdn, '/shelf=') - instr(p.fdn, 'rack=') - 5) as DNSUBNECODE,
         substr(p.fdn,
                instr(p.fdn, 'shelf=') + 6,
                instr(p.fdn, '/slot=') - instr(p.fdn, 'shelf=') - 6) as DNPACKAGECODE,
         substr(p.fdn,
                instr(p.fdn, 'slot=') + 5,
                instr(p.fdn, '/port=') - instr(p.fdn, 'slot=') - 5) as OLTSLOT,
         substr(p.fdn, instr(p.fdn, 'port=') + 5) as PORTNO,
         (select t.label_cn
            from (select distinct bc.label_cn, gc.related_ne_cuid
                    from (select *
                            from t_rofh_full_address
                          union all
                          select * from t_attemp_t_rofh_full_address) fa,
                         (select *
                            from gpon_cover
                          union all
                          select * from t_attemp_gpon_cover) gc,
                         (select *
                            from business_community
                          union all
                          select * from t_attemp_business_community) bc
                   where fa.cuid = gc.standard_addr
                     and fa.related_community_cuid = bc.cuid) t
           where pos.related_cab_cuid = t.related_ne_cuid) as VILLAGE_NAME,
         '' as VILLAGE_ID,
         wp.prj_code as PRJCODE,
         wp.parent_prj_code as PARENTPRJCODE,
         (select p.prj_name from t_wf_project p where p.prj_code = wp.parent_prj_code) prjname ,
         olt.dev_ip as OLTTELENETIPADDR,
         olt.native_ems_name as OLTIDINEMS,
         (select manageip
            from T_COM_CITYNET_DEVICE
           where cuid = t.related_orig_logic_cuid) as UPBASIP,
         substr((select t_logic_port.label_cn
                  from t_logic_port
                 where cuid = t.related_orig_port_cuid),
                -5,
                1) as UPBASSLOT,
         substr((select t_logic_port.label_cn
                  from t_logic_port
                 where cuid = t.related_orig_port_cuid),
                -3,
                1) as UPBASPACKAGE,
         substr((select t_logic_port.label_cn
                  from t_logic_port
                 where cuid = t.related_orig_port_cuid),
                -1,
                1) as UPBASPORT,
         p.vlan as OLTSVLAN,
         '200' as OLTCVLAN,
         pos.RATION as SPLITRATIO,
         '一级' as SPLITLEVEL,
         pos.LABEL_CN as PARENT_NAME,
         --ptp.label_cn as PARENT_OBDCODE,
         'cmcctest' as PASSWORD,
         '1' as DEVICE_TYPE,
         '1' as OPERATIONTYPE,
         sysdate as OPERATIONTIME,
         '0' as RESULT,
         '' OPT_POWER,
         '' as FAILDESC,
         '' call_result,
         wp.cuid cuid,
         pos.cuid   pos_cuid,
         (select label_cn from ptp where cuid = pos.related_port_cuid) ponportname
  from
  trans_element   olt,
         card            c,
         ptp             p,
        t_logic_link    t,
         t_attemp_an_pos pos,
         t_wf_project    wp
   where olt.cuid = c.related_device_cuid
     and c.cuid = p.related_card_cuid
     and pos.related_project_cuid = wp.cuid
     and t.related_dest_logic_cuid = olt.cuid
     and t.related_a_bmclass_cuid = 'T_LOGIC_IP_NETWORK_BRAS'
     and pos.related_olt_cuid = olt.cuid
     and pos.related_port_cuid=p.cuid


