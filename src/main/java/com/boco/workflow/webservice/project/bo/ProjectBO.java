package com.boco.workflow.webservice.project.bo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.workflow.webservice.builder.PrjStatusBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.PrjStatus;

@Service
public class ProjectBO {

	@Autowired
	private IbatisDAO IbatisDAO;
	
	@Autowired
	private ProjectDAO projectDAO;

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryProjectByCode(String cuid) throws SQLException {
		
		List<Map<String,Object>> list = IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.PROJECT_SQL_MAP + ".queryProjectByCode", cuid);
		Map<String,Object> map = new HashMap<String,Object>();
		if(list != null && list.size() > 0){
			map = list.get(0);
		}
		return map;
	}
	
	public void insertHanging(String cuid) throws SQLException{
		IbatisDAO.getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".insertHanging", cuid);
	}
	public void deleteHanging(String PRJCODE) throws SQLException{
		IbatisDAO.getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteHanging", PRJCODE);
	}
	public void updateHanging(Map map) throws SQLException{
		IbatisDAO.getSqlMapClient().update(NetWorkConstant.PROJECT_SQL_MAP + ".updateHanging", map);
	}
	public String queryIdByCode(String prjCode, String parentPrjCode) throws SQLException {
		
		PrjStatus prjStatus = PojoBuilderFactory.getBuilder(PrjStatusBuilder.class).addPrjCode(prjCode).addParentPrjCode(parentPrjCode).build();
		return projectDAO.getIdByCode(prjStatus);
	}
	
	public List<Map<String, String>> queryActiveByCuid(String cuid) throws SQLException {
		@SuppressWarnings("unchecked")
		List<Map<String, String>> list = IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.PROJECT_SQL_MAP + ".queryActiveByCuid",cuid);
		return list;
	}
	
}
