package com.boco.workflow.webservice.upload.service;

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
import com.boco.workflow.webservice.space.bo.CoverBO;
import com.boco.workflow.webservice.upload.bo.ImportBasicDataBO;
import com.boco.workflow.webservice.upload.servlet.ImportResultDO;
/**
 * POS端口整改导入处理类
 */
public class GponCoverImportExcel {
	private static String[] dataColumns = new String[] {"CUID","RELATED_NE_CUID","STANDARD_ADDR","COVER_INFO","REMARK","REGIONTYPE1","REGIONTYPE2","BUSINESS_TYPE"};
	private static String[] excelColumns = new String[] {"属性中文名称","关联设备","关联标准地址","覆盖范围描述","备注","覆盖场景","聚类场景","业务类型"};
	private String dataErrorMsg = "数据库中不存在，请检查！"; 
	private String dataEmptyMsg = "信息为空，请检查！";
	private static final String GPON_COVER="GPON_COVER";
	
	private Map<String,String> regionType1Enum = new HashMap<String, String>(){
		{
			put("家庭场景","1");put("校园场景","2");put("聚类场景","3");
		}
	};
	private Map<String,String> businesstypes = new HashMap<String, String>(){
		{
			put("语音","1");put("宽带","2");put("IPTV","3");put("一网通","4");
		}
	};
	private Map<String,String> regionType2Enum = new HashMap<String, String>(){
		{
			put("集体宿舍楼","31");put("党政军","32");put("沿街商铺","33");
			put("住宅区商铺","34");put("商业楼宇","35");
			put("工业园区","36");
		}
	};
	
	
	/**
	 * 返回操作导入相应的BO
	 */
	private static CoverBO getGponCoverManageBO() {
		return (CoverBO)SpringContextUtil.getBean("coverBO");
	}
	
	public  ImportResultDO importData(Workbook writeWorkBook ,Sheet writeSheet,
			String pathname, String excelName,String prjcode) throws Exception {;
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>(); 
		ImportResultDO importResultDO = new ImportResultDO(excelName);
		try {
			// 取得sheet
			int lastRows = writeSheet.getLastRowNum();
			Row xrow=writeSheet.getRow(0);//获取第一行求的列数
			int lastColumns =xrow.getLastCellNum();
			List<String> errorlist = new ArrayList<String>();
			// 取得每一列对应的列号，然后进行校验判断。
			getHeadingMap(writeSheet, errorlist,lastColumns);	
			if(!errorlist.isEmpty()){
				errorlist.add("请使用正确模板导入数据,可点击模板下载按钮进行下载。");
				importResultDO.setInfo(errorlist);
				importResultDO.setSuccess(false);
				importResultDO.setShowFileUrl(false);
				return importResultDO;
			}
			int erroNum = 0;
			int dataRowNum = 4;
			for (int i = dataRowNum; i <= lastRows; i++) {
				Row xRow = writeSheet.getRow(i);
				if(xRow!=null){
					Map<String,Object> dataMap = verificationCell(writeWorkBook, writeSheet, xRow, i,lastColumns);	
					if(!ImportCommonMethod.isRowExistError(xRow,lastColumns)
							&&dataMap.get("TYPE")!=null&&dataMap.get("CUID")!=null){
						dataMap.put("RELATED_PROJECT_CUID", prjcode);
						dataList.add(dataMap);
					}else{
						erroNum++;
					}
				}
			}
			importResultDO.setInfo("导入表格中共有:"+(lastRows-dataRowNum+1)+"条数据");
			if(erroNum>0){
				importResultDO.setSuccess(false);
				importResultDO.setInfo("有"+erroNum+"条错误数据,数据未进行导入。");
			}else{
				if(dataList!=null&&dataList.size()>0){
					ImportCommonMethod.importGponCoverToDB(dataList,"覆盖范围",importResultDO);
					importResultDO.setSuccess(true);
				}else{
					importResultDO.setSuccess(true);
					importResultDO.setInfo("有0条数据,数据未进行导入。");
				}
			}
			return importResultDO;
		} catch (Exception e) {
			LogHome.getLog().error("覆盖范围导入失败", e);	
			e.printStackTrace();
			throw new Exception("导入失败请与管理员联系!"+ e.getMessage());
		}
	}
	public  Map<String,Object> verificationCell(Workbook writeWorkBook, Sheet writeSheet,Row xRow, int i ,int lastColumns)
			throws Exception{
		LogHome.getLog().info("覆盖范围校验start====");
		Map<String,Object> dataMap = new HashMap<String,Object>();
		try{
			//关联设备
			int c = 1;
			String neName =  ImportCommonMethod.getCellValue(xRow.getCell(c));
			String neCuid = "";
			if(!ImportCommonMethod.isEmpty(neName)){
				Map<String,Object> tempObj = getGponCoverManageBO().selectRelatedNeCuidByLabeCn(neName);
				if(tempObj!=null){
					neCuid = ObjectUtils.toString(tempObj.get("CUID"));
					dataMap.put(dataColumns[1],neCuid);
					dataMap.put("DEVICE_NAME", neName);
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataErrorMsg);
				}
			}else{
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
			}
			c++;
			//关联标准地址
			String fullAddressName = ImportCommonMethod.getCellValue(xRow.getCell(c));
			String fullAddressCuid  = "";
			if(!ImportCommonMethod.isEmpty(fullAddressName)){
		    	Map<String,Object> tempObj = getGponCoverManageBO().selectFullAddressByLabeCn(fullAddressName);
				if(tempObj!=null){
					fullAddressCuid = tempObj.get("CUID").toString();
					dataMap.put(dataColumns[c],fullAddressCuid);
				    if(!ImportCommonMethod.isEmpty(neCuid)){
				    	Map<String, Object> resultMap = getGponCoverManageBO().selectGponCoverByNeAndAddress(neCuid,fullAddressCuid);
				    	if(resultMap!=null&&resultMap.size()>0){
				    		
				    		if("0".equals(ObjectUtils.toString(resultMap.get("CUID")))){
				    			ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,"覆盖范围已归档！");
				    		}else{
					    		dataMap.put("TYPE","UPDATE");
					    		dataMap.put(dataColumns[0],ObjectUtils.toString(resultMap.get("CUID")));
				    		}
				    	}else{
				    		dataMap.put(dataColumns[0],CUIDHexGenerator.getInstance().generate(GPON_COVER));
				    		dataMap.put("TYPE","INSERT");
				    	}
				    }
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataErrorMsg);
				}
			}else{
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
			}
			c++;
			//覆盖范围描述
			String coverInfo = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(coverInfo)){
				dataMap.put(dataColumns[c],coverInfo);  
			}
			c++;
			//备注
			String remark = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(remark)){
				dataMap.put(dataColumns[c],remark);  
			}
			c++;
			//覆盖场景
			String regiontype1 = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(regiontype1)){
				dataMap.put(dataColumns[c],regionType1Enum.get(regiontype1)); 
			}else{
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
			}
			c++;
			//聚类场景
			String regiontype2 = ImportCommonMethod.getCellValue(xRow.getCell(c));
			String type1=regionType1Enum.get(regiontype1);
			if(!ImportCommonMethod.isEmpty(regiontype1) && type1.trim().equals("3")){
				if(!ImportCommonMethod.isEmpty(regiontype2)){
					dataMap.put(dataColumns[c],regionType2Enum.get(regiontype2));
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
				}
			}else{
				dataMap.put(dataColumns[c],"");
			}
			//业务类型
			c++;
			String businesstype = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(businesstype)  ){
				dataMap.put(dataColumns[c],businesstypes.get(businesstype));
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
			}
			LogHome.getLog().info("覆盖范围校验end====");
		} catch (Exception e) {
			LogHome.getLog().error("覆盖范围出错", e);		
			throw new Exception("覆盖范围出错："+e.getMessage());
		}
		return dataMap;
	}
	/**
	 * 检查模板里的列名称
	 * @param sheet
	 * @param headingRow
	 * @param errorlist
	 * @param lastColumns
	 * @return
	 */
	private static Map getHeadingMap(Sheet sheet, List errorlist,int lastColumns) {
		Map headingMap = new HashMap();
		Row xRow = sheet.getRow(0);
		for(int i=0;i<lastColumns;i++){
			Object columnName = xRow.getCell(i);
			if(columnName==null||!columnName.toString().equals(excelColumns[i])){
				errorlist.add("该模板的第"+(i+1)+"列名称为空或跟模板EXCEL中对应列名称不一致!");
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
}
