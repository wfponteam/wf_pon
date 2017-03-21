package com.boco.workflow.webservice.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.pojo.Project;
import com.boco.workflow.webservice.pojo.ProjectNameSpace;

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

	/**
	 * 更新工程状态
	 * @param prjStatus
	 * @throws SQLException 
	 */
	public void updateProjectStatus(PrjStatus prjStatus) throws SQLException {
		
		
		this.getBaseDAO().getSqlMapClient().update(NetWorkConstant.PROJECT_SQL_MAP + ".updateProjectStatus", prjStatus);
		
	}
	
	public void moveResourse(ProjectNameSpace pns) throws SQLException{
		
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".movePos",pns);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveOnu",pns);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".movePtp",pns);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveCard",pns);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveCommunity",pns);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveAddress",pns);
		this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".moveCover",pns);
		
	}
	
	public void removeResourse(String prjcode) throws SQLException{
		
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempPtp",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempCard",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempPos",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempOnu",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempCommunity",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempAddress",prjcode);
		this.getBaseDAO().getSqlMapClient().delete(NetWorkConstant.PROJECT_SQL_MAP + ".deleteAttempCover",prjcode);
		
	}
}
