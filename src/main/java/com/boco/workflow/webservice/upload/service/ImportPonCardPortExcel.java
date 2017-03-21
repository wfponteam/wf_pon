package com.boco.workflow.webservice.upload.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.equip.bo.OnuManageBO;
import com.boco.workflow.webservice.upload.bo.ImportBasicDataBO;
import com.boco.workflow.webservice.upload.constants.AnFdnHelper;
import com.boco.workflow.webservice.upload.constants.Constant;
import com.boco.workflow.webservice.upload.constants.ElementEnum;
import com.boco.workflow.webservice.upload.constants.RackEnum;
import com.boco.workflow.webservice.upload.servlet.ImportResultDO;


public class ImportPonCardPortExcel {
	Map cardKindMap;
	//初始化这些值，因为这样校验才更加方便
	public ImportPonCardPortExcel( Map cardKindMap) {		
		this.cardKindMap = cardKindMap;
	}	
	
	public OnuManageBO getOnuManageBO(){
		return (OnuManageBO) SpringContextUtil.getBean("onuManageBO");
	}
	

	/**
	 * 根据Servlet传递来的方法调用，进行具体导入类的识别，Excel的名称是：CARDPORT
	 */
	public  ImportResultDO importCardPortBasicData(Workbook writeWorkBook ,Sheet writeSheet,String pathname, String excelName) throws Exception {
		Map<String,Map> neMaps = new HashMap<String,Map>();
		Map cardFdnMap=new HashMap();
		Map portMaps = new HashMap();
		Map ptpNoMap=new HashMap();
		List portList = new ArrayList(); 
		Map<String,Map> cardMaps = new HashMap<String,Map>();
		ImportResultDO importResultDO = new ImportResultDO(excelName);
		try {
			// 取得sheet
			int lastRows = writeSheet.getLastRowNum();
			Row xrow=writeSheet.getRow(0);//获取第一行求的列数
			int lastColumns =xrow.getLastCellNum();
			List errorlist = new ArrayList();
			// 取得每一列对应的列号，然后进行校验判断。
			Map headingMap = getCardPortHeadingMap(writeSheet, errorlist,lastColumns);	
			if(!errorlist.isEmpty()){
				importResultDO.setInfo(errorlist);
				importResultDO.setSuccess(false);
				importResultDO.setShowFileUrl(false);
				return importResultDO;
			}
			int portName = Integer.parseInt(headingMap.get(Constant.PORTNAME).toString());	
			int neName   = Integer.parseInt(headingMap.get(Constant.RELATEDNECUIDSB).toString());
			int erroNum = 0;
			String msg = "";
			for (int i = 5; i <= lastRows; i++) {
				Row xRow = writeSheet.getRow(i);	
				if(xRow != null){
					Object neNameStr=xRow.getCell(neName);
					Object ptpNameStr=xRow.getCell(portName);
					if(neNameStr!= null && !"".equals(neNameStr.toString().trim())
							&&ptpNameStr!= null && !"".equals(ptpNameStr.toString().trim())){
						Map mapPort = null;

						if(excelName.equals(Constant.POSPORTNAME)){
							mapPort = verificationPosPortCell(writeWorkBook, writeSheet, xRow, headingMap, i, lastColumns, portMaps,ptpNoMap,neMaps);						
						}
						if(excelName.equals(Constant.ONUPORTNAME)){
							mapPort = verificationOnuPortCell(writeWorkBook, writeSheet, xRow, headingMap, i,  lastColumns, portMaps, cardFdnMap,ptpNoMap,neMaps);
						}
						if(!ImportCommonMethod.isRowExistError(xRow, lastColumns)){
							//ptp赋值
							addPortModel(mapPort,xRow , headingMap, i,excelName,cardMaps,portList,neMaps);
						}else{
							erroNum++;
						}
					}
				}
			}	
			importResultDO.setInfo("导入表格中共有:"+(lastRows-4)+"条数据");
			importResultDO.setSuccess(true);
			if(erroNum>0){
				importResultDO.setSuccess(false);
				importResultDO.setInfo("有"+erroNum+"条错误数据,数据未进行导入.");
			}else{
				if(portList!=null&&portList.size()>0){
					String errorMsg = getRationError(portList,excelName);
					if(!"".equals(errorMsg)){
						importResultDO.setSuccess(false);
						importResultDO.setInfo(errorMsg);
					}else{
						ImportCommonMethod.ImportPonCardPtpExcelToDB(portList,cardMaps,excelName, importResultDO);
						importResultDO.setSuccess(true);
					}
				}
			}
			return importResultDO;
		} catch (Exception e) {
			LogHome.getLog().error(excelName+"导入失败", e);			
			throw new Exception("导入失败请与管理员联系!");
		}
		
	}
	/**
	 * 将端口Excel中的数据读取到List，卡板数据保存在cardMaps中，
	 * OLT端口，卡板名称不为空：从数据库查询到则更新，否则插入新的板卡
	 * POS端口，自动生成板卡名称，从数据查询如果查到则更新，否则插入新的板卡
	 * ONU端口，卡板名称为空，则自动生成卡板名称，然后从数据查询如果查到则更新，否则插入新的板卡
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void addPortModel(Map mapPort,Row xRow ,Map headingMap,int i,String excelName,
			Map<String,Map> cardMaps,List<Map> portList,Map<String,Map> neMaps) throws Exception{
		LogHome.getLog().info(excelName+"获取值start====");
		try{
			Map mapCard = new HashMap();
			String neName = mapPort.get("NE_NAME")==null?"":mapPort.get("NE_NAME").toString();
			if(!ImportCommonMethod.isEmpty(neName)){
				Map neMap = neMaps.get(neName);
				if(neMap!=null){
					String neCuid = ObjectUtils.toString(neMap.get("CUID"));
					String neFdn = ObjectUtils.toString(neMap.get("FDN"));
					String neVendor = ObjectUtils.toString(neMap.get("RELATED_VENDOR_CUID"));
					String neDistrictCuid  = ObjectUtils.toString(neMap.get("RELATED_DISTRICT_CUID"));
					String ration = ObjectUtils.toString(neMap.get("RATION"));
					String neEms  = ObjectUtils.toString(neMap.get("RELATED_EMS_CUID"));
					String portNo = ObjectUtils.toString(mapPort.get("PORT_NO"));
					//if(excelName.equals(Constant.OLTPORTNAME)||excelName.equals(Constant.ONUPORTNAME)){
					mapPort.put("RELATED_EMS_CUID", neEms);
					//}
					mapPort.put("RELATED_NE_CUID", neCuid);
					mapPort.put("RATION", ration);
					mapPort.put("RELATED_DISTRICT_CUID", neDistrictCuid);
					mapCard.put("RELATED_DEVICE_CUID", neCuid);
					//板卡名称,单元盘型号，所属机槽编号
					String cardNameStr = "";String cardModel = "";String cardFdn = "";String cardComponet = "";String portFdn = "";
					// V_CARD_FDN  :=  V_NE_FDN||':EquipmentHolder=/rack=1/shelf=1/slot=1:Equipment=1';
		            //V_RELATED_UPPER_COMPONENT_CUID := 'EQUIPMENT_HOLDER-'||V_NE_FDN||':EquipmentHolder=/rack=1/shelf=1/slot=1';
					if(excelName.equals(Constant.POSPORTNAME)){
						cardNameStr = "POS无板卡";
						cardFdn = neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1:Equipment=1";
						cardComponet = "EQUIPMENT_HOLDER-"+neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1";
						portFdn = neFdn+":PTP=/rack=1/shelf=1/slot=1/port="+portNo;
						mapPort.put("FDN", portFdn);
						mapPort.put("SYS_NO", "1-1-1-"+portNo);
					}else if(excelName.equals(Constant.ONUPORTNAME)){
						cardNameStr = "ONU无板卡";
						cardFdn = neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1:Equipment=1";
						cardComponet = "EQUIPMENT_HOLDER-"+neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1";
						portFdn = neFdn+":PTP=/rack=1/shelf=1/slot=1/port="+portNo;
						mapPort.put("FDN", portFdn);
						mapPort.put("SYS_NO", "1-1-1-"+portNo);
					}else{
						cardNameStr =  ObjectUtils.toString(mapPort.get("CARD_NAME"));
						cardModel = ObjectUtils.toString(mapPort.get("CARD_MODEL"));
						cardFdn = ObjectUtils.toString(mapPort.get("CARD_FDN"));
						cardComponet = ObjectUtils.toString(mapPort.get("CARD_RELATED_UPPER_COMPONENT_CUID"));
					}
					
					if(excelName.equals(Constant.POSPORTNAME)){
						mapPort.put("DEV_TYPE", ElementEnum.PON_CONFIG_TYPE._pos);
						mapCard.put("DEV_TYPE", ElementEnum.PON_CONFIG_TYPE._pos);
						if(cardMaps.containsKey(neName+cardNameStr)){
							mapCard = cardMaps.get(neName+cardNameStr);
							mapPort.put("RELATED_CARD_CUID", mapCard.get("CUID"));
						}else{
							//查询是否有板块，如果没有则创建
							List<Map> tempList = ImportCommonMethod.oltManageBO.getCardByFdnAndNeCuid(cardFdn, neCuid);
							if(tempList!=null&&tempList.size()>0){
								Map tempCard = tempList.get(0);
								mapPort.put("RELATED_CARD_CUID",tempCard.get("CUID"));
								mapCard.put("LABEL_CN", cardNameStr);
								mapCard.put("CUID", tempCard.get("CUID"));
								mapCard.put("TYPE", "update");
							}else{
								//创建新的卡板
								String cuidCard = CUIDHexGenerator.getInstance().generate("CARD");

								mapCard.put("CUID", cuidCard);
								mapPort.put("RELATED_CARD_CUID",cuidCard);
								mapCard.put("LABEL_CN",cardNameStr);
								mapCard.put("MODEL","");
								mapCard.put("CARD_KIND","");
								mapCard.put("GT_VERSION", 0);
								mapCard.put("ISDELETE", 0);
								mapCard.put("PROJECT_STATE", 0);
								mapCard.put("CREATE_TIME", new Timestamp(System.currentTimeMillis()));
								mapCard.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));				
								mapCard.put("IS_PERMIT_SYS_DEL", 0);
								mapCard.put("SERVICE_STATE", 1);
								mapCard.put("USE_TYPE", 1);
								mapCard.put("PHOTOELECTRICITY", 1);
								mapCard.put("LIVE_CYCLE",2);
								mapCard.put("USE_STATE", 1);
								mapCard.put("OBJECT_TYPE_CODE", 1001);	
								mapCard.put("TYPE", "add");
							}
							mapCard.put("FDN", cardFdn);
							mapCard.put("RELATED_UPPER_COMPONENT_CUID", cardComponet);
							if(!ImportCommonMethod.isEmpty(neVendor)){
								mapCard.put("VENDOR", neVendor);
							}
							cardMaps.put(neName+cardNameStr,mapCard);
						}
					}
					if(excelName.equals(Constant.ONUPORTNAME)){
						mapPort.put("DEV_TYPE", ElementEnum.PON_CONFIG_TYPE._onu);
						mapCard.put("DEV_TYPE", ElementEnum.PON_CONFIG_TYPE._onu);
						if(cardMaps.containsKey(neName+cardNameStr)){
							mapCard = cardMaps.get(neName+cardNameStr);
							mapPort.put("RELATED_CARD_CUID", mapCard.get("CUID"));
						}else{
							//查询是否有板块，如果没有则创建
							List<Map> tempList = ImportCommonMethod.oltManageBO.getCardByFdnAndNeCuid(cardFdn, neCuid);
							if(tempList!=null&&tempList.size()>0){
								Map tempCard = tempList.get(0);
								mapPort.put("RELATED_CARD_CUID",tempCard.get("CUID"));
								mapCard.put("LABEL_CN", cardNameStr);
								mapCard.put("CUID", tempCard.get("CUID"));
								mapCard.put("TYPE", "update");
							}else{
								//创建新的卡板
								String cuidCard =CUIDHexGenerator.getInstance().generate("CARD");
								mapCard.put("CUID", cuidCard);
								mapPort.put("RELATED_CARD_CUID",cuidCard);
								mapCard.put("LABEL_CN",cardNameStr);
								mapCard.put("MODEL","");
								mapCard.put("CARD_KIND","");
								mapCard.put("GT_VERSION", 0);
								mapCard.put("ISDELETE", 0);
								mapCard.put("PROJECT_STATE", 0);
								mapCard.put("CREATE_TIME", new Timestamp(System.currentTimeMillis()));
								mapCard.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));				
								mapCard.put("IS_PERMIT_SYS_DEL", 0);
								mapCard.put("SERVICE_STATE", 1);
								mapCard.put("USE_TYPE", 1);
								mapCard.put("PHOTOELECTRICITY", 1);
								mapCard.put("LIVE_CYCLE",2);
								mapCard.put("USE_STATE", 1);
								mapCard.put("OBJECT_TYPE_CODE", 1001);	
								mapCard.put("TYPE", "add");
							}
							mapCard.put("FDN", cardFdn);
							mapCard.put("RELATED_UPPER_COMPONENT_CUID", cardComponet);
							if(!ImportCommonMethod.isEmpty(neVendor)){
								mapCard.put("VENDOR", neVendor);
							}
							cardMaps.put(neName+cardNameStr,mapCard);
						}
					}
					//端口速率
					if(ImportCommonMethod.isEnum(Constant.PORTRATE,mapPort.get("PORT_RATE")+"")){			
						if("20M".equals(mapPort.get("PORT_RATE"))){
							mapPort.put("PORT_RATE",48L);
						}else{
							mapPort.put("PORT_RATE", (java.lang.Long) RackEnum.RATR_TYPE.getValue(mapPort.get("PORT_RATE")+""));
						}
					}
					//端口类型
					if(ImportCommonMethod.isEnum(Constant.PORTSUBTYPE,mapPort.get("PORT_SUB_TYPE")+"")){			
						mapPort.put("PORT_SUB_TYPE",(java.lang.Long) RackEnum.PORT_SUB_TYPE.getValue(mapPort.get("PORT_SUB_TYPE")+""));
					}
					//光电属性
					if(mapPort.get("PORT_TYPE")==null||mapPort.get("PORT_TYPE").toString().length()==0){
						mapPort.put("PORT_TYPE",1);
					}else{
						if(ImportCommonMethod.isEnum(Constant.PORTTYPE,mapPort.get("PORT_TYPE")+"")){			
							mapPort.put("PORT_TYPE",(java.lang.Long) RackEnum.PORT_TYPE.getValue(mapPort.get("PORT_TYPE")+""));
						}
					}
					//端口状态
					mapPort.put("PORT_STATE",1);
//					if(mapPort.get("portState)==null||mapPort.get("portState).toString().length()==0){
//						mapPort.put("portState,1);
//					}else{
//						if(ImportCommonMethod.isEnum(Constant.PORTSTATE,mapPort.get("portState)+"")){			
//							mapPort.put("portState,(java.lang.Long) RackEnum.PORT_STATE.getValue(mapPort.get("portState)+""));
//						}
//					}
					List<Map> tempList = ImportCommonMethod.oltManageBO.
							getPtpByLabelCnAndCardCuidAndNeCuid(mapPort.get("LABEL_CN")+"", mapPort.get("RELATED_CARD_CUID")+"", mapPort.get("RELATED_NE_CUID")+"");
					if(tempList!=null&&tempList.size()>0){
						mapPort.put("TYPE","update");
						mapPort.put("CUID", tempList.get(0).get("CUID").toString());
					}else {
						String cuidPort = CUIDHexGenerator.getInstance().generate("PTP");
	

						mapPort.put("CUID", cuidPort);
						mapPort.put("GT_VERSION", 0);
						mapPort.put("ISDELETE", 0);
						mapPort.put("PROJECT_STATE", 0);
						mapPort.put("CREATE_TIME", new Timestamp(System.currentTimeMillis()));
						mapPort.put("IS_CHANNEL", 0);
						mapPort.put("TERMINATION_MODE", 1);
						mapPort.put("DIRECTIONALITY", 2);
						mapPort.put("MSTP_NANFCMODE", 1);
						mapPort.put("MSTP_PORT_TYPE", 1);
						mapPort.put("IS_PERMIT_SYS_DEL", 0);
						mapPort.put("MSTP_FLOWCTRL", 1);
						mapPort.put("MSTP_ANFCMODE", 1);
						mapPort.put("MSTP_LCAS_FLAG", 1);
						mapPort.put("MSTP_PPTENABLE", 1);
						mapPort.put("MSTP_EDETECT", 1);
						mapPort.put("MSTP_TAG_FLAG", 1);
						mapPort.put("MSTP_ENCAPFORMAT", 1);
						mapPort.put("MSTP_ENCAPPROTOCOL", 1);
						mapPort.put("MSTP_CFLEN", 1);
						mapPort.put("MSTP_BMSGSUPPRESS", 1);
						mapPort.put("MSTP_FCSCALSEQ", 1);
						mapPort.put("LOOP_STATE", 1);
						mapPort.put("MSTP_PORTENABLE", 1);
						mapPort.put("DOMAIN", 0);
						mapPort.put("MSTP_WORKMODE", 1);
						mapPort.put("MSTP_PORT_SERVICETYPE", 1);
						mapPort.put("MSTP_SCRAMBEL", 1);
						mapPort.put("LINE_BRANCH_TYPE", 1);
						mapPort.put("OBJECT_TYPE_CODE", 15013);
						mapPort.put("MSTP_EXTENDEADER", 1);
						mapPort.put("LIVE_CYCLE",2);
						mapPort.put("USE_STATE", 1);
						mapPort.put("TYPE","add");
					}
					//根据所属设备CUID和归属板卡CUID，判断端口是否已经存储
					mapPort.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
					portList.add(mapPort);
				}
			}
			LogHome.getLog().info(excelName+"获取值end====");
		} catch (Exception e) {
			LogHome.getLog().error("接入网-板卡端口校验装入Map出错", e);
			throw new Exception("接入网-板卡端口校验装入Map出错："+e.getMessage());
		} 	
	}
	
	/**
	 * POS的端口校验，并获取EXCEL数据
	 */
	@SuppressWarnings("unchecked")
	public  Map verificationPosPortCell(Workbook writeWorkBook, Sheet writeSheet,Row xRow ,Map headingMap, 
			int i ,int lastColumns,Map portNameMap,Map ptpNoMap,Map<String,Map> neMaps) throws Exception{
		LogHome.getLog().info("POS端口校验start====");
		//所属设备
		int relatedNeCuid = Integer.parseInt(headingMap.get(Constant.RELATEDNECUIDSB).toString());
		//端口名称
		int portName = Integer.parseInt(headingMap.get(Constant.PORTNAME).toString());
		//端口编号
		int portNo = Integer.parseInt(headingMap.get(Constant.PORTNO).toString());
		//端口速率
		int portRate = Integer.parseInt(headingMap.get(Constant.PORTRATE).toString());
		//端口类型
		int portSubType = Integer.parseInt(headingMap.get(Constant.PORTSUBTYPE).toString());
		//光电属性
		int portType = Integer.parseInt(headingMap.get(Constant.PORTTYPE).toString());
		//端口状态
		//int portState = Integer.parseInt(headingMap.get(Constant.PORTSTATE).toString());
		//端口备注
		int remark = Integer.parseInt(headingMap.get(Constant.REMARK).toString());
		Map map = new HashMap();
		try{
			//非空校验
			ImportCommonMethod.decideFields(writeWorkBook, writeSheet, i, lastColumns);
			//所属设备校验
			String neStr =  ImportCommonMethod.getCellValue(xRow.getCell(relatedNeCuid));
			//String neFdn = "";
			//String neCuid = "";
			if(!ImportCommonMethod.isEmpty(neStr)){
				map.put("NE_NAME",neStr);
				if(!neMaps.containsKey(neStr)){
					Map neMap = ImportCommonMethod.verificationAllNeName(writeWorkBook, writeSheet, relatedNeCuid, i, lastColumns,Constant.RELATEDNECUIDSB);
					if(neMap!=null){
						//neCuid = neMap.get("CUID")+"";
						String neFdnTemp = neMap.get("FDN")+"";
						boolean isFdn = false;boolean isDis = false;
						if(ImportCommonMethod.isEmpty(neFdnTemp)){
							ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,relatedNeCuid, lastColumns,"FDN信息为空，请检查！");
						}else{
							if(!ImportCommonMethod.verificationNeFdnInfo(neFdnTemp, "olt")){
								ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,relatedNeCuid, lastColumns,"FDN信息不符合规定，请检查！");
							}else{
								isFdn = true;
							}
						}
						if(ImportCommonMethod.isEmpty(neMap.get("RELATED_DISTRICT_CUID")+"")){
							ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,relatedNeCuid,  lastColumns,"区域信息为空，请检查！");
						}else{
							isDis = true;
						}
						if(isFdn&&isDis){
							//neFdn = neFdnTemp;
							neMaps.put(neStr, neMap);
						}
					}
				}else {
					Map neMap = neMaps.get(neStr);
					//neCuid = neMap.get("CUID").toString();
					//neFdn = neMap.get("FDN").toString();
				}
			}
			//String cardStr = "";
			String ptpSubtype = ImportCommonMethod.getCellValue(xRow.getCell(portSubType));
			//String cardFdn = "";
			if(!ImportCommonMethod.isEmpty(ptpSubtype)){
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, portSubType, i, lastColumns,Constant.PORTSUBTYPE);
				map.put("PORT_SUB_TYPE", ptpSubtype);
				/*if(ptpSubtype.equals("分光器上联口")){
					cardStr = neStr+"-1";
				}else{
					cardStr = neStr+"-2";
				}
				map.put("CARD_NAME",cardStr);
				map.put("CARD_TEMP_FDN",(int)(Math.random()*10)+"-"+(int)(Math.random()*10)+"-"+(int)(Math.random()*10+30));
				if(ImportCommonMethod.isNotEmpty(neFdn)){
					String cardFdn1 = getCardPashFdn(map.get("CARD_TEMP_FDN")+"");
					cardFdn = AnFdnHelper.makeOltCardFdn(neFdn,cardFdn1, "1");
					map.put("CARD_FDN", cardFdn);
					map.put("CARD_RELATED_UPPER_COMPONENT_CUID", EquipmentHolder.CLASS_NAME
							+ AnFdnHelper.fdnSpliterD + neFdn + AnFdnHelper.fdnSpliter
							+ AnFdnHelper.HolderHeader + cardFdn1);
				}*/
			}
			String ptpStr = ImportCommonMethod.getCellValue(xRow.getCell(portName));
			if(!ImportCommonMethod.isEmpty(ptpStr)){
				map.put("LABEL_CN", ptpStr);
				ImportCommonMethod.VerificationNameRepeat(writeWorkBook, writeSheet, portName, i, lastColumns,portNameMap);
			}
			
			String ptpNo = ImportCommonMethod.getCellValue(xRow.getCell(portNo));
			if(!ImportCommonMethod.isEmpty(ptpNo)){
				map.put("PORT_NO", ptpNo);
//				if(!ImportCommonMethod.isNotEmpty(map.get("CARD_TEMP_FDN")+"")){
//					map.put("sysNo, map.get("CARD_TEMP_FDN")+"-"+ptpNo);
//				}
			}
				/*ImportCommonMethod.verificationNumber(writeWorkBook, writeSheet, portNo, i, lastColumns,Constant.PORTNO);
				if(!ImportCommonMethod.isEmpty(neStr)){
					//  将excel 中获取 strCardName-strPortNo  然后放入 checkPortFdnMap <设备名称+端口号 , 端口号>  ，最后核查是否已经重复
					if(ptpNoMap.containsKey(neStr+cardStr+"-"+ptpNo)){
						ImportCommonMethod.verificationSamePortFdn(writeWorkBook, writeSheet, portNo, i, lastColumns,Constant.PORTNO);
					}else{
						if(neMaps.get(neStr)!=null){
							if(ImportCommonMethod.isSamePortFdn(writeWorkBook, writeSheet,neStr,cardStr,ptpStr,ptpNo,portNo,i, lastColumns,Constant.PORTNO,neMaps.get(neStr).get("CUID").toString())){
								ImportCommonMethod.verificationSamePortFdn(writeWorkBook, writeSheet, portNo, i, lastColumns,Constant.PORTNO);
							}else{
								ptpNoMap.put(neStr+cardStr+"-"+ptpNo, ptpNo);
							}
						}
					}
				}
				if(ImportCommonMethod.isNotEmpty(cardFdn)){
					String ptpFdn = AnFdnHelper.getPortFdnByCardFdnAndPortNo(cardFdn,Integer.parseInt(ptpNo));
					map.put("fdn,ptpFdn);
				}
			}*/
			String ptpRate = ImportCommonMethod.getCellValue(xRow.getCell(portRate));
			if(!ImportCommonMethod.isEmpty(ptpRate)){
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, portRate, i, lastColumns,Constant.PORTRATE);
				map.put("PORT_RATE", ptpRate);
			}
			String ptpType = ImportCommonMethod.getCellValue(xRow.getCell(portType));
			if(!ImportCommonMethod.isEmpty(ptpType)){
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, portType, i, lastColumns,Constant.PORTTYPE);  
				map.put("PORT_TYPE", ptpType);
			}
//			String ptpState = ImportCommonMethod.getCellValue(xRow.getCell(portState));
//			if(!ImportCommonMethod.isEmpty(ptpState)){
//				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, portState, i, lastColumns,Constant.PORTSTATE);
//				map.put("portState, ptpState);
//			}
			String remarkStr = ImportCommonMethod.getCellValue(xRow.getCell(remark));
			if(!ImportCommonMethod.isEmpty(remarkStr)){
				map.put("REMARK", remarkStr);
			}
			LogHome.getLog().info("POS端口校验end====");
		} catch (Exception e) {
			LogHome.getLog().error("接入网-POS端口校验出错", e);		
			throw new Exception("接入网-POS端口校验出错："+e.getMessage());
		}
		return map;
	}
	/**
	 * ONU端口校验，并获取EXCEL数据
	 */
	public  Map  verificationOnuPortCell(Workbook writeWorkBook, Sheet writeSheet,Row xRow ,Map headingMap,
			int i ,int lastColumns,Map portNameMap,Map cardFdnMap,Map ptpNoMap,Map<String,Map> neMaps) throws Exception{
		LogHome.getLog().info("ONU端口校验start====");
		//所属设备
		int relatedNeCuid = Integer.parseInt(headingMap.get(Constant.RELATEDNECUIDSB).toString());
		//端口名称
		int portName = Integer.parseInt(headingMap.get(Constant.PORTNAME).toString());
		//端口编号
		int portNo = Integer.parseInt(headingMap.get(Constant.PORTNO).toString());
		//端口速率
		int portRate = Integer.parseInt(headingMap.get(Constant.PORTRATE).toString());
		//端口类型
		int portSubType = Integer.parseInt(headingMap.get(Constant.PORTSUBTYPE).toString());
		//光电属性
		int portType = Integer.parseInt(headingMap.get(Constant.PORTTYPE).toString());
		//端口状态
		//int portState = Integer.parseInt(headingMap.get(Constant.PORTSTATE).toString());
		//端口VLAN
		int vlan = Integer.parseInt(headingMap.get(Constant.VLAN).toString());
		//端口备注
		int remark = Integer.parseInt(headingMap.get(Constant.REMARK).toString());
		Map map = new HashMap();
		try{
			//非空校验
			ImportCommonMethod.decideFields(writeWorkBook, writeSheet, i, lastColumns);
			//所属设备校验
			String neStr =  ImportCommonMethod.getCellValue(xRow.getCell(relatedNeCuid));
			//String neCuid = "";
			//String neFdn = "";
			if(!ImportCommonMethod.isEmpty(neStr)){
				map.put("NE_NAME",neStr);
				if(!neMaps.containsKey(neStr)){
					Map neMap = ImportCommonMethod.verificationAllNeName(writeWorkBook, writeSheet, relatedNeCuid, i, lastColumns,Constant.RELATEDNECUIDSB);
					if(neMap!=null){
						//neCuid = neMap.get("CUID")+"";
						String neFdnTemp = neMap.get("FDN")+"";
						boolean isFdn = false;boolean isEms = false;boolean isDis = false;
						if(ImportCommonMethod.isEmpty(neFdnTemp)){
							ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,relatedNeCuid, lastColumns,"FDN信息为空，请检查！");
						}else{
							if(!ImportCommonMethod.verificationNeFdnInfo(neFdnTemp, "olt")){
								ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,relatedNeCuid, lastColumns,"FDN信息不符合规定，请检查！");
							}else{
								isFdn = true;
							}
						}
						if(ImportCommonMethod.isEmpty(neMap.get("RELATED_EMS_CUID")+"")){
							ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,relatedNeCuid,  lastColumns,"EMS信息为空，请检查！");
						}else {
							isEms = true;
						}
						if(ImportCommonMethod.isEmpty(neMap.get("RELATED_DISTRICT_CUID")+"")){
							ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,relatedNeCuid,  lastColumns,"区域信息为空，请检查！");
						}else{
							isDis = true;
						}
						if(isFdn&&isEms&&isDis){
							//neFdn = neFdnTemp;
							neMaps.put(neStr, neMap);
						}
					}
				}else {
					Map neMap = neMaps.get(neStr);
					//neCuid = neMap.get("CUID").toString();
					//neFdn = neMap.get("FDN").toString();
				}
			}
			
			String ptpSubtype = ImportCommonMethod.getCellValue(xRow.getCell(portSubType));
			if(!ImportCommonMethod.isEmpty(ptpSubtype)){
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, portSubType, i, lastColumns,Constant.PORTSUBTYPE);
				map.put("PORT_SUB_TYPE", ptpSubtype);
			}
			
/*			String cardStr = ImportCommonMethod.getCellValue(xRow.getCell(cardName));
			if(!ImportCommonMethod.isEmpty(cardStr)){
				map.put("CARD_NAME",cardStr);
			}else {
				if("ONU上联口".equals(ptpSubtype)){
					cardStr = neStr+"-1";
				}else{
					cardStr = neStr+"-2";
				}
				map.put("CARD_NAME",cardStr);
			}
			String modelStr = ImportCommonMethod.getCellValue(xRow.getCell(singeModel));
			if(!ImportCommonMethod.isEmpty(modelStr)){
				map.put("CARD_MODEL", modelStr);
				ImportCommonMethod.verificationRelated(writeWorkBook, xRow,singeModel, lastColumns,Constant.SINGLEMODEL,cardKindMap);
			}
			String cardTempFdn = ImportCommonMethod.getCellValue(xRow.getCell(relatedUpperComponentCuid));
			boolean res = true;
			boolean cardSam = true;
			boolean fdnSam = true;
			if(!ImportCommonMethod.isEmpty(cardTempFdn)){
			    res = ImportCommonMethod.verificationCardFdn(writeWorkBook, writeSheet, relatedUpperComponentCuid, i, lastColumns,Constant.RELATEDUPPERCOMPONENTCUID);
				//判断Excel中相同架框槽的板卡是否一致
				if(cardFdnMap.containsKey("FDNSAME"+neStr+cardTempFdn)){
					if(!cardFdnMap.get("FDNSAME"+neStr+cardTempFdn).equals(cardStr)){
						fdnSam = false;
						ImportCommonMethod.verificationSameFdnCard(writeWorkBook, writeSheet, cardName, i, lastColumns,Constant.RELATEDUPPERCOMPONENTCUID);
					}//如果相等的话不做任何操作
				}else{
					cardFdnMap.put("FDNSAME"+neStr+cardTempFdn,cardStr);
				}
				//判断Excel中相同板卡的架框槽是否一致
				if(cardFdnMap.containsKey("CARD"+neStr+cardStr)){
					if(!cardFdnMap.get("CARD"+neStr+cardStr).equals(cardTempFdn)){
						cardSam = false;
						ImportCommonMethod.verificationSameCardFdn(writeWorkBook, writeSheet, relatedUpperComponentCuid, i, lastColumns,Constant.RELATEDUPPERCOMPONENTCUID);
					}//如果相等的话不做任何操作
				}else{
					cardFdnMap.put("CARD"+neStr+cardStr,cardTempFdn);
				}
				map.put("CARD_TEMP_FDN", cardTempFdn);
			}else{
				map.put("CARD_TEMP_FDN",(int)(Math.random()*10)+"-"+(int)(Math.random()*10)+"-"+(int)(Math.random()*10+30));
			}
			String cardFdn = "";
			if(ImportCommonMethod.isNotEmpty(neFdn)){
				String cardFdn1 = getCardPashFdn(map.get("CARD_TEMP_FDN")+"");
				String cardFdn2 = AnFdnHelper.makeOltCardFdn(neFdn,cardFdn1, "1");
				if(ImportCommonMethod.isNotEmpty(cardTempFdn)&&res&&cardSam&&fdnSam){
					//校验板卡FDN是否已经存在
					List<Map> tempList = ImportCommonMethod.oltManageBO.
							isExistCardFdn(cardFdn);
					if(tempList!=null&&tempList.size()>0){
						Map map2 = tempList.get(0);
						if(!cardStr.equals(map2.get("LABEL_CN").toString())){
							ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,relatedUpperComponentCuid, lastColumns,"根据所属机槽编号生成的板卡FDN信息数据库中已存在，请检查！");
						}else {
							cardFdn = cardFdn2;
						}
					}else {
						cardFdn = cardFdn2;
					}
					if(ImportCommonMethod.isNotEmpty(cardFdn)){
						map.put("CARD_FDN", cardFdn);
						map.put("CARD_RELATED_UPPER_COMPONENT_CUID", EquipmentHolder.CLASS_NAME
								+ AnFdnHelper.fdnSpliterD + neFdn + AnFdnHelper.fdnSpliter
								+ AnFdnHelper.HolderHeader + cardFdn1);
					}
				}
			}*/
			String ptpStr = ImportCommonMethod.getCellValue(xRow.getCell(portName));
			if(!ImportCommonMethod.isEmpty(ptpStr)){
				map.put("LABEL_CN", ptpStr);
				ImportCommonMethod.VerificationNameRepeat(writeWorkBook, writeSheet, portName, i, lastColumns,portNameMap);
			}
			String ptpNo = ImportCommonMethod.getCellValue(xRow.getCell(portNo));
			if(!ImportCommonMethod.isEmpty(ptpNo)){
				map.put("PORT_NO", ptpNo);
//				if(!ImportCommonMethod.isNotEmpty(map.get("CARD_TEMP_FDN")+"")){
//					map.put("sysNo, map.get("CARD_TEMP_FDN")+"-"+ptpNo);
//				}
				ImportCommonMethod.verificationNumber(writeWorkBook, writeSheet, portNo, i, lastColumns,Constant.PORTNO);
			}       
					/*if(!ImportCommonMethod.isEmpty(neStr)&& !ImportCommonMethod.isEmpty(cardStr)){
					//  将excel 中获取 strCardName-strPortNo  然后放入 checkPortFdnMap <设备名称+机盘名+端口号 , 端口号>  ，最后核查是否已经重复
					if(ptpNoMap.containsKey(neStr+cardStr+"-"+ptpNo)){
						ImportCommonMethod.verificationSamePortFdn(writeWorkBook, writeSheet, portNo, i, lastColumns,Constant.PORTNO);
					}else{
						if(neMaps.get(neStr)!=null){
							if(ImportCommonMethod.isSamePortFdn(writeWorkBook, writeSheet,neStr,cardStr,ptpStr,ptpNo,portNo,i, lastColumns,Constant.PORTNO,neMaps.get(neStr).get("CUID").toString())){
								ImportCommonMethod.verificationSamePortFdn(writeWorkBook, writeSheet, portNo, i, lastColumns,Constant.PORTNO);
							}else{
								ptpNoMap.put(neStr+cardStr+"-"+ptpNo, ptpNo);
							}
						}
					}
				}
				if(ImportCommonMethod.isNotEmpty(cardFdn)){
					String ptpFdn = AnFdnHelper.getPortFdnByCardFdnAndPortNo(cardFdn,Integer.parseInt(ptpNo));
					if(ImportCommonMethod.isNotEmpty(cardTempFdn)){
						//校验端口FDN是否已经存在
						List<Map> tempList = ImportCommonMethod.oltManageBO.
								isExistPtpFdn(ptpFdn);
						if(tempList!=null&&tempList.size()>0){
							Map map2 = tempList.get(0);
							if(!ptpStr.equals(map2.get("LABEL_CN").toString())){
								ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,relatedUpperComponentCuid, lastColumns,"根据所属机槽编号生成的端口FDN信息数据库中已存在，请检查！");
							}else {
								map.put("fdn,ptpFdn);
							}
						}else {
							map.put("fdn,ptpFdn);
						}
					}
				}
			}*/
			
			String ptpRate = ImportCommonMethod.getCellValue(xRow.getCell(portRate));
			if(!ImportCommonMethod.isEmpty(ptpRate)){
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, portRate, i, lastColumns,Constant.PORTRATE);
				map.put("PORT_RATE", ptpRate);
			}
			
			String ptpType = ImportCommonMethod.getCellValue(xRow.getCell(portType));
			if(!ImportCommonMethod.isEmpty(ptpType)){
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, portType, i, lastColumns,Constant.PORTTYPE);  
				map.put("PORT_TYPE", ptpType);
			}
			
//			String ptpState = ImportCommonMethod.getCellValue(xRow.getCell(portState));
//			if(!ImportCommonMethod.isEmpty(ptpState)){
//				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, portState, i, lastColumns,Constant.PORTSTATE);
//				map.put("portState, ptpState);
//			}
			String vlanStr = ImportCommonMethod.getCellValue(xRow.getCell(vlan));
			if(!ImportCommonMethod.isEmpty(vlanStr)){
				ImportCommonMethod.verificationVLANNumber(writeWorkBook, writeSheet, vlan, i, lastColumns,Constant.VLAN);
				map.put("VLAN", vlanStr);
			}
			String remarkStr = ImportCommonMethod.getCellValue(xRow.getCell(remark));
			if(!ImportCommonMethod.isEmpty(remarkStr)){
				map.put("REMARK", remarkStr);
			}
			LogHome.getLog().info("ONU端口校验end====");
		} catch (Exception e) {
			LogHome.getLog().error("接入网-ONU端口校验出错", e);		
			throw new Exception("接入网-OUN端口校验出错："+e.getMessage());
		}
		return map;
	}
	
	/**
	 * 判断导入模板的列名称是否为空
	 */
	private static Map getCardPortHeadingMap(Sheet sheet, List errorlist,int lastColumns) {
		Map headingMap = new HashMap();
		Row xRow = sheet.getRow(0);
		for(int i=1;i<lastColumns;i++){
			Object columnName = xRow.getCell(i);
			if(columnName != null){
				headingMap.put(columnName.toString(),i);
			}else{
				errorlist.add("该模板的第"+(i+1)+"列头名称不能为空!");
			}
		}
		return headingMap;
	}
	/**
	 * 返回操作导入相应的BO
	 * 
	 * @return
	 */
	private static ImportBasicDataBO getImportBasicDataBO() {
		return (ImportBasicDataBO) SpringContextUtil.getBean("importBasicDataBO");
	}
	/**
	 * 将机槽编号转化为FDN信息
	 */
	private String getCardPashFdn(String cardFdn){
		String[] split = cardFdn.split("-");
		if(split.length == 3){
			if("NA".equals(split[0])){
				split[0] = "-1";
			}
			return AnFdnHelper.fdnSpliterC+AnFdnHelper.RackHeader+split[0]
					+AnFdnHelper.fdnSpliterC+AnFdnHelper.ShelfHeader+split[1]
					+AnFdnHelper.fdnSpliterC+AnFdnHelper.SlotHeader+split[2];
		}else{
			return null;
		}
	}
	private String getRationError(List portList,String excelName) throws SQLException{
		String errorMsg = "";
		//统计模板中非上联口的数量
		Map<String,Integer> nePortMap = new HashMap<String,Integer>();
		//统计模板中上联口的数量
		Map<String,Integer> neUpPortMap = new HashMap<String,Integer>();
		for(int listIndex = 0;listIndex<portList.size();listIndex++){
			Map portMap = (Map)portList.get(listIndex);
			String related_ne_cuid = ObjectUtils.toString(portMap.get("RELATED_NE_CUID"));
			String pcType = ObjectUtils.toString(portMap.get("TYPE"));
			String portSubType = ObjectUtils.toString(portMap.get("PORT_SUB_TYPE"));
			if("add".equals(pcType)){
				if(nePortMap.get(related_ne_cuid)==null){
					//12 POS上联口  14 ONU上联口
					if(portSubType!=null&&portSubType.length()>0&&("12".equals(portSubType)||"14".equals(portSubType))){
						if(neUpPortMap.get(related_ne_cuid)==null){
							neUpPortMap.put(related_ne_cuid, 1);
						}else{
							neUpPortMap.put(related_ne_cuid, Integer.parseInt(ObjectUtils.toString(neUpPortMap.get(related_ne_cuid)))+1);
						}
					}else{
						nePortMap.put(related_ne_cuid, 1);
					}
				}else{
					if(portSubType!=null&&portSubType.length()>0&&("12".equals(portSubType)||"14".equals(portSubType))){
						if(neUpPortMap.get(related_ne_cuid)==null){
							neUpPortMap.put(related_ne_cuid, 1);
						}else{
							neUpPortMap.put(related_ne_cuid, Integer.parseInt(ObjectUtils.toString(neUpPortMap.get(related_ne_cuid)))+1);
						}
					}else{
						nePortMap.put(related_ne_cuid, Integer.parseInt(ObjectUtils.toString(nePortMap.get(related_ne_cuid)))+1);
					}
				}
			}
		}
		String neStr = "";
		for(String ne : nePortMap.keySet()){
			neStr += "'"+ne+"',";
		}
		if(!"".equals(neStr)){
			neStr = neStr.substring(0,neStr.length()-1);
			//只有POS端口导入时，才校验分光比
			if(excelName.equals(Constant.POSPORTNAME)){
				List posPortList = getOnuManageBO().getPosPortCount(neStr);
				for(int j=0;j<posPortList.size();j++){
					Map nePort = (Map)posPortList.get(j);
					String neCuid = ObjectUtils.toString(nePort.get("RELATED_NE_CUID"));
					String ration = ObjectUtils.toString(nePort.get("RATION"));
					String labelCn = ObjectUtils.toString(nePort.get("LABEL_CN"));
					int ptpNum = Integer.parseInt(ObjectUtils.toString(nePort.get("PTPNUM")));
					int upPtpNum = Integer.parseInt(ObjectUtils.toString(nePort.get("UPPTPNUM")));
					if(ration!=null&&ration.length()>0&&ration.indexOf(":")!=-1){
						String[] resultArray = ration.split(":");
						if(resultArray.length==2){
							int a = Integer.parseInt(resultArray[0]);
							if(neUpPortMap.get(neCuid)!=null&&(neUpPortMap.get(neCuid)+upPtpNum)>a){
								errorMsg += labelCn+"的分光比是"+ration+"，目前该设备的上联端口数为"+upPtpNum+"个，导入模板中上联口"+neUpPortMap.get(neCuid)+"个，不允许导入<br>";
							}else{
								int b = Integer.parseInt(resultArray[1]);
								if(nePortMap.get(neCuid)!=null&&(nePortMap.get(neCuid)+ptpNum-upPtpNum)>b){
									errorMsg += labelCn+"的分光比是"+ration+"，目前该设备的端口总数为"+ptpNum+"个，导入模板中";
									if(neUpPortMap.get(neCuid)!=null){
										errorMsg += "上联端口数"+neUpPortMap.get(neCuid)+"个，";
									}
									errorMsg += "非上联端口数"+nePortMap.get(neCuid)+"个，";
									if(upPtpNum>0){
										errorMsg += "已存在上联端口数"+upPtpNum+"个，";
									}
									if(ptpNum-upPtpNum>0){
										errorMsg += "已存在非上联端口数"+(ptpNum-upPtpNum)+"个，";
									}
									errorMsg += "不允许导入<br>";
								}
							}
						}else{
							errorMsg += labelCn+"的分光比是"+ration+"，数据异常请修复！<br>";
						}
					}else{
						errorMsg += labelCn+"的分光比是"+ration+"，数据异常请修复！<br>";
					}
				}
			}
			if(excelName.equals(Constant.ONUPORTNAME)){
				List onuPortList = getOnuManageBO().getOnuPortCount(neStr);
				for(int j=0;j<onuPortList.size();j++){
					Map nePort = (Map)onuPortList.get(j);
					String neCuid = ObjectUtils.toString(nePort.get("RELATED_NE_CUID"));
					String labelCn = ObjectUtils.toString(nePort.get("LABEL_CN"));
					int ptpNum = Integer.parseInt(ObjectUtils.toString(nePort.get("PTPNUM")));
					int upPtpNum = Integer.parseInt(ObjectUtils.toString(nePort.get("UPPTPNUM")));
					if(neUpPortMap.get(neCuid)!=null&&(neUpPortMap.get(neCuid)+upPtpNum)>1){
						errorMsg += labelCn+"目前该设备的上联端口数为"+upPtpNum+"个，导入模板中上联口数量"+neUpPortMap.get(neCuid)+"个，不允许导入<br>";
					}
				}
			}
		}
		return errorMsg;
	}
}
