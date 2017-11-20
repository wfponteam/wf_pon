package com.boco.workflow.webservice.upload.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.space.bo.FullAddressBO;
import com.boco.workflow.webservice.upload.bo.ImportBasicDataBO;
import com.boco.workflow.webservice.upload.constants.GetCh2Spell;
import com.boco.workflow.webservice.upload.servlet.ImportResultDO;

/**
 * 标准地址导入处理类
 */
public class ImportAddressExcel {
	private static String[] dataColumns = { 
			"CUID", 
			"LABEL_CN", 
			"PROVINCE",
			"CITY", 
			"COUNTY", 
			"TOWN", 
			"COMMUNITY", 
			"ROAD", 
			"ROAD_NUMBER",
			"VILLAGES", 
			"VILLAGE_ALIAS", 
			"BUILDING", 
			"UNIT_NO", 
			"FLOOR_NO",
			"ROOM_NO", 
			"LONGITUDE", 
			"LATITUDE", 
			"ABBREVIATION", 
			"PINYIN",
			"POSTCODE", 
			"RELATED_COMMUNITY_CUID",
			"REGIONTYPE1",
			"REGIONTYPE2"
			};

	private static String[] excelColumns = { 
			"属性中文名称", 
			"名称", 
			"省份", 
			"地市", 
			"区/县",
			"镇乡/街道办", 
			"社区/行政村", 
			"路/巷/街", 
			"门牌号码", 
			"小区/单位/学校/村组", 
			"小区别名", 
			"楼栋号",
			"单元", 
			"楼层", 
			"房号/村内编号", 
			"经度", 
			"纬度", 
			"地址简称", 
			"地址拼音", 
			"邮政编码", 
			"所属业务区",
			"地域属性一级分类",
			"地域属性二级分类"
	};
	private Map<String,String> regionType1Enum = new HashMap<String, String>(){
		{
			put("城市","1");put("乡镇","2");put("农村","3");
		}
	};
	private Map<String,String> regionType2Enum = new HashMap<String, String>(){
		{
			put("商铺","11");put("物流园区","12");put("城中村","13");put("小区","14");
			put("县辖城区","21");put("城中村","22");put("小区","23");put("商铺","24");
			put("非中心村","31");put("自然村","32");
		}
	};
	private String dataErrorMsg = "数据库中不存在，请检查！";
	private String dataEmptyMsg = "信息为空，请检查！";
	private String dataNumberMsg = "不是浮点数，请检查！";
	private String dataRepeatMsg = "信息重复，请检查！";
	private String dataExistMsg = "数据库已存在，请检查！";
	private String dataEnumMsg = "枚举值不存在，请检查！";
	/**
	 * 返回操作导入相应的BO
	 */
	private static FullAddressBO getFullAddressManageBO() {
		return (FullAddressBO) SpringContextUtil.getBean("fullAddressBO");
	}

	public ImportResultDO importData(
										Workbook writeWorkBook, Sheet writeSheet, String pathname,String excelName,String prjcode) 
												throws Exception {

		Map<String, String> nameMaps = new HashMap<String, String>();
		Map<String, String> districtMaps = new HashMap<String, String>();
		Map<String, String> businessMaps = new HashMap<String, String>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		ImportResultDO importResultDO = new ImportResultDO(excelName);
		try {
			// 取得sheet
			int lastRows = writeSheet.getLastRowNum();
			Row xrow = writeSheet.getRow(0);
			// 获取第一行求的列数
			int lastColumns = xrow.getLastCellNum();
			List<String> errorlist = new ArrayList<String>();
			// 取得每一列对应的列号，然后进行校验判断。
			getHeadingMap(writeSheet, errorlist, lastColumns);
			if (!errorlist.isEmpty()) {
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
				if (xRow != null) {
					Map<String, Object> dataMap = verificationCell(
							writeWorkBook, writeSheet, xRow, i, lastColumns,
							nameMaps, districtMaps, businessMaps,prjcode);
					if (!ImportCommonMethod.isRowExistError(xRow, lastColumns)
							&& dataMap.get("TYPE") != null
							&& dataMap.get("CUID") != null) {
						dataMap.put("RELATED_PROJECT_CUID", prjcode);
						dataList.add(dataMap);
					} else {
						erroNum++;
					}
				}
			}
			importResultDO.setInfo("导入表格中共有:" + (lastRows - dataRowNum + 1)+ "条数据");
			if (erroNum > 0) {
				importResultDO.setSuccess(false);
				importResultDO.setInfo("有" + erroNum + "条错误数据,数据未进行导入。");
			} else {
				if (dataList != null && dataList.size() > 0) {
					ImportCommonMethod.importAddressToDB(dataList, "标准地址",importResultDO);
					importResultDO.setSuccess(true);
				} else {
					importResultDO.setSuccess(true);
					importResultDO.setInfo("有0条数据,数据未进行导入。");
				}
			}
			return importResultDO;
		} catch (Exception e) {
			LogHome.getLog().error("标准地址导入失败", e);
			e.printStackTrace();
			throw new Exception("导入失败请与管理员联系!" + e.getMessage());
		}
	}

	public Map<String, Object> verificationCell(Workbook writeWorkBook,
			Sheet writeSheet, Row xRow, int i, int lastColumns,
			Map<String, String> nameMaps, Map<String, String> districtMaps,
			Map<String, String> businessMaps,String prjcode) throws Exception {
		
		LogHome.getLog().info("标准地址校验start====");
		Map dataMap = new HashMap();
		try {
			int c = 1;
			String adrName = ImportCommonMethod.getCellValue(xRow.getCell(c));
			String adrCuid = "";
			if (!ImportCommonMethod.isEmpty(adrName)) {
				Map tempObj = getFullAddressManageBO().selectAddressInfoByLabeCn(adrName);
				if (tempObj != null) {
					
					String type = tempObj.get("TYPE").toString();
					String pCode = IbatisDAOHelper.getStringValue(tempObj, "RELATED_PROJECT_CUID");
					if("0".equals(type)){
						
						ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
								c, lastColumns, "标准地址名称已归档！");
					}else if(!prjcode.equals(pCode)){
						ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
								c, lastColumns, "标准地址已在工程"+pCode+"中！");
					}else{
						adrCuid = tempObj.get("CUID").toString();
						dataMap.put(dataColumns[0], adrCuid);
					}
					
				}
			}
			c++;

			String provinceName = ImportCommonMethod.getCellValue(xRow.getCell(c));
			String provinceCuid = "";
			String adrGroupName = "";
			if (!ImportCommonMethod.isEmpty(provinceName)) {
				adrGroupName = provinceName;
				String key = 2 + provinceName;
				if (districtMaps.containsKey(key)) {
					provinceCuid = (String) districtMaps.get(key);
					dataMap.put(dataColumns[c], provinceCuid);
				} else {
					Map tempObj = getFullAddressManageBO()
							.selectDistrictInfoByLabeCn(provinceName, 2, null);
					if (tempObj != null) {
						provinceCuid = tempObj.get("CUID").toString();
						dataMap.put(dataColumns[c], provinceCuid);
						districtMaps.put(key, provinceCuid);
					} else {
						ImportCommonMethod.printErrorInfo(writeWorkBook,
								writeSheet, i, c, lastColumns,
								this.dataErrorMsg);
					}
				}
			} else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String cityName = ImportCommonMethod.getCellValue(xRow.getCell(c));
			String cityCuid = "";
			if (!ImportCommonMethod.isEmpty(cityName)) {
				adrGroupName = adrGroupName + "|" + cityName;
				if (!ImportCommonMethod.isEmpty(provinceCuid)) {
					String key = 3 + provinceCuid + cityName;
					if (districtMaps.containsKey(key)) {
						cityCuid = (String) districtMaps.get(key);
						dataMap.put(dataColumns[c], cityCuid);
					} else {
						Map tempObj = getFullAddressManageBO()
								.selectDistrictInfoByLabeCn(cityName, 3,
										provinceCuid);
						if (tempObj != null) {
							cityCuid = tempObj.get("CUID").toString();
							dataMap.put(dataColumns[c], cityCuid);
							districtMaps.put(key, cityCuid);
						} else {
							ImportCommonMethod.printErrorInfo(writeWorkBook,
									writeSheet, i, c, lastColumns,
									this.dataErrorMsg);
						}
					}
				}
			} else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String countyName = ImportCommonMethod
					.getCellValue(xRow.getCell(c));
			String countyCuid = "";
			if (!ImportCommonMethod.isEmpty(countyName)) {
				adrGroupName = adrGroupName + "|" + countyName;
				if (!ImportCommonMethod.isEmpty(cityCuid)) {
					String key = 4 + cityCuid + countyName;
					if (districtMaps.containsKey(key)) {
						countyCuid = (String) districtMaps.get(key);
						dataMap.put(dataColumns[c], countyCuid);
					} else {
						Map tempObj = getFullAddressManageBO()
								.selectDistrictInfoByLabeCn(countyName, 4,
										cityCuid);
						if (tempObj != null) {
							countyCuid = tempObj.get("CUID").toString();
							dataMap.put(dataColumns[c], countyCuid);
							districtMaps.put(key, countyCuid);
						} else {
							ImportCommonMethod.printErrorInfo(writeWorkBook,
									writeSheet, i, c, lastColumns,
									this.dataErrorMsg);
						}
					}
				}
			} else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String townName = ImportCommonMethod.getCellValue(xRow.getCell(c));
			String townCuid = "";
			if (!ImportCommonMethod.isEmpty(townName)) {
				adrGroupName = adrGroupName + "|" + townName;
				if (!ImportCommonMethod.isEmpty(countyCuid)) {
					String key = 3 + countyCuid + townName;
					if (districtMaps.containsKey(key)) {
						townCuid = (String) districtMaps.get(key);
						dataMap.put(dataColumns[c], townCuid);
					} else {
						Map tempObj = getFullAddressManageBO()
								.selectDistrictInfoByLabeCn(townName, 5,
										countyCuid);
						if (tempObj != null) {
							townCuid = tempObj.get("CUID").toString();
							dataMap.put(dataColumns[c], townCuid);
							districtMaps.put(key, townCuid);
						} else {
							ImportCommonMethod.printErrorInfo(writeWorkBook,
									writeSheet, i, c, lastColumns,
									this.dataErrorMsg);
						}
					}
				}
			} else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String community = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(community)) {
				//adrGroupName = adrGroupName + "|" + community;
				dataMap.put(dataColumns[c], community);
			}
			c++;

			String road = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(road)) {
				adrGroupName = adrGroupName + "|" + road;
				dataMap.put(dataColumns[c], road);
			}else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String roadNumber = ImportCommonMethod
					.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(roadNumber)) {
				//adrGroupName = adrGroupName + "|" + roadNumber;
				dataMap.put(dataColumns[c], roadNumber);
			}
			c++;

			String villages = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(villages)) {
				adrGroupName = adrGroupName + "|" + villages;
				dataMap.put(dataColumns[c], villages);
			} else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String villagesAlias = ImportCommonMethod.getCellValue(xRow
					.getCell(c));
			if (!ImportCommonMethod.isEmpty(villagesAlias)) {
				//adrGroupName = adrGroupName + "|" + villagesAlias;
				dataMap.put(dataColumns[c], villagesAlias);
			}
			c++;

			String bullding = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(bullding)) {
				adrGroupName = adrGroupName + "|" + bullding;
				dataMap.put(dataColumns[c], bullding);
			}else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String unitno = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(unitno)) {
				adrGroupName = adrGroupName + "|" + unitno;
				dataMap.put(dataColumns[c], unitno);
			}else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String floorno = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(floorno)) {
				adrGroupName = adrGroupName + "|" + floorno;
				dataMap.put(dataColumns[c], floorno);
			}else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String roomno = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(roomno)) {
				adrGroupName = adrGroupName + "|" + roomno;
				dataMap.put(dataColumns[c], roomno);
			} else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;
			if (!ImportCommonMethod.isEmpty(adrGroupName)) {
				if (nameMaps.containsKey(adrGroupName)) {
					ImportCommonMethod.printOnlyErrorInfo(writeWorkBook,
							writeSheet, i, 1, lastColumns, "与"
									+ (String) nameMaps.get(adrGroupName)
									+ "地址" + this.dataRepeatMsg);
				} else {
					nameMaps.put(adrGroupName, "第" + (i + 1) + "行");
					Map<String,Object> map = getFullAddressManageBO().isExistAddressInfoByLabeCn(
							adrGroupName, adrCuid);
					if(map != null){
						
						if("0".equals(IbatisDAOHelper.getStringValue(map, "TYPE"))){
							ImportCommonMethod.printOnlyErrorInfo(writeWorkBook,
								writeSheet, i, 1, lastColumns, "标准地址["
										+ adrGroupName + "],"
										+ this.dataExistMsg);
						}else{
							adrCuid = IbatisDAOHelper.getStringValue(map, "CUID");
						}
					} 
						dataMap.put(dataColumns[1], adrGroupName);
						if (!ImportCommonMethod.isEmpty(adrCuid)) {
							dataMap.put("TYPE", "UPDATE");
							dataMap.put(dataColumns[0], adrCuid);
						} else {
							dataMap.put(dataColumns[0], CUIDHexGenerator.getInstance().generate("T_ROFH_FULL_ADDRESS"));
							dataMap.put("TYPE", "INSERT");
						}
					
				}
			}

			String longitude = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(longitude)) {
				if (longitude.indexOf(".") == -1) {
					longitude = longitude + ".0";
				}
				if (!ImportCommonMethod.isDouble(longitude))
					ImportCommonMethod.printErrorInfo(writeWorkBook,
							writeSheet, i, c, lastColumns, this.dataNumberMsg);
				else {
					dataMap.put(dataColumns[c], longitude);
				}
			}
			c++;

			String latitude = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(latitude)) {
				if (latitude.indexOf(".") == -1) {
					latitude = latitude + ".0";
				}
				if (!ImportCommonMethod.isDouble(latitude))
					ImportCommonMethod.printErrorInfo(writeWorkBook,
							writeSheet, i, c, lastColumns, this.dataNumberMsg);
				else {
					dataMap.put(dataColumns[c], latitude);
				}
			}
			c++;

			String abbreviation = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(abbreviation)) {
				dataMap.put(dataColumns[c], abbreviation);
			}
			c++;

			String pinyin = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(pinyin)) {
				dataMap.put(dataColumns[c], pinyin);
			}else{
				dataMap.put("PINYIN", GetCh2Spell.getBeginCharacter(adrGroupName));
			}
			dataMap.put("PINYIN", GetCh2Spell.getBeginCharacter(adrGroupName));
			c++;

			String postcode = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(postcode)) {
				dataMap.put(dataColumns[c], postcode);
			}
			c++;

			String business = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(business)) {
				if (businessMaps.containsKey(business)) {
					dataMap.put(dataColumns[c], businessMaps.get(business));
				} else {
					Map tempObj = getFullAddressManageBO()
							.selectBusinessCommunityInfoByLabeCn(business);
					if (tempObj != null) {
						String businessCuid = tempObj.get("CUID").toString();
						dataMap.put(dataColumns[c], businessCuid);
						businessMaps.put(business, businessCuid);
					} else {
						ImportCommonMethod.printErrorInfo(writeWorkBook,
								writeSheet, i, c, lastColumns,
								this.dataErrorMsg);
					}
				}
			}else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,
						c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String regionType1 = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(regionType1)) {
				if(regionType1Enum.containsKey(regionType1)){
			    	dataMap.put(dataColumns[c],regionType1Enum.get(regionType1));
			    }else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEnumMsg);
			    }
			}else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,c, lastColumns, this.dataEmptyMsg);
			}
			c++;

			String regionType2 = ImportCommonMethod.getCellValue(xRow.getCell(c));
			if (!ImportCommonMethod.isEmpty(regionType2)) {
				if(regionType2.equals("小区")  ){
					if(regionType1Enum.get(regionType1).equals("1")){
						dataMap.put(dataColumns[c],"14");
					}else{
						dataMap.put(dataColumns[c],"23");
					}
				}else if(regionType2.equals("城中村")  ){
					if(regionType1Enum.get(regionType1).equals("1")){
						dataMap.put(dataColumns[c],"13");
					}else{
						dataMap.put(dataColumns[c],"22");
					}
				}else if(regionType2.equals("商铺")  ){
					if(regionType1Enum.get(regionType1).equals("1")){
						dataMap.put(dataColumns[c],"11");
					}else{
						dataMap.put(dataColumns[c],"24");
					}
				}else if(regionType2Enum.containsKey(regionType2) && regionType2Enum.get(regionType2).substring(0, 1).equals(regionType1Enum.get(regionType1))){
			    	dataMap.put(dataColumns[c],regionType2Enum.get(regionType2));
			    }else{
					ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet,i,c,lastColumns,dataEnumMsg);
			    }
			}else {
				ImportCommonMethod.printErrorInfo(writeWorkBook, writeSheet, i,c, lastColumns, this.dataEmptyMsg);
			}
			dataMap.put("LAST_MODIFY_TIME",new Timestamp(System.currentTimeMillis()));
			LogHome.getLog().info("标准地址校验end====");
		} catch (Exception e) {
			LogHome.getLog().error("标准地址出错", e);
			throw new Exception("标准地址出错：" + e.getMessage());
		}
		return dataMap;
	}

	/**
	 * 检查模板里的列名称
	 * 
	 * @param sheet
	 * @param headingRow
	 * @param errorlist
	 * @param lastColumns
	 * @return
	 */
	private static Map getHeadingMap(Sheet sheet, List errorlist,
			int lastColumns) {
		Map headingMap = new HashMap();
		Row xRow = sheet.getRow(0);
		for (int i = 0; i < lastColumns; i++) {
			Object columnName = xRow.getCell(i);
			if (columnName == null
					|| !columnName.toString().equals(excelColumns[i])) {
				errorlist.add("该模板的第" + (i + 1) + "列名称为空或跟模板EXCEL中对应列名称不一致!");
			}
		}
		/*Row xRowData = sheet.getRow(4);
		if (xRowData == null
				|| xRowData.getCell(0) == null
				|| !"从此开始填写>>".equals(xRowData.getCell(0)
						.getRichStringCellValue().toString())) {
			errorlist.add("该模板文件已被修改，请检查!");
		}*/
		return headingMap;
	}

	/**
	 * 返回操作导入相应的BO
	 * 
	 * @return
	 */
	private static ImportBasicDataBO getImportBasicDataBO() {
		return (ImportBasicDataBO) SpringContextUtil
				.getBean("importBasicDataBO");
	}
	
		
}
