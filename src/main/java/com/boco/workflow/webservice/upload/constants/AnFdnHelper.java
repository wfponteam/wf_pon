package com.boco.workflow.webservice.upload.constants;

import org.apache.commons.lang.StringUtils;

/*     */
/*     */ import com.boco.common.util.debug.LogHome;

/*     */
/*     */ public class AnFdnHelper
/*     */ {
	/*     */ public static final String EmsHeader = "EMS=";
	/*     */ public static final String OltHeader = "ManagedElement=";
	/*     */ public static final String OnuHeader = "ONU=";
	/*     */ public static final String PosHeader = "POS=";
	/*     */ public static final String HolderHeader = "EquipmentHolder=";
	/*     */ public static final String CardHeader = "Equipment=";
	/*     */ public static final String PtpHeader = "PTP=";
	/*     */ public static final String FtpHeader = "FTP=";
	/*     */ public static final String RackHeader = "rack=";
	/*     */ public static final String ShelfHeader = "shelf=";
	/*     */ public static final String SlotHeader = "slot=";
	/*     */ public static final String PortHeader = "port=";
	/*     */ public static final String fdnSpliter = ":";
	/*     */ public static final String fdnSpliterB = "=";
	/*     */ public static final String fdnSpliterC = "/";
	/*     */ public static final String fdnSpliterD = "-";

	/*     */
	/*     */ public static String makeEmsFdn(String emsName)
	/*     */ {
		/*  33 */ if (!(StringUtils.isEmpty(emsName))) {
			/*  34 */ return new StringBuilder().append("EMS=").append(emsName).toString();
			/*     */ }
		/*  36 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOltFdn(String emsFdn, String oltName)
	/*     */ {
		/*  41 */ if ((!(StringUtils.isEmpty(emsFdn))) && (!(StringUtils.isEmpty(oltName)))) {
			/*  42 */ return new StringBuilder().append(emsFdn).append(":").append("ManagedElement=").append(oltName)
					.toString();
			/*     */ }
		/*  44 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOltFdnByNames(String emsName, String oltName)
	/*     */ {
		/*  49 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))) {
			/*  50 */ return new StringBuilder().append(makeEmsFdn(emsName)).append(":").append("ManagedElement=")
					.append(oltName).toString();
			/*     */ }
		/*  52 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOnuFdn(String emsFdn, String oltFdn, String onuName)
	/*     */ {
		/*  57 */ if ((!(StringUtils.isEmpty(emsFdn))) && (!(StringUtils.isEmpty(oltFdn)))
				&& (!(StringUtils.isEmpty(onuName)))) {
			/*  58 */ return new StringBuilder().append(emsFdn).append(":").append(oltFdn).append(":").append("ONU=")
					.append(onuName).toString();
			/*     */ }
		/*  60 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOnuFdnByNames(String emsName, String oltName, String onuName)
	/*     */ {
		/*  65 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(onuName)))) {
			/*  66 */ return new StringBuilder().append(makeOltFdnByNames(emsName, oltName)).append(":").append("ONU=")
					.append(onuName).toString();
			/*     */ }
		/*  68 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makePosFdn(String oltFdn, String posName)
	/*     */ {
		/*  73 */ if ((!(StringUtils.isEmpty(oltFdn))) && (!(StringUtils.isEmpty(posName)))) {
			/*  74 */ return new StringBuilder().append(oltFdn).append(":").append("POS=").append(posName).toString();
			/*     */ }
		/*  76 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makePosFdnByNames(String emsName, String oltName, String posName)
	/*     */ {
		/*  81 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(posName)))) {
			/*  82 */ return new StringBuilder().append(makeOltFdnByNames(emsName, oltName)).append(":").append("POS=")
					.append(posName).toString();
			/*     */ }
		/*  84 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOltHolderFdn(String emsName, String oltName, String equipmentHolderName)
	/*     */ {
		/*  89 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(equipmentHolderName)))) {
			/*  90 */ return new StringBuilder().append(makeOltFdnByNames(emsName, oltName)).append(":")
					.append("EquipmentHolder=").append(equipmentHolderName).toString();
			/*     */ }
		/*  92 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOltHolderFdn(String oltFdn, String equipmentHolderName)
	/*     */ {
		/*  97 */ if ((!(StringUtils.isEmpty(oltFdn))) && (!(StringUtils.isEmpty(equipmentHolderName)))) {
			/*  98 */ return new StringBuilder().append(oltFdn).append(":").append("EquipmentHolder=")
					.append(equipmentHolderName).toString();
			/*     */ }
		/* 100 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOnuHolderFdn(String emsName, String oltName, String onuName,
			String equipmentHolderName)
	/*     */ {
		/* 105 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(onuName))) && (!(StringUtils.isEmpty(equipmentHolderName)))) {
			/* 106 */ return new StringBuilder().append(makeOnuFdnByNames(emsName, oltName, onuName)).append(":")
					.append("EquipmentHolder=").append(equipmentHolderName).toString();
			/*     */ }
		/* 108 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOnuHolderFdn(String onuFdn, String equipmentHolderName)
	/*     */ {
		/* 114 */ if ((!(StringUtils.isEmpty(onuFdn))) && (!(StringUtils.isEmpty(equipmentHolderName)))) {
			/* 115 */ return new StringBuilder().append(onuFdn).append(":").append("EquipmentHolder=")
					.append(equipmentHolderName).toString();
			/*     */ }
		/* 117 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makePosHolderFdn(String emsName, String oltName, String posName,
			String equipmentHolderName)
	/*     */ {
		/* 122 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(posName))) && (!(StringUtils.isEmpty(equipmentHolderName)))) {
			/* 123 */ return new StringBuilder().append(makePosFdnByNames(emsName, oltName, posName)).append(":")
					.append("EquipmentHolder=").append(equipmentHolderName).toString();
			/*     */ }
		/* 125 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makePosHolderFdn(String posFdn, String equipmentHolderName)
	/*     */ {
		/* 131 */ if ((!(StringUtils.isEmpty(posFdn))) && (!(StringUtils.isEmpty(equipmentHolderName)))) {
			/* 132 */ return new StringBuilder().append(posFdn).append(":").append("EquipmentHolder=")
					.append(equipmentHolderName).toString();
			/*     */ }
		/* 134 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOltCardFdn(String emsName, String oltname, String equipmentHolderName,
			String equipment)
	/*     */ {
		/* 139 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltname)))
				&& (!(StringUtils.isEmpty(equipmentHolderName))))
		/*     */ {
			/* 141 */ if (StringUtils.isEmpty(equipment)) {
				/* 142 */ equipment = "1";
				/*     */ }
			/* 144 */ return new StringBuilder().append(makeOltFdnByNames(emsName, oltname)).append(":")
					.append("EquipmentHolder=").append(equipmentHolderName).append(":").append("Equipment=")
					.append(equipment).toString();
			/*     */ }
		/* 146 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOltCardFdn(String OltFdn, String equipmentHolderName, String equipment)
	/*     */ {
		/* 151 */ if ((!(StringUtils.isEmpty(OltFdn))) && (!(StringUtils.isEmpty(equipmentHolderName))))
		/*     */ {
			/* 153 */ if (StringUtils.isEmpty(equipment)) {
				/* 154 */ equipment = "1";
				/*     */ }
			/* 156 */ return new StringBuilder().append(OltFdn).append(":").append("EquipmentHolder=")
					.append(equipmentHolderName).append(":").append("Equipment=").append(equipment).toString();
			/*     */ }
		/* 158 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOnuCardFdn(String emsName, String oltName, String onuName,
			String equipmentHolderName, String equipment)
	/*     */ {
		/* 163 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(onuName))) && (!(StringUtils.isEmpty(equipmentHolderName))))
		/*     */ {
			/* 165 */ if (StringUtils.isEmpty(equipment)) {
				/* 166 */ equipment = "1";
				/*     */ }
			/* 168 */ return new StringBuilder().append(makeOnuFdnByNames(emsName, oltName, onuName)).append(":")
					.append("EquipmentHolder=").append(equipmentHolderName).append(":").append("Equipment=")
					.append(equipment).toString();
			/*     */ }
		/* 170 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOnuCardFdn(String onuFdn, String equipmentHolderName, String equipment)
	/*     */ {
		/* 175 */ if ((!(StringUtils.isEmpty(onuFdn))) && (!(StringUtils.isEmpty(equipmentHolderName))))
		/*     */ {
			/* 177 */ if (StringUtils.isEmpty(equipment)) {
				/* 178 */ equipment = "1";
				/*     */ }
			/* 180 */ return new StringBuilder().append(onuFdn).append(":").append("EquipmentHolder=")
					.append(equipmentHolderName).append(":").append("Equipment=").append(equipment).toString();
			/*     */ }
		/* 182 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makePosCardFdn(String emsName, String oltName, String posName,
			String equipmentHolderName, String equipment)
	/*     */ {
		/* 187 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(posName))) && (!(StringUtils.isEmpty(equipmentHolderName))))
		/*     */ {
			/* 189 */ if (StringUtils.isEmpty(equipment)) {
				/* 190 */ equipment = "1";
				/*     */ }
			/* 192 */ return new StringBuilder().append(makePosFdnByNames(emsName, oltName, posName)).append(":")
					.append("EquipmentHolder=").append(equipmentHolderName).append(":").append("Equipment=")
					.append(equipment).toString();
			/*     */ }
		/* 194 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makePosCardFdn(String posFdn, String equipmentHolderName, String equipment)
	/*     */ {
		/* 199 */ if ((!(StringUtils.isEmpty(posFdn))) && (!(StringUtils.isEmpty(equipmentHolderName))))
		/*     */ {
			/* 201 */ if (StringUtils.isEmpty(equipment)) {
				/* 202 */ equipment = "1";
				/*     */ }
			/* 204 */ return new StringBuilder().append(posFdn).append(":").append("EquipmentHolder=")
					.append(equipmentHolderName).append(":").append("Equipment=").append(equipment).toString();
			/*     */ }
		/* 206 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOltPortFdn(String emsName, String oltName, String portName)
	/*     */ {
		/* 211 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(portName)))) {
			/* 212 */ return new StringBuilder().append(makeOltFdnByNames(emsName, oltName)).append(":").append("PTP=")
					.append(portName).toString();
			/*     */ }
		/* 214 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOltPortFdnByPortInfo(String oltFdn, String ponPortInfo)
	/*     */ {
		/* 219 */ if ((!(StringUtils.isEmpty(oltFdn))) && (!(StringUtils.isEmpty(ponPortInfo))))
		/*     */ {
			/* 221 */ String[] port = ponPortInfo.split("-");
			/* 222 */ if ((port != null) && (port.length == 4)) {
				/* 223 */ return new StringBuilder().append(oltFdn).append(":").append("PTP=").append("/")
						.append("rack=").append(port[0]).append("/").append("shelf=").append(port[1]).append("/")
						.append("slot=").append(port[2]).append("/").append("port=").append(port[3]).toString();
				/*     */ }
			/*     */
			/* 228 */ return "";
			/*     */ }
		/*     */
		/* 231 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOltPortFdn(String oltFdn, String portName)
	/*     */ {
		/* 236 */ if ((!(StringUtils.isEmpty(oltFdn))) && (!(StringUtils.isEmpty(portName)))) {
			/* 237 */ return new StringBuilder().append(oltFdn).append(":").append("PTP=").append(portName).toString();
			/*     */ }
		/* 239 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOnuPortFdn(String emsName, String oltName, String onuName, String portName)
	/*     */ {
		/* 244 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(onuName))) && (!(StringUtils.isEmpty(portName)))) {
			/* 245 */ return new StringBuilder().append(makeOnuFdnByNames(emsName, oltName, onuName)).append(":")
					.append("PTP=").append(portName).toString();
			/*     */ }
		/* 247 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makeOnuPortFdn(String onuFdn, String portName)
	/*     */ {
		/* 252 */ if ((!(StringUtils.isEmpty(onuFdn))) && (!(StringUtils.isEmpty(portName)))) {
			/* 253 */ return new StringBuilder().append(onuFdn).append(":").append("PTP=").append(portName).toString();
			/*     */ }
		/* 255 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makePosPortFdn(String emsName, String oltName, String posName, String portName)
	/*     */ {
		/* 260 */ if ((!(StringUtils.isEmpty(emsName))) && (!(StringUtils.isEmpty(oltName)))
				&& (!(StringUtils.isEmpty(posName))) && (!(StringUtils.isEmpty(portName)))) {
			/* 261 */ return new StringBuilder().append(makePosFdnByNames(emsName, oltName, posName)).append(":")
					.append("PTP=").append(portName).toString();
			/*     */ }
		/* 263 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String makePosPortFdn(String posFdn, String portName)
	/*     */ {
		/* 268 */ if ((!(StringUtils.isEmpty(posFdn))) && (!(StringUtils.isEmpty(portName)))) {
			/* 269 */ return new StringBuilder().append(posFdn).append(":").append("PTP=").append(portName).toString();
			/*     */ }
		/* 271 */ return "";
		/*     */ }

	/*     */
	/*     */ public static String getEmsFdnByStr(String souFdn)
	/*     */ {
		/* 276 */ if (souFdn == null) {
			/* 277 */ return null;
			/*     */ }
		/* 279 */ int emsPlace = souFdn.indexOf("EMS=");
		/* 280 */ String[] fdnBuf = souFdn.split(":");
		/* 281 */ if (emsPlace < 0) {
			/* 282 */ return new StringBuilder().append("EMS=").append(souFdn).toString();
			/*     */ }
		/* 284 */ return fdnBuf[0];
		/*     */ }

	/*     */
	/*     */ public static String getEmsNameByStr(String souFdn)
	/*     */ {
		/* 289 */ if (souFdn == null) {
			/* 290 */ return null;
			/*     */ }
		/* 292 */ int emsPlace = souFdn.indexOf("EMS=");
		/* 293 */ String[] fdnBuf = souFdn.split(":");
		/* 294 */ if (emsPlace < 0) {
			/* 295 */ return souFdn;
			/*     */ }
		/* 297 */ String[] emsName = fdnBuf[0].split("=");
		/* 298 */ return emsName[1];
		/*     */ }

	/*     */
	/*     */ public static String getMeFdnByStr(String souFdn, String header)
	/*     */ {
		/* 303 */ if (souFdn == null) {
			/* 304 */ return null;
			/*     */ }
		/* 306 */ if (StringUtils.isEmpty(header)) {
			/* 307 */ return null;
			/*     */ }
		/* 309 */ if (souFdn.indexOf(header) < 0) {
			/* 310 */ return null;
			/*     */ }
		/* 312 */ String[] fdnBuf = souFdn.split(":");
		/* 313 */ if ("ManagedElement=".equals(header)) {
			/* 314 */ if (fdnBuf.length >= 2)
				/* 315 */ return new StringBuilder().append(fdnBuf[0]).append(":").append(fdnBuf[1]).toString();
			/*     */ }
			/* 317 */ else if ("ONU=".equals(header)) {
			/* 318 */ if (fdnBuf.length >= 3) /* 319 */ return new StringBuilder().append(fdnBuf[0]).append(":")
					.append(fdnBuf[1]).append(":").append(fdnBuf[2]).toString();
			/*     */ }
			/* 321 */ else if ("POS=".equals(header))
		/*     */ {
			/* 323 */ if (souFdn.indexOf("EquipmentHolder=") > -1)
			/*     */ {
				/* 325 */ return souFdn.substring(0, souFdn.indexOf("EquipmentHolder=") - 1);
			}
			/* 326 */ if (souFdn.indexOf("PTP=") > -1)
			/*     */ {
				/* 328 */ return souFdn.substring(0, souFdn.indexOf("PTP=") - 1);
				/*     */ }
			/* 330 */ return souFdn;
			/*     */ }
		/*     */
		/* 333 */ return null;
		/*     */ }

	/*     */
	/*     */ public static int getSlotNumByStr(String souFdn) {
		/* 337 */ int slotNum = -1;
		/* 338 */ if (souFdn != null) {
			/* 339 */ int place = souFdn.indexOf("/slot=");
			/* 340 */ if (place > -1) {
				/* 341 */ ++place;
				/* 342 */ int endIndex = souFdn.indexOf(":", place);
				/*     */ String[] fdnBuf;
				/* 344 */ if (endIndex >= 0) /* 345 */ fdnBuf = souFdn.substring(place, endIndex).split("/");
				/*     */ else {
					/* 347 */ fdnBuf = souFdn.substring(place).split("/");
					/*     */ }
				/* 349 */ String[] rackBuf = fdnBuf[0].split("=");
				/* 350 */ if (rackBuf.length >= 2) {
					/* 351 */ slotNum = new Integer(rackBuf[1]).intValue();
					/*     */ }
				/*     */ }
			/*     */ }
		/* 355 */ return slotNum;
		/*     */ }

	/*     */
	/*     */ public static int getPortNumByStr(String souFdn) {
		/* 359 */ int slotNum = -1;
		/* 360 */ if (souFdn != null) {
			/* 361 */ int place = souFdn.indexOf("/port=");
			/* 362 */ if (place > -1) {
				/* 363 */ ++place;
				/* 364 */ int endIndex = souFdn.indexOf(":", place);
				/*     */ String[] fdnBuf;
				/* 366 */ if (endIndex >= 0) /* 367 */ fdnBuf = souFdn.substring(place, endIndex).split("/");
				/*     */ else {
					/* 369 */ fdnBuf = souFdn.substring(place).split("/");
					/*     */ }
				/* 371 */ String[] rackBuf = fdnBuf[0].split("=");
				/* 372 */ if (rackBuf.length >= 2) {
					/* 373 */ slotNum = new Integer(rackBuf[1]).intValue();
					/*     */ }
				/*     */ }
			/*     */ }
		/* 377 */ return slotNum;
		/*     */ }

	/*     */
	/*     */ public static int getShelfNumByStr(String souFdn) {
		/* 381 */ int shelfNum = -1;
		/* 382 */ if (souFdn != null) {
			/* 383 */ int place = souFdn.indexOf("/shelf=");
			/* 384 */ if (place > -1) {
				/* 385 */ ++place;
				/* 386 */ int endIndex = souFdn.indexOf(":", place);
				/*     */ String[] fdnBuf;
				/* 388 */ if (endIndex >= 0) /* 389 */ fdnBuf = souFdn.substring(place, endIndex).split("/");
				/*     */ else {
					/* 391 */ fdnBuf = souFdn.substring(place).split("/");
					/*     */ }
				/* 393 */ String[] rackBuf = fdnBuf[0].split("=");
				/* 394 */ if (rackBuf.length >= 2) {
					/* 395 */ shelfNum = new Integer(rackBuf[1]).intValue();
					/*     */ }
				/*     */ }
			/*     */ }
		/* 399 */ return shelfNum;
		/*     */ }

	/*     */
	/*     */ public static String getMeFdnByPtpFdn(String ptpFdn) {
		/* 403 */ if (ptpFdn == null) {
			/* 404 */ return null;
			/*     */ }
		/* 406 */ if (ptpFdn.indexOf("PTP=") < 0) {
			/* 407 */ return null;
			/*     */ }
		/* 409 */ String[] fdnBuf = ptpFdn.split(":PTP=");
		/* 410 */ if ((fdnBuf != null) && (fdnBuf.length == 2)) {
			/* 411 */ return fdnBuf[0];
			/*     */ }
		/* 413 */ return null;
		/*     */ }

	/*     */
	/*     */ public static String getCardFdnByPtpFdn(String ptpFdn)
	/*     */ {
		/* 418 */ if (ptpFdn == null) {
			/* 419 */ return null;
			/*     */ }
		/* 421 */ if (ptpFdn.indexOf("PTP=") < 0) {
			/* 422 */ return null;
			/*     */ }
		/* 424 */ String[] fdnBuf = ptpFdn.split(":PTP=");
		/* 425 */ if ((fdnBuf != null) && (fdnBuf.length == 2)) {
			/* 426 */ String meFdn = fdnBuf[0];
			/* 427 */ if ((!(StringUtils.isEmpty(meFdn))) && (!(StringUtils.isEmpty(fdnBuf[1])))) {
				/* 428 */ fdnBuf = fdnBuf[1].split("/port=");
				/* 429 */ if ((fdnBuf != null) && (fdnBuf.length == 2)) {
					/* 430 */ String holderLocation = fdnBuf[0];
					/* 431 */ if (holderLocation.indexOf("domain") > -1)
					/*     */ {
						/* 433 */ int holderPlace = holderLocation.indexOf("/domain");
						/* 434 */ holderLocation = holderLocation.substring(0, holderPlace);
						/*     */ }
					/* 436 */ return new StringBuilder().append(meFdn).append(":").append("EquipmentHolder=")
							.append(holderLocation).append(":").append("Equipment=").append("1").toString();
					/*     */ }
				/*     */ }
			/*     */ }
		/* 440 */ return null;
		/*     */ }

	/*     */
	/*     */ public static String getEquipmentHolderFdnByCardFdn(String cardFdn) {
		/* 444 */ if (StringUtils.isEmpty(cardFdn)) {
			/* 445 */ return null;
			/*     */ }
		/* 447 */ if (cardFdn.indexOf("Equipment=") < 0) {
			/* 448 */ return null;
			/*     */ }
		/* 450 */ String[] fdnBuf = cardFdn.split(":Equipment=");
		/* 451 */ if ((fdnBuf != null) && (fdnBuf.length == 2)) {
			/* 452 */ return fdnBuf[0];
			/*     */ }
		/* 454 */ return null;
		/*     */ }

	/*     */
	/*     */ public static String getDefalutEquipmentHolderStr(String rack)
	/*     */ {
		/* 460 */ if (StringUtils.isEmpty(rack)) {
			/* 461 */ return null;
			/*     */ }
		/* 463 */ return new StringBuilder().append(rack).append("/").append("shelf=").append("1").append("/")
				.append("slot=").append("1").toString();
		/*     */ }

	/*     */
	/*     */ public static String getPortFdnByStr(String souFdn) {
		/* 467 */ if (StringUtils.isEmpty(souFdn)) {
			/* 468 */ return null;
			/*     */ }
		/* 470 */ if (souFdn.indexOf("PTP=") < 0) {
			/* 471 */ return null;
			/*     */ }
		/* 473 */ String[] fdnBuf = souFdn.split("PTP=");
		/* 474 */ if ((fdnBuf != null) && (fdnBuf.length >= 2)) {
			/* 475 */ String temp = fdnBuf[1];
			/* 476 */ if (!(StringUtils.isEmpty(temp))) {
				/* 477 */ String[] tempArray = temp.split(":");
				/* 478 */ if (tempArray != null) {
					/* 479 */ if (tempArray.length > 1) {
						/* 480 */ return new StringBuilder().append(fdnBuf[0]).append("PTP=").append(tempArray[0])
								.toString();
						/*     */ }
					/* 482 */ return new StringBuilder().append(fdnBuf[0]).append("PTP=").append(fdnBuf[1]).toString();
					/*     */ }
				/*     */ }
			/*     */ }
		/* 486 */ return null;
		/*     */ }

	/*     */
	/*     */ public static String getEquitFdnByStr(String souFdn)
	/*     */ {
		/* 496 */ if (souFdn == null) {
			/* 497 */ return null;
			/*     */ }
		/* 499 */ String neFdn = "";
		/* 500 */ if (souFdn.indexOf("ONU=") > 0) /* 501 */ neFdn = getMeFdnByStr(souFdn, "ONU=");
		/* 502 */ else if (souFdn.indexOf("POS=") > 0) /* 503 */ neFdn = getMeFdnByStr(souFdn, "POS=");
		/*     */ else {
			/* 505 */ neFdn = getMeFdnByStr(souFdn, "ManagedElement=");
			/*     */ }
		/* 507 */ StringBuilder holderFdnBuffer = new StringBuilder();
		/* 508 */ String tmpFdn = souFdn.substring(neFdn.length());
		/* 509 */ int nePlace = tmpFdn.indexOf("Equipment=");
		/* 510 */ String[] fdnBuf = tmpFdn.split(":");
		/* 511 */ if (nePlace < 0)
		/*     */ {
			/* 513 */ int ptpPlace = souFdn.indexOf("PTP=");
			/* 514 */ int ftpPlace = souFdn.indexOf("FTP=");
			/* 515 */ if ((ptpPlace < 0) && (ftpPlace < 0)) {
				/* 516 */ return null;
				/*     */ }
			/* 518 */ holderFdnBuffer.append(neFdn);
			/* 519 */ holderFdnBuffer.append(":");
			/* 520 */ String[] ptp = fdnBuf[1].split("=", 2);
			/* 521 */ holderFdnBuffer.append("EquipmentHolder=");
			/*     */ int portplace;
			/* 523 */ if (ptp[1].indexOf("domain") < 0) /* 524 */ portplace = ptp[1].indexOf("/port");
			/*     */ else {
				/* 526 */ portplace = ptp[1].indexOf("/domain");
				/*     */ }
			/* 528 */ holderFdnBuffer.append(ptp[1].substring(0, portplace));
			/* 529 */ holderFdnBuffer.append(":");
			/* 530 */ holderFdnBuffer.append("Equipment=1");
			/* 531 */ return holderFdnBuffer.toString();
			/*     */ }
		/*     */
		/* 535 */ if (fdnBuf.length >= 3) {
			/* 536 */ holderFdnBuffer.append(neFdn);
			/* 537 */ holderFdnBuffer.append(":");
			/* 538 */ holderFdnBuffer.append(fdnBuf[1]);
			/* 539 */ holderFdnBuffer.append(":");
			/* 540 */ holderFdnBuffer.append(fdnBuf[2]);
			/*     */ } else {
			/* 542 */ holderFdnBuffer.setLength(0);
			/*     */ }
		/* 544 */ return holderFdnBuffer.toString();
		/*     */ }

	/*     */
	/*     */ public static String getNeHolderFdnByStr(String souFdn)
	/*     */ {
		/* 555 */ if (souFdn == null) {
			/* 556 */ return null;
			/*     */ }
		/* 558 */ String neFdn = "";
		/* 559 */ if (souFdn.indexOf("ONU=") > 0) /* 560 */ neFdn = getMeFdnByStr(souFdn, "ONU=");
		/* 561 */ else if (souFdn.indexOf("POS=") > 0) /* 562 */ neFdn = getMeFdnByStr(souFdn, "POS=");
		/*     */ else {
			/* 564 */ neFdn = getMeFdnByStr(souFdn, "ManagedElement=");
			/*     */ }
		/* 566 */ StringBuilder holderFdnBuffer = new StringBuilder();
		/* 567 */ String tmpFdn = souFdn.substring(neFdn.length());
		/* 568 */ int nePlace = tmpFdn.indexOf("EquipmentHolder=");
		/* 569 */ String[] fdnBuf = tmpFdn.split(":");
		/* 570 */ if (nePlace < 0) {
			/* 571 */ int ptpPlace = souFdn.indexOf("PTP=");
			/* 572 */ int ftpPlace = souFdn.indexOf("FTP=");
			/* 573 */ if ((ptpPlace < 0) && (ftpPlace < 0)) {
				/* 574 */ return null;
				/*     */ }
			/* 576 */ holderFdnBuffer.append(neFdn);
			/* 577 */ holderFdnBuffer.append(":");
			/* 578 */ String[] ptp = fdnBuf[1].split("=", 2);
			/* 579 */ holderFdnBuffer.append("EquipmentHolder=");
			/* 580 */ int portplace = 0;
			/* 581 */ if (ptp[1].indexOf("domain") < 0) /* 582 */ portplace = ptp[1].indexOf("/port");
			/*     */ else {
				/* 584 */ portplace = ptp[1].indexOf("/domain");
				/*     */ }
			/* 586 */ holderFdnBuffer.append(ptp[1].substring(0, portplace));
			/*     */ }
			/*     */ else {
			/* 589 */ holderFdnBuffer.append(neFdn);
			/* 590 */ holderFdnBuffer.append(":");
			/* 591 */ holderFdnBuffer.append(fdnBuf[1]);
			/*     */ }
		/* 593 */ return holderFdnBuffer.toString();
		/*     */ }

	/*     */
	/*     */ public static String getPortIdByPtpFdn(String ptpFdn) {
		/* 597 */ if ((StringUtils.isEmpty(ptpFdn)) || (ptpFdn.indexOf("PTP=") < 0)) {
			/* 598 */ return null;
			/*     */ }
		/* 600 */ String[] fdnBuf = ptpFdn.split("PTP=");
		/* 601 */ if ((fdnBuf != null) && (fdnBuf.length == 2)) {
			/* 602 */ String portId = fdnBuf[1];
			/* 603 */ if (!(StringUtils.isEmpty(portId))) {
				/* 604 */ if (portId.indexOf("domain") > -1)
				/*     */ {
					/* 606 */ portId = new StringBuilder().append(portId.substring(0, portId.indexOf("/domain")))
							.append(portId.substring(portId.indexOf("/port"), portId.length())).toString();
					/*     */ }
				/* 608 */ fdnBuf = portId.split("/");
				/* 609 */ if ((fdnBuf != null) && (fdnBuf.length == 5) && (!(StringUtils.isEmpty(fdnBuf[1])))
						&& (fdnBuf[1].indexOf("rack=") > -1) && (!(StringUtils.isEmpty(fdnBuf[2])))
						&& (fdnBuf[2].indexOf("shelf=") > -1) && (!(StringUtils.isEmpty(fdnBuf[3])))
						&& (fdnBuf[3].indexOf("slot=") > -1) && (!(StringUtils.isEmpty(fdnBuf[4])))
						&& (fdnBuf[4].indexOf("port=") > -1))
				/*     */ {
					/* 614 */ StringBuilder sb = new StringBuilder();
					/* 615 */ String[] rackArray = fdnBuf[1].split("=");
					/* 616 */ if (rackArray[1].contains("-")) /* 617 */ sb.append("NA-");
					/*     */ else {
						/* 619 */ sb.append(new StringBuilder().append(fdnBuf[1].split("=")[1]).append("-").toString());
						/*     */ }
					/* 621 */ if (fdnBuf[2].split("=")[1].contains("-")) /* 622 */ sb.append("NA-");
					/*     */ else {
						/* 624 */ sb.append(new StringBuilder().append(fdnBuf[2].split("=")[1]).append("-").toString());
						/*     */ }
					/* 626 */ if (fdnBuf[3].split("=")[1].contains("-")) /* 627 */ sb.append("NA-");
					/*     */ else {
						/* 629 */ sb.append(new StringBuilder().append(fdnBuf[3].split("=")[1]).append("-").toString());
						/*     */ }
					/* 631 */ sb.append(fdnBuf[4].split("=")[1]);
					/* 632 */ return sb.toString();
					/*     */ }
				/*     */ }
			/*     */ }
		/* 636 */ return null;
		/*     */ }

	/*     */
	/*     */ public static String getPortFdnByCardFdnAndPortNo(String cardFdn, int portNo)
	/*     */ {
		/* 647 */ String portFdn = "";
		/* 648 */ if (!(StringUtils.isEmpty(cardFdn))) {
			/* 649 */ int holderIndex = cardFdn.indexOf("EquipmentHolder=");
			/* 650 */ int cardIndex = cardFdn.indexOf("Equipment=");
			/* 651 */ if ((holderIndex > -1) && (cardIndex > -1)) {
				/* 652 */ String neFdn = cardFdn.substring(0, holderIndex);
				/* 653 */ String holderInfo = cardFdn.substring(holderIndex, cardIndex - 1);
				/* 654 */ portFdn = new StringBuilder().append(neFdn).append("PTP=")
						.append(holderInfo.substring("EquipmentHolder=".length())).append("/").append("port=")
						.append(portNo).toString();
				/*     */ }
			/*     */
			/*     */ }
		/*     */
		/* 659 */ return portFdn;
		/*     */ }

	/*     */
	/*     */ public static String replacePonMeFdn(String sourceFdn)
	/*     */ {
		/*     */ try
		/*     */ {
			/* 675 */ if ((StringUtils.isEmpty(sourceFdn)) || (sourceFdn.indexOf("ManagedElement=") < 0)) {
				/* 676 */ return null;
				/*     */ }
			/* 678 */ String[] tempArray = sourceFdn.split(":");
			/* 679 */ if ((sourceFdn.indexOf("POS=") > -1) || (sourceFdn.indexOf("ONU=") > -1))
			/*     */ {
				/* 681 */ if ((tempArray != null) && (tempArray.length >= 3) &&
						/* 682 */ (!(StringUtils.isEmpty(tempArray[2]))) && ((
						/* 682 */ (tempArray[2].indexOf("POS=") > -1) || (sourceFdn.indexOf("ONU=") > -1)))) {
					/* 683 */ String[] meStr = tempArray[2].split("=");
					/* 684 */ if ((meStr != null) && (!(StringUtils.isEmpty(meStr[1])))) {
						/* 685 */ return makeOltFdn(tempArray[0], meStr[1]);
						/*     */ }
					/*     */ }
				/*     */ }
				/* 689 */ else if (sourceFdn.indexOf("ManagedElement=") > -1)
			/*     */ {
				/* 691 */ if ((tempArray != null) && (tempArray.length >= 2)) /* 692 */ return new StringBuilder()
						.append(tempArray[0]).append(":").append(tempArray[1]).toString();
				/*     */ }
				/*     */ else/* 695 */ return null;
			/*     */ }
			/*     */ catch (Exception ex)
		/*     */ {
			/* 699 */ LogHome.getLog()
					.error(new StringBuilder().append("转换传输FDN失败,").append(sourceFdn).append(ex).toString());
			/*     */ }
		/* 701 */ return null;
		/*     */ }
	/*     */ }