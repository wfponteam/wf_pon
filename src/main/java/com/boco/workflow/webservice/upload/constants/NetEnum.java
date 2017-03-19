package com.boco.workflow.webservice.upload.constants;

import com.boco.core.utils.lang.GenericEnum;

/*     */

/*     */
/*     */ public class NetEnum extends GenericEnum
/*     */ {
	/*   6 */ public static final SystemLevel SYSTEM_LEVEL_CH = new SystemLevel();
	/*   7 */ public static final ConnectStatus CONNECT_STATUS_CH = new ConnectStatus();
	/*   8 */ public static final NmType NM_TYPE_CH = new NmType();
	/*   9 */ public static final ServiceMode SERVICE_MODE_CH = new ServiceMode();
	/*  10 */ public static final Direction DIRECTION_CH = new Direction();
	/*  11 */ public static final SegType SEG_TYPE_CH = new SegType();
	/*  12 */ public static final SegType1 SEG_TYPE_CH1 = new SegType1();
	/*  13 */ public static final SystemType SYSTEM_TYPE_CH = new SystemType();
	/*  14 */ public static final SystemType1 SYSTEM_TYPE_CH1 = new SystemType1();
	/*  15 */ public static final TransMedia TRANS_MEDIA_CH = new TransMedia();
	/*  16 */ public static final OwnerShip OWNER_SHIP_CH = new OwnerShip();
	/*  17 */ public static final ProjectState PROJECT_STATE_CH = new ProjectState();
	/*  18 */ public static final MaintMode MAINT_MODE_CH = new MaintMode();
	/*  19 */ public static final NetType NET_TYPE_CH = new NetType();
	/*  20 */ public static final WDMSysProjectType WDM_SYS_PROJECT_TYPE_CH = new WDMSysProjectType();
	/*  21 */ public static final SysProjectType SYS_PROJECT_TYPE_CH = new SysProjectType();
	/*  22 */ public static final LinePairState LINEPAIR_STATE_CH = new LinePairState();
	/*  23 */ public static final RecoveryType RECOVERY_TYPE_CH = new RecoveryType();
	/*  24 */ public static final ReversionMode REVERSION_MODE_CH = new ReversionMode();
	/*  25 */ public static final ProtectState PROTECT_STATE_CH = new ProtectState();
	/*  26 */ public static final LoopState LOOP_STATE_CH = new LoopState();
	/*  27 */ public static final PortLoopState PORT_LOOP_STATE_CH = new PortLoopState();
	/*  28 */ public static final WdmRateType WDM_RATE_TYPE = new WdmRateType();
	/*  29 */ public static final EmsType EMS_TYPE = new EmsType();
	/*  30 */ public static final ConversionResult CONVERSION_RESULT = new ConversionResult();
	/*  31 */ public static final CtpShowModel CTP_SHOW_MODEL = new CtpShowModel();
	/*  32 */ public static final AlarmAutosyncType ALARM_AUTOSYNC_TYPE = new AlarmAutosyncType();
	/*  33 */ public static final SupportTopoLevel SUPPORT_TOPO_LEVEL = new SupportTopoLevel();
	/*  34 */ public static final BackBoneProperty BACK_BONE_PROPERTY = new BackBoneProperty();
	/*  35 */ public static final BackBoneGroup BACK_BONE_GROUP = new BackBoneGroup();
	/*  36 */ public static final TopoWorkModel TOPO_WORK_MODEL = new TopoWorkModel();
	/*     */
	/*  38 */ public static final LifeCycleStatus Life_Cycle_Status = new LifeCycleStatus();
	/*     */
	/*  40 */ public static final ImportDataState date_state = new ImportDataState();

	/*     */
	/*     */ public static class ImportDataState extends GenericEnum
	/*     */ {
		/*     */ public static final long _state0 = 0L;
		/*     */ public static final long _state1 = 1L;

		/*     */
		/*     */ public ImportDataState()
		/*     */ {
			/* 563 */ super.putEnum(Long.valueOf(0L), "预导入");
			/* 564 */ super.putEnum(Long.valueOf(1L), "正式导入");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class LifeCycleStatus extends GenericEnum
	/*     */ {
		/*     */ public static final long _status0 = 0L;
		/*     */ public static final long _status1 = 1L;
		/*     */ public static final long _status2 = 2L;
		/*     */ public static final long _status3 = 3L;
		/*     */ public static final long _status4 = 4L;
		/*     */ public static final long _status5 = 5L;
		/*     */ public static final long _status7 = 7L;
		/*     */ public static final long _status8 = 8L;
		/*     */ public static final long _status9 = 9L;

		/*     */
		/*     */ private LifeCycleStatus()
		/*     */ {
			/* 546 */ super.putEnum(Long.valueOf(0L), "未确定");
			/* 547 */ super.putEnum(Long.valueOf(1L), "规划");
			/* 548 */ super.putEnum(Long.valueOf(2L), "设计");
			/* 549 */ super.putEnum(Long.valueOf(3L), "工程在建");
			/* 550 */ super.putEnum(Long.valueOf(4L), "工程无业务");
			/* 551 */ super.putEnum(Long.valueOf(5L), "工程有业务");
			/* 552 */ super.putEnum(Long.valueOf(7L), "现网无业务");
			/* 553 */ super.putEnum(Long.valueOf(8L), "现网有业务");
			/* 554 */ super.putEnum(Long.valueOf(9L), "退网");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class BackBoneGroup extends GenericEnum
	/*     */ {
		/*     */ public static final long _group1 = 1L;
		/*     */ public static final long _group2 = 2L;

		/*     */
		/*     */ private BackBoneGroup()
		/*     */ {
			/* 527 */ super.putEnum(Long.valueOf(1L), "分组1");
			/* 528 */ super.putEnum(Long.valueOf(2L), "分组2");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class BackBoneProperty extends GenericEnum
	/*     */ {
		/*     */ public static final long _BONECOLLECT = 1L;
		/*     */ public static final long _BONENODE = 2L;
		/*     */ public static final long _INSYSTEMNE = 3L;

		/*     */
		/*     */ private BackBoneProperty()
		/*     */ {
			/* 515 */ super.putEnum(Long.valueOf(1L), "骨干汇聚点");
			/* 516 */ super.putEnum(Long.valueOf(2L), "次骨干节点");
			/* 517 */ super.putEnum(Long.valueOf(3L), "楼内系统网元");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class SupportTopoLevel extends GenericEnum
	/*     */ {
		/*     */ public static final long _ptp = 1L;
		/*     */ public static final long _ne = 2L;

		/*     */
		/*     */ private SupportTopoLevel()
		/*     */ {
			/* 503 */ super.putEnum(Long.valueOf(1L), "端口");
			/* 504 */ super.putEnum(Long.valueOf(2L), "网元");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class AlarmAutosyncType extends GenericEnum
	/*     */ {
		/*     */ public static final int _period = 1;
		/*     */ public static final int _time = 2;

		/*     */
		/*     */ private AlarmAutosyncType()
		/*     */ {
			/* 494 */ super.putEnum(Integer.valueOf(1), "周期同步");
			/* 495 */ super.putEnum(Integer.valueOf(2), "定时同步");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class CtpShowModel extends GenericEnum
	/*     */ {
		/*     */ public static final int _order = 1;
		/*     */ public static final int _space = 2;

		/*     */
		/*     */ private CtpShowModel()
		/*     */ {
			/* 484 */ super.putEnum(Integer.valueOf(1), "顺序");
			/* 485 */ super.putEnum(Integer.valueOf(2), "间插");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class WdmRateType extends GenericEnum
	/*     */ {
		/*     */ public static final int _1 = 1;
		/*     */ public static final int _2 = 2;
		/*     */ public static final int _3 = 3;

		/*     */
		/*     */ private WdmRateType()
		/*     */ {
			/* 473 */ super.putEnum(Integer.valueOf(1), "2.5G");
			/* 474 */ super.putEnum(Integer.valueOf(2), "10G");
			/* 475 */ super.putEnum(Integer.valueOf(3), "40G");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class SysProjectType extends GenericEnum
	/*     */ {
		/*     */ public static final long _NONE = 0L;
		/*     */ public static final long _LSR = 1L;
		/*     */ public static final long _UPSR2 = 2L;
		/*     */ public static final long _BLSR2 = 3L;
		/*     */ public static final long _UMSPR2F = 4L;
		/*     */ public static final long _BMSPR2F = 5L;
		/*     */ public static final long _BMSPR4F = 6L;
		/*     */ public static final long _PSR = 7L;
		/*     */ public static final long _MLSR = 8L;
		/*     */ public static final long _MIX = 9L;
		/*     */ public static final long _NMLSR = 10L;

		/*     */
		/*     */ private SysProjectType()
		/*     */ {
			/* 446 */ super.putEnum(Long.valueOf(0L), "未知");
			/* 447 */ super.putEnum(Long.valueOf(1L), "线路保护");
			/* 448 */ super.putEnum(Long.valueOf(2L), "二纤单向通道保护环");
			/* 449 */ super.putEnum(Long.valueOf(3L), "二纤双向通道保护环");
			/* 450 */ super.putEnum(Long.valueOf(4L), "二纤单向复用段保护环");
			/* 451 */ super.putEnum(Long.valueOf(5L), "二纤双向复用段保护环");
			/* 452 */ super.putEnum(Long.valueOf(6L), "四纤双向复用段保护环");
			/* 453 */ super.putEnum(Long.valueOf(7L), "光通路保护");
			/* 454 */ super.putEnum(Long.valueOf(8L), "1+1复用段线性保护");
			/* 455 */ super.putEnum(Long.valueOf(9L), "混合保护");
			/* 456 */ super.putEnum(Long.valueOf(10L), "1:N复用段线性保护");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class WDMSysProjectType extends GenericEnum
	/*     */ {
		/*     */ public static final long _NONE = 0L;
		/*     */ public static final long _LSR = 1L;
		/*     */ public static final long _UPSR2 = 2L;
		/*     */ public static final long _BLSR2 = 3L;
		/*     */ public static final long _UMSPR2F = 4L;
		/*     */ public static final long _BMSPR2F = 5L;
		/*     */ public static final long _BMSPR4F = 6L;
		/*     */ public static final long _PSR = 7L;
		/*     */ public static final long _MLSR = 8L;

		/*     */
		/*     */ private WDMSysProjectType()
		/*     */ {
			/* 400 */ super.putEnum(Long.valueOf(0L), "未知");
			/* 401 */ super.putEnum(Long.valueOf(1L), "线路保护倒换");
			/* 402 */ super.putEnum(Long.valueOf(2L), "二纤单向通道倒换环");
			/* 403 */ super.putEnum(Long.valueOf(3L), "二纤双向通道倒换环");
			/* 404 */ super.putEnum(Long.valueOf(4L), "二纤单向复用段倒换环");
			/* 405 */ super.putEnum(Long.valueOf(5L), "二纤双向复用段倒换环");
			/* 406 */ super.putEnum(Long.valueOf(6L), "四纤双向复用段倒换环");
			/* 407 */ super.putEnum(Long.valueOf(7L), "光通路保护倒换");
			/* 408 */ super.putEnum(Long.valueOf(8L), "1+1复用段线性保护");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class NetType extends GenericEnum
	/*     */ {
		/*     */ public static final int _round = 1;
		/*     */ public static final int _chain = 2;

		/*     */
		/*     */ private NetType()
		/*     */ {
			/* 381 */ super.putEnum(Integer.valueOf(1), "环型");
			/* 382 */ super.putEnum(Integer.valueOf(2), "链型");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class MaintMode extends GenericEnum
	/*     */ {
		/*     */ public static final int _none = 0;
		/*     */ public static final int _self = 1;
		/*     */ public static final int _instead = 2;

		/*     */
		/*     */ private MaintMode()
		/*     */ {
			/* 368 */ super.putEnum(Integer.valueOf(0), "未知");
			/* 369 */ super.putEnum(Integer.valueOf(1), "自维");
			/* 370 */ super.putEnum(Integer.valueOf(2), "代维");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class ProjectState extends GenericEnum
	/*     */ {
		/*     */ public static final int _design = 1;
		/*     */ public static final int _build = 2;
		/*     */ public static final int _finish = 3;
		/*     */ public static final int _disuse = 4;
		/*     */ public static final int _maintenance = 5;

		/*     */
		/*     */ private ProjectState()
		/*     */ {
			/* 353 */ super.putEnum(Integer.valueOf(1), "设计");
			/* 354 */ super.putEnum(Integer.valueOf(2), "在建");
			/* 355 */ super.putEnum(Integer.valueOf(3), "竣工");
			/* 356 */ super.putEnum(Integer.valueOf(4), "废弃");
			/* 357 */ super.putEnum(Integer.valueOf(5), "维护");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class OwnerShip extends GenericEnum
	/*     */ {
		/*     */ public static final int _unknow = 0;
		/*     */ public static final int _alone = 1;
		/*     */ public static final int _cooperate = 2;
		/*     */ public static final int _he = 3;
		/*     */ public static final int _hire = 4;
		/*     */ public static final int _buy = 5;
		/*     */ public static final int _zhihuan = 6;

		/*     */
		/*     */ private OwnerShip()
		/*     */ {
			/* 335 */ super.putEnum(Integer.valueOf(1), "自建");
			/* 336 */ super.putEnum(Integer.valueOf(2), "共建");
			/* 337 */ super.putEnum(Integer.valueOf(4), "租用");
			/* 338 */ super.putEnum(Integer.valueOf(5), "购买");
			/* 339 */ super.putEnum(Integer.valueOf(3), "合建");
			/* 340 */ super.putEnum(Integer.valueOf(6), "置换");
			/* 341 */ super.putEnum(Integer.valueOf(0), "未知");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class TransMedia extends GenericEnum
	/*     */ {
		/*     */ public static final int _wire = 1;
		/*     */ public static final int _micwave = 2;
		/*     */ public static final int _smicwave = 3;
		/*     */ public static final int _satellite = 4;
		/*     */ public static final int _directraph = 5;
		/*     */ public static final int _cable = 6;
		/*     */ public static final int _infrared = 7;
		/*     */ public static final int _adsl = 8;
		/*     */ public static final int _hdsl = 9;
		/*     */ public static final int _commix = 10;

		/*     */
		/*     */ private TransMedia()
		/*     */ {
			/* 313 */ super.putEnum(Integer.valueOf(1), "光缆");
			/* 314 */ super.putEnum(Integer.valueOf(2), "微波");
			/* 315 */ super.putEnum(Integer.valueOf(3), "小微波");
			/* 316 */ super.putEnum(Integer.valueOf(4), "卫星");
			/* 317 */ super.putEnum(Integer.valueOf(5), "直连电路");
			/* 318 */ super.putEnum(Integer.valueOf(6), "电缆");
			/* 319 */ super.putEnum(Integer.valueOf(7), "红外");
			/* 320 */ super.putEnum(Integer.valueOf(8), "ADSL");
			/* 321 */ super.putEnum(Integer.valueOf(9), "HDSL");
			/* 322 */ super.putEnum(Integer.valueOf(10), "混合");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class SystemType1 extends GenericEnum
	/*     */ {
		/*     */ public static final int _sdh = 1;
		/*     */ public static final int _pdh = 2;
		/*     */ public static final int _dwdm = 3;
		/*     */ public static final int _wbm = 4;
		/*     */ public static final int _hhm = 5;

		/*     */
		/*     */ private SystemType1()
		/*     */ {
			/* 292 */ super.putEnum(Integer.valueOf(1), "SDH");
			/* 293 */ super.putEnum(Integer.valueOf(2), "PDH");
			/* 294 */ super.putEnum(Integer.valueOf(3), "WDM");
			/* 295 */ super.putEnum(Integer.valueOf(4), "微波");
			/* 296 */ super.putEnum(Integer.valueOf(5), "混合");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class SystemType extends GenericEnum
	/*     */ {
		/*     */ public static final int _sdh = 1;
		/*     */ public static final int _pdh = 2;
		/*     */ public static final int _dwdm = 3;

		/*     */
		/*     */ private SystemType()
		/*     */ {
			/* 279 */ super.putEnum(Integer.valueOf(1), "SDH");
			/* 280 */ super.putEnum(Integer.valueOf(2), "PDH");
			/* 281 */ super.putEnum(Integer.valueOf(3), "WDM");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class Direction extends GenericEnum
	/*     */ {
		/*     */ public static final int _asc = 1;
		/*     */ public static final int _desc = 2;

		/*     */
		/*     */ private Direction()
		/*     */ {
			/* 269 */ super.putEnum(Integer.valueOf(1), "单向");
			/* 270 */ super.putEnum(Integer.valueOf(2), "双向");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class ConversionResult extends GenericEnum
	/*     */ {
		/*     */ public static final long _success = 1L;
		/*     */ public static final long _failing = 2L;

		/*     */
		/*     */ private ConversionResult()
		/*     */ {
			/* 258 */ super.putEnum(Long.valueOf(1L), "成功");
			/* 259 */ super.putEnum(Long.valueOf(2L), "失败");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class ServiceMode extends GenericEnum
	/*     */ {
		/*     */ public static final int _mainuse = 1;
		/*     */ public static final int _prepareuse = 2;

		/*     */
		/*     */ private ServiceMode()
		/*     */ {
			/* 248 */ super.putEnum(Integer.valueOf(1), "主用");
			/* 249 */ super.putEnum(Integer.valueOf(2), "备用");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class EmsType extends GenericEnum
	/*     */ {
		/*     */ public static final int _no = 0;
		/*     */ public static final int _sdh = 1;
		/*     */ public static final int _wdm = 3;
		/*     */ public static final int _ptn = 8;
		/*     */ public static final int _pon = 9;
		/*     */ public static final int _olp = 10;
		/*     */ public static final int _otn = 11;
		/*     */ public static final int _odn = 12;
		/*     */ public static final int _msap = 13;

		/*     */
		/*     */ private EmsType()
		/*     */ {
			/* 232 */ super.putEnum(Integer.valueOf(0), "无");
			/* 233 */ super.putEnum(Integer.valueOf(1), "SDH");
			/* 234 */ super.putEnum(Integer.valueOf(3), "WDM");
			/* 235 */ super.putEnum(Integer.valueOf(8), "PTN");
			/* 236 */ super.putEnum(Integer.valueOf(9), "PON");
			/* 237 */ super.putEnum(Integer.valueOf(10), "OLP");
			/* 238 */ super.putEnum(Integer.valueOf(11), "OTN");
			/* 239 */ super.putEnum(Integer.valueOf(12), "ODN");
			/* 240 */ super.putEnum(Integer.valueOf(13), "多业务接入");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class NmType extends GenericEnum
	/*     */ {
		/*     */ public static final int _ems = 1;
		/*     */ public static final int _snms = 2;
		/*     */ public static final int _emssnms = 3;

		/*     */
		/*     */ private NmType()
		/*     */ {
			/* 213 */ super.putEnum(Integer.valueOf(1), "EMS");
			/* 214 */ super.putEnum(Integer.valueOf(2), "SNMS");
			/* 215 */ super.putEnum(Integer.valueOf(3), "EMS+SNMS");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class ConnectStatus extends GenericEnum
	/*     */ {
		/*     */ public static final int _disconnect = 1;
		/*     */ public static final int _connect = 2;
		/*     */ public static final int _partconnect = 3;

		/*     */
		/*     */ private ConnectStatus()
		/*     */ {
			/* 201 */ super.putEnum(Integer.valueOf(1), "断开");
			/* 202 */ super.putEnum(Integer.valueOf(2), "连接");
			/* 203 */ super.putEnum(Integer.valueOf(3), "部分连接");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class SystemLevel extends GenericEnum
	/*     */ {
		/*     */ public static final int _country = 1;
		/*     */ public static final int _province = 2;
		/*     */ public static final int _countygg = 3;
		/*     */ public static final int _countyhj = 4;
		/*     */ public static final int _countyjr = 5;

		/*     */
		/*     */ private SystemLevel()
		/*     */ {
			/* 187 */ super.putEnum(Integer.valueOf(1), "省际系统");
			/* 188 */ super.putEnum(Integer.valueOf(2), "省内系统");
			/* 189 */ super.putEnum(Integer.valueOf(3), "本地骨干");
			/* 190 */ super.putEnum(Integer.valueOf(4), "本地汇聚");
			/* 191 */ super.putEnum(Integer.valueOf(5), "本地接入");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class SegType extends GenericEnum
	/*     */ {
		/*     */ public static final int _reuse = 1;
		/*     */ public static final int _rebirth = 2;
		/*     */ public static final int _rayreuse = 3;
		/*     */ public static final int _zoomout = 4;
		/*     */ public static final int _digital = 5;

		/*     */
		/*     */ private SegType()
		/*     */ {
			/* 171 */ super.putEnum(Integer.valueOf(1), "复用段层");
			/* 172 */ super.putEnum(Integer.valueOf(2), "再生段层");
			/* 173 */ super.putEnum(Integer.valueOf(3), "光复用段层");
			/* 174 */ super.putEnum(Integer.valueOf(4), "光放大段层");
			/* 175 */ super.putEnum(Integer.valueOf(5), "数字信号层");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class SegType1 extends GenericEnum
	/*     */ {
		/*     */ public static final int _OSP = 1;
		/*     */ public static final int _RS = 2;
		/*     */ public static final int _MS = 3;
		/*     */ public static final int _HPLP = 4;
		/*     */ public static final int _OCH = 5;
		/*     */ public static final int _OMS = 6;
		/*     */ public static final int _OTS = 7;
		/*     */ public static final int _OSC = 8;
		/*     */ public static final int _OADM = 9;
		/*     */ public static final int _PCM = 10;
		/*     */ public static final int _OFD = 11;

		/*     */
		/*     */ private SegType1()
		/*     */ {
			/* 148 */ super.putEnum(Integer.valueOf(1), "光同步物理接口");
			/* 149 */ super.putEnum(Integer.valueOf(2), "再生段层");
			/* 150 */ super.putEnum(Integer.valueOf(3), "复用段层");
			/* 151 */ super.putEnum(Integer.valueOf(4), "通道层");
			/* 152 */ super.putEnum(Integer.valueOf(5), "光通路层");
			/* 153 */ super.putEnum(Integer.valueOf(6), "光复用段层");
			/* 154 */ super.putEnum(Integer.valueOf(7), "光传送层");
			/* 155 */ super.putEnum(Integer.valueOf(8), "光监控通路");
			/* 156 */ super.putEnum(Integer.valueOf(9), "OADM");
			/* 157 */ super.putEnum(Integer.valueOf(10), "调制器性能");
			/* 158 */ super.putEnum(Integer.valueOf(11), "光放大段层");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class LinePairState extends GenericEnum
	/*     */ {
		/*     */ public static final long _UNUSE = 1L;
		/*     */ public static final long _INUSE = 2L;
		/*     */ public static final long _PREUSE = 3L;
		/*     */ public static final long _ERROR = 4L;
		/*     */ public static final long _CANNOTUSE = 5L;

		/*     */
		/*     */ private LinePairState()
		/*     */ {
			/* 126 */ super.putEnum(Long.valueOf(1L), "未用");
			/* 127 */ super.putEnum(Long.valueOf(2L), "在用");
			/* 128 */ super.putEnum(Long.valueOf(3L), "预占");
			/* 129 */ super.putEnum(Long.valueOf(4L), "坏线对");
			/* 130 */ super.putEnum(Long.valueOf(5L), "不可用线对");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class RecoveryType extends GenericEnum
	/*     */ {
		/*     */ public static final long _AUTO = 1L;
		/*     */ public static final long _BYHAND = 2L;

		/*     */
		/*     */ private RecoveryType()
		/*     */ {
			/* 112 */ super.putEnum(Long.valueOf(1L), "自动恢复");
			/* 113 */ super.putEnum(Long.valueOf(2L), "人工恢复");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class ReversionMode extends GenericEnum
	/*     */ {
		/*     */ public static final long _RECOVER = 1L;
		/*     */ public static final long _NOTRECOVER = 2L;

		/*     */
		/*     */ private ReversionMode()
		/*     */ {
			/* 101 */ super.putEnum(Long.valueOf(1L), "恢复");
			/* 102 */ super.putEnum(Long.valueOf(2L), "不恢复");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class ProtectState extends GenericEnum
	/*     */ {
		/*     */ public static final long _NORMAL = 1L;
		/*     */ public static final long _ROTATE = 2L;

		/*     */
		/*     */ private ProtectState()
		/*     */ {
			/*  90 */ super.putEnum(Long.valueOf(1L), "正常状态");
			/*  91 */ super.putEnum(Long.valueOf(2L), "倒换状态");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class PortLoopState extends GenericEnum
	/*     */ {
		/*     */ public static final long _NA = 1L;
		/*     */ public static final long _INNERLOOP = 2L;
		/*     */ public static final long _OUTERLOOP = 3L;

		/*     */
		/*     */ private PortLoopState()
		/*     */ {
			/*  78 */ super.putEnum(Long.valueOf(2L), "内环回");
			/*  79 */ super.putEnum(Long.valueOf(3L), "外环回");
			/*  80 */ super.putEnum(Long.valueOf(1L), "无环回");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class LoopState extends GenericEnum
	/*     */ {
		/*     */ public static final long _NA = 1L;
		/*     */ public static final long _CHANNELLOOP = 2L;
		/*     */ public static final long _DEVICELOOP = 3L;
		/*     */ public static final long _LINELOOP = 4L;

		/*     */
		/*     */ private LoopState()
		/*     */ {
			/*  64 */ super.putEnum(Long.valueOf(2L), "通道环回");
			/*  65 */ super.putEnum(Long.valueOf(3L), "设备环回");
			/*  66 */ super.putEnum(Long.valueOf(4L), "线路环回");
			/*  67 */ super.putEnum(Long.valueOf(1L), "无环回");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class TopoWorkModel extends GenericEnum
	/*     */ {
		/*     */ public static final long _WORK = 1L;
		/*     */ public static final long _PROTECT = 2L;

		/*     */
		/*     */ private TopoWorkModel()
		/*     */ {
			/*  51 */ super.putEnum(Long.valueOf(1L), "工作");
			/*  52 */ super.putEnum(Long.valueOf(2L), "保护");
			/*     */ }
		/*     */ }
	/*     */ }