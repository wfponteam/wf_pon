package com.boco.workflow.webservice.equip.bo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.bean.SpringContextUtil;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.core.ibatis.vo.ResultMap;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.dao.utils.BoUtil;
import com.boco.workflow.webservice.upload.bo.ImportBasicDataBO;
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
		
		BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".updateOnuPtpFreeAtte", cuids);
		
		//删除onu
		BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deleteOnuByCuid", cuids);
		//删除覆盖范围
	//	BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deleteGponCoverbyNeCuid", cuids);
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
		this.createCardInfo(cuid, 2);
	
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
	public void createCardInfo(String neCuid,int devType){

		try {
			Map<String,Object> map = getDevByCuid(neCuid);
			if(map != null){

				
				String neFdn = IbatisDAOHelper.getStringValue(map, "FDN");
				String labelCn = IbatisDAOHelper.getStringValue(map, "LABEL_CN");
				String ration = IbatisDAOHelper.getStringValue(map, "RATION");
				String district =  IbatisDAOHelper.getStringValue(map, "RELATED_DISTRICT_CUID");
				String relatedEmsCuid = IbatisDAOHelper.getStringValue(map, "RELATED_EMS_CUID");
				
			//	String cardFdn = neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1:Equipment=1";
				List<Map> cardList = getCardByNeCuid(neCuid);
				String cardCuid = "";
				
				List<Map<String,Object>> insertPtpList = new ArrayList<Map<String,Object>>();
				Map<String,Object> mapPort = null;
				
				if(cardList != null && cardList.size() > 0){
					for(Map cardMap : cardList){
						cardCuid = ObjectUtils.toString(cardMap.get("CUID"));
						String fdn = IbatisDAOHelper.getStringValue(cardMap, "FDN");
						Map<String,Object> cardmap=new HashMap<String,Object>();
						cardmap.put("CUID", cardCuid);
						cardmap.put("FDN", fdn.contains("slot=1") ? neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1:Equipment=1" : neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=2:Equipment=1");
						cardmap.put("RELATED_UPPER_COMPONENT_CUID", "EQUIPMENT_HOLDER-"+neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1");
						this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updateCardInfoByNe", cardmap);
						
						if(fdn.contains("slot=2")){
							
							//扩展下联口
							if(!"0".equals(ration)){
								
								int num = Integer.parseInt(ration.substring(ration.indexOf(":") + 1, ration.length()));
								
								//max portnum
								int maxNo = (Integer)this.IbatisDAO.getSqlMapClient().queryForObject(NetWorkConstant.EQUIP_SQL_MAP + ".getMaxPortNoByNeCuid", neCuid);
								if(maxNo < num){
									
									for( maxNo++; maxNo <= num ; maxNo ++ ){
										
										mapPort = new HashMap<String,Object>();
										mapPort.put("LABEL_CN",labelCn + "-2-VBoard-" + String.format("%02d", maxNo));
										mapPort.put("PORT_NO", maxNo);
										mapPort.put("RELATED_NE_CUID", neCuid);
										mapPort.put("RATION", ration);
										mapPort.put("FDN", neFdn+":PTP=/rack=1/shelf=1/slot=2/port=" + maxNo);
										mapPort.put("SYS_NO", "1-1-1-"+maxNo);
										mapPort.put("DEV_TYPE", devType);
										mapPort.put("RELATED_CARD_CUID", cardCuid);
										mapPort.put("PORT_SUB_TYPE",13);
										mapPort.put("PORT_TYPE",2);
										mapPort.put("RELATED_DISTRICT_CUID",district);
										mapPort.put("PORT_STATE",1);
										mapPort.put("RELATED_EMS_CUID",relatedEmsCuid);
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
										mapPort.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
										insertPtpList.add(mapPort);
									}
								}
							}
						}
						
					}
					//更新ONU端口或POS端口所属板卡和FDN

					this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updatePtpInfoByCard", neCuid);
				}else{
					Map<String,Object> cardmap=new HashMap<String,Object>();
					cardCuid = CUIDHexGenerator.getInstance().generate("CARD");
					cardmap.put("CUID", cardCuid);
					cardmap.put("LABEL_CN", labelCn + "-2-VBoard");
					cardmap.put("RELATED_DEVICE_CUID", neCuid);
					cardmap.put("FDN", neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=2:Equipment=1");
					cardmap.put("RELATED_UPPER_COMPONENT_CUID", "EQUIPMENT_HOLDER-"+neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=2");
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
					
					//新增下联口
					if(!"0".equals(ration)){
						
						int num = Integer.parseInt(ration.substring(ration.indexOf(":") + 1, ration.length()));
						
						for(int portNum = 1 ; portNum <= num ; portNum ++ ){
							
							mapPort = new HashMap<String,Object>();
							mapPort.put("LABEL_CN",labelCn + "-2-VBoard-" + String.format("%02d", portNum));
							mapPort.put("PORT_NO", portNum);
							mapPort.put("RELATED_EMS_CUID",relatedEmsCuid);
							mapPort.put("RELATED_DISTRICT_CUID",district);
							mapPort.put("RELATED_NE_CUID", neCuid);
							mapPort.put("RATION", ration);
							mapPort.put("FDN", neFdn+":PTP=/rack=1/shelf=1/slot=2/port=" + portNum);
							mapPort.put("SYS_NO", "1-1-1-"+portNum);
							mapPort.put("DEV_TYPE", devType);
							mapPort.put("RELATED_CARD_CUID", cardCuid);
							mapPort.put("PORT_SUB_TYPE",13);
							mapPort.put("PORT_TYPE",2);
							mapPort.put("PORT_STATE",1);
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
							mapPort.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
							insertPtpList.add(mapPort);
						}
					}
						
						
						//上联口
						cardmap=new HashMap<String,Object>();
						cardCuid = CUIDHexGenerator.getInstance().generate("CARD");
						cardmap.put("CUID", cardCuid);
						cardmap.put("LABEL_CN", labelCn + "-1-VBoard");
						cardmap.put("RELATED_DEVICE_CUID", neCuid);
						cardmap.put("FDN", neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1:Equipment=1");
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
						
					//	if(!"0".equals(ration)){
							
							mapPort = new HashMap<String,Object>();
							mapPort.put("LABEL_CN",labelCn + "-1-VBoard-01");
							mapPort.put("PORT_NO", "01");
							mapPort.put("RELATED_EMS_CUID",relatedEmsCuid);
							mapPort.put("RELATED_DISTRICT_CUID",district);
							mapPort.put("RELATED_NE_CUID", neCuid);
							mapPort.put("RATION", ration);
							mapPort.put("FDN", neFdn+":PTP=/rack=1/shelf=1/slot=1/port=01");
							mapPort.put("SYS_NO", "1-1-1-01");
							mapPort.put("DEV_TYPE", devType);
							mapPort.put("RELATED_CARD_CUID", cardCuid);
							mapPort.put("PORT_SUB_TYPE","0".equals(ration) ? 14 : 12);
							mapPort.put("PORT_TYPE",2);
							mapPort.put("PORT_STATE",2);
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
							mapPort.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
							insertPtpList.add(mapPort);
						
				}
				((ImportBasicDataBO)(SpringContextUtil.getBean("importBasicDataBO"))).importPtpBatchInsert(insertPtpList,
						"自动生成pos端口");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Map<String,Object> getDevByCuid(String neCuid){
	
		if(StringUtils.isNotEmpty(neCuid)){
			String sql = "select * from(  SELECT FDN ,label_cn ,ration,related_district_cuid,related_ems_cuid FROM t_attemp_AN_POS T WHERE T.CUID = '"+neCuid+"'"+
					" UNION ALL "
					+ " SELECT FDN,label_cn ,'0' ration,related_district_cuid,related_ems_cuid FROM t_attemp_AN_ONU T WHERE T.CUID = '"+neCuid+"' )";
			List<Map<String,Object>> resList =  this.IbatisDAO.querySql(sql);
			if(resList!=null&&resList.size()>0){
				
				return resList.get(0);

			}
		}
		return null;
	}
	
	/*
	 * 根据设备CUID，板块FDN，判断板卡是否存在
	 */
	public List getCardByNeCuid(String neCuid){
		
		return this.IbatisDAO.querySql("SELECT CUID,LABEL_CN,fdn FROM t_attemp_card WHERE  1 =1  AND RELATED_DEVICE_CUID='"+neCuid+"' ");
		
	}
	
	/*
	 * 根据设备CUID，判断端口是否存在
	 */
	public List getPtpByAndNeCuid(String neCuid){
		if(!ImportCommonMethod.isEmpty(neCuid)){
			String sql = "SELECT P.CUID,P.PORT_NO FROM t_attemp_PTP P WHERE  RELATED_NE_CUID='"+neCuid+"'";
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
			sql += " union all SELECT CUID,LABEL_CN,PORT_SUB_TYPE,port_state FROM t_attemp_ptp WHERE LABEL_CN='"+ptpName+"' AND RELATED_NE_CUID='"+neCuid+"'";
			return this.IbatisDAO.querySql(sql);
		}else{
			return null;
		}
	}



	public String getOldOnuPortCuid(String onuCuid) {
		
		String sql = "select related_pos_port_cuid from t_attemp_an_onu where cuid = '"+ onuCuid + "'";
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
