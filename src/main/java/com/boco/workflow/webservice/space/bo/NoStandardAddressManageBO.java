package com.boco.workflow.webservice.space.bo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.upload.constants.GetCh2Spell;
import com.boco.workflow.webservice.upload.service.ImportCommonMethod;


@Service
public class NoStandardAddressManageBO {
	@Autowired
	private IbatisDAO IbatisDAO;


	
	public String insertAndUpdateData(Map<String,String> map) throws Exception{
		
		String CUID = map.get("CUID")==null?"":map.get("CUID")+"";
		if("INSERT".equals(map.get("TYPE")+"")){
			insertAddressInfo(map);
		}
		if("UPDATE".equals(map.get("TYPE")+"")){
			updateAddressInfo(map);
		}
		return CUID;
	}
	
	public String insertAddressInfo(Map<String,String> map) throws Exception{
		String labelCn = map.get("LABEL_CN")==null?"":map.get("LABEL_CN");
		if(isExistAddressInfoByLabeCn(labelCn, null)){
			return "E该标准地址已存在！";
		}else{
			String cuid = CUIDHexGenerator.getInstance().generate("NO_T_ROFH_FULL_ADDRESS");
			map.put("CUID", cuid);
			String pinyin=GetCh2Spell.getBeginCharacter(labelCn);
		  	map.put("PINYIN", pinyin);
			
			map.put("FLAG", "1");
			this.IbatisDAO.getSqlMapClient().insert( "FULLADDRESS.insertAddressInfo2", map);
			return "R"+cuid;
		}
	}
	
	public String updateAddressInfo(Map<String,String> map) throws Exception{
		String labelCn = map.get("LABEL_CN")==null?"":map.get("LABEL_CN");
		String cuid = map.get("CUID")==null?"":map.get("CUID");
		String pinyin=GetCh2Spell.getBeginCharacter(labelCn);
	  	map.put("PINYIN", pinyin);
		map.put("FLAG", "1");
		this.IbatisDAO.getSqlMapClient().update( "FULLADDRESS.updateAddressInfo", map);
	    //级联更新覆盖范围
	    updateGponCoverByAddress(map);
	    //级联更新业务区,暂时不处理
	    return "R"+cuid;
	}
	
	private void updateGponCoverByAddress(Map map) throws SQLException{
		this.IbatisDAO.getSqlMapClient().update("FULLADDRESS.updateGponCoverByAddress", map);
	}
	
	/**
	 * 根据名称查询标准地址
	 */
	public Map<String,Object> selectAddressInfoByLabeCn(String labelCn) throws Exception{
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList("FULLADDRESS.selectAddressInfoByLabeCn",labelCn);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 判断区域是否已经存在
	 */
	public Map<String,Object> selectDistrictInfoByLabeCn(String labelCn,int dataType,String relatedSpaceCuid) throws Exception{
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("LABEL_CN", labelCn);
		param.put("DATA_TYPE", dataType);
		if(!ImportCommonMethod.isEmpty(relatedSpaceCuid)){
			param.put("RELATED_SPACE_CUID", relatedSpaceCuid);
		}
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList("FULLADDRESS.selectDistrictInfoByLabeCn",param);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 判断标准地址是否已经存在
	 */
	public boolean isExistAddressInfoByLabeCn(String labelCn,String cuid) throws Exception{
		Map<String,Object> tempObj = selectAddressInfoByLabeCn(labelCn);
		if(tempObj!=null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断业务区是否已经存在
	 */
	public Map<String,Object> selectBusinessCommunityInfoByLabeCn(String labelCn) throws Exception{
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList("FULLADDRESS.selectBusinessCommunityInfoByLabeCn",labelCn);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	

}
