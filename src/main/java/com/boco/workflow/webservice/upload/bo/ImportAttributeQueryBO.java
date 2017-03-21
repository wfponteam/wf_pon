package com.boco.workflow.webservice.upload.bo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.ibatis.vo.ResultMap;

@SuppressWarnings({"deprecation","rawtypes","unchecked"})
@Repository
public class ImportAttributeQueryBO {
	Map<String, String> emsMap = new HashMap<String, String>();
	Map<String, String> roomMap = new HashMap<String, String>();
	Map<String, String> rackMap = new HashMap<String, String>();
	Map<String, String> siteMap = new HashMap<String, String>();
	Map<String, String> districtMap = new HashMap<String, String>();
	Map<String, String> vendorMap = new HashMap<String, String>();
	Map<String, String> districtNewMap = new HashMap<String, String>();
	Map<String, String> accesspointMap = new HashMap<String, String>();
	Map<String, String> cabMap  = new HashMap<String, String>();
	Map<String, Map> roomAccesspointMap = new HashMap<String, Map>();
	Map<String, String> onuMap = new HashMap<String, String>();
	Map<String, Map> posMap = new HashMap<String, Map>();
	Map<String, Map> ptpMap = new HashMap<String, Map>();
	Map<String, Map> posUpDevMap = new HashMap<String, Map>();
	private static final String EQUIP_SQL_MAP = "EQUIP";
	private static final String IMPROT_BASIC_DATA_SQL_MAP0 = "IMPROTBASEDATA";
	
	@Autowired
	private IbatisDAO IbatisDAO;


	// 查询生产厂商和型号关系是否存在 2016年4月14日19:44:12
	public List getProducterAndModelRelated(String producter, String model,int cfgtype) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("producter", producter);
		map.put("CFG_TYPE", cfgtype);
		map.put("model", model);
		List list = this.IbatisDAO.getSqlMapClient()
				.queryForList(IMPROT_BASIC_DATA_SQL_MAP0 + ".getProducterAndModelRelated",
						map);
		return list;
	}

	public String getPropertyCuidByName(String nameValue, String property) throws SQLException {
		if (!StringUtils.isEmpty(nameValue)) {
			Map<String, String> cacheMap = null;
			String sqlMethod = null;
			if (property.equals("NMS_SYSTEM")) {
				cacheMap = emsMap;
				sqlMethod = EQUIP_SQL_MAP + ".queryEmsCuidByLabelcn";
			} else if (property.equals("ROOM")) {
				cacheMap = roomMap;
				sqlMethod = IMPROT_BASIC_DATA_SQL_MAP0 + ".getRelatedRoom";
			} else if (property.equals("RACK")) {
				cacheMap = rackMap;
				sqlMethod = IMPROT_BASIC_DATA_SQL_MAP0 + ".getRelatedRack";
			} else if (property.equals("DISTRICT")) {
				cacheMap = districtMap;
				sqlMethod = EQUIP_SQL_MAP + ".getRelatedDistrict";
			} else if (property.equals("DEVICE_VENDOR")) {
				cacheMap = vendorMap;
				sqlMethod = IMPROT_BASIC_DATA_SQL_MAP0
						+ ".quertyVendorCuidByName";
			} else if (property.equals("DISTRICT_3")) {
				cacheMap = districtNewMap;
				sqlMethod = EQUIP_SQL_MAP + ".getDistrictByLabelCn3";
			} else if (property.equals("ACCESSPOINT")) {
				cacheMap = accesspointMap;
				sqlMethod = EQUIP_SQL_MAP + ".queryAccessPointCuidByLabelCn";
			} else if (property.equals("CAB")) {
				cacheMap = cabMap;
				sqlMethod = EQUIP_SQL_MAP + ".queryRelatedCabCuidByLabelcn";
			}
			if (cacheMap.containsKey(nameValue)) {
				return cacheMap.get(nameValue);
			} else {
				List<String> list = this.IbatisDAO.getSqlMapClient()
						.queryForList(sqlMethod, nameValue);
				if (list != null && !list.isEmpty()
						&& !StringUtils.isEmpty(list.get(0))) {
					cacheMap.put(nameValue, list.get(0));
					return list.get(0);
				}
			}
		}
		return null;
	}

	public Map getRoomAccessPointByName(String nameValue) {
//		if (roomAccesspointMap.containsKey(nameValue)) {
//			return roomAccesspointMap.get(nameValue);
//		} else {
			List<Map> list = this.IbatisDAO.getSqlMapClientTemplate()
					.queryForList(
							EQUIP_SQL_MAP + ".queryRoomORAccessPointByLabelCn",
							nameValue);
			if (list != null && list.size() > 0) {
				Map m = list.get(0);
				roomAccesspointMap.put(nameValue, m);
				return m;
			}
//		}
		return null;
	}

	public Map getPosByName(String nameValue) throws SQLException {
//		if (posMap.containsKey(nameValue)) {
//			return posMap.get(nameValue);
//		} else {
			List<Map> list = this.IbatisDAO.getSqlMapClient().queryForList(EQUIP_SQL_MAP + ".queryPosBylabelcn",
							nameValue);
			if (list != null && list.size() > 0) {
				Map m = list.get(0);
				posMap.put(nameValue, m);
				return m;
			}
//		}
		return null;
	}
	public Map getPosByCuid(String nameValue) {

			List<ResultMap> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".getPosByCuid",
							nameValue);
			if (list != null && list.size() > 0) {
				Map m = list.get(0);
				return m;
			}
//		}
		return null;
	}

	public String getOnuByName(String nameValue) throws SQLException {
//		if (onuMap.containsKey(nameValue)) {
//			return onuMap.get(nameValue);
//		} else {
			List<String> list = this.IbatisDAO.getSqlMapClient()
					.queryForList(EQUIP_SQL_MAP + ".queryOnuByLabelcn",
							nameValue);
			if (list != null && list.size() > 0) {
				String  cuid = list.get(0);
				onuMap.put(nameValue, cuid);
				return cuid;
			}
//		}
		return null;
	}
/**
 * 将POS的上联设备分开-导入为二级分光器
 * @param nameValue
 * @return
 * @throws SQLException 
 */
	public Map getPosUpDevByName(String nameValue) throws SQLException {
//		if (posUpDevMap.containsKey(nameValue)) {
//			return posUpDevMap.get(nameValue);
//		} else {
			List<Map> list = this.IbatisDAO.getSqlMapClient().queryForList(EQUIP_SQL_MAP + ".getPosUpDevByName",
							nameValue);
			if (list != null && list.size() > 0) {
				Map m = list.get(0);
				posUpDevMap.put(nameValue, m);
				return m;
			}
//		}
		return null;
	}
	/**
	 * 将POS的上联设备分开-导入为一级级分光器
	 * @param nameValue
	 * @return
	 * @throws SQLException 
	 *//*
	public Map getPosUpDevOltByName(String nameValue) {
			List<Map> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".getPosUpDevOltByName",
							nameValue);
			if (list != null && list.size() > 0) {
				Map m = list.get(0);
				posUpDevMap.put(nameValue, m);
				return m;
			}
		return null;
	}*/
	public Map getPtpByNameAndNeCuid(String nameValue, String neCuid) throws SQLException {
//		if (ptpMap.containsKey(neCuid + nameValue)) {
//			return posMap.get(neCuid + nameValue);
//		} else {
			Map p = new HashMap();
			p.put("LABEL_CN", nameValue);
			p.put("RELATED_NE_CUID", neCuid);
			List<Map> list = this.IbatisDAO.getSqlMapClient().queryForList(EQUIP_SQL_MAP + ".getPtpByNameAndNeCuid", p);
			if (list != null && list.size() > 0) {
				Map m = list.get(0);
				posMap.put(neCuid + nameValue, m);
				return m;
			}
//		}
		return null;
	}

	public String getSiteByRoom(String roomCuid) {
		if (!StringUtils.isEmpty(roomCuid)) {
			if (siteMap.containsKey(roomCuid)) {
				return siteMap.get(roomCuid);
			} else {
				List<String> siteCuidList = null;
				try {
					siteCuidList = (this.IbatisDAO.getSqlMapClientTemplate()
							.queryForList(EQUIP_SQL_MAP + ".getRelatedSite",
									roomCuid));
				} catch (DataAccessException e) {
					// //现场库中room表里没有related_site_cuid,吃掉异常
				}
				if (siteCuidList == null || siteCuidList.isEmpty()
						|| siteCuidList.size() < 1) {
					siteCuidList = (this.IbatisDAO.getSqlMapClientTemplate()
							.queryForList(EQUIP_SQL_MAP + ".getRelatedSpace",
									roomCuid));
				}
				if (siteCuidList != null && !siteCuidList.isEmpty()
						&& !StringUtils.isEmpty(siteCuidList.get(0))) {
					siteMap.put(roomCuid, siteCuidList.get(0));
					return siteCuidList.get(0);
				} else {
					siteCuidList = (this.IbatisDAO.getSqlMapClientTemplate()
							.queryForList(EQUIP_SQL_MAP + ".getRelatedSpace",
									roomCuid));
					siteMap.put(roomCuid, siteCuidList.get(0));
					return siteCuidList.get(0);
				}
			}
		}
		return null;
	}

	/**
	 * 获取POS上联设备-OLT的FDN信息
	 * by lichao
	 */
	public Map getOltByLabelCuid(String nameValue) {
			List<Map> list = this.IbatisDAO.getSqlMapClientTemplate()
					.queryForList(EQUIP_SQL_MAP + ".queryOltBylabelCuid",
							nameValue);
			if (list != null && list.size() > 0) {
				Map m = list.get(0);
				return m;
			}
		return null;
	}

	
	/**
	 * 获取ONU上联设备-pos的FDN信息  （导入二级分光器也调用此方法）
	 * by lichao
	 * @throws SQLException 
	 */
	public Map getPosByLabelCuid(String nameValue) throws SQLException {
			List<Map> list = this.IbatisDAO.getSqlMapClient()
					.queryForList(EQUIP_SQL_MAP + ".queryPosBylabelCuid",
							nameValue);
			if (list != null && list.size() > 0) {
				Map m = list.get(0);
				return m;
			}
		return null;
	}		
	
	
	public Map getFdnByEms(String name) throws SQLException {
		
		
		List<Map> list = this.IbatisDAO.getSqlMapClient().queryForList(EQUIP_SQL_MAP + ".queryEmsByName",name);
				
						
		Map m = list.get(0);
		return m;
	}

	public List getOltByLabelCn(String name, String district,
			String standardName) {
		Map<String, String> oltMap = new HashMap<String, String>();
		oltMap.put("LABEL_CN", name);
		if (StringUtils.isNotBlank(district)) {
			oltMap.put("RELATED_DISTRICT_CUID", district + "%");
		}
		if (StringUtils.isNotBlank(district)) {
			oltMap.put("STANDARD_NAME", standardName);
		}
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(
				IMPROT_BASIC_DATA_SQL_MAP0 + ".queryOltBylabelcn", oltMap);
	}

	public List queryDistrictBylabelcn(String name) {
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(
				IMPROT_BASIC_DATA_SQL_MAP0 + ".queryDistrictBylabelcn", name);
	}

	public List queryMaintainBylabelcn(String name) {
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(
				IMPROT_BASIC_DATA_SQL_MAP0 + ".queryMaintainBylabelcn", name);
	}
	
	public String getRelatedDistrictCuidByCabCuid(String name){
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(
				EQUIP_SQL_MAP + ".queryRelatedDistrictCuidByCabCuid", name).get(0).toString();
	}
}
