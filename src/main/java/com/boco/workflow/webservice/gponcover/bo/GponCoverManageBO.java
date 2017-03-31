package com.boco.workflow.webservice.gponcover.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.workflow.webservice.dao.utils.BoUtil;
@Service
public class GponCoverManageBO {
@Autowired
protected IbatisDAO IbatisDAO;
	
	private static final String GPONCOVER_SQL_MAP = "GPONCOVER";
	
	public IbatisDAO getIbatisDAO() {
		return IbatisDAO;
	}

	public void setIbatisDAO(IbatisDAO ibatisDAO) {
		IbatisDAO = ibatisDAO;
	}
	
	public String batchInsertGponCover(List<Map<String, String>> list) throws Exception{
		for(Map<String,String> gponCoverMap : list){
			gponCoverMap.put("CUID", CUIDHexGenerator.getInstance().generate("GPON_COVER"));
		}
		try{
			BoUtil.batchInsert(IbatisDAO.getSqlMapClient(), GPONCOVER_SQL_MAP + ".insertGponCover", list);
		}catch(Exception ee){
			 ee.printStackTrace();
			 return ee.getMessage();
		}
		
		return "";
	}
	
	public String batchUpdateGponCover(List<Map<String, String>> list) throws Exception{
		
		try{
			BoUtil.batchUpdate(IbatisDAO.getSqlMapClient(), GPONCOVER_SQL_MAP + ".updateGponCover", list);
		}catch(Exception ee){
			 ee.printStackTrace();
			 return ee.getMessage();
		}
		
		return "";
	}
	
	public String deleteGponCover(List<Map<String, String>> list){
		List cuidlist = new ArrayList();
		for(Map<String,String> gponCoverMap : list){
			String cuid = ObjectUtils.toString(gponCoverMap.get("GPONCOVER_CUID"));
			Map map = new HashMap();
			map.put("CUID", cuid);
			cuidlist.add(map);
		}
		try{
			BoUtil.batchDelete(IbatisDAO.getSqlMapClient(), GPONCOVER_SQL_MAP + ".deleteGponCover", cuidlist);
		}catch(Exception ee){
			 ee.printStackTrace();
			 return ee.getMessage();
		}
		return "";
	}
	
	public Map<String,Object> selectRelatedNeCuidByLabeCn(String neName){
		List<Map<String,Object>> neMapList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(GPONCOVER_SQL_MAP + ".selectRelatedNeCuidByLabeCn", neName);
		if(neMapList!=null&&neMapList.size()>0){
			return neMapList.get(0);
		}else{
			return null;
		}
	}
	
	public Map<String,Object> selectFullAddressByLabeCn(String addressName){
		List<Map<String,Object>> addressList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(GPONCOVER_SQL_MAP + ".selectFullAddressByLabeCn", addressName);
		if(addressList!=null&&addressList.size()>0){
			return addressList.get(0);
		}else{
			return null;
		}
	}
	
	public Map<String,Object> selectGponCoverByNeAndAddress(String neCuid,String addressCuid){
		Map parameterMap = new HashMap();
		parameterMap.put("RELATED_NE_CUID",neCuid);
		parameterMap.put("STANDARD_ADDR", addressCuid);
		Map<String,Object> addressMap = (HashMap<String,Object>)this.IbatisDAO.getSqlMapClientTemplate().queryForObject(GPONCOVER_SQL_MAP + ".selectGponCoverByNeAndAddress", parameterMap);
		return addressMap;
	}


}
