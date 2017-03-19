/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
/*     */ package com.boco.workflow.webservice.upload.constants;

import com.boco.core.utils.lang.GenericEnum;

/*     */

/*     */
/*     */ public abstract interface AnmsConst
/*     */ {
	/*   7 */ public static final AuthType AUTH_TYPE = new AuthType();
	/*   8 */ public static final OwnerShipMan OWNERSHIP_MAN = new OwnerShipMan();
	/*   9 */ public static final DiagnosisType DIAGNOSIS_TYPE = new DiagnosisType();
	/*  10 */ public static final InternetFaultType INTERNET_FAULT_TYPE = new InternetFaultType();
	/*  11 */ public static final VoiceFaultType VOICE_FAULT_TYPE = new VoiceFaultType();
	/*  12 */ public static final IptvFaultType IPTV_FAULT_TYPE = new IptvFaultType();
	/*  13 */ public static final AutoManual AUTO_MANUAL = new AutoManual();
	/*     */ public static final String USER_CUID = "USER_CUID";
	/*     */ public static final String SESSION_ID = "SESSION_ID";
	/*     */ public static final String DIAGNOSIS_LOCATION = "<$LOCATION>";
	/*     */ public static final String DIAGNOSIS_FLOW_EXCEPTION = "DIAGNOSIS_FLOW_EXCEPTION";
	/*  18 */ public static final ACCESSTYPE ACCESS_TYPE = new ACCESSTYPE();
	/*  19 */ public static final ElementOwnerShip OWNERSHIP = new ElementOwnerShip();
	/*     */ public static final String DIAGNOSIS_PART_SUCCESS = "DIAGNOSIS_PART_SUCCESS";
	/*     */ public static final String AN_RADIUS_SYNC_TIME = "AN_RADIUS_SYNC_TIME";
	/*     */ public static final String AN_RADIUS_FTP_URL = "AN_RADIUS_FTP_URL";
	/*     */ public static final String AN_RADIUS_FTP_USER_NAME = "AN_RADIUS_FTP_USER_NAME";
	/*     */ public static final String AN_RADIUS_FTP_PASSWORD = "AN_RADIUS_FTP_PASSWORD";
	/*     */ public static final String AN_RADIUS_FTP_PATH = "AN_RADIUS_FTP_PATH";
	/*     */ public static final String AN_RADIUS_GET_FTP_PORT = "AN_RADIUS_GET_FTP_PORT";
	/*  30 */ public static final FaultDevType FAULT_DEV_TYPE = new FaultDevType();
	/*     */
	/*  32 */ public static final ItemGroup ITEMGROUP = new ItemGroup();
	/*     */
	/* 420 */ public static final DevType devType = new DevType();
	/*     */ public static final int COMMIT_COUNT = 500;
	/* 445 */ public static final RadiusState radiusState = new RadiusState();
	/*     */ public static final String RED = "red";
	/*     */ public static final String ORANGE = "orange";
	/*     */ public static final String YELLOW = "yellow";
	/*     */ public static final String BLUE = "royalblue";

	/*     */
	/*     */ public static class RadiusState extends GenericEnum<Integer>
	/*     */ {
		/*     */ public static final int NORMAL = 1;
		/*     */ public static final int VLAN_DIFF = 2;
		/*     */ public static final int MORE_THAN_ONE_PTP = 3;
		/*     */ public static final int OLT_IP_NOT_EXIST = 4;
		/*     */ public static final int ONU_PORT_IS_NULL = 5;
		/*     */ public static final int RADIUS_IS_INCOMPLETE = 6;
		/*     */ public static final int BANDWIDTH_INCONSISTENT = 7;
		/*     */ public static final int ONE_PORT_MORE_ACCOUNT = 8;
		/*     */ public static final int OLT_AND_BRAS_NULL = 9;
		/*     */ public static final int OLT_SVLAN_NOT_EXIST = 10;
		/*     */ public static final int UNABLE_TO_MONITOR = 11;

		/*     */
		/*     */ private RadiusState()
		/*     */ {
			/* 460 */ putEnum(Integer.valueOf(1), "正常");
			/* 461 */ putEnum(Integer.valueOf(2), "VLAN比对不一致");
			/* 462 */ putEnum(Integer.valueOf(3), "端口VLAN信息重复");
			/* 463 */ putEnum(Integer.valueOf(4), "OLT IP在存量数据中不存在");
			/* 464 */ putEnum(Integer.valueOf(5), "无法定位ONU端口");
			/* 465 */ putEnum(Integer.valueOf(6), "RADIUS数据不完整");
			/* 466 */ putEnum(Integer.valueOf(7), "带宽不一致");
			/* 467 */ putEnum(Integer.valueOf(8), "同端口不同账号");
			/* 468 */ putEnum(Integer.valueOf(9), "BRAS逻辑端口下无OLT");
			/* 469 */ putEnum(Integer.valueOf(10), "OLT SVLAN不存在");
			/* 470 */ putEnum(Integer.valueOf(11), "无法监控");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class DevType extends GenericEnum<Integer>
	/*     */ {
		/*     */ public static final int OLT = 1;
		/*     */ public static final int POS = 2;
		/*     */ public static final int ONU = 3;
		/*     */ public static final int EQUIPMENT_HOLDER = 4;
		/*     */ public static final int CARD = 5;
		/*     */ public static final int PTP = 6;
		/*     */ public static final int TOPOLINK = 7;
		/*     */ public static final int SWITCH_ELEMENT = 8;
		/*     */ public static final int OPTICAL_RECEIVER = 9;

		/*     */
		/*     */ private DevType()
		/*     */ {
			/* 432 */ putEnum(Integer.valueOf(1), "OLT");
			/* 433 */ putEnum(Integer.valueOf(2), "POS");
			/* 434 */ putEnum(Integer.valueOf(3), "ONU");
			/* 435 */ putEnum(Integer.valueOf(4), "EQUIPMENT_HOLDER");
			/* 436 */ putEnum(Integer.valueOf(5), "CARD");
			/* 437 */ putEnum(Integer.valueOf(6), "PTP");
			/* 438 */ putEnum(Integer.valueOf(7), "TOPOLINK");
			/* 439 */ putEnum(Integer.valueOf(8), "SWITCH_ELEMENT");
			/* 440 */ putEnum(Integer.valueOf(9), "OPTICAL_RECEIVER");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class LOCATION_STATE
	/*     */ {
		/*     */ public static final String VALUE = "VALUE";
		/*     */ }

	/*     */
	/*     */ public static class MAC_INFO
	/*     */ {
		/*     */ public static final String MAC = "MAC";
		/*     */ public static final String VLAN = "VLAN";
		/*     */ }

	/*     */
	/*     */ public static class PON_PORT_STATE
	/*     */ {
		/*     */ public static final String ADMIN = "AdminState";
		/*     */ public static final String OPERATE = "OperState";
		/*     */ }

	/*     */
	/*     */ public static class DIAGNOSIS_MODE
	/*     */ {
		/*     */ public static final long AUTO = 1L;
		/*     */ public static final long MANUAL = 2L;
		/*     */ }

	/*     */
	/*     */ public static class DIAGNOSIS_PARAM
	/*     */ {
		/*     */ public static final String OLT_ID = "OLT_ID";
		/*     */ public static final String ME_ID = "ME_ID";
		/*     */ public static final String DIAGNOSIS_RESULT_CUID = "DIAGNOSIS_RESULT_CUID";
		/*     */ public static final String PON_ID = "PON_ID";
		/*     */ public static final String ONU_ID = "ONU_ID";
		/*     */ public static final String ONU_IP = "ONU_IP";
		/*     */ public static final String EMS_NAME = "EMS_NAME";
		/*     */ public static final String BEGIN_TIME = "BEGIN_TIME";
		/*     */ public static final String END_TIME = "END_TIME";
		/*     */ public static final String OLT_PON_CARD = "OLT_PON_CARD";
		/*     */ public static final String ONU_PORT = "ONU_PORT";
		/*     */ public static final String APPLICATION_RATE_DOWN = "APPLICATION_RATE_DOWN";
		/*     */ public static final String POTS_GAIN_MIN = "POTS_GAIN_MIN";
		/*     */ public static final String POTS_GAIN_MAX = "POTS_GAIN_MAX";
		/*     */ public static final String UPLINK_PORT = "UPLINK_PORT";
		/*     */ public static final String ONU_BOARD = "ONU_BOARD";
		/*     */ public static final String TEL = "TEL";
		/*     */ public static final String OLT_MEMORY_RATE = "OLT_MEMORY_RATE";
		/*     */ public static final String OLT_CPU_RATE = "OLT_CPU_RATE";
		/*     */ public static final String OLT_PON_PORT_POWER_T = "OLT_PON_PORT_POWER_T";
		/*     */ public static final String OLT_PON_PORT_POWER_R = "OLT_PON_PORT_POWER_R";
		/*     */ public static final String ONU_PON_PORT_POWER_T = "ONU_PON_PORT_POWER_T";
		/*     */ public static final String ONU_PON_PORT_POWER_R = "ONU_PON_PORT_POWER_R";
		/*     */ public static final String OLT_UPLINK_PORT_POWER_T = "OLT_UPLINK_PORT_POWER_T";
		/*     */ public static final String OLT_UPLINK_PORT_POWER_R = "OLT_UPLINK_PORT_POWER_R";
		/*     */ public static final String OPTICAL_MODULE_TEMPERATURE = "OPTICAL_MODULE_TEMPERATURE";
		/*     */ public static final String OPTICAL_MODULE_VOLTAGE = "OPTICAL_MODULE_VOLTAGE";
		/*     */ public static final String OPTICAL_MODULE_CURRTXBIAS = "OPTICAL_MODULE_CURRTXBIAS";
		/*     */ public static final String OLT_PON_CARD_CPU_RATE = "OLT_PON_CARD_CPU_RATE";
		/*     */ public static final String OLT_PON_CARD_MEM_RATE = "OLT_PON_CARD_MEM_RATE";
		/*     */ public static final String OLT_MAIN_CARD_CPU_RATE = "OLT_MAIN_CARD_CPU_RATE";
		/*     */ public static final String OLT_MAIN_CARD_MEM_RATE = "OLT_MAIN_CARD_MEM_RATE";
		/*     */ public static final String OLT_UPLINK_BANDWIDTH_RATE = "OLT_UPLINK_BANDWIDTH_RATE";
		/*     */ public static final String OLT_PON_BANDWIDTH_RATE = "OLT_PON_BANDWIDTH_RATE";
		/*     */ public static final String OLT_PON_PORT_PACKET_LOSS_RATE = "OLT_PON_PORT_PACKET_LOSS_RATE";
		/*     */ public static final String OLT_UPLINK_PORT_PACKET_LOSS_RATE = "OLT_UPLINK_PORT_PACKET_LOSS_RATE";
		/*     */ public static final String ONU_CPU_RATE = "ONU_CPU_RATE";
		/*     */ public static final String ONU_MEMORY_RATE = "ONU_MEMORY_RATE";
		/*     */ public static final String ONU_UNI_BANDWIDTH_RATE = "ONU_UNI_BANDWIDTH_RATE";
		/*     */ public static final String ONU_PON_BANDWIDTH_RATE = "ONU_PON_BANDWIDTH_RATE";
		/*     */ public static final String ONU_PON_PORT_PACKET_LOSS_RATE = "ONU_PON_PORT_PACKET_LOSS_RATE";
		/*     */ public static final String ONU_UNI_PORT_PACKET_LOSS_RATE = "ONU_UNI_PORT_PACKET_LOSS_RATE";
		/*     */ public static final String POTS_GAIN = "POTS_GAIN";
		/*     */ public static final String VOICE_MEAN_DELAY = "VOICE_MEAN_DELAY";
		/*     */ public static final String VOICE_MEAN_JITTER = "VOICE_MEAN_JITTER";
		/*     */ }

	/*     */
	/*     */ public static class ElementOwnerShip extends GenericEnum
	/*     */ {
		/*     */ public static final long _self = 1L;
		/*     */ public static final long _build = 2L;
		/*     */ public static final long _join = 3L;
		/*     */ public static final long _rent = 4L;
		/*     */ public static final long _buy = 5L;
		/*     */ public static final long _change = 6L;

		/*     */
		/*     */ private ElementOwnerShip()
		/*     */ {
			/* 285 */ super.putEnum(Long.valueOf(1L), "自建");
			/* 286 */ super.putEnum(Long.valueOf(2L), "共建");
			/* 287 */ super.putEnum(Long.valueOf(3L), "合建");
			/* 288 */ super.putEnum(Long.valueOf(4L), "租用");
			/* 289 */ super.putEnum(Long.valueOf(5L), "购买");
			/* 290 */ super.putEnum(Long.valueOf(6L), "置换");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class ItemGroup extends GenericEnum
	/*     */ {
		/*     */ public static final long DEV_STATE = 1L;
		/*     */ public static final long PER_STATE = 2L;
		/*     */ public static final long NET_STATE = 3L;
		/*     */ public static final long VOICE_STATE = 4L;
		/*     */ public static final long OTHERS = 5L;

		/*     */
		/*     */ private ItemGroup()
		/*     */ {
			/* 266 */ super.putEnum(Long.valueOf(1L), "设备状态");
			/* 267 */ super.putEnum(Long.valueOf(2L), "性能状态");
			/* 268 */ super.putEnum(Long.valueOf(3L), "网络状态");
			/* 269 */ super.putEnum(Long.valueOf(4L), "语音状态");
			/* 270 */ super.putEnum(Long.valueOf(5L), "其他");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class FaultDevType extends GenericEnum
	/*     */ {
		/*     */ public static final long OLT_SIDE = 1L;
		/*     */ public static final long USER_SIDE = 2L;
		/*     */ public static final long ONU_SIDE = 3L;
		/*     */ public static final long OPTICAL_FAULT = 4L;
		/*     */ public static final long NET_FAULT = 5L;
		/*     */ public static final long BRAS_SIDE = 6L;

		/*     */
		/*     */ private FaultDevType()
		/*     */ {
			/* 244 */ super.putEnum(Long.valueOf(1L), "OLT故障");
			/* 245 */ super.putEnum(Long.valueOf(2L), "用户侧故障");
			/* 246 */ super.putEnum(Long.valueOf(3L), "ONU故障");
			/* 247 */ super.putEnum(Long.valueOf(4L), "光纤故障");
			/* 248 */ super.putEnum(Long.valueOf(5L), "网络侧故障");
			/* 249 */ super.putEnum(Long.valueOf(6L), "BRAS故障");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class IptvFaultType extends GenericEnum
	/*     */ {
		/*     */ public static final long ALL = 0L;
		/*     */ public static final long IPTV_UNUSABLE = 21L;
		/*     */ public static final long IPTV_POORQUALITY = 22L;

		/*     */
		/*     */ private IptvFaultType()
		/*     */ {
			/* 224 */ super.putEnum(Long.valueOf(0L), "全部");
			/* 225 */ super.putEnum(Long.valueOf(21L), "IPTV无法使用");
			/* 226 */ super.putEnum(Long.valueOf(22L), "IPTV质量差");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class VoiceFaultType extends GenericEnum
	/*     */ {
		/*     */ public static final long ALL = 0L;
		/*     */ public static final long VOICE_OFFHOOK_BUZZ = 10L;
		/*     */ public static final long VOICE_OFFHOOK_BUSY = 11L;
		/*     */ public static final long VOICE_OUTGO = 12L;
		/*     */ public static final long VOICE_INCOMING = 13L;
		/*     */ public static final long VOICE_SINGLEPASS = 14L;
		/*     */ public static final long VOICE_NOISE = 15L;
		/*     */ public static final long VOICE_REPLY = 16L;
		/*     */ public static final long VOICE_CONTINUALLY = 17L;
		/*     */ public static final long VOICE_FAX = 18L;

		/*     */
		/*     */ private VoiceFaultType()
		/*     */ {
			/* 205 */ super.putEnum(Long.valueOf(0L), "全部");
			/* 206 */ super.putEnum(Long.valueOf(10L), "摘机无音");
			/* 207 */ super.putEnum(Long.valueOf(11L), "摘机忙音");
			/* 208 */ super.putEnum(Long.valueOf(12L), "呼出异常");
			/* 209 */ super.putEnum(Long.valueOf(13L), "呼入异常");
			/* 210 */ super.putEnum(Long.valueOf(14L), "单通/双不通");
			/* 211 */ super.putEnum(Long.valueOf(15L), "杂音");
			/* 212 */ super.putEnum(Long.valueOf(16L), "回音");
			/* 213 */ super.putEnum(Long.valueOf(17L), "时断时续");
			/* 214 */ super.putEnum(Long.valueOf(18L), "传真异常");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class InternetFaultType extends GenericEnum
	/*     */ {
		/*     */ public static final long ALL = 0L;
		/*     */ public static final long NOT_ONLINE = 1L;
		/*     */ public static final long FREQUENT_OFFLINE = 2L;
		/*     */ public static final long INTERNET_SLOW = 3L;

		/*     */
		/*     */ private InternetFaultType()
		/*     */ {
			/* 184 */ super.putEnum(Long.valueOf(0L), "全部");
			/* 185 */ super.putEnum(Long.valueOf(1L), "无法上网");
			/* 186 */ super.putEnum(Long.valueOf(2L), "上网常掉线");
			/* 187 */ super.putEnum(Long.valueOf(3L), "上网慢");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class DiagnosisType extends GenericEnum
	/*     */ {
		/*     */ public static final long OLT_FAULT_DIAGNOSIS = 1L;
		/*     */ public static final long ONU_FAULT_DIAGNOSIS = 2L;
		/*     */ public static final long ODN_FAULT_DIAGNOSIS = 3L;
		/*     */ public static final long INTERNET_BUS_DIAGNOSIS = 4L;
		/*     */ public static final long VOICE_BUS_DIAGNOSIS = 5L;
		/*     */ public static final long IPTV_BUS_DIAGNOSIS = 6L;

		/*     */
		/*     */ private DiagnosisType()
		/*     */ {
			/* 167 */ super.putEnum(Long.valueOf(1L), "OLT故障诊断");
			/* 168 */ super.putEnum(Long.valueOf(2L), "ONU故障诊断");
			/* 169 */ super.putEnum(Long.valueOf(3L), "ODN故障诊断");
			/* 170 */ super.putEnum(Long.valueOf(4L), "上网业务诊断");
			/* 171 */ super.putEnum(Long.valueOf(5L), "语音业务诊断");
			/* 172 */ super.putEnum(Long.valueOf(6L), "IPTV业务诊断");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class AutoManual extends GenericEnum
	/*     */ {
		/*     */ public static final long AUTO = 1L;
		/*     */ public static final long MANUAL = 2L;

		/*     */
		/*     */ private AutoManual()
		/*     */ {
			/* 154 */ super.putEnum(Long.valueOf(1L), "自动");
			/* 155 */ super.putEnum(Long.valueOf(2L), "手动");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class ACCESSTYPE extends GenericEnum
	/*     */ {
		/*     */ public static final long CC = 1L;
		/*     */ public static final long HC = 2L;
		/*     */ public static final long CCHC = 3L;
		/*     */ public static final long OTHER = 4L;

		/*     */
		/*     */ private ACCESSTYPE()
		/*     */ {
			/* 143 */ super.putEnum(Long.valueOf(1L), "集客");
			/* 144 */ super.putEnum(Long.valueOf(2L), "家客");
			/* 145 */ super.putEnum(Long.valueOf(3L), "集客/家客共用");
			/* 146 */ super.putEnum(Long.valueOf(3L), "其他");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class AuthType extends GenericEnum
	/*     */ {
		/*     */ public static final long SN = 1L;
		/*     */ public static final long MAC = 2L;
		/*     */ public static final long PASSWORD = 3L;

		/*     */
		/*     */ private AuthType()
		/*     */ {
			/* 131 */ super.putEnum(Long.valueOf(1L), "SN");
			/* 132 */ super.putEnum(Long.valueOf(2L), "MAC");
			/* 133 */ super.putEnum(Long.valueOf(3L), "PASSWORD");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static class OwnerShipMan extends GenericEnum
	/*     */ {
		/*     */ public static final long _move = 1L;
		/*     */ public static final long _unicom = 2L;
		/*     */ public static final long _communication = 3L;
		/*     */ public static final long _crc = 4L;
		/*     */ public static final long _randt = 5L;
		/*     */ public static final long _government = 6L;
		/*     */ public static final long _custom = 7L;
		/*     */ public static final long _property = 8L;
		/*     */ public static final long _other = 9L;

		/*     */
		/*     */ private OwnerShipMan()
		/*     */ {
			/* 112 */ super.putEnum(Long.valueOf(1L), "移动");
			/* 113 */ super.putEnum(Long.valueOf(2L), "联通");
			/* 114 */ super.putEnum(Long.valueOf(3L), "电信");
			/* 115 */ super.putEnum(Long.valueOf(4L), "铁通");
			/* 116 */ super.putEnum(Long.valueOf(5L), "广电");
			/* 117 */ super.putEnum(Long.valueOf(6L), "政府");
			/* 118 */ super.putEnum(Long.valueOf(7L), "客户自有");
			/* 119 */ super.putEnum(Long.valueOf(8L), "物业");
			/* 120 */ super.putEnum(Long.valueOf(9L), "其它");
			/*     */ }
		/*     */ }

	/*     */
	/*     */ public static final class OwnerShipMan1
	/*     */ {
		/*     */ public static final int 移动 = 1;
		/*     */ public static final int 联通 = 2;
		/*     */ public static final int 电信 = 3;
		/*     */ public static final int 铁通 = 4;
		/*     */ public static final int 广电 = 5;
		/*     */ public static final int 政府 = 6;
		/*     */ public static final int 客户自有 = 7;
		/*     */ public static final int 物业 = 8;
		/*     */ public static final int 其它 = 9;
		/*     */ }

	/*     */
	/*     */ public static final class ConfirmDataType
	/*     */ {
		/*     */ public static final int UPPER = 0;
		/*     */ public static final int LOWER = 1;
		/*     */ }

	/*     */
	/*     */ public static final class DataState
	/*     */ {
		/*     */ public static final int NORMAL = 0;
		/*     */ public static final int ADD = 1;
		/*     */ public static final int MODIFY = 2;
		/*     */ public static final int DELETE = 3;
		/*     */ public static final int UNKNOW = 4;
		/*     */ }

	/*     */
	/*     */ public static final class AlarmFilterType
	/*     */ {
		/*     */ public static final int DISTRICT = 1;
		/*     */ public static final int EMS = 2;
		/*     */ public static final int OLT = 3;
		/*     */ public static final int ONU = 4;
		/*     */ public static final int CARD = 5;
		/*     */ public static final int PTP = 6;
		/*     */ public static final int ALARM_NAME = 7;
		/*     */ }
	/*     */ }
