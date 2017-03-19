package com.boco.workflow.webservice.space.bo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.workflow.webservice.constants.NetWorkConstant;

@Service
public class CoverBO {
	
	@Autowired
	private IbatisDAO IbatisDAO;
	
	public Map<String,Object> selectRelatedNeCuidByLabeCn(String neName) throws SQLException{
		List<Map<String,Object>> neMapList = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.GPONCOVER_SQL_MAP + ".selectRelatedNeCuidByLabeCn", neName);
		if(neMapList!=null&&neMapList.size()>0){
			return neMapList.get(0);
		}else{
			return null;
		}
	}
	
	public Map<String,Object> selectFullAddressByLabeCn(String addressName) throws SQLException{
		List<Map<String,Object>> addressList = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.GPONCOVER_SQL_MAP + ".selectFullAddressByLabeCn", addressName);
		if(addressList!=null&&addressList.size()>0){
			return addressList.get(0);
		}else{
			return null;
		}
	}
	
	public Map<String,Object> selectGponCoverByNeAndAddress(String neCuid,String addressCuid) throws SQLException{
		Map parameterMap = new HashMap();
		parameterMap.put("RELATED_NE_CUID",neCuid);
		parameterMap.put("STANDARD_ADDR", addressCuid);
		Map<String,Object> addressMap = (HashMap<String,Object>)this.IbatisDAO.getSqlMapClient().queryForObject(NetWorkConstant.GPONCOVER_SQL_MAP + ".selectGponCoverByNeAndAddress", parameterMap);
		return addressMap;
	}

}
