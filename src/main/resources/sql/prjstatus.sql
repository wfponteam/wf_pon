-- Create table
create table T_WF_PRJ_STATUS_RECORD
(
  PRJ_ID      VARCHAR2(255),
  PRJ_STATUS  VARCHAR2(32),
  UPDATE_TIME DATE
);
-- Add comments to the columns 
comment on column T_WF_PRJ_STATUS_RECORD.PRJ_ID
  is '任务id';
comment on column T_WF_PRJ_STATUS_RECORD.PRJ_STATUS
  is '任务状态';
comment on column T_WF_PRJ_STATUS_RECORD.UPDATE_TIME
  is '更新时间';