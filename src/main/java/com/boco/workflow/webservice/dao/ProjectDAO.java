package com.boco.workflow.webservice.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.pojo.Project;

@Repository
public class ProjectDAO extends AbstractDAO{

	private static final Logger logger = Logger.getLogger(ProjectDAO.class);
	
	/**
	 * 新建工程
	 * @param project
	 * @throws SQLException 
	 */
	
	public void insertProject(Project project) throws SQLException{
		
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".insertProject", project);
		
	}
	
	public void deleteProject(String id) throws SQLException{
		
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteProject", id);
	}
	
	public void updateProject(Project project) throws SQLException{
		
		this.getBaseDAO().getSqlMapClient().update(NetWorkConstant.PROJECT_SQL_MAP + ".updateProject", project);
	}

	/**
	 * 更新工程状态
	 * @param prjStatus
	 * @throws SQLException 
	 */
	public void updateProjectStatus(PrjStatus prjStatus) throws SQLException {
		this.getBaseDAO().getSqlMapClient().update(NetWorkConstant.PROJECT_SQL_MAP + ".updateProjectStatus", prjStatus);
	}
	public String queryActiveByPrjcode(Map map) throws SQLException {
		@SuppressWarnings("unchecked")
		List<String> list = this.getBaseDAO().getSqlMapClient().queryForList(NetWorkConstant.PROJECT_SQL_MAP + ".queryActiveByPrjcode",map);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		
		return null;
	}
	public void moveResourse(String prjcode) throws SQLException{
		
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".movePos",prjcode);
//		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveOnu",pns);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".movePtp",prjcode);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveCard",prjcode);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveCommunity",prjcode);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveAddress",prjcode);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveCover",prjcode);
		
	}
	
	public void removeResourse(String prjcode) throws SQLException{
		
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempPtp",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempCard",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempPos",prjcode);
	//	this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempOnu",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempCommunity",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempAddress",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempCover",prjcode);
		
	}
	
	public Map<String,String> getIdByCode(PrjStatus prjStatus) throws SQLException{
		
		List<Map<String,String>> list = this.getBaseDAO().getSqlMapClient().queryForList(NetWorkConstant.PROJECT_SQL_MAP + ".getIdByCode",prjStatus);
		if(list != null && list.size() > 0){
			
			return list.get(0);
		}
		
		return null;
	}

	public void updatePos(String cuid) throws SQLException {
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".updatePos",cuid);
	}

	public void updateAddress(String cuid)throws SQLException {
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".updateAddress",cuid);
	}

	public void updatePtp(String cuid)throws SQLException {
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".updatePtp",cuid);
	}

	public List<String> queryAddressIds(String cuid) throws SQLException {
		
		return this.getBaseDAO().getSqlMapClient().queryForList(NetWorkConstant.PROJECT_SQL_MAP + ".queryAddressIds", cuid);
	}

	public void deleteAddressRelInfo(String id) throws SQLException {
		
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAddressRelInfo", id);
	}

	public void deleteHangingPos(List<String> names) throws SQLException  {
		
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteHangingPos", names);
	}

	public List<String> queryNoHanging(String cuid) throws SQLException  {
		
		return this.getBaseDAO().getSqlMapClient().queryForList(NetWorkConstant.PROJECT_SQL_MAP + ".queryNoHanging", cuid);
	}
}
