package com.boco.workflow.webservice.businesscommunity.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.dao.utils.BoUtil;
@Service

public class BusinessCommunityManageBO {
	private static final String BUSINESSCOMMUNITY_SQL_MAP = "BUSINESSCOMMUNITY";
	@Autowired
	protected IbatisDAO IbatisDAO;
	

	
	public boolean isExistBCInfoByLabeCn(String labelCn){
		boolean exists = false;
		List resultList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(BUSINESSCOMMUNITY_SQL_MAP + ".selectBusinessCommunityByLabeCn", labelCn);
		if(resultList!=null&&resultList.size()>0){
			exists = true;
		}
		return exists;
	}
	/**
	 * 查询业务区名称是否存在
	 * 
	 * @param name
	 */
	public boolean isBcNameExist(String name,String cityCuid) {
		if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(cityCuid)) {
			
			HashMap map = new HashMap();
			map.put("LABEL_CN", name);
			map.put("CITY", cityCuid);
			List list = null;
			list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(BUSINESSCOMMUNITY_SQL_MAP + ".selectBusinessCommunityByLabeCn", map);
			if (null != list && list.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public Map<String,Object> queryBCInfoByLabelCn(Map paramMap){
		Map<String,Object> result = new HashMap<String,Object>();
		List resultList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(BUSINESSCOMMUNITY_SQL_MAP + ".selectBusinessCommunityByLabeCn", paramMap);
		if(resultList!=null&&resultList.size()>0){
			result = (Map<String,Object>)resultList.get(0);
		}
		return result;
		
	}
	
	public Map<String,Object> selectDistrictByLabeCn(String labelCn){
		Map<String,Object> result = new HashMap<String,Object>();
		List resultList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(BUSINESSCOMMUNITY_SQL_MAP + ".selectDistrictByLabeCn", labelCn);
		if(resultList!=null&&resultList.size()>0){
			result = (Map<String,Object>)resultList.get(0);
		}
		return result;
	}
	
	public  Map<String,Object> selectMaintainDeptByLabeCn(String labelCn){
		Map<String,Object> result = new HashMap<String,Object>();
		List resultList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(BUSINESSCOMMUNITY_SQL_MAP + ".selectMaintainDeptByLabeCn", labelCn);
		if(resultList!=null&&resultList.size()>0){
			result = (Map<String,Object>)resultList.get(0);
		}
		return result;
	}
	
	public Map<String,Object> selectBooleanByLabeCn(String labelCn){
		Map<String,Object> result = new HashMap<String,Object>();
		List resultList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(BUSINESSCOMMUNITY_SQL_MAP + ".selectBooleanByLabeCn", labelCn);
		if(resultList!=null&&resultList.size()>0){
			result = (Map<String,Object>)resultList.get(0);
		}
		return result;
	}
	
	
		
	public String insertBCInfo(Map paramMap){
		String msg = "";
		
		String labelCn = ObjectUtils.toString(paramMap.get("LABEL_CN"));
		String cityCuid = ObjectUtils.toString(paramMap.get("CITY"));
		if(isBcNameExist(labelCn,cityCuid)){
			return labelCn+"在数据中已存在！";
		}else{
			String cuid = BusinessCommunityManageBO.getCuidByClassName("BUSINESS_COMMUNITY");
			paramMap.put("CUID", cuid);
			this.IbatisDAO.getSqlMapClientTemplate().insert(BUSINESSCOMMUNITY_SQL_MAP + ".insertBCInfo", paramMap);
			return cuid;
		}
	}
	
	public String updateBCInfo(Map paramMap){
		String msg = "";
		String cuid = ObjectUtils.toString(paramMap.get("CUID"));
		this.IbatisDAO.getSqlMapClientTemplate().insert(BUSINESSCOMMUNITY_SQL_MAP + ".updateBCInfo", paramMap);
		return cuid;

	}
	
	public Map getLoadBCInfoByCuid(String cuid){
		List resultList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(BUSINESSCOMMUNITY_SQL_MAP + ".selectBusinessCommunityByCuid", cuid);
		Assert.isTrue(resultList!=null&&resultList.size()>0,"根据cuid没有找到数据！");
		Map result = (Map)resultList.get(0);
		return result;
	}
	
	public Map getBCInfoNoTransByCuid(String cuid){
		List resultList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(BUSINESSCOMMUNITY_SQL_MAP + ".selectBusinessCommunityNoTransByCuid", cuid);
		Assert.isTrue(resultList!=null&&resultList.size()>0,"根据cuid没有找到数据！");
		Map result = (Map)resultList.get(0);
		return result;
	}
	
	public String deleteBCInfo( List<Map> cuidList){
		try{
			BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),BUSINESSCOMMUNITY_SQL_MAP + ".deleteBusinessCommunity", cuidList);
		}catch(Exception ee){
			 ee.printStackTrace();
			 return ee.getMessage();
		}
		return "";
	}
	public static  String getCuidByClassName(String className) {
		return CUIDHexGenerator.getInstance().generate(className);
	}
}
