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
public class VillageBO {

	@Autowired
	private IbatisDAO IbatisDAO;
	
	
	public Map<String,Object> selectDistrictByLabeCn(String labelCn) throws SQLException{
		Map<String,Object> result = new HashMap<String,Object>();
		List resultList = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.BUSINESSCOMMUNITY_SQL_MAP + ".selectDistrictByLabeCn", labelCn);
		if(resultList!=null&&resultList.size()>0){
			result = (Map<String,Object>)resultList.get(0);
		}
		return result;
	}
	
	public Map<String,Object> queryBCInfoByLabelCn(Map paramMap) throws SQLException{
		Map<String,Object> result = new HashMap<String,Object>();
		List resultList = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.BUSINESSCOMMUNITY_SQL_MAP + ".selectBusinessCommunityByLabeCn", paramMap);
		if(resultList!=null&&resultList.size()>0){
			result = (Map<String,Object>)resultList.get(0);
		}
		return result;
		
	}
	
	public  Map<String,Object> selectMaintainDeptByLabeCn(String labelCn) throws SQLException{
		Map<String,Object> result = new HashMap<String,Object>();
		List resultList = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.BUSINESSCOMMUNITY_SQL_MAP + ".selectMaintainDeptByLabeCn", labelCn);
		if(resultList!=null&&resultList.size()>0){
			result = (Map<String,Object>)resultList.get(0);
		}
		return result;
	}
	
	public Map<String,Object> selectBooleanByLabeCn(String labelCn) throws SQLException{
		Map<String,Object> result = new HashMap<String,Object>();
		List resultList = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.BUSINESSCOMMUNITY_SQL_MAP + ".selectBooleanByLabeCn", labelCn);
		if(resultList!=null&&resultList.size()>0){
			result = (Map<String,Object>)resultList.get(0);
		}
		return result;
	}
}
