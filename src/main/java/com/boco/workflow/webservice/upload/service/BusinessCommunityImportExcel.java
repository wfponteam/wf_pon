package com.boco.workflow.webservice.upload.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.space.bo.VillageBO;
import com.boco.workflow.webservice.upload.bo.ImportBasicDataBO;
import com.boco.workflow.webservice.upload.servlet.ImportResultDO;

/**
 * POS端口整改导入处理类
 */
public class BusinessCommunityImportExcel {
	private static String[] dataColumns = new String[] {"CUID","LABEL_CN","CITY","ADDRESS","BUILDINGS_NUM","UNITS_NUM","HOUSEHOLDS_NUM","MAINTAIN_PERSON","MAINTAIN_DEPT","WARNING_VALUE","CONTACT_NUMBER","CONSTRUCT_DEPT","IS_PRIORITY","IS_LIGHT_CHANGED","IS_OVERLAPP","BUILD_TYPE","EAST_LONGITUDE","EAST_LATITUDE","WEST_LONGITUDE","WEST_LATITUDE","SOUTH_LONGITUDE","SOUTH_LATITUDE","NORTH_LONGITUDE","NORTH_LATITUDE"};
	private static String[] excelColumns = new String[] {"属性中文名称","名称","地市","地址","楼宇数","总单元数","总户数","维护责任人","维护单位","预警阈值(%)","联系电话","施工单位","是否重点小区","是否光改小区","是否重叠小区","小区类型","东向经度","东向纬度","西向经度","西向纬度","南向经度","南向纬度","北向经度","北向纬度"};
	private String dataErrorMsg = "数据库中不存在，请检查！"; 
	private String dataEmptyMsg = "信息为空，请检查！";
	private String dataEnumMsg = "枚举值不存在，请检查！";
	private String dataIntMsg = "不是正整数，请检查！";
	private String dataNumberMsg = "不是有效数字，请检查！";
	private String yjfz = "预警阈值的范围是0~100";
	private static final String BUSINESS_COMMUNITY="BUSINESS_COMMUNITY";

	private Map<String,String> buildTypeEnum = new HashMap<String, String>(){
		{
			put("自建","1");put("光割","2");put("光改","3");
			put("第三方","4");put("无线","5");put("商铺","6");put("一网通","7");
		}
	};
	/**
	 * 返回操作导入相应的BO
	 */
	private static VillageBO getBusinessCommunityManageBO() {
		return (VillageBO) SpringContextUtil.getBean("villageBO");
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
					ImportCommonMethod.importBusinessCommunityToDB(dataList,"业务区",importResultDO);
					importResultDO.setSuccess(true);
				}else{
					importResultDO.setSuccess(false);
					importResultDO.setInfo("有0条数据,数据未进行导入。");
				}
			}
			return importResultDO;
		} catch (Exception e) {
			LogHome.getLog().error("业务区导入失败", e);	
			e.printStackTrace();
			throw new Exception("导入失败请与管理员联系!"+ e.getMessage());
		}
	}
	public  Map<String,Object> verificationCell(Workbook writeWorkBook, Sheet writeSheet,Row xRow, int i ,int lastColumns)
			throws Exception{
		LogHome.getLog().info("业务区校验start====");
		Map<String,Object> dataMap = new HashMap<String,Object>();
		try{
			//名称
			int c = 1;
			String label_cn =  ImportCommonMethod.getCellValue(xRow.getCell(c));
			String neCuid = "";
			
			c++;
			//地市
			String city = ImportCommonMethod.getCellValue(xRow.getCell(c));
			String cityCuid  = "";
			if(!ImportCommonMethod.isEmpty(city)){
		    	Map<String,Object> tempObj = getBusinessCommunityManageBO().selectDistrictByLabeCn(city);
				if(tempObj!=null&&tempObj.size()>0){
					cityCuid = ObjectUtils.toString(tempObj.get("CUID"));
					dataMap.put(dataColumns[c],cityCuid);
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataErrorMsg);
				}
			}else{
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
			}
			//修改业务去名称校验
			if(!ImportCommonMethod.isEmpty(label_cn)){
				dataMap.put("LABEL_CN", label_cn);
				Map<String,Object> tempObj = getBusinessCommunityManageBO().queryBCInfoByLabelCn(dataMap);
				if(tempObj!=null&&tempObj.size()>0){
					neCuid = ObjectUtils.toString(tempObj.get("CUID"));
					dataMap.put(dataColumns[0],neCuid);
					dataMap.put("TYPE","UPDATE");
				}else{
		    		dataMap.put(dataColumns[0],CUIDHexGenerator.getInstance().generate(BUSINESS_COMMUNITY));
		    		dataMap.put("TYPE","INSERT");
		    	}
			}else{
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c-1,lastColumns,dataEmptyMsg);
			}
			//地址
			c++;
			String address = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(address)){
				dataMap.put(dataColumns[c],address);  
			}else{
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
			}
			//楼宇数
			c++;
			String buildings_num = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(buildings_num)){
				if(ImportCommonMethod.isIntegerNumber(buildings_num)==null){
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataIntMsg);
				}else{
					dataMap.put(dataColumns[c],buildings_num);  
				}
				
			}
			//总单元数
			c++;
			String units_num = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(units_num)){
				if(ImportCommonMethod.isIntegerNumber(units_num)==null){
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataIntMsg);
				}
				else{
					dataMap.put(dataColumns[c],units_num);  
				}
				
			}
			//总户数
			c++;
			String households_num = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(households_num)){
				if(ImportCommonMethod.isIntegerNumber(households_num)==null){
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataIntMsg);
				}else{
					dataMap.put(dataColumns[c],households_num);
				}
			}
			//维护责任人
			c++;
			String maintain_person = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(maintain_person)){
				dataMap.put(dataColumns[c],maintain_person);  
			}
			//家宽代维公司
			c++;
			String maintain_dept = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(maintain_dept)){
				Map<String,Object> tempObj = getBusinessCommunityManageBO().selectMaintainDeptByLabeCn(maintain_dept);
				if(tempObj!=null&&tempObj.size()>0){
					maintain_dept = tempObj.get("CUID").toString();
					dataMap.put(dataColumns[c],maintain_dept); 
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataErrorMsg);
				}
			}else{
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
			}
			//预警阈值(%)
			c++;
			String warning_value = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(warning_value)){
				if(!ImportCommonMethod.isNumber(warning_value)){
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataNumberMsg);
				}else if(!(Double.parseDouble(warning_value)>0&&Double.parseDouble(warning_value)<=100)){
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,yjfz);
				}else{
					dataMap.put(dataColumns[c],warning_value);  
				}
			}
			//联系电话
			c++;
			String contact_number = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(contact_number)){
				//Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
				//添加更多类型的电话号码校验（14开头，17开头）  2016年4月14日17:43:17
				Pattern p=Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
				Matcher m = p.matcher(contact_number);
				if(m.matches()){
					dataMap.put(dataColumns[c],contact_number);
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,"不是有效的联系方式");
				}
			}else{
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
			}
			
			//施工单位
			c++;
			String construct_dept = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(construct_dept)){
				Map<String,Object> tempObj = getBusinessCommunityManageBO().selectMaintainDeptByLabeCn(construct_dept);
				if(tempObj!=null&&tempObj.size()>0){
					construct_dept = tempObj.get("CUID").toString();
					dataMap.put(dataColumns[c],construct_dept); 
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataErrorMsg);
				}
			}
			
			//是否重点小区 
			c++;
			String is_priority = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(is_priority)){
				Map<String,Object> tempObj = getBusinessCommunityManageBO().selectBooleanByLabeCn(is_priority);
				if(tempObj!=null&&tempObj.size()>0){
					is_priority = tempObj.get("CUID").toString();
					dataMap.put(dataColumns[c],is_priority); 
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataErrorMsg);
				}
			}
			
			//是否光改小区
			c++;
			String is_light_changed = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(is_light_changed)){
				Map<String,Object> tempObj = getBusinessCommunityManageBO().selectBooleanByLabeCn(is_light_changed);
				if(tempObj!=null&&tempObj.size()>0){
					is_light_changed = tempObj.get("CUID").toString();
					dataMap.put(dataColumns[c],is_light_changed); 
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataErrorMsg);
				}
			}
			
			//是否重叠小区
			c++;
			String is_overlapp = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(is_overlapp)){
				Map<String,Object> tempObj = getBusinessCommunityManageBO().selectBooleanByLabeCn(is_overlapp);
				if(tempObj!=null&&tempObj.size()>0){
					is_overlapp = tempObj.get("CUID").toString();
					dataMap.put(dataColumns[c],is_overlapp); 
				}else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataErrorMsg);
				}
			}
			
			
			
			//小区类型
			c++;
			String BUILD_TYPE = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if(!ImportCommonMethod.isEmpty(BUILD_TYPE)){
				if(buildTypeEnum.containsKey(BUILD_TYPE)){
			    	dataMap.put(dataColumns[c],buildTypeEnum.get(BUILD_TYPE));
			    }else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEnumMsg);
			    }
			}else{
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEmptyMsg);
			}
			
			 c++;                                                                       
			 String EAST_LONGITUDE  = ImportCommonMethod.getCellValue(xRow.getCell(c)); 
			 dataMap.put(dataColumns[c],EAST_LONGITUDE); 
			 c++;                                                                       
			 String EAST_LATITUDE   = ImportCommonMethod.getCellValue(xRow.getCell(c)); 
			 dataMap.put(dataColumns[c],EAST_LATITUDE); 
			 c++;                                                                       
			 String WEST_LONGITUDE  = ImportCommonMethod.getCellValue(xRow.getCell(c)); 
			 dataMap.put(dataColumns[c],WEST_LONGITUDE);
			 c++;                                                                       
			 String WEST_LATITUDE   = ImportCommonMethod.getCellValue(xRow.getCell(c)); 
			 dataMap.put(dataColumns[c],WEST_LATITUDE);
			 c++;                                                                       
			 String SOUTH_LONGITUDE = ImportCommonMethod.getCellValue(xRow.getCell(c)); 
			 dataMap.put(dataColumns[c],SOUTH_LONGITUDE);
			 c++;                                                                       
			 String SOUTH_LATITUDE  = ImportCommonMethod.getCellValue(xRow.getCell(c)); 
			 dataMap.put(dataColumns[c],SOUTH_LATITUDE);
			 c++;                                                                       
			 String NORTH_LONGITUDE = ImportCommonMethod.getCellValue(xRow.getCell(c)); 
			 dataMap.put(dataColumns[c],NORTH_LONGITUDE);
			 c++;                                                                       
			 String NORTH_LATITUDE  = ImportCommonMethod.getCellValue(xRow.getCell(c)); 
			 dataMap.put(dataColumns[c],NORTH_LATITUDE);

			
			
			LogHome.getLog().info("业务区校验end====");
		} catch (Exception e) {
			LogHome.getLog().error("业务区出错", e);		
			throw new Exception("业务区出错："+e.getMessage());
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
