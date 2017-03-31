package com.boco.workflow.webservice.gponcover.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;

import com.boco.workflow.webservice.gponcover.bo.GponCoverManageBO;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.utils.id.CUIDHexGenerator;
public class GponCoverManageAction {
	private static final String CUID="CUID";
	private static final String GPON_COVER="GPON_COVER";
	GponCoverManageBO gponCoverManageBO = getGponCoverManageBO();			
	private GponCoverManageBO getGponCoverManageBO() {
		return (GponCoverManageBO) SpringContextUtil.getBean("gponCoverManageBO");
	}
	public String saveGponCover(HttpServletRequest request, List<Map<String, String>> selVals) throws Exception {
		String msg = gponCoverManageBO.batchInsertGponCover(selVals);		
		return msg;
	}
	
	public String updateGponCover(HttpServletRequest request, List<Map<String, String>> selVals) throws Exception {
		List<Map<String, String>> insertList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> updateList = new ArrayList<Map<String, String>>();
		for(Map<String,String> selVal:selVals){
			String cuid = ObjectUtils.toString(selVal.get(CUID));
			if("".equals(cuid)){
				selVal.put(CUID, CUIDHexGenerator.getInstance().generate(GPON_COVER));
				insertList.add(selVal);
			}
			else{
				updateList.add(selVal);
			}
		}
		String msg = "";
		if(insertList!=null&&insertList.size()>0){
			this.saveGponCover(request,insertList);
		}
		
		if("".equals(msg)&&updateList!=null&&updateList.size()>0){
			msg = gponCoverManageBO.batchUpdateGponCover(updateList);		
		}
		return msg;
		
	}
	
	public String deleteGponCover(HttpServletRequest request, List<Map<String, String>> selVals) throws Exception {
		String msg = gponCoverManageBO.deleteGponCover(selVals);		
		return msg;
	}
}
