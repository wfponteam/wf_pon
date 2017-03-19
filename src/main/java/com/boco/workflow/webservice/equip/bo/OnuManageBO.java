package com.boco.workflow.webservice.equip.bo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.core.ibatis.vo.ResultMap;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.dao.utils.BoUtil;
import com.boco.workflow.webservice.upload.service.ImportCommonMethod;

/**
 * onu管理bo
 * @author gaoyang 2017年3月13日
 *
 */
@Service
public class OnuManageBO {

	@Autowired
	private IbatisDAO IbatisDAO;
	
	@Autowired
	private PtpManageBO ptpManageBO;
	/**
	 * 删除onu相关信息
	 * @param cuids
	 * @throws SQLException
	 */
	public void deleteOnuList(List<String> cuids) throws SQLException{
		
		//删除关联板卡
		BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deleteCardByNeCuid", cuids);
		//释放上联端口
		BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".updateOnuPtpFree", cuids);
		//删除onu
		BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deleteOnuByCuid", cuids);
		//删除覆盖范围
		BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deleteGponCoverbyNeCuid", cuids);
	}

	

    /**
     * 查询onu名称是否存在
     * @param name
     * @return
     * @throws SQLException 
     */
	public boolean isOnuNameExist(String name) throws SQLException {
		
		if (StringUtils.isNotBlank(name)) {
			@SuppressWarnings("unchecked")
			List<String> list = this.IbatisDAO.getSqlMapClient().queryForList(
					NetWorkConstant.EQUIP_SQL_MAP + ".queryOnuByLabelcn", name);
			if (null != list && list.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 根据网元型号得到对应的设备供应商
	 * @throws SQLException 
	 */
	public Map<String,String> queryVendorByModel(String modelCuid) throws SQLException {
		
		Map<String,String> returnMap = new HashMap<String,String>();
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryVendorBymodel", modelCuid);
		if(!list.isEmpty() && list.size()>0){
			Map<String,Object> map = (Map<String,Object>)list.get(0);
			returnMap.put("text", map.get("LABEL_CN").toString());
			returnMap.put("value", map.get("CUID").toString());
		}
		return returnMap;
	}

	/*
	 * 查询onu信息
	 */
	public Map<String,Object> queryOnuByCuid(String cuid) throws SQLException {
		
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryOnuByCuid", cuid);
		Map<String, Object> returnMap = BoUtil.convertResult2Panel(list);

		return returnMap;
			
		
	}

	/**
	 * 新增onu信息
	 * @param map
	 * @throws SQLException 
	 */
	public void addOnuInfo(Map<String, Object> map) throws Exception {

		String cuid = CUIDHexGenerator.getInstance().generate("TRANS_ELEMENT");

		//插入的时候加入了一些定值
		map.put("CUID", cuid);
		map.put("DEV_CUID", "AN_ONU" + "-" + cuid);
		
		if(map.get("CREATE_TIME") == null){
			map.put("CREATE_TIME",new Timestamp(System.currentTimeMillis()));
		}else{
			map.put("CREATE_TIME", new Date(IbatisDAOHelper.getLongValue(map, "CREATE_TIME")));
		}
		map.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));

		String cabCuid = map.get("RELATED_ACCESS_POINT").toString();
		List<?> dcuids = IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryRelatedDistrictCuidByCabCuid",cabCuid);
		String relatedDistrictCuid = BoUtil.getSingleValueByList(dcuids, "RELATED_DISTRICT_CUID");
		map.put("RELATED_DISTRICT_CUID", relatedDistrictCuid);
		
		//占用新端口
		ptpManageBO.updatePortState("2", IbatisDAOHelper.getStringValue(map,"RELATED_POS_PORT_CUID"));
		  
		this.IbatisDAO.getSqlMapClient().insert(NetWorkConstant.EQUIP_SQL_MAP + ".insertOnuInfo", map);
	
	}

	/**
	 * 修改onu
	 * @param map
	 */
	public void modifyOnuInfo(Map<String,Object> map) throws Exception{

		String onuCuid = ObjectUtils.toString(map.get("CUID"));
		map.put("LAST_MODIFY_TIME",new Timestamp(System.currentTimeMillis()));
		
		if(StringUtils.isNotBlank((String)map.get("CREATE_TIME"))){
			map.put("CREATE_TIME", new Date(IbatisDAOHelper.getLongValue(map, "CREATE_TIME")));
		}
		this.getDistrictCuidByCab(map);

		String oldPort = IbatisDAOHelper.getStringValue(map, "oldPort");
		String newPort = IbatisDAOHelper.getStringValue(map,"RELATED_POS_PORT_CUID");
		if(oldPort == null || !oldPort.equals(newPort)){
			//释放旧端口	
			if(StringUtils.isNotBlank(oldPort)){
				ptpManageBO.updatePortState("1", oldPort);
			}
			//占用新端口
			ptpManageBO.updatePortState("2", newPort);
		}
		
		this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updateOnuInfo", map);
		
		///更新板卡和端口的名称
		this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updateCardNameByOnu", onuCuid);
		this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updatePtpNameByOnu", onuCuid);
		
	}
	
	/**
	 * 获取地区id
	 * @param map
	 * @throws SQLException 
	 */
	private void getDistrictCuidByCab(Map<String,Object> map) throws SQLException{
		
		String cabCuid = map.get("RELATED_ACCESS_POINT").toString();
		List<?> dcuids = IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryRelatedDistrictCuidByCabCuid",cabCuid);
		String relatedDistrictCuid = BoUtil.getSingleValueByList(dcuids, "RELATED_DISTRICT_CUID");
		map.put("RELATED_DISTRICT_CUID", relatedDistrictCuid);
	}
	
	/**
	 * POS或ONU，初始化板卡 
	 */
	public Map createCardInfo(String neCuid,String neFdn,String _devType,int devType){
		Map res = null;
		try {
			if(!ImportCommonMethod.isEmpty(neCuid)&&!ImportCommonMethod.isEmpty(_devType)){
				if(ImportCommonMethod.isEmpty(neFdn)){
					neFdn = getDevFdnByCuid(neCuid);
				}
				res = new HashMap<String,Object>();
				String cardFdn = neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1:Equipment=1";
				List<Map> cardList = getCardByFdnAndNeCuid(cardFdn, neCuid);
				String cardCuid = "";
				if(cardList!=null&&cardList.size()>0){
					Map cardMap = cardList.get(0);
					cardCuid = ObjectUtils.toString(cardMap.get("CUID"));
					res.put("CUID", cardCuid);
					res.put("LABEL_CN", _devType.toUpperCase()+"无板卡");
					res.put("FDN", cardFdn);
					res.put("DEV_TYPE", devType);
					res.put("RELATED_UPPER_COMPONENT_CUID", "EQUIPMENT_HOLDER-"+neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1");
					this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updateCardInfoByNe", res);
				}else{
					Map<String,Object> cardmap=new HashMap<String,Object>();
					cardCuid = CUIDHexGenerator.getInstance().generate("CARD");
					cardmap.put("CUID", cardCuid);
					res.put("CUID", cardCuid);
					res.put("FDN", cardFdn);

					cardmap.put("LABEL_CN", _devType.toUpperCase()+"无板卡");
					cardmap.put("RELATED_DEVICE_CUID", neCuid);
					cardmap.put("FDN", cardFdn);
					cardmap.put("RELATED_UPPER_COMPONENT_CUID", "EQUIPMENT_HOLDER-"+neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1");
					cardmap.put("CREATE_TIME", new Timestamp(System.currentTimeMillis()));
					cardmap.put("GT_VERSION", 0);
					cardmap.put("ISDELETE", 0);
					cardmap.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
					cardmap.put("PROJECT_STATE", 0);
					cardmap.put("IS_PERMIT_SYS_DEL", 0);
					cardmap.put("SERVICE_STATE", 1);
					cardmap.put("USE_TYPE", 1);
					cardmap.put("OBJECT_TYPE_CODE", "5000");
					cardmap.put("LIVE_CYCLE", 1);
					cardmap.put("USE_STATE", 1);
					cardmap.put("PHOTOELECTRICITY", 1);
					cardmap.put("DEV_TYPE", devType);
					this.IbatisDAO.getSqlMapClient().insert(NetWorkConstant.EQUIP_SQL_MAP + ".insertCardInfo", cardmap);
				}
				//更新ONU端口或POS端口所属板卡和FDN
				List<Map> ptpList = getPtpByAndNeCuid(neCuid);
				if(ptpList!=null&&ptpList.size()>0){
					for(Map m:ptpList){
						String portNo = ObjectUtils.toString(m.get("PORT_NO"));
						Map<String,Object> paramPtp=new HashMap<String,Object>();
						paramPtp.put("CUID", m.get("CUID"));
						paramPtp.put("RELATED_CARD_CUID",cardCuid);
						paramPtp.put("FND", neFdn+":PTP=/rack=1/shelf=1/slot=1/port="+portNo);
						paramPtp.put("SYS_NO", "1-1-1-"+portNo);
						paramPtp.put("DEV_TYPE", devType);
						this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updatePtpInfoByCard", paramPtp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	public String getDevFdnByCuid(String neCuid){
		String devType = "";
		if(StringUtils.isNotEmpty(neCuid)){
			String sql = "SELECT("
					+ " SELECT FDN FROM TRANS_ELEMENT T WHERE T.CUID = '"+neCuid+"'"+
					" UNION ALL "
					+ "  SELECT FDN FROM AN_POS T WHERE T.CUID = '"+neCuid+"'"+
					" UNION ALL "
					+ " SELECT FDN FROM AN_ONU T WHERE T.CUID = '"+neCuid+"') AS FDN "
					+ " FROM DUAL ";
			List<Map> resList =  this.IbatisDAO.querySql(sql);
			if(resList!=null&&resList.size()>0){
				Map temp = resList.get(0);
				if(temp!=null){
					devType = temp.get("FDN")==null?"":temp.get("FDN").toString();
				}
			}
		}
		return devType;
	}
	
	/*
	 * 根据设备CUID，板块FDN，判断板卡是否存在
	 */
	public List getCardByFdnAndNeCuid(String cardFdn,String neCuid){
		if(cardFdn!=null&&cardFdn.length()>0){
			return this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM CARD WHERE FDN='"+cardFdn+"'  AND RELATED_DEVICE_CUID='"+neCuid+"' ");
		}else{
			return this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM CARD WHERE  1 =1  AND RELATED_DEVICE_CUID='"+neCuid+"' ");
		}
	}
	
	/*
	 * 根据设备CUID，判断端口是否存在
	 */
	public List getPtpByAndNeCuid(String neCuid){
		if(!ImportCommonMethod.isEmpty(neCuid)){
			String sql = "SELECT P.CUID,P.PORT_NO FROM PTP P WHERE  RELATED_NE_CUID='"+neCuid+"'"
					+ " AND NOT EXISTS (SELECT 1 FROM CARD C WHERE C.CUID = P.RELATED_CARD_CUID) ";
			return this.IbatisDAO.querySql(sql);
		}else{
			return null;
		}
	}
	
	/*
	 * 根据设备CUID，判断端口是否存在
	 */
	public List getPtpByLabelCnAndNeCuid(String ptpName,String neCuid){
		if(!ImportCommonMethod.isEmpty(ptpName)&&!ImportCommonMethod.isEmpty(neCuid)){
			String sql = "SELECT CUID,LABEL_CN,PORT_SUB_TYPE,port_state FROM PTP WHERE LABEL_CN='"+ptpName+"' AND RELATED_NE_CUID='"+neCuid+"'";
			return this.IbatisDAO.querySql(sql);
		}else{
			return null;
		}
	}



	public String getOldOnuPortCuid(String onuCuid) {
		
		String sql = "select related_pos_port_cuid from an_onu where cuid = '"+ onuCuid + "'";
		List<ResultMap<String, String>> list = this.IbatisDAO.querySql(sql);
		return list.get(0).get("RELATED_POS_PORT_CUID");
	}
	
	public List getOnuPortCount(String neCuidStr) throws SQLException {
		List list = this.IbatisDAO.getSqlMapClient().queryForList(
				NetWorkConstant.EQUIP_SQL_MAP + ".getOnuPortCount", neCuidStr);
		return list;
	}
	
	public List getPosPortCount(String neCuidStr) throws SQLException{
		List list =  this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".getPosPortCount", neCuidStr);
		return list;
	}
	
	
}
