package com.boco.workflow.webservice.upload.constants;

import com.boco.core.utils.lang.GenericEnum;

/*      */

/*      */
/*      */ public class RackEnum
/*      */ {
	/*   25 */ public static final RackOwnerShip RACK_OWNER_SHIP = new RackOwnerShip();
	/*   26 */ public static final ServiceType SERVICE_TYPE = new ServiceType();
	/*   27 */ public static final SwitchPortStateType SWITCHPORTSTATE_TYPE = new SwitchPortStateType();
	/*   28 */ public static final SwitchPortType SWITCHPORT_TYPE = new SwitchPortType();
	/*   29 */ public static final ResistanceType RESISTANCE_TYPE = new ResistanceType();
	/*   30 */ public static final ServiceState SERVICE_STATE = new ServiceState();
	/*   31 */ public static final TransPortStateType PORT_STATE = new TransPortStateType();
	/*   32 */ public static final TransPortType PORT_TYPE = new TransPortType();
	/*   33 */ public static final TransPortSubType PORT_SUB_TYPE = new TransPortSubType();
	/*   34 */ public static final TransPortType1 PORT_TYPE1 = new TransPortType1();
	/*   35 */ public static final TerminationMode TERMINATION_MODE = new TerminationMode();
	/*   36 */ public static final Directionality DIRECTIONALITY = new Directionality();
	/*   37 */ public static final IsConnState IS_CONN_STATE = new IsConnState();
	/*   38 */ public static final Domain DOMAIN = new Domain();
	/*   39 */ public static final EthType ETH_TYPE = new EthType();
	/*   40 */ public static final AtmType ATM_TYPE = new AtmType();
	/*   41 */ public static final Rate RATR_TYPE = new Rate();
	/*   42 */ public static final PathState PATH_STATE = new PathState();
	/*   43 */ public static final AlarmImportance ALARMIMPORTANCE = new AlarmImportance();
	/*      */ public static final String STRREFUSEDEL = "strRefuseDel";
	/*   45 */ public static final mstpPortType MSTP_PORT_TYPE = new mstpPortType();
	/*   46 */ public static final mstpTagFlag MSTP_TAG_FLAG = new mstpTagFlag();
	/*   47 */ public static final msptFlowCtrl MSTP_FLOWCTRL = new msptFlowCtrl();
	/*   48 */ public static final mstpPortEnable MSTP_PORTENABLE = new mstpPortEnable();
	/*   49 */ public static final mstpPtpEnable MSTP_PPTENABLE = new mstpPtpEnable();
	/*   50 */ public static final mstpWorkMode MSTP_WORKMODE = new mstpWorkMode();
	/*   51 */ public static final mstpPortType2 MSTP_PORT_TYPE2 = new mstpPortType2();
	/*   52 */ public static final mstpPortServiceType MSTP_PORT_SERVICETYPE = new mstpPortServiceType();
	/*   53 */ public static final mstpNanfcMode MSTP_NANFCMODE = new mstpNanfcMode();
	/*   54 */ public static final mstpAnfcMode MSTP_ANFCMODE = new mstpAnfcMode();
	/*   55 */ public static final mstpBmsgsuppress MSTP_BMSGSUPPRESS = new mstpBmsgsuppress();
	/*   56 */ public static final mstpEdetect MSTP_EDETECT = new mstpEdetect();
	/*   57 */ public static final mstpENCAPPROTOCOL MSTP_ENCAPPROTOCOL = new mstpENCAPPROTOCOL();
	/*   58 */ public static final mstpENCAPFORMAT MSTP_ENCAPFORMAT = new mstpENCAPFORMAT();
	/*   59 */ public static final mstpLcasFlag MSTP_LCAS_FLAG = new mstpLcasFlag();
	/*   60 */ public static final mstpSCRAMBEL MSTP_SCRAMBEL = new mstpSCRAMBEL();
	/*   61 */ public static final mstpEXTENDEADER MSTP_EXTENDEADER = new mstpEXTENDEADER();
	/*   62 */ public static final mstpCFLEN MSTP_CFLEN = new mstpCFLEN();
	/*   63 */ public static final mstpFCSCALSEQ MSTP_FCSCALSEQ = new mstpFCSCALSEQ();
	/*   64 */ public static final mstpBindPathLevel MSTP_BINDPATHLEVEL = new mstpBindPathLevel();
	/*   65 */ public static final mstpBindPathDir MSTP_BINDPATHDIR = new mstpBindPathDir();
	/*   66 */ public static final mstpEthServiceType MSTP_ETHSERVICETYPE = new mstpEthServiceType();
	/*   67 */ public static final mstpEthServiceDirection MSTP_ETHSERVICEDIRECTION = new mstpEthServiceDirection();
	/*   68 */ public static final CheckState checkState = new CheckState();
	/*   69 */ public static final CheckResult checkResult = new CheckResult();
	/*   70 */ public static final BranchCheckResult branchCheckResult = new BranchCheckResult();
	/*   71 */ public static final CheckTaskState checkTaskstate = new CheckTaskState();
	/*   72 */ public static final GuideLine guideLine = new GuideLine();
	/*   73 */ public static final SwitchElementType SWITCHELEMENT_TYPE = new SwitchElementType();
	/*   74 */ public static final Position positiion = new Position();
	/*   75 */ public static final SuppressType SUPPRESS_TYPE = new SuppressType();
	/*   76 */ public static final SuppressTypeSdh SUPPRESS_TYPE_SDH = new SuppressTypeSdh();
	/*   77 */ public static final SuppressTypeWdm SUPPRESS_TYPE_WDM = new SuppressTypeWdm();
	/*   78 */ public static final RootAlarmType ROOT_ALARM_TYPE = new RootAlarmType();
	/*   79 */ public static final AlarmDetectDirection ALARM_DETECT_DIRECTION = new AlarmDetectDirection();
	/*   80 */ public static final AlarmDetectDirectionNo ALARM_DETECT_DIRECTION_NO = new AlarmDetectDirectionNo();
	/*   81 */ public static final DeviceColorCfgEnum DEVICE_COLOR_CFG_ENUM = new DeviceColorCfgEnum();
	/*   82 */ public static final DetectBoxid DETECT_BOXID = new DetectBoxid();
	/*   83 */ public static final DfPortCheckEnum DF_PORT_CHECK_ENUM = new DfPortCheckEnum();
	/*      */
	/*   85 */ public static final SncState SNC_STATE = new SncState();
	/*   86 */ public static final SncType SNC_TYPE = new SncType();
	/*   87 */ public static final SncASFlag SNC_AS_FLAG = new SncASFlag();
	/*   88 */ public static final SncProtectLevel SNS_PROTECT_LEVEL = new SncProtectLevel();
	/*   89 */ public static final SncReRouteAllow SNC_REROUTE_ALLOW = new SncReRouteAllow();
	/*   90 */ public static final SncNetWorkRoute SNC_NETWORK_ROUTE = new SncNetWorkRoute();
	/*   91 */ public static final ImpedanceType IMPEDANCE = new ImpedanceType();
	/*      */
	/*   93 */ public static final ApplyType APPLY_TYPE = new ApplyType();
	/*   94 */ public static final LinePairType LINE_PAIR_TYPE = new LinePairType();
	/*   95 */ public static final ExtendType EXTEND_TYPE = new ExtendType();
	/*   96 */ public static final InterFaceType INTERFACE_TYPE = new InterFaceType();
	/*   97 */ public static final LinePairUseState LINE_PAIR_USE_STATE = new LinePairUseState();
	/*   98 */ public static final LinePairPhyAble LINE_PAIR_PHY_ABLE = new LinePairPhyAble();
	/*   99 */ public static final ResourceType RESOURCE_TYPE = new ResourceType();
	/*  100 */ public static final PreWriteResState writeRes_State = new PreWriteResState();
	/*      */
	/*  104 */ public static final LoginLog LOGIN_LOG = new LoginLog();

	/*      */
	/*      */ public static class ApplyType extends GenericEnum
	/*      */ {
		/*      */ public static final long _ims = 1L;
		/*      */ public static final long _wlan = 2L;
		/*      */ public static final long _per = 3L;

		/*      */
		/*      */ private ApplyType()
		/*      */ {
			/* 1557 */ super.putEnum(Long.valueOf(1L), "IMS");
			/* 1558 */ super.putEnum(Long.valueOf(2L), "WLAN");
			/* 1559 */ super.putEnum(Long.valueOf(3L), "个人/政企");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class ImpedanceType extends GenericEnum
	/*      */ {
		/*      */ public static final long _unknown = 1L;
		/*      */ public static final long _75 = 2L;
		/*      */ public static final long _120 = 3L;

		/*      */
		/*      */ private ImpedanceType()
		/*      */ {
			/* 1545 */ super.putEnum(Long.valueOf(1L), "未知");
			/* 1546 */ super.putEnum(Long.valueOf(2L), "75欧姆");
			/* 1547 */ super.putEnum(Long.valueOf(3L), "120欧姆");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SncNetWorkRoute extends GenericEnum
	/*      */ {
		/*      */ public static final long _NA = 1L;
		/*      */ public static final long _NO = 2L;
		/*      */ public static final long _YES = 3L;

		/*      */
		/*      */ private SncNetWorkRoute()
		/*      */ {
			/* 1533 */ super.putEnum(Long.valueOf(1L), "未知");
			/* 1534 */ super.putEnum(Long.valueOf(2L), "不能");
			/* 1535 */ super.putEnum(Long.valueOf(3L), "能");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SncReRouteAllow extends GenericEnum
	/*      */ {
		/*      */ public static final long _emsDo = 1L;
		/*      */ public static final long _allow = 2L;
		/*      */ public static final long _notAllow = 3L;

		/*      */
		/*      */ private SncReRouteAllow()
		/*      */ {
			/* 1518 */ super.putEnum(Long.valueOf(1L), "让EMS自己决定");
			/* 1519 */ super.putEnum(Long.valueOf(2L), "允许");
			/* 1520 */ super.putEnum(Long.valueOf(3L), "不允许");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SncProtectLevel extends GenericEnum
	/*      */ {
		/*      */ public static final long _PREEMPTIBLE = 1L;
		/*      */ public static final long _UNPROTECTED = 2L;
		/*      */ public static final long _PARTIALLY_PROTECTED = 3L;
		/*      */ public static final long _FULLY_PROTECTED = 4L;
		/*      */ public static final long _HIGHLY_PROTECTED = 5L;

		/*      */
		/*      */ private SncProtectLevel()
		/*      */ {
			/* 1501 */ super.putEnum(Long.valueOf(1L), "可预占的");
			/* 1502 */ super.putEnum(Long.valueOf(2L), "没有保护的");
			/* 1503 */ super.putEnum(Long.valueOf(3L), "部分被保护");
			/* 1504 */ super.putEnum(Long.valueOf(4L), "被完整的保护");
			/* 1505 */ super.putEnum(Long.valueOf(5L), "被高度保护");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SncASFlag extends GenericEnum
	/*      */ {
		/*      */ public static final long _notAs = 1L;
		/*      */ public static final long _partAs = 2L;
		/*      */ public static final long _fullAs = 3L;

		/*      */
		/*      */ private SncASFlag()
		/*      */ {
			/* 1484 */ super.putEnum(Long.valueOf(1L), "非智能");
			/* 1485 */ super.putEnum(Long.valueOf(2L), "智能融合");
			/* 1486 */ super.putEnum(Long.valueOf(3L), "纯智能");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SncType extends GenericEnum
	/*      */ {
		/*      */ public static final long _ST_SIMPLE = 1L;
		/*      */ public static final long _ST_ADD_DROP_A = 2L;
		/*      */ public static final long _ST_ADD_DROP_Z = 3L;

		/*      */
		/*      */ private SncType()
		/*      */ {
			/* 1470 */ super.putEnum(Long.valueOf(1L), "ST_SIMPLE");
			/* 1471 */ super.putEnum(Long.valueOf(2L), "ST_ADD_DROP_A");
			/* 1472 */ super.putEnum(Long.valueOf(3L), "ST_ADD_DROP_Z");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SncState extends GenericEnum
	/*      */ {
		/*      */ public static final long _active = 1L;
		/*      */ public static final long _pending = 2L;
		/*      */ public static final long _partial = 3L;
		/*      */ public static final long _notexistent = 4L;

		/*      */
		/*      */ private SncState()
		/*      */ {
			/* 1453 */ super.putEnum(Long.valueOf(1L), "active");
			/* 1454 */ super.putEnum(Long.valueOf(2L), "pending");
			/* 1455 */ super.putEnum(Long.valueOf(3L), "partial");
			/* 1456 */ super.putEnum(Long.valueOf(4L), "notexistent");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class DfPortCheckEnum extends GenericEnum
	/*      */ {
		/*      */ public static final long _Success = 0L;
		/*      */ public static final long _NoConnect = 1L;
		/*      */ public static final long _LostConnect = 2L;
		/*      */ public static final long _ExceptionPort = 3L;
		/*      */ public static final long _ExceptionEndPTPPort = 4L;
		/*      */ public static final long _UnKnowError = 5L;

		/*      */
		/*      */ private DfPortCheckEnum()
		/*      */ {
			/* 1438 */ super.putEnum(Long.valueOf(0L), "核查成功");
			/* 1439 */ super.putEnum(Long.valueOf(1L), "无连接信息");
			/* 1440 */ super.putEnum(Long.valueOf(2L), "缺少连接信息");
			/* 1441 */ super.putEnum(Long.valueOf(3L), "连接关系异常");
			/* 1442 */ super.putEnum(Long.valueOf(4L), "电路路由信息与配线架连接信息不一致");
			/* 1443 */ super.putEnum(Long.valueOf(5L), "未知错误");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class DetectBoxid extends GenericEnum
	/*      */ {
		/*      */ public static final long _1 = 1L;
		/*      */ public static final long _2 = 2L;
		/*      */ public static final long _3 = 3L;
		/*      */ public static final long _4 = 4L;
		/*      */ public static final long _5 = 5L;

		/*      */
		/*      */ private DetectBoxid()
		/*      */ {
			/* 1411 */ super.putEnum(Long.valueOf(1L), "1");
			/* 1412 */ super.putEnum(Long.valueOf(2L), "2");
			/* 1413 */ super.putEnum(Long.valueOf(3L), "3");
			/* 1414 */ super.putEnum(Long.valueOf(4L), "4");
			/* 1415 */ super.putEnum(Long.valueOf(5L), "5");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class DeviceColorCfgEnum extends GenericEnum
	/*      */ {
		/* 1379 */ public static String RACK = "RACK";
		/* 1380 */ public static String SUBRACK = "SUBRACK";
		/* 1381 */ public static String SHELF = "SHELF";
		/* 1382 */ public static String SUBSHELF = "SUBSHELF";
		/* 1383 */ public static String SLOT = "SLOT";
		/* 1384 */ public static String SUBSLOT = "SUBSLOT";
		/* 1385 */ public static String CARD = "CARD";
		/* 1386 */ public static String ODFMODULE = "ODFMODULE";
		/* 1387 */ public static String DDFMODULE = "DDFMODULE";

		/*      */
		/*      */ private DeviceColorCfgEnum() {
			/* 1390 */ super.putEnum(RACK, "机架");
			/* 1391 */ super.putEnum(SUBRACK, "子架");
			/* 1392 */ super.putEnum(SHELF, "机框");
			/* 1393 */ super.putEnum(SUBSHELF, "子框");
			/* 1394 */ super.putEnum(SLOT, "机槽");
			/* 1395 */ super.putEnum(SUBSLOT, "子槽");
			/* 1396 */ super.putEnum(CARD, "机盘");
			/* 1397 */ super.putEnum(ODFMODULE, "ODM模块");
			/* 1398 */ super.putEnum(DDFMODULE, "DDF模块");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class AlarmDetectDirectionNo extends GenericEnum
	/*      */ {
		/*      */ public static final int _NA = 3;

		/*      */
		/*      */ private AlarmDetectDirectionNo()
		/*      */ {
			/* 1375 */ super.putEnum(Integer.valueOf(3), "无");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class AlarmDetectDirection extends GenericEnum
	/*      */ {
		/*      */ public static final int _inner = 1;
		/*      */ public static final int _outer = 2;
		/*      */ public static final int _NA = 3;

		/*      */
		/*      */ private AlarmDetectDirection()
		/*      */ {
			/* 1365 */ super.putEnum(Integer.valueOf(3), "无");
			/* 1366 */ super.putEnum(Integer.valueOf(1), "内部");
			/* 1367 */ super.putEnum(Integer.valueOf(2), "外部");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class RootAlarmType extends GenericEnum
	/*      */ {
		/*      */ public static final int _NA = 1;
		/*      */ public static final int _NotRoot = 2;
		/*      */ public static final int _SimpleRoot = 3;
		/*      */ public static final int _RealRoot = 4;

		/*      */
		/*      */ private RootAlarmType()
		/*      */ {
			/* 1352 */ super.putEnum(Integer.valueOf(1), "未处理");
			/* 1353 */ super.putEnum(Integer.valueOf(2), "衍生告警");
			/* 1354 */ super.putEnum(Integer.valueOf(3), "单根告警");
			/* 1355 */ super.putEnum(Integer.valueOf(4), "根告警");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SuppressTypeWdm extends GenericEnum
	/*      */ {
		/*      */ public static final int _UpServ = 1;
		/*      */ public static final int _DownServ = 2;
		/*      */ public static final int _SamePort = 3;
		/*      */ public static final int _ServiceEnd = 4;
		/*      */ public static final int _TopoEnd = 7;
		/*      */ public static final int _ServiceSide = 8;
		/*      */ public static final int _WaveSide = 9;

		/*      */
		/*      */ private SuppressTypeWdm()
		/*      */ {
			/* 1317 */ super.putEnum(Integer.valueOf(1), "业务上游");
			/* 1318 */ super.putEnum(Integer.valueOf(2), "业务下游");
			/* 1319 */ super.putEnum(Integer.valueOf(3), "同端口");
			/* 1320 */ super.putEnum(Integer.valueOf(4), "业务对端");
			/* 1321 */ super.putEnum(Integer.valueOf(7), "拓扑对端");
			/* 1322 */ super.putEnum(Integer.valueOf(8), "业务侧");
			/* 1323 */ super.putEnum(Integer.valueOf(9), "波分侧");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SuppressTypeSdh extends GenericEnum
	/*      */ {
		/*      */ public static final int _UpServ = 1;
		/*      */ public static final int _DownServ = 2;
		/*      */ public static final int _SamePort = 3;
		/*      */ public static final int _ServiceEnd = 4;
		/*      */ public static final int _MSEnd = 5;
		/*      */ public static final int _RSEnd = 6;
		/*      */ public static final int _TopoEnd = 7;

		/*      */
		/*      */ private SuppressTypeSdh()
		/*      */ {
			/* 1296 */ super.putEnum(Integer.valueOf(1), "业务上游");
			/* 1297 */ super.putEnum(Integer.valueOf(2), "业务下游");
			/* 1298 */ super.putEnum(Integer.valueOf(3), "同端口");
			/* 1299 */ super.putEnum(Integer.valueOf(4), "业务对端");
			/* 1300 */ super.putEnum(Integer.valueOf(5), "复用段对端");
			/* 1301 */ super.putEnum(Integer.valueOf(6), "再生段对端");
			/* 1302 */ super.putEnum(Integer.valueOf(7), "拓扑对端");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SuppressType extends GenericEnum
	/*      */ {
		/*      */ public static final int _UpServ = 1;
		/*      */ public static final int _DownServ = 2;
		/*      */ public static final int _SamePort = 3;
		/*      */ public static final int _ServiceEnd = 4;
		/*      */ public static final int _MSEnd = 5;
		/*      */ public static final int _RSEnd = 6;
		/*      */ public static final int _TopoEnd = 7;
		/*      */ public static final int _ServiceSide = 8;
		/*      */ public static final int _WaveSide = 9;

		/*      */
		/*      */ private SuppressType()
		/*      */ {
			/* 1273 */ super.putEnum(Integer.valueOf(1), "业务上游");
			/* 1274 */ super.putEnum(Integer.valueOf(2), "业务下游");
			/* 1275 */ super.putEnum(Integer.valueOf(3), "同端口");
			/* 1276 */ super.putEnum(Integer.valueOf(4), "业务对端");
			/* 1277 */ super.putEnum(Integer.valueOf(5), "复用段对端");
			/* 1278 */ super.putEnum(Integer.valueOf(6), "再生段对端");
			/* 1279 */ super.putEnum(Integer.valueOf(7), "拓扑对端");
			/* 1280 */ super.putEnum(Integer.valueOf(8), "业务侧");
			/* 1281 */ super.putEnum(Integer.valueOf(9), "波分侧");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SwitchElementType extends GenericEnum
	/*      */ {
		/*      */ public static final long _nul = 1L;
		/*      */ public static final long _insert = 2L;
		/*      */ public static final long _order = 3L;

		/*      */
		/*      */ private SwitchElementType()
		/*      */ {
			/* 1255 */ super.putEnum(Long.valueOf(1L), "未知");
			/* 1256 */ super.putEnum(Long.valueOf(2L), "间插方式");
			/* 1257 */ super.putEnum(Long.valueOf(3L), "顺序方式");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class GuideLine extends GenericEnum
	/*      */ {
		/*      */ public static final long _AU4 = 1L;
		/*      */ public static final long _2M = 2L;
		/*      */ public static final long _transPath = 3L;
		/*      */ public static final long _2MPtp = 4L;
		/*      */ public static final long _idleChildHole = 5L;
		/*      */ public static final long _fiberUsingRate = 6L;

		/*      */
		/*      */ private GuideLine()
		/*      */ {
			/* 1237 */ super.putEnum(Long.valueOf(1L), "AU4Spare");
			/* 1238 */ super.putEnum(Long.valueOf(2L), "2MUseRate");
			/* 1239 */ super.putEnum(Long.valueOf(3L), "TransPathUseRate");
			/* 1240 */ super.putEnum(Long.valueOf(4L), "2MPortUseRate");
			/* 1241 */ super.putEnum(Long.valueOf(5L), "IdleChildHole");
			/* 1242 */ super.putEnum(Long.valueOf(6L), "FiberUsingRate");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class CheckTaskState extends GenericEnum
	/*      */ {
		/*      */ public static final long _notcheck = 1L;
		/*      */ public static final long _checking = 2L;
		/*      */ public static final long _checkend = 3L;
		/*      */ public static final long _checkExceptionend = 4L;
		/*      */ public static final long _checkPaused = 5L;

		/*      */
		/*      */ private CheckTaskState()
		/*      */ {
			/* 1159 */ super.putEnum(Long.valueOf(1L), "未核查");
			/* 1160 */ super.putEnum(Long.valueOf(2L), "正在核查");
			/* 1161 */ super.putEnum(Long.valueOf(3L), "核查结束");
			/* 1162 */ super.putEnum(Long.valueOf(4L), "核查异常终止");
			/* 1163 */ super.putEnum(Long.valueOf(5L), "核查暂停");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class AlarmImportance extends GenericEnum
	/*      */ {
		/*      */ public static final long _all = 0L;
		/*      */ public static final long _opticalAlarm = 1L;
		/*      */ public static final long _branckOpticalAlarm = 2L;
		/*      */ public static final long _other = 3L;
		/*      */ }

	/*      */
	/*      */ public static class BranchCheckResult extends GenericEnum
	/*      */ {
		/*      */ public static final long _unknow = 1L;
		/*      */ public static final long _allsucess = 2L;
		/*      */ public static final long _partsucess = 3L;
		/*      */ public static final long _allfail = 4L;

		/*      */
		/*      */ private BranchCheckResult()
		/*      */ {
			/* 1131 */ super.putEnum(Long.valueOf(1L), "未知");
			/* 1132 */ super.putEnum(Long.valueOf(2L), "全部成功");
			/* 1133 */ super.putEnum(Long.valueOf(3L), "部分成功");
			/* 1134 */ super.putEnum(Long.valueOf(4L), "全部失败");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class CheckResult extends GenericEnum
	/*      */ {
		/*      */ public static final long _unknow = 1L;
		/*      */ public static final long _sucess = 2L;
		/*      */ public static final long _fail = 3L;

		/*      */
		/*      */ private CheckResult()
		/*      */ {
			/* 1117 */ super.putEnum(Long.valueOf(1L), "未知");
			/* 1118 */ super.putEnum(Long.valueOf(2L), "成功");
			/* 1119 */ super.putEnum(Long.valueOf(3L), "失败");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class CheckState extends GenericEnum
	/*      */ {
		/*      */ public static final long _notcheck = 1L;
		/*      */ public static final long _checking = 2L;
		/*      */ public static final long _checkend = 3L;

		/*      */
		/*      */ private CheckState()
		/*      */ {
			/* 1104 */ super.putEnum(Long.valueOf(1L), "未核查");
			/* 1105 */ super.putEnum(Long.valueOf(2L), "正在核查");
			/* 1106 */ super.putEnum(Long.valueOf(3L), "核查结束");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class PathState extends GenericEnum
	/*      */ {
		/*      */ public static final long _idle = 1L;
		/*      */ public static final long _inuse = 2L;
		/*      */ public static final long _preuse = 3L;
		/*      */ public static final long _rddc = 4L;
		/*      */ public static final long _resKeep = 5L;

		/*      */
		/*      */ private PathState()
		/*      */ {
			/* 1089 */ super.putEnum(Long.valueOf(1L), "空闲");
			/* 1090 */ super.putEnum(Long.valueOf(2L), "已用");
			/* 1091 */ super.putEnum(Long.valueOf(3L), "预占");
			/* 1092 */ super.putEnum(Long.valueOf(4L), "冗余");
			/* 1093 */ super.putEnum(Long.valueOf(5L), "保留");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class ProtectMode extends GenericEnum
	/*      */ {
		/*      */ public static final long _idle = 1L;
		/*      */ public static final long _inuse = 2L;

		/*      */
		/*      */ private ProtectMode()
		/*      */ {
			/* 1074 */ super.putEnum(Long.valueOf(1L), "PA_NA");
			/* 1075 */ super.putEnum(Long.valueOf(2L), "PA_PSR_RELATED");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class Rate extends GenericEnum
	/*      */ {
		/*      */ public static final long _2M = 1L;
		/*      */ public static final long _8M = 2L;
		/*      */ public static final long _10M = 3L;
		/*      */ public static final long _34M = 4L;
		/*      */ public static final long _45M = 5L;
		/*      */ public static final long _68M = 6L;
		/*      */ public static final long _100M = 7L;
		/*      */ public static final long _140M = 8L;
		/*      */ public static final long _155M = 9L;
		/*      */ public static final long _280M = 10L;
		/*      */ public static final long _310M = 11L;
		/*      */ public static final long _565M = 12L;
		/*      */ public static final long _622M = 13L;
		/*      */ public static final long _1G = 14L;
		/*      */ public static final long _1D25G = 15L;
		/*      */ public static final long _2D5G = 16L;
		/*      */ public static final long _10G = 17L;
		/*      */ public static final long _20G = 18L;
		/*      */ public static final long _40G = 19L;
		/*      */ public static final long _80G = 20L;
		/*      */ public static final long _120G = 21L;
		/*      */ public static final long _6M = 22L;
		/*      */ public static final long _12M = 23L;
		/*      */ public static final long _16M = 24L;
		/*      */ public static final long _4M = 25L;
		/*      */ public static final long _64k = 26L;
		/*      */ public static final long _NA = 27L;
		/*      */ public static final long _3D5G = 28L;
		/*      */ public static final long _320G = 29L;
		/*      */ public static final long _400G = 30L;
		/*      */ public static final long _800G = 31L;
		/*      */ public static final long _1600G = 32L;
		/*      */ public static final long _32M = 33L;
		/*      */ public static final long _FE = 34L;
		/*      */ public static final long _GE = 35L;
		/*      */ public static final long _10GE = 36L;
		/*      */ public static final long _FC100 = 37L;
		/*      */ public static final long _FC200 = 38L;
		/*      */ public static final long _FC400 = 39L;
		/*      */ public static final long _FC800 = 40L;
		/*      */ public static final long _40GE = 41L;
		/*      */ public static final long _4G = 42L;
		/*      */ public static final long _8G = 43L;
		/*      */ public static final long _100GE = 44L;
		/*      */ public static final long _256S = 45L;
		/*      */ public static final long _FC = 46L;
		/*      */ public static final long _100G = 47L;

		/*      */
		/*      */ private Rate()
		/*      */ {
			/* 1011 */ super.putEnum(Long.valueOf(1L), "2M");
			/* 1012 */ super.putEnum(Long.valueOf(2L), "8M");
			/* 1013 */ super.putEnum(Long.valueOf(3L), "10M");
			/* 1014 */ super.putEnum(Long.valueOf(4L), "34M");
			/* 1015 */ super.putEnum(Long.valueOf(5L), "45M");
			/* 1016 */ super.putEnum(Long.valueOf(6L), "68M");
			/* 1017 */ super.putEnum(Long.valueOf(7L), "100M");
			/* 1018 */ super.putEnum(Long.valueOf(8L), "140M");
			/* 1019 */ super.putEnum(Long.valueOf(9L), "155M");
			/* 1020 */ super.putEnum(Long.valueOf(10L), "280M");
			/* 1021 */ super.putEnum(Long.valueOf(11L), "310M");
			/* 1022 */ super.putEnum(Long.valueOf(12L), "565M");
			/* 1023 */ super.putEnum(Long.valueOf(13L), "622M");
			/* 1024 */ super.putEnum(Long.valueOf(14L), "1G");
			/* 1025 */ super.putEnum(Long.valueOf(15L), "1.25G");
			/* 1026 */ super.putEnum(Long.valueOf(16L), "2.5G");
			/* 1027 */ super.putEnum(Long.valueOf(17L), "10G");
			/* 1028 */ super.putEnum(Long.valueOf(18L), "20G");
			/* 1029 */ super.putEnum(Long.valueOf(19L), "40G");
			/* 1030 */ super.putEnum(Long.valueOf(20L), "80G");
			/* 1031 */ super.putEnum(Long.valueOf(21L), "120G");
			/* 1032 */ super.putEnum(Long.valueOf(22L), "6M");
			/* 1033 */ super.putEnum(Long.valueOf(23L), "12M");
			/* 1034 */ super.putEnum(Long.valueOf(24L), "16M");
			/* 1035 */ super.putEnum(Long.valueOf(25L), "4M");
			/* 1036 */ super.putEnum(Long.valueOf(26L), "64k");
			/* 1037 */ super.putEnum(Long.valueOf(27L), "NA");
			/* 1038 */ super.putEnum(Long.valueOf(28L), "3.5G");
			/*      */
			/* 1041 */ super.putEnum(Long.valueOf(29L), "320G");
			/* 1042 */ super.putEnum(Long.valueOf(30L), "400G");
			/* 1043 */ super.putEnum(Long.valueOf(31L), "800G");
			/* 1044 */ super.putEnum(Long.valueOf(32L), "1600G");
			/*      */
			/* 1046 */ super.putEnum(Long.valueOf(33L), "32M");
			/*      */
			/* 1049 */ super.putEnum(Long.valueOf(34L), "FE");
			/* 1050 */ super.putEnum(Long.valueOf(35L), "GE");
			/* 1051 */ super.putEnum(Long.valueOf(36L), "10GE");
			/* 1052 */ super.putEnum(Long.valueOf(37L), "FC100");
			/* 1053 */ super.putEnum(Long.valueOf(38L), "FC200");
			/* 1054 */ super.putEnum(Long.valueOf(39L), "FC400");
			/* 1055 */ super.putEnum(Long.valueOf(40L), "FC800");
			/* 1056 */ super.putEnum(Long.valueOf(41L), "40GE");
			/* 1057 */ super.putEnum(Long.valueOf(42L), "4G");
			/* 1058 */ super.putEnum(Long.valueOf(43L), "8G");
			/* 1059 */ super.putEnum(Long.valueOf(44L), "100GE");
			/* 1060 */ super.putEnum(Long.valueOf(45L), "256S");
			/* 1061 */ super.putEnum(Long.valueOf(46L), "FC");
			/* 1062 */ super.putEnum(Long.valueOf(47L), "100G");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class AtmType extends GenericEnum
	/*      */ {
		/*      */ public static final long ATM = 1L;
		/*      */ public static final long ATMTRUNK = 2L;
		/*      */ public static final long RPR = 3L;

		/*      */
		/*      */ private AtmType()
		/*      */ {
			/*  947 */ super.putEnum(Long.valueOf(1L), "ATM");
			/*  948 */ super.putEnum(Long.valueOf(2L), "ATMTRUNK");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class EthType extends GenericEnum
	/*      */ {
		/*      */ public static final long MAC = 1L;
		/*      */ public static final long MP = 2L;
		/*      */ public static final long RPR = 3L;

		/*      */
		/*      */ private EthType()
		/*      */ {
			/*  933 */ super.putEnum(Long.valueOf(1L), "MAC");
			/*  934 */ super.putEnum(Long.valueOf(2L), "MP");
			/*  935 */ super.putEnum(Long.valueOf(3L), "RPR");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class Domain extends GenericEnum
	/*      */ {
		/*      */ public static final long SDH = 1L;
		/*      */ public static final long ETH = 2L;
		/*      */ public static final long ATM = 3L;
		/*      */ public static final long UNKNOWN = 0L;
		/*      */ public static final long WDM = 5L;
		/*      */ public static final long RTN = 6L;
		/*      */ public static final long PTN = 7L;
		/*      */ public static final long LAG = 8L;
		/*      */ public static final long IMA = 9L;

		/*      */
		/*      */ private Domain()
		/*      */ {
			/*  913 */ super.putEnum(Long.valueOf(1L), "sdh");
			/*  914 */ super.putEnum(Long.valueOf(2L), "eth");
			/*  915 */ super.putEnum(Long.valueOf(3L), "atm");
			/*  916 */ super.putEnum(Long.valueOf(0L), "未知");
			/*  917 */ super.putEnum(Long.valueOf(5L), "wdm");
			/*  918 */ super.putEnum(Long.valueOf(6L), "rtn");
			/*  919 */ super.putEnum(Long.valueOf(7L), "ptn");
			/*  920 */ super.putEnum(Long.valueOf(8L), "lag");
			/*  921 */ super.putEnum(Long.valueOf(9L), "ima");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class IsConnState extends GenericEnum
	/*      */ {
		/*      */ public static final long Y = 1L;
		/*      */ public static final long N = 0L;

		/*      */
		/*      */ private IsConnState()
		/*      */ {
			/*  894 */ super.putEnum(Long.valueOf(1L), "TRUE");
			/*  895 */ super.putEnum(Long.valueOf(0L), "FALSE");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class Position extends GenericEnum
	/*      */ {
		/*      */ public static final long POSITION_INNER_BOTTOM = 10L;
		/*      */ public static final long POSITION_INNER_LEFT = 12L;
		/*      */ public static final long POSITION_INNER_RIGHT = 13L;
		/*      */ public static final long POSITION_INNER_TOP = 11L;
		/*      */ public static final long POSITION_CENTER = 1L;

		/*      */
		/*      */ private Position()
		/*      */ {
			/*  879 */ super.putEnum(Long.valueOf(1L), "中间");
			/*  880 */ super.putEnum(Long.valueOf(11L), "上面");
			/*  881 */ super.putEnum(Long.valueOf(10L), "下面");
			/*  882 */ super.putEnum(Long.valueOf(12L), "左面");
			/*  883 */ super.putEnum(Long.valueOf(13L), "右面");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class Directionality extends GenericEnum
	/*      */ {
		/*      */ public static final long _NA = 1L;
		/*      */ public static final long BIDIRECTIONAL = 2L;
		/*      */ public static final long SOURCE = 3L;
		/*      */ public static final long SINK = 4L;

		/*      */
		/*      */ private Directionality()
		/*      */ {
			/*  864 */ super.putEnum(Long.valueOf(1L), "NA");
			/*  865 */ super.putEnum(Long.valueOf(2L), "双向");
			/*  866 */ super.putEnum(Long.valueOf(3L), "源");
			/*  867 */ super.putEnum(Long.valueOf(4L), "宿");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class TerminationMode extends GenericEnum
	/*      */ {
		/*      */ public static final long NA = 1L;
		/*      */ public static final long TERMINATED_AVAILABLE_MAPPING = 2L;
		/*      */ public static final long NEITHER_TERMINATED_NOR_AVAILABLE_MAPPING = 3L;

		/*      */
		/*      */ private TerminationMode()
		/*      */ {
			/*  849 */ super.putEnum(Long.valueOf(1L), "NA");
			/*  850 */ super.putEnum(Long.valueOf(2L), "TERMINATED_AVAILABLE_MAPPING");
			/*  851 */ super.putEnum(Long.valueOf(3L), "NEITHER_TERMINATED_NOR_AVAILABLE_MAPPING");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class TransPortType1 extends GenericEnum
	/*      */ {
		/*      */ public static final long ELEC_PORT = 1L;
		/*      */ public static final long LIGHT_PORT = 2L;
		/*      */ public static final long UNKNOW_PORT = 0L;

		/*      */
		/*      */ private TransPortType1()
		/*      */ {
			/*  837 */ super.putEnum(Long.valueOf(1L), "电口");
			/*  838 */ super.putEnum(Long.valueOf(2L), "光口");
			/*  839 */ super.putEnum(Long.valueOf(0L), "未知");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class TransPortSubType extends GenericEnum
	/*      */ {
		/*      */ public static final long UNKNOW_PORT = 0L;
		/*      */ public static final long MSTP_PORT = 3L;
		/*      */ public static final long IF_PORT = 5L;
		/*      */ public static final long RF_PORT = 6L;
		/*      */ public static final long IMA_PORT = 7L;
		/*      */ public static final long LAG_PORT = 8L;
		/*      */ public static final long GPON_PORT = 9L;
		/*      */ public static final long EPON_PORT = 10L;
		/*      */ public static final long OLTUP_PORT = 11L;
		/*      */ public static final long LIGHTUP_PORT = 12L;
		/*      */ public static final long LIGHTDOWN_PORT = 13L;
		/*      */ public static final long ONUUP_PORT = 14L;
		/*      */ public static final long FE_PORT = 15L;
		/*      */ public static final long POTS_PORT = 16L;
		/*      */ public static final long SDH_PORT = 21L;
		/*      */ public static final long PDH_PORT = 22L;
		/*      */ public static final long ADSL_PORT = 23L;
		/*      */ public static final long ADSL2_PLUS_PORT = 24L;
		/*      */ public static final long POS_PORT = 25L;
		/*      */ public static final long SHDSL_PORT = 26L;
		/*      */ public static final long VDSL2_PORT = 27L;
		/*      */ public static final long ATM_PORT = 28L;
		/*      */ public static final long ETH = 29L;
		/*      */ public static final long PSTN = 30L;
		/*      */ public static final long WDM_PORT = 31L;
		/*      */ public static final long PTN_PORT = 32L;
		/*      */ public static final long PON_OPT_GE = 33L;
		/*      */ public static final long PON_ELC_GE = 36L;
		/*      */ public static final long PON_ELC_FE = 37L;
		/*      */ public static final long PON_OPT_FE = 38L;
		/*      */ public static final long XDK = 39L;
		/*      */ public static final long PTN_L2_VPORT = 41L;
		/*      */ public static final long PTN_L3_VPORT = 42L;

		/*      */
		/*      */ private TransPortSubType()
		/*      */ {
			/*  766 */ super.putEnum(Long.valueOf(0L), "未知");
			/*      */
			/*  771 */ super.putEnum(Long.valueOf(3L), "MSTP口");
			/*  772 */ super.putEnum(Long.valueOf(5L), "中频口");
			/*  773 */ super.putEnum(Long.valueOf(6L), "射频口");
			/*  774 */ super.putEnum(Long.valueOf(7L), "IMA口");
			/*  775 */ super.putEnum(Long.valueOf(8L), "LAG口");
			/*  776 */ super.putEnum(Long.valueOf(9L), "GPON口");
			/*  777 */ super.putEnum(Long.valueOf(10L), "EPON口");
			/*      */
			/*  779 */ super.putEnum(Long.valueOf(11L), "OLT上联口");
			/*  780 */ super.putEnum(Long.valueOf(12L), "分光器上联口");
			/*  781 */ super.putEnum(Long.valueOf(13L), "分光器下联口");
			/*  782 */ super.putEnum(Long.valueOf(14L), "ONU上联口");
			/*  783 */ super.putEnum(Long.valueOf(15L), "FE");
			/*  784 */ super.putEnum(Long.valueOf(16L), "POTS");
			/*      */
			/*  786 */ super.putEnum(Long.valueOf(21L), "SDH口");
			/*  787 */ super.putEnum(Long.valueOf(22L), "PDH");
			/*  788 */ super.putEnum(Long.valueOf(23L), "ADSL");
			/*  789 */ super.putEnum(Long.valueOf(24L), "ADSL2_PLUS");
			/*  790 */ super.putEnum(Long.valueOf(25L), "POS");
			/*  791 */ super.putEnum(Long.valueOf(26L), "SHDSL");
			/*  792 */ super.putEnum(Long.valueOf(27L), "VDSL2");
			/*  793 */ super.putEnum(Long.valueOf(28L), "ATM");
			/*  794 */ super.putEnum(Long.valueOf(29L), "ETH");
			/*  795 */ super.putEnum(Long.valueOf(30L), "PSTN");
			/*  796 */ super.putEnum(Long.valueOf(31L), "波分口");
			/*  797 */ super.putEnum(Long.valueOf(32L), "PTN口");
			/*      */
			/*  799 */ super.putEnum(Long.valueOf(33L), "光GE口");
			/*  800 */ super.putEnum(Long.valueOf(36L), "电GE口");
			/*  801 */ super.putEnum(Long.valueOf(37L), "电FE口");
			/*  802 */ super.putEnum(Long.valueOf(38L), "光FE口");
			/*  803 */ super.putEnum(Long.valueOf(39L), "虚端口");
			/*  804 */ super.putEnum(Long.valueOf(41L), "2层虚端口");
			/*  805 */ super.putEnum(Long.valueOf(42L), "3层虚端口");
			/*      */ }

		/*      */
		/*      */ private TransPortSubType(long domain)
		/*      */ {
			/*  811 */ if ((domain != 2L) && (domain != 3L)) /*      */ return;
			/*  813 */ super.putEnum(Long.valueOf(3L), "MSTP口");
			/*  814 */ super.putEnum(Long.valueOf(0L), "未知");
			/*      */ }

		/*      */
		/*      */ public TransPortSubType getTransPortSubType(long domain)
		/*      */ {
			/*  820 */ return new TransPortSubType(domain);
			/*      */ }

		/*      */
		/*      */ public TransPortSubType getAll()
		/*      */ {
			/*  825 */ return new TransPortSubType();
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class TransPortType extends GenericEnum
	/*      */ {
		/*      */ public static final long ELEC_PORT = 1L;
		/*      */ public static final long LIGHT_PORT = 2L;
		/*      */ public static final long LOGIC_PORT = 4L;

		/*      */
		/*      */ private TransPortType()
		/*      */ {
			/*  641 */ super.putEnum(Long.valueOf(1L), "电口");
			/*  642 */ super.putEnum(Long.valueOf(2L), "光口");
			/*      */
			/*  644 */ super.putEnum(Long.valueOf(4L), "逻辑口");
			/*      */ }

		/*      */
		/*      */ private TransPortType(long domain)
		/*      */ {
			/*  673 */ if (domain != 1L) /*      */ return;
			/*  675 */ super.putEnum(Long.valueOf(1L), "电口");
			/*  676 */ super.putEnum(Long.valueOf(2L), "光口");
			/*      */ }

		/*      */
		/*      */ public TransPortType getTransPortType(long domain)
		/*      */ {
			/*  682 */ return new TransPortType(domain);
			/*      */ }

		/*      */
		/*      */ public TransPortType getAll()
		/*      */ {
			/*  687 */ return new TransPortType();
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class TransPortStateType extends GenericEnum
	/*      */ {
		/*      */ public static final long _unknown = 0L;
		/*      */ public static final long _idle = 1L;
		/*      */ public static final long _inuse = 2L;
		/*      */ public static final long _preuse = 3L;
		/*      */ public static final long _bad = 4L;
		/*      */ public static final long _resKeep = 5L;

		/*      */
		/*      */ private TransPortStateType()
		/*      */ {
			/*  593 */ super.putEnum(Long.valueOf(0L), "未知");
			/*  594 */ super.putEnum(Long.valueOf(1L), "空闲");
			/*  595 */ super.putEnum(Long.valueOf(2L), "占用");
			/*  596 */ super.putEnum(Long.valueOf(3L), "预占用");
			/*  597 */ super.putEnum(Long.valueOf(4L), "坏端口");
			/*  598 */ super.putEnum(Long.valueOf(5L), "保留");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class ServiceState extends GenericEnum
	/*      */ {
		/*      */ public static final long USER = 1L;
		/*      */ public static final long UNUSER = 2L;

		/*      */
		/*      */ private ServiceState()
		/*      */ {
			/*  578 */ super.putEnum(Long.valueOf(1L), "可用");
			/*  579 */ super.putEnum(Long.valueOf(2L), "不可用");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class ResistanceType extends GenericEnum
	/*      */ {
		/*      */ public static final long _120 = 1L;
		/*      */ public static final long _75 = 2L;
		/*      */ public static final long _fc = 3L;
		/*      */ public static final long _sc = 4L;

		/*      */
		/*      */ private ResistanceType()
		/*      */ {
			/*  566 */ super.putEnum(Long.valueOf(1L), "120欧姆");
			/*  567 */ super.putEnum(Long.valueOf(2L), "75欧姆");
			/*  568 */ super.putEnum(Long.valueOf(3L), "FC");
			/*  569 */ super.putEnum(Long.valueOf(4L), "SC");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SwitchPortType extends GenericEnum
	/*      */ {
		/*      */ public static final long BIDIRECT_ELEMENT = 1L;
		/*      */ public static final long BIDIRECT_FULL = 2L;

		/*      */
		/*      */ private SwitchPortType()
		/*      */ {
			/*  553 */ super.putEnum(Long.valueOf(1L), "双向电");
			/*  554 */ super.putEnum(Long.valueOf(2L), "双向充");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class InterFaceType extends GenericEnum
	/*      */ {
		/*      */ public static final long _UNKNOW = 0L;
		/*      */ public static final long _ETHERNET = 1L;
		/*      */ public static final long _E1 = 2L;

		/*      */
		/*      */ private InterFaceType()
		/*      */ {
			/*  542 */ super.putEnum(Long.valueOf(0L), "未知");
			/*  543 */ super.putEnum(Long.valueOf(1L), "以太网口");
			/*  544 */ super.putEnum(Long.valueOf(2L), "E1");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class SwitchPortStateType extends GenericEnum
	/*      */ {
		/*      */ public static final long _idle = 1L;
		/*      */ public static final long _inuse = 2L;
		/*      */ public static final long _preuse = 3L;
		/*      */ public static final long _prerelease = 4L;
		/*      */ public static final long _prerelease_preuse = 5L;
		/*      */ public static final long _bad = 6L;

		/*      */
		/*      */ private SwitchPortStateType()
		/*      */ {
			/*  527 */ super.putEnum(Long.valueOf(1L), "空闲");
			/*  528 */ super.putEnum(Long.valueOf(2L), "占用");
			/*  529 */ super.putEnum(Long.valueOf(3L), "预占用");
			/*  530 */ super.putEnum(Long.valueOf(4L), "预释放");
			/*  531 */ super.putEnum(Long.valueOf(5L), "预占用释放");
			/*  532 */ super.putEnum(Long.valueOf(6L), "坏端口");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class ServiceType extends GenericEnum
	/*      */ {
		/*      */ public static final long _talk = 1L;
		/*      */ public static final long _dcn = 2L;

		/*      */
		/*      */ private ServiceType()
		/*      */ {
			/*  511 */ super.putEnum(Long.valueOf(1L), "语音");
			/*  512 */ super.putEnum(Long.valueOf(2L), "DCN");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class RackOwnerShip extends GenericEnum
	/*      */ {
		/*      */ public static final long _unknow = 0L;
		/*      */ public static final long _self = 1L;
		/*      */ public static final long _leasehold = 2L;

		/*      */
		/*      */ private RackOwnerShip()
		/*      */ {
			/*  499 */ super.putEnum(Long.valueOf(1L), "自建");
			/*  500 */ super.putEnum(Long.valueOf(2L), "租用");
			/*  501 */ super.putEnum(Long.valueOf(0L), "未知");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpFCSCALSEQ extends GenericEnum
	/*      */ {
		/*      */ public static final long _Littleendian = 1L;
		/*      */ public static final long _Bigendian = 2L;

		/*      */
		/*      */ private mstpFCSCALSEQ()
		/*      */ {
			/*  487 */ super.putEnum(Long.valueOf(1L), "Little endian");
			/*  488 */ super.putEnum(Long.valueOf(2L), "Big endian");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpCFLEN extends GenericEnum
	/*      */ {
		/*      */ public static final long _FCS32 = 1L;
		/*      */ public static final long _FCS16 = 2L;
		/*      */ public static final long _No = 3L;

		/*      */
		/*      */ private mstpCFLEN()
		/*      */ {
			/*  475 */ super.putEnum(Long.valueOf(1L), "FCS32");
			/*  476 */ super.putEnum(Long.valueOf(2L), "FCS16");
			/*  477 */ super.putEnum(Long.valueOf(3L), "No");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpEXTENDEADER extends GenericEnum
	/*      */ {
		/*      */ public static final long _No = 1L;
		/*      */ public static final long _Yes = 2L;

		/*      */
		/*      */ private mstpEXTENDEADER()
		/*      */ {
			/*  463 */ super.putEnum(Long.valueOf(1L), "No");
			/*  464 */ super.putEnum(Long.valueOf(2L), "Yes");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpSCRAMBEL extends GenericEnum
	/*      */ {
		/*      */ public static final long _x431 = 1L;
		/*      */ public static final long _x481 = 2L;
		/*      */ public static final long _NotScramble = 3L;

		/*      */
		/*      */ private mstpSCRAMBEL()
		/*      */ {
			/*  451 */ super.putEnum(Long.valueOf(1L), "[x43+1]");
			/*  452 */ super.putEnum(Long.valueOf(2L), "[x48+1]");
			/*  453 */ super.putEnum(Long.valueOf(3L), "Not Scramble");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpLcasFlag extends GenericEnum
	/*      */ {
		/*      */ public static final long _Enabled = 1L;
		/*      */ public static final long _Disabled = 2L;

		/*      */
		/*      */ private mstpLcasFlag()
		/*      */ {
			/*  439 */ super.putEnum(Long.valueOf(1L), "Enabled");
			/*  440 */ super.putEnum(Long.valueOf(2L), "Disabled");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpENCAPFORMAT extends GenericEnum
	/*      */ {
		/*      */ public static final long _MartinioE = 1L;
		/*      */ public static final long _CCCoE = 2L;
		/*      */ public static final long _VMANoE = 3L;
		/*      */ public static final long _MartinioP = 4L;

		/*      */
		/*      */ private mstpENCAPFORMAT()
		/*      */ {
			/*  426 */ super.putEnum(Long.valueOf(1L), "MartinioE");
			/*  427 */ super.putEnum(Long.valueOf(2L), "CCCoE");
			/*  428 */ super.putEnum(Long.valueOf(3L), "VMANoE");
			/*  429 */ super.putEnum(Long.valueOf(4L), "MartinioP");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpENCAPPROTOCOL extends GenericEnum
	/*      */ {
		/*      */ public static final long _HDLC = 1L;
		/*      */ public static final long _LAPS = 2L;
		/*      */ public static final long _ATM = 3L;
		/*      */ public static final long _PPP = 4L;
		/*      */ public static final long _GFP = 5L;

		/*      */
		/*      */ private mstpENCAPPROTOCOL()
		/*      */ {
			/*  410 */ super.putEnum(Long.valueOf(1L), "HDLC");
			/*  411 */ super.putEnum(Long.valueOf(2L), "LAPS");
			/*  412 */ super.putEnum(Long.valueOf(3L), "ATM");
			/*  413 */ super.putEnum(Long.valueOf(4L), "PPP");
			/*  414 */ super.putEnum(Long.valueOf(5L), "GFP");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpEdetect extends GenericEnum
	/*      */ {
		/*      */ public static final long _Enabled = 1L;
		/*      */ public static final long _Disabled = 2L;

		/*      */
		/*      */ private mstpEdetect()
		/*      */ {
			/*  396 */ super.putEnum(Long.valueOf(1L), "Enabled");
			/*  397 */ super.putEnum(Long.valueOf(2L), "Disabled");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpBmsgsuppress extends GenericEnum
	/*      */ {
		/*      */ public static final long _Enabled = 1L;
		/*      */ public static final long _Disabled = 2L;

		/*      */
		/*      */ private mstpBmsgsuppress()
		/*      */ {
			/*  385 */ super.putEnum(Long.valueOf(1L), "Enabled");
			/*  386 */ super.putEnum(Long.valueOf(2L), "Disabled");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpAnfcMode extends GenericEnum
	/*      */ {
		/*      */ public static final long _Disabled = 1L;
		/*      */ public static final long _NonSymmetricFlowControl = 2L;
		/*      */ public static final long _SymmetricFlowControl = 3L;
		/*      */ public static final long _SymmetricOrNonSymmetricFlowControl = 4L;

		/*      */
		/*      */ private mstpAnfcMode()
		/*      */ {
			/*  372 */ super.putEnum(Long.valueOf(1L), "Disabled");
			/*  373 */ super.putEnum(Long.valueOf(2L), "NonSymmetricFlowControl");
			/*  374 */ super.putEnum(Long.valueOf(3L), "SymmetricFlowControl");
			/*  375 */ super.putEnum(Long.valueOf(4L), "SymmetricOrNonSymmetricFlowControl");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpNanfcMode extends GenericEnum
	/*      */ {
		/*      */ public static final long _Disabled = 1L;
		/*      */ public static final long _SymmetricFlowControl = 2L;
		/*      */ public static final long _SendOnly = 3L;
		/*      */ public static final long _ReceiveOnly = 4L;

		/*      */
		/*      */ private mstpNanfcMode()
		/*      */ {
			/*  357 */ super.putEnum(Long.valueOf(1L), "Disabled");
			/*  358 */ super.putEnum(Long.valueOf(2L), "SymmetricFlowControl");
			/*  359 */ super.putEnum(Long.valueOf(3L), "SendOnly");
			/*  360 */ super.putEnum(Long.valueOf(4L), "ReceiveOnly");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpPortServiceType extends GenericEnum
	/*      */ {
		/*      */ public static final long _SW = 1L;
		/*      */ public static final long _PKT = 2L;

		/*      */
		/*      */ private mstpPortServiceType()
		/*      */ {
			/*  344 */ super.putEnum(Long.valueOf(1L), "SW");
			/*  345 */ super.putEnum(Long.valueOf(2L), "PKT");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpPortType2 extends GenericEnum
	/*      */ {
		/*      */ public static final long _PE = 1L;
		/*      */ public static final long _P = 2L;

		/*      */
		/*      */ private mstpPortType2()
		/*      */ {
			/*  333 */ super.putEnum(Long.valueOf(1L), "PE");
			/*  334 */ super.putEnum(Long.valueOf(2L), "P");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpWorkMode extends GenericEnum
	/*      */ {
		/*      */ public static final long _10Mhalf = 1L;
		/*      */ public static final long _10Mall = 2L;
		/*      */ public static final long _100Mhalf = 3L;
		/*      */ public static final long _100Mall = 4L;
		/*      */ public static final long _arrange = 5L;
		/*      */ public static final long _1000Mhalf = 6L;
		/*      */ public static final long _1000Mall = 7L;

		/*      */
		/*      */ private mstpWorkMode()
		/*      */ {
			/*  317 */ super.putEnum(Long.valueOf(1L), "10M半双工");
			/*  318 */ super.putEnum(Long.valueOf(2L), "10M全双工");
			/*  319 */ super.putEnum(Long.valueOf(3L), "100M半双工");
			/*  320 */ super.putEnum(Long.valueOf(4L), "100M全双工");
			/*  321 */ super.putEnum(Long.valueOf(6L), "1000M半双工");
			/*  322 */ super.putEnum(Long.valueOf(7L), "1000M全双工");
			/*  323 */ super.putEnum(Long.valueOf(5L), "自协商");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpPtpEnable extends GenericEnum
	/*      */ {
		/*      */ public static final long _Enabled = 1L;
		/*      */ public static final long _Disabled = 2L;

		/*      */
		/*      */ private mstpPtpEnable()
		/*      */ {
			/*  301 */ super.putEnum(Long.valueOf(1L), "Enabled");
			/*  302 */ super.putEnum(Long.valueOf(2L), "Disabled");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpPortEnable extends GenericEnum
	/*      */ {
		/*      */ public static final long _Enabled = 1L;
		/*      */ public static final long _Disabled = 2L;

		/*      */
		/*      */ private mstpPortEnable()
		/*      */ {
			/*  290 */ super.putEnum(Long.valueOf(1L), "Enabled");
			/*  291 */ super.putEnum(Long.valueOf(2L), "Disabled");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class msptFlowCtrl extends GenericEnum
	/*      */ {
		/*      */ public static final long _Enabled = 1L;
		/*      */ public static final long _Disabled = 2L;

		/*      */
		/*      */ private msptFlowCtrl()
		/*      */ {
			/*  279 */ super.putEnum(Long.valueOf(1L), "Enabled");
			/*  280 */ super.putEnum(Long.valueOf(2L), "Disabled");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpTagFlag extends GenericEnum
	/*      */ {
		/*      */ public static final long _tagaware = 1L;
		/*      */ public static final long _access = 2L;
		/*      */ public static final long _hybrid = 3L;

		/*      */
		/*      */ private mstpTagFlag()
		/*      */ {
			/*  267 */ super.putEnum(Long.valueOf(1L), "TagAware");
			/*  268 */ super.putEnum(Long.valueOf(2L), "Access");
			/*  269 */ super.putEnum(Long.valueOf(3L), "Hybrid");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpPortType extends GenericEnum
	/*      */ {
		/*      */ public static final long _mac = 1L;
		/*      */ public static final long _ethtrunk = 2L;
		/*      */ public static final long _rpr = 3L;
		/*      */ public static final long _atm = 4L;
		/*      */ public static final long _atmtrunk = 5L;
		/*      */ public static final long _lp = 6L;

		/*      */
		/*      */ private mstpPortType()
		/*      */ {
			/*  251 */ super.putEnum(Long.valueOf(1L), "MAC");
			/*  252 */ super.putEnum(Long.valueOf(2L), "MP");
			/*  253 */ super.putEnum(Long.valueOf(3L), "RPR");
			/*  254 */ super.putEnum(Long.valueOf(4L), "ATM");
			/*  255 */ super.putEnum(Long.valueOf(5L), "ATMTRUNK");
			/*  256 */ super.putEnum(Long.valueOf(6L), "LP");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpBindPathLevel extends GenericEnum
	/*      */ {
		/*      */ public static final long _VC4 = 1L;
		/*      */ public static final long _VC3 = 2L;
		/*      */ public static final long _VC12 = 3L;

		/*      */
		/*      */ private mstpBindPathLevel()
		/*      */ {
			/*  235 */ super.putEnum(Long.valueOf(1L), "VC4");
			/*  236 */ super.putEnum(Long.valueOf(2L), "VC3");
			/*  237 */ super.putEnum(Long.valueOf(3L), "VC12");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpBindPathDir extends GenericEnum
	/*      */ {
		/*      */ public static final long _None = 1L;
		/*      */ public static final long _DoubleDir = 2L;
		/*      */ public static final long _Upper = 3L;
		/*      */ public static final long _Down = 4L;

		/*      */
		/*      */ private mstpBindPathDir()
		/*      */ {
			/*  218 */ super.putEnum(Long.valueOf(1L), "空");
			/*  219 */ super.putEnum(Long.valueOf(2L), "双向");
			/*  220 */ super.putEnum(Long.valueOf(3L), "上行");
			/*  221 */ super.putEnum(Long.valueOf(4L), "下行");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpEthServiceType extends GenericEnum
	/*      */ {
		/*      */ public static final long _EPL = 1L;
		/*      */ public static final long _EVPL = 2L;
		/*      */ public static final long _EPLAN = 3L;
		/*      */ public static final long _EVPLAN = 4L;

		/*      */
		/*      */ private mstpEthServiceType()
		/*      */ {
			/*  202 */ super.putEnum(Long.valueOf(1L), "EPL");
			/*  203 */ super.putEnum(Long.valueOf(2L), "EVPL");
			/*  204 */ super.putEnum(Long.valueOf(3L), "EPLAN");
			/*  205 */ super.putEnum(Long.valueOf(4L), "EVPLAN");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class mstpEthServiceDirection extends GenericEnum
	/*      */ {
		/*      */ public static final long _DoubleDir = 1L;
		/*      */ public static final long _SimpleDir = 2L;

		/*      */
		/*      */ private mstpEthServiceDirection()
		/*      */ {
			/*  189 */ super.putEnum(Long.valueOf(1L), "双向");
			/*  190 */ super.putEnum(Long.valueOf(2L), "单向");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class LoginLog extends GenericEnum
	/*      */ {
		/*      */ public static final String _login = "0";
		/*      */ public static final String _quit = "1";

		/*      */
		/*      */ private LoginLog()
		/*      */ {
			/*  179 */ super.putEnum("0", "登陆");
			/*  180 */ super.putEnum("1", "退出");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class LinePairPhyAble extends GenericEnum
	/*      */ {
		/*      */ public static final long line_pair_fine = 1L;
		/*      */ public static final long line_pair_damage = 2L;

		/*      */
		/*      */ private LinePairPhyAble()
		/*      */ {
			/*  170 */ super.putEnum(Long.valueOf(1L), "完好");
			/*  171 */ super.putEnum(Long.valueOf(2L), "破损");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class LinePairUseState extends GenericEnum
	/*      */ {
		/*      */ public static final long line_pair_free = 1L;
		/*      */ public static final long line_pair_state = 2L;
		/*      */ public static final long line_pair_pre = 3L;

		/*      */
		/*      */ private LinePairUseState()
		/*      */ {
			/*  159 */ super.putEnum(Long.valueOf(1L), "空闲");
			/*  160 */ super.putEnum(Long.valueOf(2L), "占用");
			/*  161 */ super.putEnum(Long.valueOf(3L), "预占");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class ExtendType extends GenericEnum
	/*      */ {
		/*      */ public static final long send = 1L;
		/*      */ public static final long receive = 2L;

		/*      */
		/*      */ private ExtendType()
		/*      */ {
			/*  149 */ super.putEnum(Long.valueOf(1L), "发端");
			/*  150 */ super.putEnum(Long.valueOf(2L), "收端");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class LinePairType extends GenericEnum
	/*      */ {
		/*      */ public static final long line_pair = 1L;
		/*      */ public static final long extend_line = 2L;

		/*      */
		/*      */ private LinePairType()
		/*      */ {
			/*  139 */ super.putEnum(Long.valueOf(1L), "楼间线");
			/*  140 */ super.putEnum(Long.valueOf(2L), "延伸线");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class ResourceType extends GenericEnum
	/*      */ {
		/*      */ public static final long _ptp = 1L;
		/*      */ public static final long _ctp = 2L;
		/*      */ public static final long _pq1 = 3L;
		/*      */ public static final long _pq1Not = 4L;
		/*      */ public static final long _topo = 5L;

		/*      */
		/*      */ private ResourceType()
		/*      */ {
			/*  125 */ super.putEnum(Long.valueOf(1L), "端口");
			/*  126 */ super.putEnum(Long.valueOf(2L), "时隙");
			/*  127 */ super.putEnum(Long.valueOf(3L), "槽路落PQ");
			/*  128 */ super.putEnum(Long.valueOf(4L), "槽路不落PQ");
			/*  129 */ super.putEnum(Long.valueOf(5L), "拓扑");
			/*      */ }
		/*      */ }

	/*      */
	/*      */ public static class PreWriteResState extends GenericEnum
	/*      */ {
		/*      */ public static final long _idle = 1L;
		/*      */ public static final long _use = 2L;

		/*      */
		/*      */ private PreWriteResState()
		/*      */ {
			/*  113 */ super.putEnum(Long.valueOf(1L), "空闲");
			/*  114 */ super.putEnum(Long.valueOf(2L), "占用");
			/*      */ }
		/*      */ }
	/*      */ }