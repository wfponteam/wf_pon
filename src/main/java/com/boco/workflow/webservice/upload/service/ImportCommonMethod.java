package com.boco.workflow.webservice.upload.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.core.utils.lang.TimeFormatHelper;
import com.boco.workflow.webservice.equip.bo.OltManageBO;
import com.boco.workflow.webservice.equip.bo.OnuManageBO;
import com.boco.workflow.webservice.upload.bo.ImportBasicDataBO;
import com.boco.workflow.webservice.upload.constants.AnmsConst;
import com.boco.workflow.webservice.upload.constants.Constant;
import com.boco.workflow.webservice.upload.constants.ElementEnum;
import com.boco.workflow.webservice.upload.constants.NetEnum;
import com.boco.workflow.webservice.upload.constants.RackEnum;
import com.boco.workflow.webservice.upload.servlet.ImportResultDO;




@SuppressWarnings({"rawtypes","unused","unchecked","null","deprecation","finally"})
public class ImportCommonMethod {
	
	/**
	 * 返回操作导入相应的BO
	 * 
	 * @return
	 */
	private static ImportBasicDataBO getImportBasicDataBO() {
		return (ImportBasicDataBO) SpringContextUtil
				.getBean("importBasicDataBO");
	}
	
	private static OnuManageBO getOnuManageBO() {
		return (OnuManageBO) SpringContextUtil
				.getBean("onuManageBO");
	}
	
	static OltManageBO oltManageBO = getOltManageBO();

	private static OltManageBO getOltManageBO() {
		return (OltManageBO) SpringContextUtil
				.getBean("oltManageBO");
	}
	
	/**
	 * 判断是否存在最后一列是否存在错误，假如最有一列部位空说明里面存在之，就让其返回true。
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @throws Exception
	 *             判断Excel中是否存在于此单元格是否相同的关键字
	 */
	protected static int isExistFalse(Workbook writeWorkBook, Sheet writeSheet,
			int lastColumns, int lastRows) throws Exception {
		int flaseNumber = 0;
		int length = lastRows + 1;
		for (int k = 5; k < length; k++) {
			Row xRow = writeSheet.getRow(k);
			if (xRow != null) {
				if (xRow.getCell(lastColumns) != null
						&& !StringUtils.isEmpty(xRow.getCell(lastColumns).toString())) {
					flaseNumber++;
				}
			}
		}
		return flaseNumber;
	}
	
	/**
	 * 验证Label的类型是否为空，不正确将错误打印到最后一列中去
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证Label的类型是否为空，不正确将错误打印到最后一列中去
	 */
	protected static void verificationEmpty(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant == null
					|| StringUtils.isEmpty(cellContant.toString())) {
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(j);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					// 设置最后一列记录异常信息单元格的列宽度
					// writeSheet.setColumnWidth(lastColumns,
					// writeSheet.getColumnWidth(lastColumns)*3);
					xCell.setCellValue("[" + initContant.toString() + "]"
							+ "不能为空 \r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + initContant.toString() + "]"
							+ "不能为空 \r\n";
					xCell.setCellValue(strValue);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验导入属性字段是否为空出错!", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * 将list转换成map1
	 * 
	 * @param list
	 * @return
	 */
	protected static Map changeListToMap(List list) {
		Map map = new HashMap();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map subMap = (Map) list.get(i);
				Object labelCnObj = subMap.get("LABEL_CN");
				// 如果是二级分光则删除前面添加的把二级分光添加进去
				if (subMap.get("POSTYPE") != null
						&& map.containsKey(labelCnObj)) {
					if (subMap.get("POSTYPE").toString().equals("2")) {
						map.remove(labelCnObj);
					} else {
						subMap = (Map) map.get(labelCnObj);
					}
				}
				map.put(labelCnObj, subMap);
			}
		}
		return map;
	}
	
	/**
	 * 将出错的单元格，更新成红色背景色并且在最后一列的单元格上打印错误信息
	 * 
	 * @param 打开的sheet进行修改和修饰
	 * @param 当前第几行
	 * @param 当前第几列
	 * @param 最后一列
	 * @throws Exception
	 */
	protected static void updateExcelFormat(Workbook writeWorkBook,
			Sheet writeSheet, int i, int j) throws Exception {
		try {
			CellStyle style = writeWorkBook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.RED.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			Row xRow = writeSheet.getRow(i);
			Cell xCell = xRow.getCell(j);
			if (xCell == null) {
				xCell = xRow.createCell(j);
				xCell.setCellStyle(style);

			} else {
				xCell.setCellStyle(style);
			}

		} catch (Exception e) {
			LogHome.getLog().error("", e);
		}

	}
	
	/**
	 * 批量添加和修改card与ptp
	 * 
	 * @param returnList
	 * @param originalList
	 * @param excelName
	 * @param control
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	protected static void ImportExcelToDB(Map returnMap, List originalList,
			String excelName, ImportResultDO importResultDO)
			throws Exception {
		try {
			String tableName = "";
			String tableStr = "";
			if (excelName.contains("*")) {
				String[] name = excelName.split("\\*");
				tableName = name[1];
				excelName = name[0];
			}
			List updateList = new ArrayList();
			List addList = new ArrayList();
			List<Map<String, String>> vlanList = new ArrayList<Map<String, String>>();
			for (int k = 0; k < originalList.size(); k++) {
				Map map = (Map) originalList.get(k);
				if (map.get("VLAN") != null
						&& !StringUtils.isEmpty(map.get("VLAN")
								.toString())) {
					// map.put(Ptp.AttrName.vlan,getValue(map.get(Ptp.AttrName.vlan).toString()));
					// map.put(Ptp.AttrName.vlan,ObjectUtils.toString(Ptp.AttrName.vlan));
				}
				String cuid = (String) map.get("CUID");
				if (cuid != null &&  !StringUtils.isEmpty(cuid.toString())) {
					updateList.add(map);
				} else {
					/* 针对 AN_POS,AN_ONU表中的设备CUID新增为空的BUG进行修改 2016-11-09 @auth guoxiaoqiang */
					cuid = CUIDHexGenerator.getInstance().generate("TRANS_ELEMENT");
					map.put("CUID", cuid);
					addList.add(map);
				}
				
			}
			
			List<String> newPortList = new ArrayList<String>();
			List<String> oldPortList = new ArrayList<String>();
			
			if (!addList.isEmpty()) {
				//更新覆盖范围信息
				//checkTRofhFullAddressInfo(excelName, addList);
				//批量新增信息
				getImportBasicDataBO().importBatchInsert(addList, excelName);
				if(excelName.equals(Constant.ONUNAME)||excelName.equals(Constant.POSNAME)){
					//如果是POS设备或ONU设备导入新增，则更新板卡信息
					for (int i = 0; i < addList.size(); i++) {
						Map map = (Map) addList.get(i);
						String neCuid = ObjectUtils.toString(map.get("CUID"));
						String neFdn = ObjectUtils.toString(map.get("FDN"));
						if (excelName.equals(Constant.ONUNAME)) {
							getOnuManageBO().createCardInfo(neCuid, neFdn, "ONU", 2);
							newPortList.add(ObjectUtils.toString(map.get("RELATED_POS_PORT_CUID")));
						}
						if (excelName.equals(Constant.POSNAME)) {
							getOnuManageBO().createCardInfo(neCuid, neFdn, "POS", 3);
							newPortList.add(ObjectUtils.toString(map.get("RELATED_UPNE_PORT_CUID")));
						}
						
						//更新端口状态
						String portName = excelName.equals(Constant.ONUNAME) ? "RELATED_POS_PORT_CUID":"RELATED_UPNE_PORT_CUID";
						String newPort = ObjectUtils.toString(map.get(portName));
						newPortList.add(newPort);
	
						
					}
					
					
				}
			}
			if (!updateList.isEmpty()) {
				//更新覆盖范围信息
				//checkTRofhFullAddressInfo(excelName, updateList);
				//批量更新信息
				getImportBasicDataBO().importOltBatchUpdate(updateList,excelName);
				if(excelName.equals(Constant.ONUNAME)||excelName.equals(Constant.POSNAME)){
					//如果是POS设备或ONU设备导入修改，则更新板卡信息
					for (int i = 0; i < updateList.size(); i++) {
						Map map = (Map) updateList.get(i);
						String neCuid = ObjectUtils.toString(map.get("CUID"));
						String neFdn = ObjectUtils.toString(map.get("FDN"));
						// 根据需求，在页面新增onu的时候，不新建板卡
						/*if (excelName.equals(Constant.ONUNAME)) {
							getAnOltManageBO().createCardInfo(neCuid, neFdn, "ONU", 2);
						}*/
						if (excelName.equals(Constant.POSNAME)) {
							getOnuManageBO().createCardInfo(neCuid, neFdn, "POS", 3);
						}
						
						//更新端口状态
						String portName = excelName.equals(Constant.ONUNAME) ? "RELATED_POS_PORT_CUID":"RELATED_UPNE_PORT_CUID";
						String newPort = ObjectUtils.toString(map.get(portName));
						String oldPort = String.valueOf(((Map)returnMap.get(map.get("LABEL_CN"))).get(portName));
						if(!newPort.equals(oldPort)){
							newPortList.add(newPort);
							oldPortList.add(oldPort);
						}
					}
					
				}
				
			}
			
			//更新端口状态
			//TODO:如果新端口中有重复的，需要提示用户端口被占用
			if(!newPortList.isEmpty()){
				getImportBasicDataBO().updatePortState(newPortList, "2");
			}
			if(!oldPortList.isEmpty()){
				getImportBasicDataBO().updatePortState(oldPortList, "1");
			}
			importResultDO.setInfo("添加数据:" + addList.size() + "条");
			importResultDO.setInfo("更新数据:" + updateList.size() + "条");
			importResultDO.setSuccess(true);
		} catch (Exception e) {
			LogHome.getLog().error(excelName + "导入数据库入库失败", e);
			throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
		}

	}
	
	
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 || "".equals(str)
				|| "null".equalsIgnoreCase(str)
				|| "undefined".equalsIgnoreCase(str);
	}
	
	
	/**
	 * 判断Excel中是否存在相同的关键字
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             判断Excel中是否存在于此单元格是否相同的关键字
	 */
	protected static void isExcelExist(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns, int lastRows,
			Map nameMap) throws Exception {
		boolean flag = false;
		String verificationrow = "";
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null
					&& !StringUtils.isEmpty(cellContant.toString())) {
				if (nameMap.containsKey(cellContant.toString())) {
					flag = true;
					int k = Integer.parseInt(nameMap
							.get(cellContant.toString()).toString());
					verificationrow = verificationrow + "," + k;
				}// else说明值不存在
				if (!"".equals(verificationrow)) {
					verificationrow = verificationrow.substring(1);
				}// else说明值不存在
					// 假如判断返回false说明该字符串中正确，假如是true说明该字符串中存在上述问题
				if (flag) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						xCell.setCellValue(verificationrow + "行 与 本行 ["
								+ cellContant.toString() + "]重复\r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + verificationrow + "行 与 本行["
								+ cellContant.toString() + "]重复 \r\n";
						xCell.setCellValue(strValue);
					}
				}// else说明值不存在
			}// else直接推出校验

		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * 判断板卡端口导入时板卡名称是否连续出现
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i当前第几行
	 * @param j当前第几列
	 * @param lastColumns总列数
	 * @throws Exception
	 * @param lastRows
	 * @param lastColumns
	 * @param constantValue
	 * @param nameList
	 * @return
	 * @return
	 */
	protected static void VerificationCardNameRepeat(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns) throws Exception {
		Row xRow = writeSheet.getRow(i);
		Object cellContant = xRow.getCell(j);
		Row xTitRow = writeSheet.getRow(0);
		Object titleName = xTitRow.getCell(j);
		String deviceName = cellContant.toString();
		updateExcelFormat(writeWorkBook, writeSheet, i, j);
		// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
		Cell xCell = xRow.getCell(lastColumns);
		if (xCell == null) {
			xCell = xRow.createCell(lastColumns);
			xCell.setCellValue(titleName.toString() + "页面出现重复值!\r\n");
		} else {
			String strValue = xCell.getStringCellValue();
			strValue = strValue + titleName.toString() + "页面出现重复值! \r\n";
			xCell.setCellValue(strValue);
		}
	}
	
	/*
	 * 校验设备上联端口与上联设备的关联关系
	 */
	protected static Map verificationRelatedPosPort(Workbook writeWorkBook,
			Sheet writeSheet, String posCuid, int relatedPosCuid,
			int relatedPosPortCuid, int i, int lastColumns, String constantValue)
			throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Cell relatedPosObj = xRow.getCell(relatedPosCuid);
			Cell relatedPosPortObj = xRow.getCell(relatedPosPortCuid);
			if (relatedPosObj != null && relatedPosPortObj != null) {
				List<Map> list = getOnuManageBO().getPtpByLabelCnAndNeCuid(getCellValue(relatedPosPortObj), posCuid);
				if (list == null || list.isEmpty()) {
					updateExcelFormat(writeWorkBook, writeSheet, i,relatedPosPortCuid);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Row initRow = writeSheet.getRow(0);
					Object initContant1 = initRow.getCell(relatedPosPortCuid);
					Object initContant2 = initRow.getCell(relatedPosCuid);
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						xCell.setCellValue("[" + initContant1.toString() + "]"
								+ "与[" + initContant2.toString()
								+ "]不存在关联关系!\r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + "[" + initContant1.toString()
								+ "]" + "与[" + initContant2.toString()
								+ "]不存在关联关系!\r\n";
						xCell.setCellValue(strValue);
					}
				} else {
					return list.get(0);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("verificationEmpty校验导入属性字段是否为空出错!", e);
			throw new Exception(e);
		}
		return null;
	}
	
	/**
	 * 提示错误信息
	 */
	public static void printOnlyErrorInfo(Workbook writeWorkBook,
			Sheet writeSheet, int i, int j, int lastColumns, String msg)
			throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			updateExcelFormat(writeWorkBook, writeSheet, i, j);
			// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
			Cell xCell = xRow.getCell(lastColumns);
			if (xCell == null) {
				xCell = xRow.createCell(lastColumns);
				xCell.setCellValue(msg + " \r\n");
			} else {
				String strValue = xCell.getStringCellValue();
				strValue = strValue + msg + " \r\n";
				xCell.setCellValue(strValue);
			}
		} catch (Exception e) {
			LogHome.getLog().error("提示错误信息!" + msg, e);
			throw new Exception(e);
		}
	}
	
	/**
	 * 验证IP的是否符合ip的格式，不正确将错误打印到最后一列中去
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证IP的是否符合ip的格式，不正确将错误打印到最后一列中去
	 */
	protected static void verificationIp(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null && !"".equals(cellContant.toString())) {
				// 定义正则表达式，假如ip符合ip的格式要求，就返回true，不匹配就返回false
				String regExp = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
				Pattern pattern = Pattern.compile(regExp);
				Matcher matcher = pattern.matcher(cellContant.toString());
				if (!matcher.find()) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						xCell.setCellValue(cellContant.toString()
								+ "Ip的格式不正确 \r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + cellContant.toString()
								+ "Ip的格式不正确 \r\n";
						xCell.setCellValue(strValue);
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证IP格式出错，请您核实一下您的IP格式！", e);
			throw new Exception("验证IP格式出错，请您核实一下您的IP格式！");
		}
	}
	
	/**
	 * 验证枚举值是否存在，不正确将错误打印到最后一列中去
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证枚举值是否存在，不正确将错误打印到最后一列中去
	 */
	protected static void verificationEnum(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		try {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			if (cellContant != null
					&& !StringUtils.isEmpty(cellContant.toString())) {
				if (!isEnum(constantValue, cellContant.toString())) {
					updateExcelFormat(writeWorkBook, writeSheet, i, j);
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell = xRow.getCell(lastColumns);
					if (xCell == null) {
						xCell = xRow.createCell(lastColumns);
						// 设置最后一列记录异常信息单元格的列宽度
						// writeSheet.setColumnWidth(lastColumns,
						// writeSheet.getColumnWidth(lastColumns)*3);
						xCell.setCellValue("[" + constantValue + "]枚举值不存在 \r\n");
					} else {
						String strValue = xCell.getStringCellValue();
						strValue = strValue + "[" + cellContant.toString()
								+ "]枚举值不存在 \r\n";
						xCell.setCellValue(strValue);
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证枚举值存在失败，请您仔细核实一下您的数据是否正确！", e);
			throw new Exception("验证枚举值存在失败，请您仔细核实一下您的数据是否正确！");
		}
	}
	
	/**
	 * 验证Date的类型是否正确，不正确将错误打印到最后一列中去
	 * 
	 * 打开的的副本ws环境可以对单元格进行操作的
	 * 
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证Date的类型是否正确，不正确将错误打印到最后一列中去
	 */
	protected static void verificationDate(Workbook writeWorkBook,
			Sheet writeSheet, int j, int i, int lastColumns,
			String constantValue) throws Exception {
		Row xRow = writeSheet.getRow(i);
		Cell xCell = xRow.getCell(j);
		if (xCell == null) {
			return;
		}
		try {
			String val = "";
			boolean flag = false;
			if (xCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				Date dt = HSSFDateUtil.getJavaDate(xCell.getNumericCellValue());
				val = dateformat.format(dt);
			} else {
				val = xRow.getCell((short) j).getStringCellValue();
			}
			Date datResult = TimeFormatHelper.checkIsDate(val);
			if (datResult != null) {
				flag = true;
			}
			if (!flag) {
				updateExcelFormat(writeWorkBook, writeSheet, i, j);

				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Cell cell = xRow.getCell(lastColumns);
				if (cell == null) {
					cell = xRow.createCell(lastColumns);
					// 设置最后一列记录异常信息单元格的列宽度
					// writeSheet.setColumnWidth(lastColumns,
					// writeSheet.getColumnWidth(lastColumns)*3);
					if (!StringUtils.isEmpty(val)) {
						cell.setCellValue(xCell.toString() + "日期格式不正确\r\n");
					}
				} else {
					if (!StringUtils.isEmpty(val)) {
						cell.setCellValue(cell.getStringCellValue()
								+ xCell.toString() + "日期格式不正确\r\n");
					}
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("验证日期类型出错", e);
			throw new Exception("验证日期类型出错");
		}

	}
	
	/**
	 * 验证枚举值是否存在，一般的是必须存在的，否则就会出现不存在的值，true是存在，false是不存在
	 * 
	 * @param condition
	 *            条件（Excel的表名）
	 * @param name
	 *            单元格的内容
	 * @throws Exception
	 *             验证枚举值是否存在，一般的是必须存在的，否则就会出现不存在的值，true是存在，false是不存在
	 */
	protected static boolean isEnum(String condition, String name) {
		boolean flag = false;
		Map subEnum = new HashMap();
		if (Constant.LIVECYCLE.equals(condition)) {
			subEnum = ElementEnum.LIVE_CYCLE.getAllEnum();
		} else if (Constant.OWNERSHIP.equals(condition)) {
			subEnum = NetEnum.OWNER_SHIP_CH.getAllEnum();
		} else if (Constant.OWNERSHIPMAN.equals(condition)) {
			subEnum = AnmsConst.OWNERSHIP_MAN.getAllEnum();
		} else if (Constant.MAINTMODE.equals(condition)) {
			subEnum = ElementEnum.ELEMENT_MAINT_MODE.getAllEnum();
		} else if (Constant.DIRECTION.equals(condition)) {
			subEnum = ElementEnum.DIRECTION.getAllEnum();
		} else if (Constant.FTTX.equals(condition)) {
			//subEnum = ElementEnum.FTTX_TYPE.getAllEnum();
			subEnum.put(1L, "FTTB");
			subEnum.put(2L, "FTTH");
			subEnum.put(3L, "LAN");
			subEnum.put(4L, "无线");
			
		} else if (Constant.ONUTYPE.equals(condition)) {
			subEnum = ElementEnum.ONU_TYPE.getAllEnum();
			//onu类型根据现场临时添加
			subEnum.put(3L, "HGU");
			subEnum.put(4L, "SFU");
		} else if (Constant.PORTRATE.equals(condition)) {
			subEnum = RackEnum.RATR_TYPE.getAllEnum();
			subEnum.put(48L, "20M");
		} else if (Constant.PORTSUBTYPE.equals(condition)) {
			subEnum = RackEnum.PORT_SUB_TYPE.getAllEnum();
		} else if (Constant.AUTHTYPE.equals(condition)) {
			// subEnum = AnmsConst.AUTH_TYPE.getAllEnum();
			subEnum.put("1", "SN");
			subEnum.put("2", "MAC");
			subEnum.put("3", "PASSWORD");
			subEnum.put("4", "LOGICID");
		} else if (Constant.PORTTYPE.equals(condition)) {
			subEnum = RackEnum.PORT_TYPE.getAllEnum();
		} else if (Constant.PORTSTATE.equals(condition)) {
			subEnum = RackEnum.PORT_STATE.getAllEnum();
		}else if(Constant.ACCESSTYPE.equals(condition)){
			subEnum = AnmsConst.ACCESS_TYPE.getAllEnum();
		}
		if (subEnum.containsValue(name)) {
			flag = true;
		}
		return flag;
	}
	
	// 判断cell的类型
		public static String getCellValue(Cell c) {
			String value = "";
			if (c != null) {
				if (c.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					value = c.getStringCellValue().trim();
				} else if (c.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					if (DateUtil.isCellDateFormatted(c)) {
						Date d = c.getDateCellValue();
						SimpleDateFormat resFormat = null;
						if (d.getHours() > 0) {
							resFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						} else {
							resFormat = new SimpleDateFormat("yyyy-MM-dd");
						}
						value = resFormat.format(d);
					} else {
						c.setCellType(HSSFCell.CELL_TYPE_STRING);
						String temp = c.getStringCellValue();
						if (temp.indexOf(".") > -1) {
							value = String.valueOf(new Double(temp)).trim();
						} else {
							value = temp.trim();
						}
					}
				} else if (c.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
					value = Boolean.toString(c.getBooleanCellValue());
				} else if (c.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
					value = "";
				} else if (c.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
					value = "";
				} else if (c.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					value = c.getStringCellValue();
					if (value != null) {
						value = value.replaceAll("#N/A", "").trim();
					}
				} else {
					value = c.toString().trim();
				}
			}
			return value;
		}
		
		
		/**
		 * //字符串强转Timestamp
		 */
		protected static Timestamp getTimestame(String time) {
			try {
				if (!StringUtils.isEmpty(time)) {
					Date date= TimeFormatHelper.checkIsDate(time);
					String timeFormat = "yyyy-MM-dd HH:mm:ss";
					
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
					String resStr = simpleDateFormat.format(date);
					return Timestamp.valueOf(resStr);
				}
				return null;
			}		
			catch (Exception ex) {
				LogHome.getLog().error("时间转换异常", ex);
			}
			return null;
		}

		public static String getOldOnuPortCuid(String onuCuid) {
			
			String id = getOnuManageBO().getOldOnuPortCuid(onuCuid);
			
			return id;
		}
		
		/**
		 * 校验最后一行是否有错误信息
		 * */
		public static boolean isRowExistError(Row row, int lastColumns)
				throws Exception {
			Cell cell = row.getCell(lastColumns);
			if (cell != null) {
				String cellVal = ImportCommonMethod.getCellValue(row
						.getCell(lastColumns));
				if (!ImportCommonMethod.isEmpty(cellVal)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * 批量添加和修改card与ptp, 更新板卡时是否动态更新端口名称control
		 */
		protected static void ImportPonCardPtpExcelToDB(List<Map> objList,
				Map<String, Map> cardMaps, String excelName, 
				ImportResultDO importResultDO) throws Exception {
			try {
				//List<Map<String, String>> cuidList = new ArrayList<Map<String, String>>();
				//POS和ONU端口导入时，不新增板卡 2016年4月19日09:50:47--为什么不创建？？？
				/**
				 * 2016.4.25  bug:导入端口后,card未能生成
				 * 
				 * 吉林：导入PORT CARD 未生成 原因分析
				 * 导入模板中取消了  POS的虚拟CARD信息
				 * 而 POS的 PORT 是挂在  虚拟的 CARD上
				 * 的, 在做 excel解析的时候,CODE中对于 创建CARD 的
				 * labelCn 为空,而且只保存OLT的板卡,
				 * 顾在做PORT的Datas 固化的时候,并未创建对应
				 * PORT的CARD
				 * 
				 * 
				 * 解决方案：
				 * 1.提取所有PORT对应的POS的LIST
				 * 2.迭代LIST查询其下若有虚拟板卡则返回板卡信息,若无,则创建虚拟CARD返回
				 * 3.根据返回的CARD信息 ,插入PORT数据
				 * 注意：ONU也存在类似问题
				 */
				//Set<String>   canImportPortTypes = new HashSet<String>(); 这块代码，完全没必要加
				//canImportPortTypes.addAll(Arrays.asList(new String[]{Constant.OLTPORTNAME, Constant.POSPORTNAME, Constant.ONUPORTNAME})) ;
				//if(canImportPortTypes.contains(excelName)){
				
				if (cardMaps != null && cardMaps.size() > 0) {
					List updateCardList = new ArrayList();
					List updateCardList1 = new ArrayList();
					List insertCardList = new ArrayList();
					for (String k : cardMaps.keySet()) {
						Map card = cardMaps.get(k);
						String pcType = card.get("TYPE") + "";
						if ("update".equals(pcType)) {
							if(excelName.equals(Constant.OLTPORTNAME)){
								updateCardList.add(card);
							}else{
								updateCardList1.add(card);
							}
						}
						if ("add".equals(pcType)) {
							insertCardList.add(card);
						}
					}
					if (updateCardList != null && updateCardList.size() > 0) {
						getImportBasicDataBO().importCardBatchUpdate( updateCardList, excelName);
//						if ("updatePtpName".equals(control)) {
//							getImportBasicDataBO().updatePtpNameByCard( updateCardList, excelName);
//						}
					}
					if (updateCardList1 != null && updateCardList1.size() > 0) {
						getImportBasicDataBO().importCardBatchUpdate1(updateCardList1, excelName);
//						if ("updatePtpName".equals(control)) {
//							getImportBasicDataBO().updatePtpNameByCard( updateCardList, excelName);
//						}
					}
					importResultDO.setInfo("更新板卡数据:" + (updateCardList.size()+updateCardList1.size())
							+ "条");
					if (insertCardList != null && insertCardList.size() > 0) {
						getImportBasicDataBO().importCardBatchInsert(
								insertCardList, excelName);
						importResultDO.setInfo("新增板卡数据:" + insertCardList.size()
								+ "条");
					}
				}
				//}
				List updatePtpList = new ArrayList();
				List insertPtpList = new ArrayList();
				for (Map ptp : objList) {
					String pcType = ptp.get("TYPE") + "";
					if ("update".equals(pcType)) {
						updatePtpList.add(ptp);
					}
					if ("add".equals(pcType)) {
						insertPtpList.add(ptp);
					}
				}
				if (updatePtpList != null && updatePtpList.size() > 0) {
					getImportBasicDataBO().importPtpBatchUpdate(updatePtpList,
							excelName);
					importResultDO.setInfo("更新端口数据:" + updatePtpList.size() + "条");
				}
				if (insertPtpList != null && insertPtpList.size() > 0) {
					getImportBasicDataBO().importPtpBatchInsert(insertPtpList,
							excelName);
					importResultDO.setInfo("新增端口数据:" + insertPtpList.size() + "条");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogHome.getLog().error(excelName + "导入数据库入库失败", e);
				throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
			}
		}
		
		/**
		 * 判断导入模板字段是否必填
		 * 
		 * @throws Exception
		 */
		public static void decideFields(Workbook writeWorkBook, Sheet writeSheet,
				int i, int lastColumns) throws Exception {
			Row proRow = writeSheet.getRow(2);
			Row cellRow = writeSheet.getRow(i);
			for (int m = 1; m < lastColumns; m++) {
				if (proRow.getCell(m).toString().indexOf("非空") != -1
						|| proRow.getCell(m).toString().indexOf("必填") != -1) {
					Object obj = cellRow.getCell(m);
					if (obj == null || "".equals(obj.toString())
							|| "null".equalsIgnoreCase(obj.toString())) {
						updateExcelFormat(writeWorkBook, writeSheet, i, m);
						Cell xCell = cellRow.getCell(lastColumns);
						if (xCell == null) {
							xCell = cellRow.createCell(lastColumns);
							xCell.setCellValue("【"
									+ writeSheet.getRow(0).getCell(m) + "】"
									+ "列不能为空\r\n");
						} else {
							String strValue = xCell.getStringCellValue();
							strValue = strValue + "【"
									+ writeSheet.getRow(0).getCell(m) + "】"
									+ "列不能为空\r\n";
							xCell.setCellValue(strValue);
						}
					}
				}
			}
		}
		
		/**
		 * 验证所属网元是否存在
		 * 
		 * @param ws
		 *            打开的的副本ws环境可以对单元格进行操作的
		 * @param sheet
		 *            第一个sheet表
		 * @param i
		 *            当前第几行
		 * @param j
		 *            当前第几列
		 * @param lastColumns
		 *            总列数
		 * @throws Exception
		 * 
		 */
		protected static Map verificationAllNeName(Workbook writeWorkBook,
				Sheet writeSheet, int relatedNeCuidInt, int i, int lastColumns,
				String constantValue) throws Exception {
			Map res = null;
			try {
				String sheetName = writeSheet.getSheetName();
				Row xRow = writeSheet.getRow(i);
				Cell xCell = xRow.getCell(relatedNeCuidInt);
				if (xCell != null && !StringUtils.isEmpty(xCell.toString())) {
					List<Map> list = new ArrayList<Map>();
					if(sheetName.indexOf("OLT端口")>=0){
						list = oltManageBO.getOLTCuidByLabelCn(xCell.toString());
					}else if(sheetName.indexOf("ONU端口")>=0){
						list = oltManageBO.getONUCuidByLabelCn(xCell.toString());
					}else if(sheetName.indexOf("POS端口")>=0){
						list = oltManageBO.getPOSCuidByLabelCn(xCell.toString());
					}else{
						list = oltManageBO.getAllNeCuidByLabelCn(xCell.toString());
					}
					if (list.isEmpty()) {
						String showmessage = "网元名称在数据库中不存在\r\n";
						updateExcelFormat(writeWorkBook, writeSheet, i,
								relatedNeCuidInt);
						// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
						Cell lastXCell = xRow.getCell(lastColumns);
						// Cell jCell=xRow.getCell(j);
						if (lastXCell == null) {
							lastXCell = xRow.createCell(lastColumns);
							// 设置最后一列记录异常信息单元格的列宽度
							// writeSheet.setColumnWidth(lastColumns,
							// writeSheet.getColumnWidth(lastColumns)*3);
							lastXCell.setCellValue(showmessage);
						} else {
							String strValue = lastXCell.getStringCellValue();
							strValue = strValue + showmessage;
							lastXCell.setCellValue(strValue);
						}
					} else {
						res = list.get(0);
					}
				}
			} catch (Exception e) {
				LogHome.getLog().error("验证关联字段是否存在，请您仔细核实一下您的外键是否存在！", e);
				throw new Exception("验证关联字段是否存在，请您仔细核实一下您的外键是否存在！");
			}
			return res;
		}
		
		/**
		 * 提示错误信息
		 */
		public static void printErrorInfo(Workbook writeWorkBook, Sheet writeSheet,
				int i, int j, int lastColumns, String msg) throws Exception {
			try {
				Row xRow = writeSheet.getRow(i);
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(j);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					xCell.setCellValue("[" + initContant.toString() + "]" + msg
							+ " \r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + initContant.toString() + "]" + msg
							+ " \r\n";
					xCell.setCellValue(strValue);
				}
			} catch (Exception e) {
				LogHome.getLog().error("提示错误信息!" + msg, e);
				throw new Exception(e);
			}
		}
		
		/**
		 * 判断设备FDN信息格式是否正确
		 */
		public static boolean verificationNeFdnInfo(String neFdn, String devType) {
			if (!ImportCommonMethod.isEmpty(neFdn)) {
				if ("olt".equals(devType) && neFdn.indexOf("EMS=") != -1
						&& neFdn.indexOf(":ManagedElement=") != -1) {
					return true;
				} else if ("pos".equals(devType) && neFdn.indexOf("EMS=") != -1
						&& neFdn.indexOf(":ManagedElement=") != -1
						&& neFdn.indexOf(":POS=") != -1) {
					return true;
				} else if ("onu".equals(devType) && neFdn.indexOf("EMS=") != -1
						&& neFdn.indexOf(":ManagedElement=") != -1
						&& neFdn.indexOf(":ONU=") != -1) {
					return true;
				} else if ("card".equals(devType) && neFdn.indexOf("EMS=") != -1
						&& neFdn.indexOf(":ManagedElement=") != -1
						&& neFdn.indexOf(":EquipmentHolder=") != -1
						&& neFdn.indexOf(":Equipment=") != -1) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		public static void verificationRelated(Workbook writeWorkBook, Row xRow,
				int j, int lastColumns, String constantValue, Map cardKindMap)
				throws Exception {
			try {
				Object cellContant = xRow.getCell(j);
				if (cellContant != null
						&& !StringUtils.isEmpty(cellContant.toString())) {
					if (constantValue.equals(Constant.SINGLEMODEL)) {
						if (!cardKindMap.containsKey(cellContant.toString())) {
							ImportCommonMethod.updateExcelFormat(writeWorkBook,
									xRow, j);
							// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
							Cell xCell = xRow.getCell(lastColumns);
							if (xCell == null) {
								xCell = xRow.createCell(lastColumns);
								// writeSheet.setColumnWidth(lastColumns,
								// writeSheet.getColumnWidth(lastColumns)*3);
								xCell.setCellValue("[" + Constant.SINGLEMODEL
										+ "]数据库中不存在!\r\n");
							} else {
								String strValue = xCell.getStringCellValue();
								strValue = strValue + "[" + Constant.SINGLEMODEL
										+ "]数据库中不存在! \r\n";
								xCell.setCellValue(strValue);
							}
						}
					}
				}
			} catch (Exception e) {
				LogHome.getLog().error("验证关联关系是否存在时出错，请您核实一下您的关联关系！", e);
				throw new Exception("验证关联关系是否存在时出错，请您核实一下您的关联关系！");
			}

		}
		
		/**
		 * 验证card的fdn 架—框-槽格式是否符合要求
		 * 
		 * @param writeWorkBook
		 * @param writeSheet
		 * @param j
		 * @param i
		 * @param lastColumns
		 * @param constantValue
		 * @throws Exception
		 */
		protected static boolean verificationCardFdn(Workbook writeWorkBook,
				Sheet writeSheet, int j, int i, int lastColumns,
				String constantValue) throws Exception {
			boolean res = true;
			try {
				Row xRow = writeSheet.getRow(i);
				Object cellContant = xRow.getCell(j);
				if (cellContant != null
						&& !StringUtils.isEmpty(cellContant.toString())) {
					// 假如判断返回false说明该字符串中正确，假如是true说明该字符串中存在上述问题
					if (!isBaseFdn(cellContant.toString())) {
						res = false;
						updateExcelFormat(writeWorkBook, writeSheet, i, j);
						// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
						Cell xCell = xRow.getCell(lastColumns);
						if (xCell == null) {
							xCell = xRow.createCell(lastColumns);
							// 设置最后一列记录异常信息单元格的列宽度
							// writeSheet.setColumnWidth(lastColumns,
							// writeSheet.getColumnWidth(lastColumns)*3);
							xCell.setCellValue("[" + constantValue
									+ "]架—框-槽不符合要求\r\n");
						} else {
							String strValue = xCell.getStringCellValue();
							strValue = strValue + "[" + constantValue
									+ "]架—框-槽不符合要求 \r\n";
							xCell.setCellValue(strValue);
						}
					}
				}
			} catch (Exception e) {
				LogHome.getLog().error(
						"验证架—框-槽符合*-*-*要求，请您仔细核实一下您的架—框-槽格式是否符合条件格式：*—*-*", e);
				throw new Exception(
						"验证架—框-槽符合*-*-*要求，请您仔细核实一下您的架—框-槽格式是否符合条件格式：*—*-*");
			}
			return res;
		}
		
		/**
		 * 相同的网元名称下板卡的架框槽(FDN)相同
		 * 
		 * @param ws
		 *            打开的的副本ws环境可以对单元格进行操作的
		 * @param sheet
		 *            第一个sheet表
		 * @param i
		 *            当前第几行
		 * @param j
		 *            当前第几列
		 * @param lastColumns
		 *            总列数
		 * @throws Exception
		 *             验证Label的类型是否为空，不正确将错误打印到最后一列中去
		 */
		protected static boolean verificationSameFdnCard(Workbook writeWorkBook,
				Sheet writeSheet, int j, int i, int lastColumns,
				String constantValue) throws Exception {
			try {
				Row xRow = writeSheet.getRow(i);
				Object cellContant = xRow.getCell(j);
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(j);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					xCell.setCellValue("同一网元下相同所属机槽编号的板卡必须相同\r\n");
					return false;
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "同一网元下相同所属机槽编号的板卡必须相同 \r\n";
					xCell.setCellValue(strValue);
					return false;
				}
			} catch (Exception e) {
				LogHome.getLog().error("", e);
				throw new Exception(e);
			}
		}
		
		/**
		 * 相同的网元名称下板卡的架框槽(FDN)相同
		 */
		protected static boolean verificationSameCardFdn(Workbook writeWorkBook,
				Sheet writeSheet, int j, int i, int lastColumns,
				String constantValue) throws Exception {
			try {
				Row xRow = writeSheet.getRow(i);
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					xCell.setCellValue("同一网元下相同板卡的所属机槽编号必须相同\r\n");
					return false;
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "同一网元下相同板卡的所属机槽编号必须相同 \r\n";
					xCell.setCellValue(strValue);
					return false;
				}
			} catch (Exception e) {
				LogHome.getLog().error("", e);
				throw new Exception(e);
			}
		}


		public static boolean isNotEmpty(String str) {
			return !isEmpty(str);
		}
		
		/**
		 * 判断OLT关联端口端口名称是否重复出现
		 * 
		 * @param ws
		 *            打开的的副本ws环境可以对单元格进行操作的
		 * @param sheet
		 *            第一个sheet表
		 * @param i当前第几行
		 * @param j当前第几列
		 * @param k 所属设备列
		 * @param lastColumns总列数
		 * @throws Exception
		 * @param lastRows
		 * @param lastColumns
		 * @param constantValue
		 * @param nameList
		 * @return
		 * @return
		 */
		protected static void VerificationPorNametRepeat(Workbook writeWorkBook,
				Sheet writeSheet, int j, int i,int k,int m,int lastColumns, Map deviceNameMap)
				throws Exception {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			Object xrelatedNeCuid =xRow.getCell(k);
			Object xcardName =xRow.getCell(m);
			Row xTitRow = writeSheet.getRow(0);
			Object titleName = xTitRow.getCell(j);
			String relatedNeCuid = xrelatedNeCuid.toString();
			String cardName = xcardName.toString();
			String deviceName = cellContant.toString()+"-"+relatedNeCuid+"-"+cardName;
			if (deviceNameMap.containsKey(deviceName)) {
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					xCell.setCellValue("[" + titleName.toString() + "]出现重复值!\r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + titleName.toString()
							+ "]出现重复值! \r\n";
					xCell.setCellValue(strValue);
				}
			} else {
				deviceNameMap.put(deviceName.toString(), i);
			}
		}
		
		
		/**
		 * 验证Number的类型是否正确，不正确将错误打印到最后一列中去
		 * 
		 * @param ws
		 *            打开的的副本ws环境可以对单元格进行操作的
		 * @param sheet
		 *            第一个sheet表
		 * @param i
		 *            当前第几行
		 * @param j
		 *            当前第几列
		 * @param lastColumns
		 *            总列数
		 * @throws Exception
		 *             验证Number的类型是否正确，不正确将错误打印到最后一列中去
		 */
		protected static void verificationNumber(Workbook writeWorkBook,
				Sheet writeSheet, int j, int i, int lastColumns,
				String constantValue) throws Exception {
			boolean flag = false;
			try {
				Row xRow = writeSheet.getRow(i);
				Cell xCell = xRow.getCell(j);
				if (xCell != null && !StringUtils.isEmpty(xCell.toString())) {
					if (xCell.getCellType() == Cell.CELL_TYPE_STRING) {
						if (isNumber(xCell.getStringCellValue())) {
							flag = true;
						}
					} else if (xCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						String dblContent = Double.toString(xCell
								.getNumericCellValue());
						if (isNumber(dblContent)) {
							flag = true;
						}
					}
					if (!flag) {
						updateExcelFormat(writeWorkBook, writeSheet, i, j);
						// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
						Cell Cell = xRow.getCell(lastColumns);
						if (Cell == null) {
							Cell = xRow.createCell(lastColumns);
							// 设置最后一列记录异常信息单元格的列宽度
							// writeSheet.setColumnWidth(lastColumns,
							// writeSheet.getColumnWidth(lastColumns)*3);
							Cell.setCellValue("[" + xCell.toString() + "]"
									+ "属性值必须是数字类型 \r\n");
						} else {
							String strValue = Cell.getStringCellValue();
							strValue = strValue + "[" + xCell.toString() + "]"
									+ "属性值必须是数字类型\r\n";
							Cell.setCellValue(strValue);
						}

					}
				}// 无值不操作

			} catch (Exception e) {
				LogHome.getLog().error("验证数字的类型出错，请您仔细核实一下您输入的信息", e);
				throw new Exception("验证数字的类型出错，请您仔细核实一下您输入的信息！");
			}

		}
		
		/**
		 * 同一板卡名称下端口的槽位(FDN)相同
		 * 
		 * @param ws
		 *            打开的的副本ws环境可以对单元格进行操作的
		 * @param sheet
		 *            第一个sheet表
		 * @param i
		 *            当前第几行
		 * @param j
		 *            当前第几列
		 * @param lastColumns
		 *            总列数
		 * @throws Exception
		 *             验证Label的类型是否为空，不正确将错误打印到最后一列中去
		 */
		protected static boolean verificationSamePortFdn(Workbook writeWorkBook,
				Sheet writeSheet, int j, int i, int lastColumns,
				String constantValue) throws Exception {
			try {
				Row xRow = writeSheet.getRow(i);
				/**
				 * 
				 * 此处，貌似有三行无用代码入参也有无用传参 问： 敢删么? 答：不敢 问：为何 答：怂
				 */
				Object cellContant = xRow.getCell(j);
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Row initRow = writeSheet.getRow(0);
				Object initContant = initRow.getCell(j);
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					xCell.setCellValue("同一板卡名称下,端口编号不能相同 \r\n");
					return false;
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "同一网元名称下,端口编号不能相同 \r\n";
					xCell.setCellValue(strValue);
					return false;
				}
			} catch (Exception e) {
				LogHome.getLog().error("", e);
				throw new Exception(e);
			}
		}
		
		protected static boolean isSamePortFdn(Workbook writeWorkBook,
				Sheet writeSheet, String neName, String cardName, String ptpStr,
				String ptpnNo, int portNoInt, int i, int lastColumns,
				String constantValue, String neCuid) throws Exception {
			try {
				Row xRow = writeSheet.getRow(i);
				Cell cellContant = xRow.getCell(portNoInt);
				List list = oltManageBO
						.isSameCardPtpExist(cardName, ptpnNo, neCuid);
				if (!list.isEmpty()) {
					String labelCn = ((Map) list.get(0)).get("LABEL_CN")
							.toString();
					if (!labelCn.equals(ptpStr)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} catch (Exception e) {
				LogHome.getLog().error("", e);
				throw new Exception(e);
			}
		}
		
		/**
		 * 验证Number的类型是否正确，不正确将错误打印到最后一列中去(VLAN:有可能是以逗号隔开的数字组成的字符串)
		 * 
		 * @param ws
		 *            打开的的副本ws环境可以对单元格进行操作的
		 * @param sheet
		 *            第一个sheet表
		 * @param i
		 *            当前第几行
		 * @param j
		 *            当前第几列
		 * @param lastColumns
		 *            总列数
		 * @throws Exception
		 *             验证Number的类型是否正确，不正确将错误打印到最后一列中去
		 */
		protected static void verificationVLANNumber(Workbook writeWorkBook,
				Sheet writeSheet, int j, int i, int lastColumns,
				String constantValue) throws Exception {
			boolean flag = false;
			try {
				Row xRow = writeSheet.getRow(i);
				Cell xCell = xRow.getCell(j);
				if (xCell != null && !StringUtils.isEmpty(xCell.toString())) {
					if (xCell.getCellType() == Cell.CELL_TYPE_STRING) {
						flag = checkVlanValue(xCell.getStringCellValue());
						/*
						 * if(isNumber(xCell.getStringCellValue())){ flag=true; }
						 */
					} else if (xCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						String dblContent = Double.toString(xCell
								.getNumericCellValue());
						if (isNumber(dblContent)) {
							flag = true;
						}
					}
					if (!flag) {
						updateExcelFormat(writeWorkBook, writeSheet, i, j);
						// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
						Cell Cell = xRow.getCell(lastColumns);
						if (Cell == null) {
							Cell = xRow.createCell(lastColumns);
							// 设置最后一列记录异常信息单元格的列宽度
							// writeSheet.setColumnWidth(lastColumns,
							// writeSheet.getColumnWidth(lastColumns)*3);
							Cell.setCellValue("[" + xCell.toString() + "]"
									+ "属性值必须是数字类型 \r\n");
						} else {
							String strValue = Cell.getStringCellValue();
							strValue = strValue + "[" + xCell.toString() + "]"
									+ "属性值必须是数字类型\r\n";
							Cell.setCellValue(strValue);
						}

					}
				}// 无值不操作

			} catch (Exception e) {
				LogHome.getLog().error("验证数字的类型出错，请您仔细核实一下您输入的信息", e);
				throw new Exception("验证数字的类型出错，请您仔细核实一下您输入的信息！");
			}

		}
		
		/**
		 * 判断名称是否连续出现
		 * 
		 * @param ws
		 *            打开的的副本ws环境可以对单元格进行操作的
		 * @param sheet
		 *            第一个sheet表
		 * @param i当前第几行
		 * @param j当前第几列
		 * @param lastColumns总列数
		 * @throws Exception
		 * @param lastRows
		 * @param lastColumns
		 * @param constantValue
		 * @param nameList
		 * @return
		 * @return
		 */
		protected static void VerificationNameRepeat(Workbook writeWorkBook,
				Sheet writeSheet, int j, int i, int lastColumns, Map deviceNameMap)
				throws Exception {
			Row xRow = writeSheet.getRow(i);
			Object cellContant = xRow.getCell(j);
			Row xTitRow = writeSheet.getRow(0);
			Object titleName = xTitRow.getCell(j);
			String deviceName = cellContant.toString();
			if (deviceNameMap.containsKey(deviceName)) {
				updateExcelFormat(writeWorkBook, writeSheet, i, j);
				// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
				Cell xCell = xRow.getCell(lastColumns);
				if (xCell == null) {
					xCell = xRow.createCell(lastColumns);
					xCell.setCellValue("[" + titleName.toString() + "]出现重复值!\r\n");
				} else {
					String strValue = xCell.getStringCellValue();
					strValue = strValue + "[" + titleName.toString()
							+ "]出现重复值! \r\n";
					xCell.setCellValue(strValue);
				}
			} else {
				deviceNameMap.put(deviceName, i);
			}
		}
		
		/**
		 * 判断字符串是否是数字
		 */
		public static boolean isNumber(String value) {
			return isInteger(value) || isDouble(value);
		}
		
		/**
		 * 判断字符串是否是整数
		 */
		protected static boolean isInteger(String value) {
			try {
				Integer.parseInt(value);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		/**
		 * 判断字符串是否是浮点数
		 */
		public static boolean isDouble(String value) {
			try {
				Double.parseDouble(value);
				if (value.contains(".")) {
					return true;
				} else {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		
		private static boolean checkVlanValue(String vlanValue) {
			boolean vlanflag = true;
			String[] vlanValueArray = vlanValue.split(",");
			for (int i = 0; i < vlanValueArray.length; i++) {
				if (!isNumber(vlanValueArray[i])) {
					vlanflag = false;
					break;
				}
			}
			return vlanflag;
		}
		
		protected static boolean isBaseFdn(String name) {
			// 1-1-13,NA-2-1,1-1-NTA
			String[] subAddress = name.split("\\-");
			String reg1 = "^([0-9]+|NA)$";
			String reg2 = "^([0-9]+)$";
			String reg3 = "^([0-9]+|[A-Za-z]+)$";
			Pattern pattern1 = Pattern.compile(reg1);
			Pattern pattern2 = Pattern.compile(reg2);
			Pattern pattern3 = Pattern.compile(reg3);
			boolean flag = false;
			if (subAddress.length == 3) {
				boolean bln0 = pattern1.matcher(subAddress[0]).find();
				boolean bln1 = pattern2.matcher(subAddress[1]).find();
				boolean bln2 = pattern3.matcher(subAddress[2]).find();
				if (bln0 && bln1 && bln2) {
					flag = true;
				} else {
					flag = false;
				}
			} else {
				flag = false;
			}
			return flag;
		}
		
		/**
		 * 将出错的单元格，更新成红色背景色并且在最后一列的单元格上打印错误信息
		 */
		protected static void updateExcelFormat(Workbook writeWorkBook, Row xRow,
				int j) throws Exception {
			try {
				CellStyle style = writeWorkBook.createCellStyle();
				style.setFillForegroundColor(IndexedColors.RED.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				Cell xCell = xRow.getCell(j);
				if (xCell == null) {
					xCell = xRow.createCell(j);
					xCell.setCellStyle(style);

				} else {
					xCell.setCellStyle(style);
				}

			} catch (Exception e) {
				LogHome.getLog().error("", e);
			}

		}
		
		/**
		 * 判断字符串是否正整数
		 */
		public static Integer isIntegerNumber(String value) {
			Integer res = null;
			try {
				int v = Integer.parseInt(value);
				if (v >= 0) {
					res = v;
				}
			} catch (NumberFormatException e) {
				res = null;
			}
			return res;
		}
		
		public static void importBusinessCommunityToDB(
				List<Map<String, Object>> dataList, String excelName,
				ImportResultDO importResultDO) throws Exception {
			try {
				List<Map<String, Object>> insertDataList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> updateDataList = new ArrayList<Map<String, Object>>();
				List<Map<String, String>> cuidList = new ArrayList<Map<String, String>>();
				if (dataList != null && dataList.size() > 0) {
					for (Map<String, Object> m : dataList) {
						String pcType = m.get("TYPE") + "";
						if ("UPDATE".equals(pcType)) {
							updateDataList.add(m);
						}
						if ("INSERT".equals(pcType)) {
							insertDataList.add(m);
						}
					}
				}
				if (updateDataList != null && updateDataList.size() > 0) {
					getImportBasicDataBO().importBusinessCommunityBatchUpdate(
							updateDataList, excelName);
					importResultDO
							.setInfo("更新业务区数据:" + updateDataList.size() + "条");
				}
				if (insertDataList != null && insertDataList.size() > 0) {
					getImportBasicDataBO().importBusinessCommunityBatchInsert(
							insertDataList, excelName);
					importResultDO
							.setInfo("新增业务区数据:" + insertDataList.size() + "条");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogHome.getLog().error(excelName + "导入数据库入库失败", e);
				throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
			}
		}
		
		/**
		 * 批量添加和修改card与ptp, 更新板卡时是否动态更新端口名称control
		 */
		public static void importAddressToDB(List<Map<String, Object>> dataList,
				String excelName, ImportResultDO importResultDO) throws Exception {
			try {
				List<Map<String, Object>> insertDataList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> updateDataList = new ArrayList<Map<String, Object>>();
				List<Map<String, String>> cuidList = new ArrayList<Map<String, String>>();
				if (dataList != null && dataList.size() > 0) {
					for (Map<String, Object> m : dataList) {
						String pcType = m.get("TYPE") + "";
						if ("UPDATE".equals(pcType)) {
							updateDataList.add(m);
						}
						if ("INSERT".equals(pcType)) {
							insertDataList.add(m);
						}
					}
				}
				if (updateDataList != null && updateDataList.size() > 0) {
					getImportBasicDataBO().importAddressBatchUpdate(updateDataList,
							excelName);
					importResultDO.setInfo("更新标准地址数据:" + updateDataList.size()
							+ "条");
				}
				if (insertDataList != null && insertDataList.size() > 0) {
					getImportBasicDataBO().importAddressBatchInsert(insertDataList,
							excelName);
					importResultDO.setInfo("新增标准地址数据:" + insertDataList.size()
							+ "条");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogHome.getLog().error(excelName + "导入数据库入库失败", e);
				throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
			}
		} 
		
		public static void importGponCoverToDB(List<Map<String, Object>> dataList,
				String excelName, ImportResultDO importResultDO) throws Exception {
			try {
				List<Map<String, Object>> insertDataList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> updateDataList = new ArrayList<Map<String, Object>>();
				List<Map<String, String>> cuidList = new ArrayList<Map<String, String>>();
				if (dataList != null && dataList.size() > 0) {
					for (Map<String, Object> m : dataList) {
						String pcType = m.get("TYPE") + "";
						if ("UPDATE".equals(pcType)) {
							updateDataList.add(m);
						}
						if ("INSERT".equals(pcType)) {
							insertDataList.add(m);
						}
					}
				}
				if (updateDataList != null && updateDataList.size() > 0) {
					getImportBasicDataBO().importGponCoverBatchUpdate(
							updateDataList, excelName);
					importResultDO.setInfo("更新覆盖范围数据:" + updateDataList.size()
							+ "条");
				}
				if (insertDataList != null && insertDataList.size() > 0) {
					getImportBasicDataBO().importGponCoverBatchInsert(
							insertDataList, excelName);
					importResultDO.setInfo("新增覆盖范围数据:" + insertDataList.size()
							+ "条");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogHome.getLog().error(excelName + "导入数据库入库失败", e);
				throw new Exception(excelName + "导入数据库入库失败，请联系管理员！");
			}
		}
		
}
