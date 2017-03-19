package com.boco.workflow.webservice.upload.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boco.core.bean.SpringContextUtil;
import com.boco.core.ibatis.dao.IbatisDAO;

public class ImportCache {
	private static ImportCache instance;
	private static Map<String, Map> emsMap;

	public static ImportCache getInstance() {
		if (instance == null) {
			instance = new ImportCache();
		}
		return instance;
	}

	public Map getEmsMap(String emsName) {
		Map returnMap = null;
		if (emsMap == null) {
			initEmsMap();
		}
		returnMap = emsMap.get(emsName);
		if (returnMap == null) {
			initEmsMap();
			returnMap = emsMap.get(emsName);
		}
		return returnMap;
	}

	private void initEmsMap() {
		if (emsMap == null) {
			emsMap = new HashMap<String, Map>();
		}
		String sql = "SELECT CUID,LABEL_CN,RELATED_SPACE_CUID FROM NMS_SYSTEM";
		List list = getIbatisDAO().querySql(sql);
		for (Object object : list) {
			Map map = (Map) object;
			emsMap.put((String) map.get("LABEL_CN"), map);
		}
	}
	
	private IbatisDAO getIbatisDAO(){
		return (IbatisDAO)SpringContextUtil.getBean("IbatisResDAO");
	}
}
