package com.boco.workflow.webservice.space.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.upload.service.ImportCommonMethod;

@Service
public class FullAddressBO {
	
	@Autowired
	private IbatisDAO IbatisDAO;
	
	
	/**
	 * 根据名称查询标准地址
	 */
	public Map<String,Object> selectAddressInfoByLabeCn(String labelCn) throws Exception{
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.FULLADDRESS_SQL_MAP+".selectAddressInfoByLabeCn",labelCn);
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
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.FULLADDRESS_SQL_MAP+".selectDistrictInfoByLabeCn",param);
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
//			if(!ImportCommonMethod.isEmpty(cuid)){
//				String tempCuid = tempObj.get("CUID").toString();
//				if(!tempCuid.equals(cuid)){
//					return true;
//				}else{
//					return false;
//				}
//			}else{
//				return true;
//			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断业务区是否已经存在
	 */
	public Map<String,Object> selectBusinessCommunityInfoByLabeCn(String labelCn) throws Exception{
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.FULLADDRESS_SQL_MAP+".selectBusinessCommunityInfoByLabeCn",labelCn);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
