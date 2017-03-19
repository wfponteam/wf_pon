package com.boco.workflow.webservice.upload.bo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.boco.common.util.debug.LogHome;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.workflow.webservice.dao.utils.BoUtil;
import com.boco.workflow.webservice.upload.constants.Constant;

@Repository
public class ImportBasicDataBO {

	@Autowired
	private IbatisDAO IbatisDAO;
	
	private static final String IMPROT_BASIC_DATA_SQL_MAP0 = "IMPROTBASEDATA";
	
	private static final String IMPROT_BASIC_DATA_SQL_MAP1 = "EQUIP";
	
	private static final String IMPORT_PON = "PONIMPORT";
	
	private static final String BUSINESSCOMMUNITY_SQL_MAP = "BUSINESSCOMMUNITY";
	
	private static final String FULLADDRESS_SQL_MAP = "FULLADDRESS";
	
	private static final String GPONCOVER_SQL_MAP = "GPONCOVER";
	/**
	 * 将网元型号的全部集合放在map中，进行校验
	 * 
	 * @return 网元型号 map结合
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getNeModelCfgCuidName() {
		
		Map<String,String> neModelMap = new HashMap<String,String>();
		List<Map<String,String>> list = this.IbatisDAO.querySql("SELECT PRODUCT_MODEL,CUID FROM NE_MODEL_CFG_TYPE");
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> mapSub = list.get(i);
			String productmodel = (String) mapSub.get("PRODUCT_MODEL");
			String cuid = (String) mapSub.get("CUID");
			neModelMap.put(productmodel, cuid);
		}
		return neModelMap;
	}
	
	public List isExistListName(List verificationList, String excelName) throws Exception {
		
		List returnList = new ArrayList();
		List<String> nameList = new ArrayList<String>();
		List subList = null;
		String queryDb = "";
		if (excelName.equals(Constant.POSNAME) || excelName.equals(Constant.POSPORTNAME)) {
			queryDb = IMPROT_BASIC_DATA_SQL_MAP0 + ".queryPosName";
		} else if (excelName.equals(Constant.ONUNAME) || excelName.equals(Constant.ONUPORTNAME)) {
			queryDb = IMPROT_BASIC_DATA_SQL_MAP0 + ".queryOnuName";
		} else if (excelName.equals("覆盖范围")) {
			queryDb = IMPROT_BASIC_DATA_SQL_MAP0 + ".queryCoverageByNeCuid";
		}
		int length = verificationList.size();
		try {
			for (int i = 0; i < length; i++) {
				nameList.add((String) verificationList.get(i));
				if (i % Constant.QUERYCOUNT == 0 && i != 0) {
					subList = this.IbatisDAO.getSqlMapClient().queryForList(queryDb,nameList);
					if (!subList.isEmpty()) {
						returnList.addAll(subList);
					}
					nameList.clear();
				}

			}
			if (!nameList.isEmpty()) {
				subList = this.IbatisDAO.getSqlMapClient().queryForList(queryDb, nameList);
			}
			if (subList != null && !subList.isEmpty()) {
				returnList.addAll(subList);
			}
		} catch (Exception e) {
			LogHome.getLog().error("与数据库校验判断名称是否存在出错，请您联系管理员！", e);
			throw new Exception("与数据库校验判断名称是否存在出错，请您联系管理员！");
		}
		return returnList;
	}
	
	/**
	 * 时间入库转型
	 * 
	 * @param time
	 */
	public Timestamp formtTime(String time) {
		try{
			if (!StringUtils.isEmpty(time)) {
				return Timestamp.valueOf(time);
			}
		}catch(Exception ex){
			LogHome.getLog().error("");
		}
		return null;
	}
	
	/**
	 * 批量导入
	 * 
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean importBatchInsert(List list, String excelName) throws Exception {
		String sqlAddress = "";
		if (excelName.equals(Constant.POSNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".insertPosInfo";
		} else if (excelName.equals(Constant.ONUNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".insertOnuInfo";
		} else if(excelName.equals("覆盖范围")){
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".insertCoverageInfo";
		}
		
		BoUtil.batchInsert(this.IbatisDAO.getSqlMapClient(), sqlAddress, list);
		
		return false;
	}
	
	/**
	 * 批量修改
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean importOltBatchUpdate(List list, String excelName) throws Exception {
		String sqlAddress = "";
		if (excelName.equals(Constant.POSNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".updatePosInfo";
		} else if (excelName.equals(Constant.ONUNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".updateOnuInfo";
		} else if (excelName.equals("覆盖范围")) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".updateCoverageInfo";
		}
		try {
			BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(), sqlAddress, list);

		} catch (Exception e) {
			LogHome.getLog().error("批量修改出错，请您联系管理员！", e);
			throw new Exception("批量修改出错，请您联系管理员！");
		}
		return false;
	}
	
	public void updatePortState(List<String> ports,String state) throws SQLException{
		String sql = "1".equals(state) ? "updatePortStateFree":"updatePortStateOccu";
	
		this.IbatisDAO.getSqlMapClient().update(IMPROT_BASIC_DATA_SQL_MAP1+"." + sql  , ports);
	}
	
	/**
	 * 得到机盘类型
	 * 
	 * @return 网元型号 map结合
	 */
	public Map<String,String>  getCardKind() {
		Map<String,String> cardNodelMap  = new HashMap<String,String> ();
		List<Map<String,String>> list = this.IbatisDAO.querySql("SELECT CUID,CARDTYPE_NAME,PUBLIC_NAME FROM CARD_KIND");
		for (int i = 0; i < list.size(); i++) {
			Map<String,String>  mapSub = list.get(i);
			String productmodel =  mapSub.get("CARDTYPE_NAME");
			String publicName = mapSub.get("PUBLIC_NAME");
			String cuidAndPublicName =  mapSub.get("CUID")+"|"+publicName;
			cardNodelMap.put(productmodel, cuidAndPublicName);
		}
		return cardNodelMap;
	}
	
	/**
	 * 批量更新卡板,卡板入库使用
	 */
	public boolean importCardBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPORT_PON + ".importCardBatchUpdate";
		try {
			BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(), sql1, list);
			
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新卡板出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新卡板出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量更新卡板,卡板入库使用
	 */
	public boolean importCardBatchUpdate1(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPROT_BASIC_DATA_SQL_MAP1 + ".updateCardInfoByNe";
		try {
			BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(), sql1, list);
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新卡板出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新卡板出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量新增卡板，卡板数据入库使用
	 */
	public boolean importCardBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPORT_PON + ".importCardBatchInsert";
		try {
			BoUtil.batchInsert(this.IbatisDAO.getSqlMapClient(), sql1, list);

		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加卡板出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加卡板出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量更新端口数据,端口数据入库使用
	 */
	public boolean importPtpBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPORT_PON + ".importPtpBatchUpdate";
		try {
			
			BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(), sql1, list);

		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新端口出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新端口出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量插入端口数据,端口数据入库使用
	 */
	public boolean importPtpBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPORT_PON + ".importPtpBatchInsert";
		try {
			BoUtil.batchInsert(this.IbatisDAO.getSqlMapClient(), sql1, list);
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加端口出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加端口出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量更新业务区数据,导入入库使用
	 */
	public boolean importBusinessCommunityBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = BUSINESSCOMMUNITY_SQL_MAP + ".updateBCInfo";
		try {
			BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(), sql1, list);
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量插入业务区数据,导入入库使用
	 */
	public boolean importBusinessCommunityBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = BUSINESSCOMMUNITY_SQL_MAP + ".insertBCInfo";
		try {
			
			BoUtil.batchInsert(this.IbatisDAO.getSqlMapClient(), sql1, list);
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量更新标准地址数据,导入入库使用
	 */
	public boolean importAddressBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = FULLADDRESS_SQL_MAP + ".updateAddressInfo";
		String sql2 = FULLADDRESS_SQL_MAP + ".updateGponCoverByAddress";
		try {
			BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(), sql1, list);
			BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(), sql2, list);

		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量插入标准地址数据,导入入库使用
	 */
	public boolean importAddressBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = FULLADDRESS_SQL_MAP + ".insertAddressInfo";
		try {
			BoUtil.batchInsert(this.IbatisDAO.getSqlMapClient(), sql1, list);
			
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量覆盖范围数据,导入入库使用
	 */
	public boolean importGponCoverBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = GPONCOVER_SQL_MAP + ".updateGponCoverForExcel";
		try {
			BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(), sql1, list);
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量插入覆盖范围数据,导入入库使用
	 */
	public boolean importGponCoverBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = GPONCOVER_SQL_MAP + ".insertGponCoverForExcel";
		try {
			BoUtil.batchInsert(this.IbatisDAO.getSqlMapClient(), sql1, list);
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加出错，请您联系管理员！");
		}
		return res;
	}
}
