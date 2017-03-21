package com.boco.workflow.webservice.upload.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.workflow.webservice.upload.bo.ImportAttributeQueryBO;
import com.boco.workflow.webservice.upload.bo.ImportBasicDataBO;
import com.boco.workflow.webservice.upload.constants.AnmsConst;
import com.boco.workflow.webservice.upload.constants.Constant;
import com.boco.workflow.webservice.upload.constants.ElementEnum;
import com.boco.workflow.webservice.upload.servlet.ImportResultDO;


/**
 * Pos设备信息导出到Excel
 */
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class ImportPosExcel {

	List posList = null;
	List posNameList = null;	
	Map nameMap=null;
	Map neModelCuidNameMap;
	//校验模板列名称是否规范
	public static String[] posExcelColumns = new String[]{
		/* 00 */	"中文名称",

		/* 02 */	"分光器名称",
		/* 03 */	"分光比",
		/* 04 */	"安装位置",
		/* 05 */	"所属上联设备",
		/* 06 */	"上联设备主用端口",
		/* 07 */	"生产厂商",
		/* 08 */	"资产归属",
		/* 09 */	"资产归属人",
		/* 10 */	"生命周期状态",
		/* 11 */	"是否直接带用户",
		/* 12 */	"入网时间",
		/* 13 */	"接入类型",
		/* 14 */	"维护班组",
		/* 15 */	"维护方式",
		/* 16 */	"建设日期",
		/* 17 */	"SN码",
		/* 18 */	"所属工程",
		/* 19 */	"一线数据维护人",
		/* 20 */	"移动公司数据质量责任人",
		/* 21 */    "上联设备备用端口"
		};
	private String relatedvendorcuid;
	/**
	 * 有参构造方法
	 * @param neModelCuidNameMap
	 */
	public ImportPosExcel(Map neModelCuidNameMap){
		this.neModelCuidNameMap = neModelCuidNameMap;
	}	
	


	
	/**
	 * 导入POS基础数据
	 */
	protected  ImportResultDO importPosBasicData(Workbook writeWorkBook, Sheet writeSheet,String pathname, String excelName,String prjcode) throws Exception {
		ImportResultDO importResultDO = new ImportResultDO(excelName);
		posList = new ArrayList();
		posNameList = new ArrayList();	
		nameMap=new HashMap();
		List returnList = null;
		try {			
			int lastRows = writeSheet.getLastRowNum();
			Row xrow=writeSheet.getRow(0);//获取第一行求的列数
			int lastColumns =xrow.getLastCellNum();
			List errorlist = new ArrayList();
			Map headingMap = getPosHeadingMap(writeSheet, 0, errorlist,lastColumns);	
			//表格头部有问题,返回异常信息
			if(!errorlist.isEmpty()){
				importResultDO.setInfo(errorlist);
				importResultDO.setSuccess(false);
				importResultDO.setShowFileUrl(false);
				return importResultDO;
			}
			// 取得每一列对应的列号，然后进行校验判断。		
			// int relatedOltCuid = Integer.parseInt(headingMap.get(Constant.RELATEDUNIONCUID).toString());
			// int relatedPortCuid = Integer.parseInt(headingMap.get(Constant.RELATEDPORTCUID).toString());
			int labelCn = Integer.parseInt(headingMap.get(Constant.POS_LABELCN).toString());
			List portNameList = new ArrayList();
			for (int i = 5; i <= lastRows; i++) {
				Row xRow = writeSheet.getRow(i);	
				if(xRow != null){
					Object name=xRow.getCell(labelCn);
					if(name != null && !"".equals(name.toString().trim())){
						verificationPosCell( writeWorkBook,  writeSheet,xRow ,headingMap,i,lastColumns,lastRows,portNameList);
						if(ImportCommonMethod.isExistFalse(writeWorkBook, writeSheet, lastColumns,lastRows) == 0){
							addPosModel(xRow , headingMap, i,prjcode);
						}
					}else{
						int labelCnInt = Integer.parseInt(headingMap.get(Constant.POS_LABELCN).toString());
						String constantValue=Constant.POS_LABELCN;
						ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, labelCnInt, i, lastColumns,constantValue);
					}
				}
			}
			int falseExitDB=ImportCommonMethod.isExistFalse(writeWorkBook, writeSheet, lastColumns,lastRows);
			importResultDO.setInfo("导入表格中共有:" + (lastRows-4) + "条数据");
			importResultDO.setSuccess(true);
			if ( falseExitDB>0) {				
				importResultDO.setSuccess(false);
				importResultDO.setInfo("错误数据:" + falseExitDB + "条");
			}else{
				if(null !=posList && !posList.isEmpty()){
					returnList = getImportBasicDataBO().isExistListName(posNameList,excelName);	
					
					Map map=ImportCommonMethod.changeListToMap(returnList);
					importResultDO.setInfo("成功导入了"+posList.size()+"条数据");
					ImportCommonMethod.ImportExcelToDB (map , posList, excelName, importResultDO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			importResultDO.setInfo("导入失败");
			LogHome.getLog().error(excelName+"导入失败", e);			
			throw new Exception(excelName+"导入失败"+e.getMessage());
		}
		return importResultDO;
	}

	
	/**
	 * POS单元格校验
	 */
	private  void verificationPosCell(Workbook writeWorkBook, Sheet writeSheet,Row xRow ,Map headingMap , int i ,int lastColumns ,int lastRows,List portNameList ) throws Exception{
		// 01.PBOSS系统分光器名称
		
		// 02.分光器名称
		int labelCn = Integer.parseInt(headingMap.get(Constant.POS_LABELCN).toString());
		// 03.分光比
		int ration = Integer.parseInt(headingMap.get(Constant.RATION).toString());
		// 04.新增——安装位置
		int relatedCabCuid = Integer.parseInt(headingMap.get(Constant.RELATEDCABCUID).toString());
		// 05.所属上联设备
		int relatedUpneCuid = Integer.parseInt(headingMap.get(Constant.RELATEDUNIONCUID).toString());
		// 06.上联设备主用端口
		int relatedUpnePortCuid = Integer.parseInt(headingMap.get("上联设备主用端口").toString());
		// 07.生产厂商
		int relatedVendorCuid = Integer.parseInt(headingMap.get(Constant.RELATEDVENDORCUID).toString());
		// 08.资产归属
		int ownership = Integer.parseInt(headingMap.get("资产归属").toString());
		// 09.资产归属人
		int ownershipMan = Integer.parseInt(headingMap.get("资产归属人").toString());
		// 10.生命周期状态
		int liveCycle = Integer.parseInt(headingMap.get(Constant.LIVECYCLE).toString());
		// 11.是否直接带用户
		int canAllocateToUser = Integer.parseInt(headingMap.get(Constant.CANALLOCATETOUSER).toString());
		// 12.入网时间
		int setUpTime = Integer.parseInt(headingMap.get(Constant.SETUPTIME).toString());
		// 13.接入类型
		int accessType = Integer.parseInt(headingMap.get(Constant.ACCESSTYPE).toString());
		// 14.维护人
		int preserver = Integer.parseInt(headingMap.get("维护班组").toString());
		// 15.维护方式
		int maintMode = Integer.parseInt(headingMap.get(Constant.MAINTMODE).toString());
		// 16.建设日期
		int creatTime = Integer.parseInt(headingMap.get(Constant.CREATTIME).toString());
		// 17.新增——SN码
		int seqNo = Integer.parseInt(headingMap.get(Constant.SEQNO).toString());
		// 18.新增——所属工程
		int relatedProjectCuid = Integer.parseInt(headingMap.get(Constant.RELATEDPROJECTCUID).toString());
		// 19.一线数据维护人
		int MAINT_PERSON = Integer.parseInt(headingMap.get("一线数据维护人").toString());
		// 20.移动公司数据质量责任人
		int DATA_QUALITY_PERSON = Integer.parseInt(headingMap.get("移动公司数据质量责任人").toString());
		// 21.上联设备备用端口
		int RELATED_PORT2_CUID = Integer.parseInt(headingMap.get("上联设备备用端口").toString());

		try {
			// 1."分光器名称"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, labelCn, i, lastColumns,Constant.POS_LABELCN);
			String posCuid = "";
			
			String posName = xRow.getCell(labelCn)==null?"":xRow.getCell(labelCn).toString();
			if(!ImportCommonMethod.isEmpty(posName)){
				Map temp = getImportAttributeQueryBO().getPosByName(posName);
				if(temp!=null){
					
					String type = ObjectUtils.toString(temp.get("TYPE"));
					if("0".equals(type)){
						ImportCommonMethod.printOnlyErrorInfo(writeWorkBook, writeSheet, i, labelCn, lastColumns, "pos名称已经归档！");
					}else{
						posCuid = ObjectUtils.toString(temp.get("CUID"));
					}
					
					}
					//ImportCommonMethod.VerificationCardNameRepeat(writeWorkBook, writeSheet, labelCn, i, lastColumns);
			}
			
			ImportCommonMethod.isExcelExist(writeWorkBook, writeSheet, labelCn, i, lastColumns,lastRows,nameMap);
			
			
			
			// 3."设备安装地址"校验
			// ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, location, i, lastColumns,Constant.LOCATION);
			/**
			 * 设备安装地址 是否在数据库中存在
			 */
			/*String locationStr  = xRow.getCell(location)==null?"":xRow.getCell(location).toString();
			if(locationStr!= null && locationStr.trim().length() > 0) {
				ImportCommonMethod.verificationAddressNew(writeWorkBook, writeSheet, location, i, lastColumns,Constant.LOCATION);
			}*/
			
			// 4."所属上联设备"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedUpneCuid, i, lastColumns,Constant.RELATEDUNIONCUID);
			// 校验所属上联设备，是否在数据库存在
			Map resMap = verificationRelated(writeWorkBook, writeSheet,  relatedUpneCuid, i, lastColumns,Constant.RELATEDUNIONCUID,xRow.getCell(relatedUpneCuid).toString());
			String upDevCuid = "";
			String upDevType = "";
			if(resMap!=null&&resMap.get("CUID")!=null){
				upDevCuid = ObjectUtils.toString(resMap.get("CUID"));
				upDevType = ObjectUtils.toString(resMap.get("UP_DEV_TYPE"));
			}
			// 5."上联设备主用端口"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedUpnePortCuid, i, lastColumns,"上联设备主用端口");
			if(xRow.getCell(relatedUpnePortCuid) != null && !StringUtils.isEmpty(xRow.getCell(relatedUpnePortCuid).toString())){
				//判断EXCEL中，上联设备主用端口是否有重复的名称
				String portName = (xRow.getCell(relatedUpnePortCuid).toString()+"-"+xRow.getCell(relatedUpnePortCuid));
				if(portNameList.contains(portName)){
					ImportCommonMethod.VerificationCardNameRepeat(writeWorkBook, writeSheet, relatedUpnePortCuid, i, lastColumns);
				}else{
					portNameList.add(portName);
					//判断EXCEL中，上联设备主用端口与所属上联设备是否存在关系
					String upDevPtpCuid = "";
					Map ptpMap = ImportCommonMethod.verificationRelatedPosPort(writeWorkBook, writeSheet,upDevCuid,relatedUpneCuid, relatedUpnePortCuid, i, lastColumns,"上联设备主用端口");
				    if(ptpMap!=null){
				    	String portSubType = ObjectUtils.toString(ptpMap.get("PORT_SUB_TYPE"));
				    	String portCuid = ObjectUtils.toString(ptpMap.get("CUID"));
				    	if("POS".equals(upDevType)&&"12".equals(portSubType)){
				    		ImportCommonMethod.printOnlyErrorInfo(writeWorkBook, writeSheet, i, relatedUpnePortCuid, lastColumns, "上联设备主用端口是分光器上联口，请重新填写所属上联设备的下联端口！");
				    	}else if("OLT".equals(upDevType)&&"11".equals(portSubType)){
				    		ImportCommonMethod.printOnlyErrorInfo(writeWorkBook, writeSheet, i, relatedUpnePortCuid, lastColumns, "上联设备主用端口是OLT上联口，请重新填写所属上联设备的下联端口！");
				    	}
				    	else{
				    		//验证上联设备和端口是否已经存在链路
				    		if(!ImportCommonMethod.isEmpty(portCuid)&&!ImportCommonMethod.isEmpty(upDevCuid)){
//				    			Map<String,String> param =new HashMap<String,String>();
//				    			param.put("DEST_NE_CUID",upDevCuid);
//				    			param.put("DEST_PTP_CUID",portCuid);
//				    			if(!ImportCommonMethod.isEmpty(posCuid)){
//				    				param.put("NOT_ORIG_NE_CUID",posCuid);
//				    			}
//				    			if(getPonTopoLinkManageBo().isPonTopoLinkExist(param)){
//				    				ImportCommonMethod.printOnlyErrorInfo(writeWorkBook, writeSheet, i, relatedUpnePortCuid, lastColumns, "所属上联设备+上联设备主用端口已存在PON拓扑链路信息，请重新填写！");
//				    			}
				    		}
				    	}
				    }
				}
				//校验，所属上联设备+上联设备主用端口
			}
			// 6."生产厂商"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedVendorCuid, i, lastColumns,Constant.RELATEDVENDORCUID);
			verificationRelated(writeWorkBook, writeSheet,  relatedVendorCuid, i, lastColumns,Constant.RELATEDVENDORCUID,xRow.getCell(relatedVendorCuid).toString());
			//校验生产厂商和设备型号直接的关系 2016年4月14日20:49:17
			/**
			 * 修改pos导入报错
			 */
			writeSheet.getRow(i).getCell(relatedVendorCuid).setCellType(Cell.CELL_TYPE_STRING);
			String producterStr=writeSheet.getRow(i).getCell(relatedVendorCuid).getStringCellValue();
			
			// 7."产权"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, ownership, i, lastColumns,"资产归属");
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, ownership, i, lastColumns,Constant.OWNERSHIP);
			// 8."产权人"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, ownershipMan, i, lastColumns,"资产归属人");
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, ownershipMan, i, lastColumns,Constant.OWNERSHIPMAN);
			// 9."SN码"的数据可空，不需要校验
			// 10"所属工程"的数据可空，不需要校验
			// 11."生命周期状态"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, liveCycle, i, lastColumns,Constant.LIVECYCLE);
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, liveCycle, i, lastColumns,Constant.LIVECYCLE);
			// 12."入网时间"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, setUpTime, i, lastColumns,Constant.SETUPTIME);
			ImportCommonMethod.verificationDate(writeWorkBook, writeSheet, setUpTime, i, lastColumns,Constant.SETUPTIME);
			// 13."分光比"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, ration, i, lastColumns,Constant.RATION);
			verificationRation(writeWorkBook, writeSheet, ration, i, lastColumns,Constant.RATION);
			// 14."接入类型"的数据可空，但需要校验枚举是否正确
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, accessType, i, lastColumns,Constant.ACCESSTYPE);
			if(xRow.getCell(accessType)!=null){
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, accessType, i, lastColumns,Constant.ACCESSTYPE);
			}
			// 15."安装位置"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedCabCuid, i, lastColumns,Constant.RELATEDCABCUID);
			verificationRelated(writeWorkBook, writeSheet,  relatedCabCuid, i, lastColumns, Constant.RELATEDCABCUID, xRow.getCell(relatedCabCuid).toString());
			// 16."维护人"的数据可空
			 ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, preserver, i, lastColumns,Constant.PRESERVER);
	
			// 17."维护方式"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, maintMode, i, lastColumns,Constant.MAINTMODE);
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, maintMode, i, lastColumns,Constant.MAINTMODE);	
			// 18."是否直接带用户"校验
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, canAllocateToUser, i, lastColumns,Constant.CANALLOCATETOUSER);
			String canAllocateToUserStr= xRow.getCell(canAllocateToUser).toString();
			if(canAllocateToUserStr.equals("是")){
			}else if(canAllocateToUserStr.equals("否")){
			}else{
				ImportCommonMethod.printOnlyErrorInfo(writeWorkBook, writeSheet, i, canAllocateToUser, lastColumns, "填写字段内容为是或否");
			}
			// 19."建设日期"的数据可空，但需要校验日起类型是否正确
			if (xRow.getCell(creatTime) !=null && !"".equals( xRow.getCell(creatTime).toString())) {
				ImportCommonMethod.verificationDate(writeWorkBook, writeSheet, creatTime, i, lastColumns,Constant.CREATTIME);
			}
			/**
			 * 20.一线数据维护人（代维/一线）
			 */
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, MAINT_PERSON, i, lastColumns,"一线数据维护人");
			/**
			 * 21.数据质量责任人（移动）
			 */
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, DATA_QUALITY_PERSON, i, lastColumns,"移动公司数据质量责任人");
			/**
			 * 22.上联设备备用端口
			 */

//			Object cellContant=xRow.getCell(relatedEmsCuid);
//			String emsName = "";
//			if(cellContant != null && !StringUtils.isEmpty(cellContant.toString())){
//				emsName = cellContant.toString();
//			}
//			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedEmsCuid, i, lastColumns,Constant.RELATEDEMSCUID);
//			verificationRelated(writeWorkBook, writeSheet,  relatedEmsCuid, i, lastColumns,Constant.RELATEDEMSCUID,emsName);
			
			//所属机房/资源点 可空
			//ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedSpaceCuid, i, lastColumns,Constant.ROOMACCESSPOINT);
			//如果机房/资源点不为空，判断是在数据库存在
			/*if(xRow.getCell(relatedSpaceCuid)!=null&& !StringUtils.isEmpty(xRow.getCell(relatedSpaceCuid).toString())){
				verificationRelated(writeWorkBook, writeSheet,  relatedSpaceCuid, i, lastColumns,Constant.ROOMACCESSPOINT,xRow.getCell(relatedSpaceCuid).toString());						
			}*/
//			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedOltPortCuid, i, lastColumns,Constant.RELATEDOLTPORTCUID);
//			if(xRow.getCell(relatedOltPortCuid) != null && !StringUtils.isEmpty(xRow.getCell(relatedOltPortCuid).toString())){
//				String portName = xRow.getCell(relatedOltPortCuid).toString();
//				if(portNameList.contains(portName)){
//					ImportCommonMethod.VerificationCardNameRepeat(writeWorkBook, writeSheet, relatedOltPortCuid, i, lastColumns);
//				}else{
//					portNameList.add(portName);
//				}
//			}
			//ImportCommonMethod.verificationAddress(writeWorkBook, writeSheet, location, i, lastColumns,Constant.LOCATION);

			
			// ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, model, i, lastColumns,Constant.MODEL);
			// verificationRelated(writeWorkBook, writeSheet,  model, i, lastColumns,Constant.MODEL,xRow.getCell(model).toString());
		
			// writeSheet.getRow(i).getCell(model).setCellType(Cell.CELL_TYPE_STRING);
			/*String modelStr=writeSheet.getRow(i).getCell(model).getStringCellValue();
			List list = getImportAttributeQueryBO().getProducterAndModelRelated(producterStr,modelStr,3);
			if(list==null||list.size()==0){
				ImportCommonMethod.updateExcelFormat(writeWorkBook,writeSheet,i, vendorCuid);
				Cell cell=xRow.getCell(lastColumns);
				if(cell==null){
					cell=xRow.createCell(lastColumns);
					cell.setCellValue("生产厂商和设备型号关系错误 \r\n");
				}else{
					String strValue=cell.getStringCellValue();
					strValue=strValue + "生产厂商和设备型号关系错误 \r\n";
					cell.setCellValue(strValue);				
				}
			}*/

			/*if (xRow.getCell(realLongitude)!=null && !"".equals(xRow.getCell(realLongitude).toString())) {
				ImportCommonMethod.verificationNumber(writeWorkBook, writeSheet, realLongitude, i, lastColumns,Constant.REALLATITUDE);
			}
			
			if (xRow.getCell(realLatitude) !=null && !"".equals( xRow.getCell(realLatitude).toString())) {
				ImportCommonMethod.verificationNumber(writeWorkBook, writeSheet, realLatitude, i, lastColumns,Constant.REALLATITUDE);
			}*/
			

			/*if(xRow.getCell(backNetWorkTime)!=null){
				ImportCommonMethod.verificationDate(writeWorkBook, writeSheet, backNetWorkTime, i, lastColumns,Constant.BACKNETWORKTIME);
			}*/
			
			// ImportCommonMethod.verificationDate(writeWorkBook, writeSheet, repairTime, i, lastColumns,Constant.REPAIRTIME);
		} catch (Exception e) {
			LogHome.getLog().error("接入网-POS设备校验出错", e);			
			throw new Exception("接入网-POS设备校验出错："+e.getMessage());
			
		}
		
	}

	
	/**
	 * 将Excel数据增加到POS模型中
	 */
	private void addPosModel(Row xRow ,Map headingMap,int i,String prjcode) throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 01.PBOSS系统分光器名称
		// 02.分光器名称
		int labelCn = Integer.parseInt(headingMap.get(Constant.POS_LABELCN).toString());
		// 03.分光比
		int ration = Integer.parseInt(headingMap.get(Constant.RATION).toString());
		// 04.新增——安装位置
		int relatedCabCuid = Integer.parseInt(headingMap.get(Constant.RELATEDCABCUID).toString());
		// 05.所属上联设备
		int relatedUpneCuid = Integer.parseInt(headingMap.get(Constant.RELATEDUNIONCUID).toString());
		// 06.上联设备主用端口
		int relatedUpnePortCuid = Integer.parseInt(headingMap.get("上联设备主用端口").toString());
		// 07.生产厂商
		int relatedvendorcuid = Integer.parseInt(headingMap.get(Constant.RELATEDVENDORCUID).toString());
		// 08.资产归属
		int ownership = Integer.parseInt(headingMap.get("资产归属").toString());
		// 09.资产归属人
		int ownershipMan = Integer.parseInt(headingMap.get("资产归属人").toString());
		// 10.生命周期状态
		int liveCycle = Integer.parseInt(headingMap.get(Constant.LIVECYCLE).toString());
		// 11.是否直接带用户
		int canAllocateToUser = Integer.parseInt(headingMap.get(Constant.CANALLOCATETOUSER).toString());
		// 12.入网时间
		int setUpTime = Integer.parseInt(headingMap.get(Constant.SETUPTIME).toString());
		// 13.接入类型
		int accessType = Integer.parseInt(headingMap.get(Constant.ACCESSTYPE).toString());
		// 14.维护人
		int preserver = Integer.parseInt(headingMap.get("维护班组").toString());
		// 15.维护方式
		int maintMode = Integer.parseInt(headingMap.get(Constant.MAINTMODE).toString());
		// 16.建设日期
		int creatTime = Integer.parseInt(headingMap.get(Constant.CREATTIME).toString());
		// 17.新增——SN码
		int seqNo = Integer.parseInt(headingMap.get(Constant.SEQNO).toString());
		// 18.新增——所属工程
		int relatedProjectCuid = Integer.parseInt(headingMap.get(Constant.RELATEDPROJECTCUID).toString());
		// 19.一线数据维护人
		int MAINT_PERSON = Integer.parseInt(headingMap.get("一线数据维护人").toString());
		// 20.移动公司数据质量责任人
		int DATA_QUALITY_PERSON = Integer.parseInt(headingMap.get("移动公司数据质量责任人").toString());
		// 21.上联设备备用端口
		int RELATED_PORT2_CUID = Integer.parseInt(headingMap.get("上联设备备用端口").toString());

		Map map = new HashMap();
		try {
			// 表格中要的主属性
//			if(xRow.getCell(relatedOltPortCuid) != null && !StringUtils.isEmpty(xRow.getCell(relatedOltPortCuid).toString())){
//				map.put("aPortName",xRow.getCell(relatedOltPortCuid).toString());
//				map.put("RELATED_OLT_PORT_CUID", xRow.getCell(relatedOltPortCuid).toString());
//			}
			// 1.分光器名称
			String posCuid = "";
			if(xRow.getCell(labelCn)!=null && !"".equals(xRow.getCell(labelCn).toString())){
				String strPosName=xRow.getCell(labelCn).toString();
				map.put("LABEL_CN", strPosName);
				posNameList.add(strPosName);
				nameMap.put(strPosName, i);
				if(!ImportCommonMethod.isEmpty(strPosName)){
					Map temp = getImportAttributeQueryBO().getPosByName(strPosName);
					if(temp!=null){
						posCuid = ObjectUtils.toString(temp.get("CUID"));
					}
				}
				
			}
			// 2.PBOSS系统分光器名称
			
			// 3.设备安装地址
			/*if(xRow.getCell(location)!=null && !"".equals(xRow.getCell(location).toString())){
				String locationStr = xRow.getCell(location).toString();
				List<Map> addressList = getAnOltManageBO().selectFullAddressByLabelCn(locationStr);
				if(addressList!=null&&addressList.size()>0){
					map.put(AnPos.AttrName.location,addressList.get(0).get("CUID"));
					String districtCuid = map.get(AnPos.AttrName.relatedDistrictCuid)+"";
					if(ImportCommonMethod.isEmpty(districtCuid)){
						map.put(AnPos.AttrName.relatedDistrictCuid,addressList.get(0).get("CITY"));
					}
				}
			}*/
			// 4.所属上联设备
			String neCuid="";
			if (xRow.getCell(relatedUpneCuid)!=null && !"".equals(xRow.getCell(relatedUpneCuid).toString())) {
				Map temp = getImportAttributeQueryBO().getPosUpDevByName(xRow.getCell(relatedUpneCuid).toString());
				if(temp!=null){
					neCuid = (String)temp.get("CUID");
					String upDevType = (String)temp.get("UP_DEV_TYPE");
					String LABEL_CN = (String)temp.get("LABEL_CN");
					map.put("UP_DEV_TYPE",upDevType);
					
					if(upDevType.toUpperCase().equals("POS")){
						map.put("RELATED_UPNE_CUID",neCuid);
						Map OLtMap = getImportAttributeQueryBO().getPosUpDevByName(LABEL_CN);
						String OltCuid = (String)OLtMap.get("RELATED_OLT_CUID");
						String OltPortCuid = (String)OLtMap.get("RELATED_PORT_CUID");
						map.put("RELATED_OLT_CUID",OltCuid);
						map.put("RELATED_PORT_CUID",OltPortCuid);
						
					}
					else if(upDevType.toUpperCase().equals("OLT")){
						map.put("RELATED_OLT_CUID",neCuid);
						map.put("RELATED_UPNE_CUID",neCuid);
						};
				
					
					
					// 5.上联设备主用端口
					if(xRow.getCell(relatedUpnePortCuid)!=null){
						//根据设备CUID，端口名称，查找端口CUID
						Map ptpMap = getImportAttributeQueryBO().getPtpByNameAndNeCuid(xRow.getCell(relatedUpnePortCuid).toString(),neCuid);
						if(ptpMap!=null){
							if(upDevType.toUpperCase().equals("OLT")){
							map.put("aPortName",xRow.getCell(relatedUpnePortCuid).toString());
							String RELATED_UPNE_PORT_CUID= (String)ptpMap.get("CUID");
							map.put("RELATED_UPNE_PORT_CUID",RELATED_UPNE_PORT_CUID);
							map.put("RELATED_PORT_CUID", RELATED_UPNE_PORT_CUID);
							}else if (upDevType.toUpperCase().equals("POS")){
								String RELATED_UPNE_PORT_CUID= (String)ptpMap.get("CUID");
								map.put("RELATED_UPNE_PORT_CUID",RELATED_UPNE_PORT_CUID);
								neCuid = (String)temp.get("CUID");
							Map	relatedportmap = getImportAttributeQueryBO().getPosByCuid(neCuid);
							String RELATED_PORT_CUID = (String)relatedportmap.get("RELATED_PORT_CUID");
								 map.put("RELATED_PORT_CUID", RELATED_PORT_CUID);
							}
						}
					}
				}
			}

			// 6.生产厂商
			if (xRow.getCell(relatedvendorcuid)!=null && !StringUtils.isEmpty(xRow.getCell(relatedvendorcuid).toString())) {
				map.put("RELATED_VENDOR_CUID", getImportAttributeQueryBO().getPropertyCuidByName(xRow.getCell(relatedvendorcuid).toString(),"DEVICE_VENDOR"));
			}
			// 7.产权
			if(xRow.getCell(ownership)!=null && !"".equals(xRow.getCell(ownership).toString())){
				String constantValue=Constant.OWNERSHIP;
				if(ImportCommonMethod.isEnum(constantValue,xRow.getCell(ownership).toString())){
					//枚举类型可能被人改过目前临时解决
					String ovnershipStr = xRow.getCell(ownership).toString();
					map.put("OWNERSHIP",AnmsConst.OWNERSHIP.getValue(ovnershipStr));
				}//else填充自动默认值
			}
			// 8.产权人
			if(xRow.getCell(ownershipMan)!=null && !"".equals(xRow.getCell(ownershipMan).toString())){
				if(ImportCommonMethod.isEnum(Constant.OWNERSHIPMAN,xRow.getCell(ownershipMan).toString())){
					map.put("OWNERSHIP_MAN",(java.lang.Long) AnmsConst.OWNERSHIP_MAN.getValue(xRow.getCell(ownershipMan).toString()));
				}
			}
			// 9.SN码
			if(xRow.getCell(seqNo)!=null && !StringUtils.isEmpty(xRow.getCell(seqNo).toString())){
				map.put("SEQNO", ImportCommonMethod.getCellValue(xRow.getCell(seqNo)));
			}
			// 10.所属工程
				map.put("RELATED_PROJECT_CUID", prjcode);
			
			// 11.生命周期状态
			if(xRow.getCell(liveCycle)!=null && !"".equals(xRow.getCell(liveCycle).toString())){
				if(ImportCommonMethod.isEnum(Constant.LIVECYCLE,xRow.getCell(liveCycle).toString())){
					map.put("LIVE_CYCLE", (java.lang.Long) ElementEnum.LIVE_CYCLE.getValue(xRow.getCell(liveCycle).toString()));
				}	
			}
			// 12.入网时间
			if (xRow.getCell(setUpTime) !=null && !"".equals(xRow.getCell(setUpTime).toString())) {
				int creatType=xRow.getCell(setUpTime).getCellType();						
				if(creatType==Cell.CELL_TYPE_NUMERIC){
					map.put("SETUP_TIME", getImportBasicDataBO().formtTime(formatter.format(xRow.getCell(setUpTime).getDateCellValue())));
				}else if(creatType==Cell.CELL_TYPE_STRING){
					map.put("SETUP_TIME", ImportCommonMethod.getTimestame(xRow.getCell(setUpTime).toString()+" 00:00:00"));
				}	
			}
			// 13.分光比
			if(xRow.getCell(ration)!=null && !"".equals(xRow.getCell(ration).toString())){
				map.put("RATION",xRow.getCell(ration).toString());
			}
			// 14.接入类型
			if(xRow.getCell(accessType) !=null && !StringUtils.isEmpty(xRow.getCell(accessType).toString())){
				String accessTyepStr = xRow.getCell(accessType).toString();
				//接入类型枚举临时解决办法
				int accessTyepInt = 4;
				if(accessTyepStr.equals("集客")){
					accessTyepInt = 1;
				}else if(accessTyepStr.equals("家客")){
					accessTyepInt = 2;
				}else if(accessTyepStr.equals("集客/家客共用")){
					accessTyepInt = 3;
				}
				map.put("ACCESS_TYPE",new Long(accessTyepInt));
			}
			// 15.新增——安装位置
			if(xRow.getCell(relatedCabCuid)!=null && !"".equals(xRow.getCell(relatedCabCuid).toString())){
				String cabName=xRow.getCell(relatedCabCuid).toString();
				String accessPointName=xRow.getCell(relatedCabCuid).toString();
				String accessPointCuid = getImportAttributeQueryBO().getPropertyCuidByName(accessPointName, "CAB");
				map.put("RELATED_CAB_CUID",accessPointCuid);
				if(accessPointCuid!=null &&!StringUtils.isEmpty(accessPointCuid)){
					String relatedDistrictCuid = getImportAttributeQueryBO().getRelatedDistrictCuidByCabCuid(accessPointCuid);
					map.put("RELATED_DISTRICT_CUID",relatedDistrictCuid);
				}
				
			}
			// 16.维护人
			
			if(xRow.getCell(preserver)!=null && !"".equals(xRow.getCell(preserver).toString())){
			
				map.put("PRESERVER", xRow.getCell(preserver).toString());
				
			}
			// 17.维护方式
			if(headingMap.get(Constant.MAINTMODE) != null){
				int maintmode = Integer.parseInt(headingMap.get(Constant.MAINTMODE).toString());
				if(xRow.getCell(maintmode) != null && !StringUtils.isEmpty(xRow.getCell(maintmode).toString())){
					map.put("MAINT_MODE",(java.lang.Long) ElementEnum.ELEMENT_MAINT_MODE.getValue( xRow.getCell(maintmode).toString()));
				}
			}
			// 18.是否直接带用户
			if(xRow.getCell(canAllocateToUser) != null && !StringUtils.isEmpty(xRow.getCell(canAllocateToUser).toString())){
				String canAllocateToUserStr = xRow.getCell(canAllocateToUser).toString();
				if(canAllocateToUserStr.equals("是")){
					map.put("CAN_ALLOCATE_TO_USER", 1);
				}else{
					map.put("CAN_ALLOCATE_TO_USER", 0);
				}
			}
			// 19.建设日期
			if (xRow.getCell(creatTime) !=null && !"".equals(xRow.getCell(creatTime).toString())) {
				int creatType=xRow.getCell(creatTime).getCellType();						
				if(creatType==Cell.CELL_TYPE_NUMERIC){
					map.put("CREATTIME", getImportBasicDataBO().formtTime(formatter.format( xRow.getCell(creatTime).getDateCellValue())));
				}else if(creatType==Cell.CELL_TYPE_STRING){
					map.put("CREATTIME", ImportCommonMethod.getTimestame(xRow.getCell(creatTime).toString()));
				};
			}
			//20.一线数据维护人
			if(xRow.getCell(MAINT_PERSON) !=null && !"".equals(xRow.getCell(MAINT_PERSON).toString())){
				map.put("MAINT_PERSON",xRow.getCell(MAINT_PERSON).toString());
			}
			//21.移动公司数据质量责任人
			if(xRow.getCell(DATA_QUALITY_PERSON) !=null && !"".equals(xRow.getCell(DATA_QUALITY_PERSON).toString())){
				map.put("DATA_QUALITY_PERSON",xRow.getCell(DATA_QUALITY_PERSON).toString());
				
			}
			//21.上联设备备用端口
			if(xRow.getCell(RELATED_PORT2_CUID) !=null && !"".equals(xRow.getCell(RELATED_PORT2_CUID).toString())){
				Map ptpMap = getImportAttributeQueryBO().getPtpByNameAndNeCuid(xRow.getCell(RELATED_PORT2_CUID).toString(),neCuid);
				if(ptpMap!=null){
					String CUID= (String)ptpMap.get("CUID");
					map.put("RELATED_PORT2_CUID",CUID);
				}
			}
			
			
			//所属机房/资源点
			/*if(xRow.getCell(relatedSpaceCuid)!=null && !StringUtils.isEmpty(xRow.getCell(relatedSpaceCuid).toString())){
				Map temp = getImportAttributeQueryBO().getRoomAccessPointByName(xRow.getCell(relatedSpaceCuid).toString());
				if(temp!=null&&temp.size()>0){
					map.put("RELATED_SPACE_CUID",temp.get("CUID"));
					getAnOltManageBO().setRelatedMapValuesNew(map, AnPos.CLASS_NAME);
				}
			}*/	
			/*if (xRow.getCell(relatedEmsCuid)!=null && !StringUtils.isEmpty(xRow.getCell(relatedEmsCuid).toString())) {
				map.put(AnPos.AttrName.relatedEmsCuid, getImportAttributeQueryBO().getPropertyCuidByName(xRow.getCell(relatedEmsCuid).toString(),NmsSystem.CLASS_NAME));
				String emsName = xRow.getCell(relatedEmsCuid).toString();
				map.put("emsName",emsName);
				Map emsMap = ImportCache.getInstance().getEmsMap(emsName);
				String relatedDistrictCuid = (String)map.get(AnPos.AttrName.relatedDistrictCuid);
				boolean isDistrictEmpty = StringUtils.isEmpty(relatedDistrictCuid);
				if (emsMap != null && !emsMap.isEmpty()) {
					map.put(AnPos.AttrName.relatedEmsCuid, emsMap.get("CUID"));
					if(isDistrictEmpty){
						map.put(AnPos.AttrName.relatedDistrictCuid, emsMap.get("RELATED_SPACE_CUID"));
					}
				}
				else{
					if(isDistrictEmpty){
						map.put(AnPos.AttrName.relatedDistrictCuid, SysProperty.getInstance().getValue("district"));
					}
				}
			}*/
			/*if(xRow.getCell(model)!=null && !"".equals(xRow.getCell(model).toString())){
				map.put(AnPos.AttrName.model, neModelCuidNameMap.get(xRow.getCell(model).toString()));
			}*/
			/*
			if(xRow.getCell(realLongitude) !=null && !"".equals(xRow.getCell(realLongitude).toString()) ){
				Float fltRealLongitude=Float.parseFloat(xRow.getCell(realLongitude).toString());
				map.put(AnPos.AttrName.realLongitude,fltRealLongitude);	
			}					
			if(xRow.getCell(realLatitude) !=null && !"".equals(xRow.getCell(realLatitude).toString())){
				Float fltRealLatitude=Float.parseFloat(xRow.getCell(realLatitude).toString());
				map.put(AnPos.AttrName.realLatitude,fltRealLatitude);
			}*/
			/*if (xRow.getCell(backNetWorkTime) !=null && !"".equals(xRow.getCell(backNetWorkTime).toString())) {
				int creatType=xRow.getCell(backNetWorkTime).getCellType();						
				if(creatType==Cell.CELL_TYPE_NUMERIC){
					map.put(AnPos.AttrName.backNetworkTime, getImportBasicDataBO().formtTime(formatter.format( xRow.getCell(backNetWorkTime).getDateCellValue())));
				}else if(creatType==Cell.CELL_TYPE_STRING){
					map.put(AnPos.AttrName.backNetworkTime, ImportCommonMethod.getTimestame(xRow.getCell(backNetWorkTime).toString()));
				}	
			}*/			
			/*if ( xRow.getCell(repairTime) !=null && !"".equals(xRow.getCell(repairTime).toString())) {
				int creatType=xRow.getCell(repairTime).getCellType();						
				if(creatType==Cell.CELL_TYPE_NUMERIC){
					map.put(AnPos.AttrName.repairTime, getImportBasicDataBO().formtTime(formatter.format( xRow.getCell(repairTime).getDateCellValue())));
				}else if(creatType==Cell.CELL_TYPE_STRING){
					map.put(AnPos.AttrName.repairTime, ImportCommonMethod.getTimestame(xRow.getCell(repairTime).toString()));
				};
			}*/
			
			//String cuid = AnmsUtilBO.getCuidByClassName(TransElement.CLASS_NAME);
			String cuid="";
			if(!ImportCommonMethod.isEmpty(posCuid)){
				cuid=posCuid;
				map.put("CUID", cuid);
				String devCuid="AN_POS-"+cuid;
				map.put("DEV_CUID", devCuid);
			}
			
			map.put("GT_VERSION", 0);
			map.put("ISDELETE", 0);
			map.put("IS_PERMIT_SYS_DEL", 0);
			map.put("CREATE_TIME", new Timestamp(System.currentTimeMillis()));
			map.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
			map.put("USE_STATE", 1);
			map.put("IS_CLOSENET", 0);
			
		/*
		 * 修正FDN 拼接不正确问题
		 * by lichao
		 */
			
			String	posFdn = "";
			String posOLtCuid="" ;

			if(map.get("UP_DEV_TYPE").equals("POS")){
				posOLtCuid =  ObjectUtils.toString(map.get("RELATED_UPNE_CUID"));		 	
				Map temp1=getImportAttributeQueryBO().getPosByLabelCuid(posOLtCuid) ; 
				posFdn = temp1.get("FDN") + ":POS=" +  ObjectUtils.toString(map.get("LABEL_CN"));
			}
			else if(map.get("UP_DEV_TYPE").equals("OLT")){
	        		posOLtCuid =  ObjectUtils.toString(map.get("RELATED_OLT_CUID"));		 	
					Map temp1=getImportAttributeQueryBO().getOltByLabelCuid(posOLtCuid) ; 
					posFdn = temp1.get("FDN") + ":POS=" +  ObjectUtils.toString(map.get("LABEL_CN"));
	           }
				map.put("FDN", posFdn);
				
			posList.add(map);
		} catch (Exception e) {
			LogHome.getLog().error("接入网-POS设备校验装入Map出错", e);			
		}
		
	}
	/**
	 * 判断pos的Excel中第o行是否存在已有字段，这些字段是必不可少的字段
	 */
	private static Map getPosHeadingMap(Sheet sheet, int headingRow, List errorlist,int lastColumns) {
		Map headingMap = new HashMap();
		Row xRow = sheet.getRow(0);
		for(int i=1;i<lastColumns;i++){
			Object columnName = xRow.getCell(i);
			if(columnName != null){
				String columnString = columnName.toString();
	    	    if(!columnString.equals(posExcelColumns[i])){
	    	    	errorlist.add("导入的EXCEL文件不符合规范,请使用模板文件");
	    		  break;
	    	    }
				headingMap.put(columnName.toString(),i);
			}else{
				errorlist.add("该模板的第"+(i+1)+"列头名称不能为空!");
			}
		}
		return headingMap;
	}
	
	private  Map verificationRelated(Workbook writeWorkBook, Sheet writeSheet, int j, int i, int lastColumns,String constantValue ,String propertyName)throws Exception {
		boolean flag = false;	
		Map res = null;
		try {
			Row xRow=writeSheet.getRow(i);
			Object cellContant=xRow.getCell(j);
			if(cellContant!=null && !StringUtils.isEmpty(cellContant.toString())){
				//表格中有EMS 机房 厂家根据EMS获得
				if (constantValue.equals(Constant.RELATEDROOMCUID)) {
					if(!StringUtils.isEmpty(getImportAttributeQueryBO().getPropertyCuidByName(propertyName,"ROOM"))){
						flag = true;
					}
				} else if (constantValue.equals(Constant.ROOMACCESSPOINT)) {	
					Map temp = getImportAttributeQueryBO().getRoomAccessPointByName(propertyName);
					if(temp!=null&&temp.size()>0){
						flag = true;
					}
				} else if (constantValue.equals(Constant.RELATEDUNIONCUID)) {	
					Map temp = getImportAttributeQueryBO().getPosUpDevByName(propertyName);
					if(temp!=null&&temp.size()>0){
						res = temp;
						flag = true;
					}
				} else if (constantValue.equals(Constant.RELATEDEMSCUID)) {	
					if(!StringUtils.isEmpty(getImportAttributeQueryBO().getPropertyCuidByName(propertyName,"NMS_SYSTEM"))){
						flag = true;
					}
				} else if (constantValue.equals(Constant.RELATEDVENDORCUID)) {
					if(!StringUtils.isEmpty(getImportAttributeQueryBO().getPropertyCuidByName(propertyName,"DEVICE_VENDOR"))){
						flag = true;
					}
				} else if (constantValue.equals(Constant.MODEL)) {
					flag = neModelCuidNameMap.containsKey(cellContant.toString());
				} else if (constantValue.equals(Constant.RELATEDCABCUID)) {
					if(!StringUtils.isEmpty(getImportAttributeQueryBO().getPropertyCuidByName(propertyName, "CAB"))){
						flag = true;
					}
				} //else 继续执行
				if (!flag) {
					ImportCommonMethod.updateExcelFormat(writeWorkBook,writeSheet,i, j);				
					
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell=xRow.getCell(lastColumns);
					if(xCell==null){
						xCell=xRow.createCell(lastColumns);
						//设置最后一列记录异常信息单元格的列宽度
//						writeSheet.setColumnWidth(lastColumns, writeSheet.getColumnWidth(lastColumns)*3);
						xCell.setCellValue(cellContant.toString()+ " 数据库中不存在 \r\n");
					}else{
						String strValue=xCell.getStringCellValue();
						strValue=strValue+cellContant.toString()+" 数据库中不存在 \r\n";
						xCell.setCellValue(strValue);				
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception("");
		}
		return res;
	}
	
	/**
	 * 验证分光比是否存在
	 */
	public static  void verificationRation(Workbook writeWorkBook, Sheet writeSheet, int j, int i, int lastColumns,String constantValue) throws Exception{
		Row xRow=writeSheet.getRow(i);
		Object cellContant=xRow.getCell(j);
		Map rationMap=new HashMap();
		rationMap.put("1:2", null);
		rationMap.put("1:4", null);
		rationMap.put("1:8", null);
		rationMap.put("1:16", null);
		rationMap.put("1:32", null);
		rationMap.put("1:64", null);
		rationMap.put("1:128", null);		
		if(cellContant!=null && !"".equals(cellContant.toString())){
			if(!rationMap.containsKey(cellContant.toString())){
				try {
					ImportCommonMethod.updateExcelFormat(writeWorkBook,writeSheet,i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell=xRow.getCell(lastColumns);
					if(xCell==null){
						xCell=xRow.createCell(lastColumns);
						xCell.setCellValue( "["+cellContant.toString()+"]枚举值不存在 \r\n");
					}else{
						String strValue=xCell.getStringCellValue();
						strValue=strValue+"["+cellContant.toString()+"]枚举值不存在 \r\n";
						xCell.setCellValue(strValue);				
					}					
				} catch (Exception e) {
					LogHome.getLog().error("分光比的枚举值不存在！", e);
					throw new Exception("分光比的枚举值不存在！");
				}	
			}
		}
	}
	
	/**
	 * 返回操作导入相应的BO
	 */
	private static ImportBasicDataBO getImportBasicDataBO() {
		return (ImportBasicDataBO) SpringContextUtil.getBean("importBasicDataBO");
	}
	
	private ImportAttributeQueryBO getImportAttributeQueryBO() {
		return (ImportAttributeQueryBO) SpringContextUtil.getBean("importAttributeQueryBO");
	}
	

	
	

}
